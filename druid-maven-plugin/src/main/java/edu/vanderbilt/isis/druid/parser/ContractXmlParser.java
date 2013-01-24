
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.vanderbilt.isis.druid.generator.GeneratorException;
import edu.vanderbilt.isis.druid.generator.Contract;

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
            logger.debug("contract: {}",
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
        logger.trace("contract {}", root);
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
        String sponsor = "";
        final NodeList sl = xml.getElementsByTagName("sponsor");
        for (int ix = 0; ix < sl.getLength();) {
            Element el = (Element) sl.item(ix);
            sponsor = el.getAttribute("name");
            break;
        }

        final List<Contract.Relation> relation_set = new ArrayList<Contract.Relation>();
        final NodeList nl = xml.getElementsByTagName("relation");
        for (int ix = 0; ix < nl.getLength(); ++ix) {
            final Contract.Relation relation = parseXmlRelation((Element) nl.item(ix));
            relation_set.add(relation);
        }
        return new Contract.Root(extract_name(xml, "name"), sponsor,
                relation_set);
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
     * @param xml
     * @return
     */
    static public Contract.Relation parseXmlRelation(final Element xml) {
        final List<Contract.Field> field_set = new ArrayList<Contract.Field>();
        final NodeList nl = xml.getElementsByTagName("field");
        for (int ix = 0; ix < nl.getLength(); ++ix) {
            field_set.add(parseXmlFieldXml((Element) nl.item(ix)));
        }

        // process the key columns
        final List<Contract.FieldRef> keycol_set = new ArrayList<Contract.FieldRef>();
        final NodeList kl = xml.getElementsByTagName("key");
        for (int ix = 0; ix < kl.getLength(); ++ix) {
            NodeList rl = xml.getElementsByTagName("ref");
            for (int jx = 0; jx < rl.getLength(); ++jx) {
                keycol_set.add(parseXmlFieldRef((Element) rl.item(jx)));
            }
        }

        Contract.RMode mode = null;
        final NodeList ml = xml.getElementsByTagName("mode");
        for (int ix = 0; ix < ml.getLength();) {
            mode = parseXmlRMode((Element) ml.item(ix));
            break;
        }

        return new Contract.Relation(extract_name(xml, "name"), mode, field_set,
                keycol_set);
    }

    static public Contract.RMode parseXmlRMode(Element xml) {
        final String type = xml.getAttribute("type");
        return new Contract.RMode(extract_name(xml, "name"), type,
                xml.getTextContent());
    }

    static public Contract.Name extract_name(final Element xml, final String attr) {
        return new Contract.Name(xml.getAttribute(attr));
    }

    static public Contract.RMode extract_mode(final Element xml) {
        return null;
    }

    /**
     * <code>
    <field type="LONG" name="lat" default="0" />
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
    static public Contract.Field parseXmlFieldXml(final Element xml) {
        final String initial = xml.getAttribute("default");

        final List<Contract.Enumeration> enum_set = new ArrayList<Contract.Enumeration>();
        final NodeList enum_list = xml.getElementsByTagName("enum");
        for (int ix = 0; ix < enum_list.getLength(); ++ix) {
            enum_set.add(parseXmlEnumeration((Element) enum_list.item(ix)));
        }

        final String type = xml.getAttribute("type");
        return new Contract.Field(extract_name(xml, "name"), type, initial,
                xml.getTextContent(), enum_set);
    }

    static public Contract.FieldRef parseXmlFieldRef(final Element xml) {
        return new Contract.FieldRef(extract_name(xml, "field"));
    }

    static public Contract.Enumeration parseXmlEnumeration(final Element xml) {
        Contract.Name key = new Contract.Name(xml.getAttribute("key"));
        int ordinal = Integer.parseInt(xml.getAttribute("value"));
        return new Contract.Enumeration(key, ordinal);
    }

}
