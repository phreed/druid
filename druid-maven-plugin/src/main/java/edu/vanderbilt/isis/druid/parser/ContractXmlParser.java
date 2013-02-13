
package edu.vanderbilt.isis.druid.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.vanderbilt.isis.druid.generator.Contract;
import edu.vanderbilt.isis.druid.generator.Contract.Name;
import edu.vanderbilt.isis.druid.generator.GeneratorException;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class ContractXmlParser {

    /**
   
     * @param logger
     * @param contractFile
     * @return
     * @throws GeneratorException
     */
    public static Contract parseXmlFile(final Logger logger, final File contractFile)
            throws GeneratorException {

        final File contractPath = contractFile;
        try {
            if (contractFile == null) {
                throw new GeneratorException("no contract specified");
            }
            if (!contractFile.exists()) {
                throw new GeneratorException("invalid contract specified " + contractFile);
            }
            logger.info("contract: {}",
                    contractPath.getCanonicalPath());
        } catch (IOException e1) {
            throw new GeneratorException("bad path for contract " + contractFile);
        }
        final DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
        final Document contractXml;
        try
        {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            contractXml = db.parse(contractPath);
            logger.info("Namespace: {}", contractXml.getNamespaceURI());
        } catch (ParserConfigurationException pce) {
            throw new GeneratorException("could not parse configuration" + pce);
        } catch (SAXException se) {
            throw new GeneratorException("could not parse via sax " + se);
        } catch (IOException ioe) {
            throw new GeneratorException("could not open configuration " + ioe);
        }

        /**
         * Build from templates based on contract
         */

        final Element de = contractXml.getDocumentElement();
        final Contract.Root root = parseXmlRoot(de);
        logger.info("contract {}", root);
        return new Contract(logger, root);
    }

    /**
     * <code>
<content-provider name="pli">
  <sponsor name="transapps.pli"/>

  <relation name="locations">
    ...
  </relation>
</content-provider>
     </code>
     *
     * @param xml
     * @return
     */
    static public Contract.Root parseXmlRoot(final Element xml) {
        final Name name = extract_attr_name(xml, "name");
        
        String sponsor = "";
        final List<Contract.Relation> relation_set = new ArrayList<Contract.Relation>();
        final NodeList nodeList = xml.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if ("relation".equals(tagName)) {
                    relation_set.add(parseXmlRelation(currentElement));
                    continue;
                }
                if ("sponsor".equals(tagName)) {
                    sponsor = currentElement.getAttribute("name");
                    continue;
                }
            }
        }
        return new Contract.Root(name, sponsor, relation_set);
    }

    /**
     * <code>
  <relation name="locations">
    <field type="TEXT" name="name" default="" />
    ...
    
    <key name="update">
      ...
    </key>
    
    <message encoding="terse">
      ...
    </message>
  </relation>
  </code>
     * 
     * @param xml
     * @return
     */
    static public Contract.Relation parseXmlRelation(final Element xml) {
        final Name name = extract_attr_name(xml, "name");
        
        final List<Contract.Field> field_set = new ArrayList<Contract.Field>();
        final List<Contract.Key> key_set = new ArrayList<Contract.Key>();
        final List<Contract.UIFieldRef> uicol_set = new ArrayList<Contract.UIFieldRef>();

        Contract.RMode mode = null;
        final List<Contract.Message> message_set = new ArrayList<Contract.Message>();
        
        NodeList nodeList = xml.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if ("field".equals(tagName)) {
                    field_set.add(parseXmlField(currentElement));
                    continue;
                }
                if ("key".equals(tagName)) {
                    key_set.add(parseXmlKey(currentElement));
                    continue;
                }
                if ("message".equals(tagName)) {
                    message_set.add(parseXmlMessage(currentElement));
                    continue;
                }
                if ("mode".equals(tagName)) {
                    mode = parseXmlRMode(currentElement);
                    continue;
                }
				if ("ui-selection-subset".equals(tagName)) {
					uicol_set.addAll(parseXmlUIFieldRef(currentElement));
					continue;
            }
        }
        return new Contract.Relation(name, mode, field_set, key_set, message_set);
    }

    static public Contract.RMode parseXmlRMode(Element xml) {
        final String type = xml.getAttribute("type");
        return new Contract.RMode(extract_attr_name(xml, "name"), type,
                xml.getTextContent());
    }

    static public Contract.Name extract_attr_name(final Element xml, final String attr) {
        return new Contract.Name(xml.getAttribute(attr));
    }

    static public Contract.RMode extract_mode(final Element xml) {
        return null;
    }

    /**
     * <code>
    field type="LONG" name="lat" default="0" />
    <field type="LONG" name="lon" default="0"/>
    <field type="LONG" name="altitude" default="0" />
    <field type="LONG" name="accuracy" default="0" />
    <field type="TIMESTAMP" name="created" default="now"/>
    <field type="TIMESTAMP" name="modified" default="now"/>
    <field type="LONG" name="hops" default="0" />
    <field type="BLOB" name="delta locations" />
    </code>
     *
     * @param xml
     * @return
     */
    static public Contract.Field parseXmlField(final Element xml) {
        final String initial = xml.getAttribute("default");

        final List<Contract.Enumeration> enum_set = new ArrayList<Contract.Enumeration>(); 
        
        final NodeList nodeList = xml.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if ("enum".equals(tagName)) {
                    enum_set.add(parseXmlEnumeration(currentElement));
                    continue;
                }
            }
        }
        final String type = xml.getAttribute("type");
        
        return new Contract.Field(extract_attr_name(xml, "name"), type, initial,
                xml.getTextContent(), enum_set);
    }

    /**
     * <code>
    <key name="update">
      <field ref="foo"/>
      <field ref="bar"/>
      ...
    </key>
    * </code>
     */

    static public Contract.Key parseXmlKey(final Element xml) {
        final Name keyName = extract_attr_name(xml, "name");
        final List<Contract.KeyFieldRef> field_set = new ArrayList<Contract.KeyFieldRef>();

        final NodeList nodeList = xml.getChildNodes();
        for (int ix = 0; ix < nodeList.getLength(); ix++) {
            final Node currentNode = nodeList.item(ix);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element currentElement = (Element) currentNode;
                final String tagName = currentElement.getTagName();
                if ("field".equals(tagName)) {
                    field_set.add(new Contract.KeyFieldRef(extract_attr_name(currentElement, "ref")));
                }
            }
        }
        return new Contract.Key(keyName, field_set);
    }
    static public List<Contract.UIFieldRef> parseXmlUIFieldRef(final Element xml) {

		List<Contract.UIFieldRef> refs = new ArrayList<Contract.UIFieldRef>();
		final NodeList nodeList = xml.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				// System.out.println("\n\nFOUND A NODE : " +
				// nodeList.getLength() + "\n\n");
				final Element currentElement = (Element) currentNode;
				refs.add(parseXmlUIRef(currentElement));
				continue;
			}
		}
		return refs;
	}

	static public Contract.UIFieldRef parseXmlUIRef(final Element xml) {
		return new Contract.UIFieldRef(extract_name(xml, "field"));
	}

	static public Contract.FieldRef parseXmlRef(final Element xml) {
		return new Contract.FieldRef(extract_name(xml, "field"));
	}

    static public Contract.Message parseXmlMessage(final Element xml) {
        return null;
    }

    static public Contract.Enumeration parseXmlEnumeration(final Element xml) {
        Contract.Name key = new Contract.Name(xml.getAttribute("key"));
        int ordinal = Integer.parseInt(xml.getAttribute("value"));
        return new Contract.Enumeration(key, ordinal);
    }

}
