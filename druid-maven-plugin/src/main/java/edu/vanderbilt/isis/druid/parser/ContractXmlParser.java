package edu.vanderbilt.isis.druid.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.vanderbilt.isis.druid.generator.Contract;
import edu.vanderbilt.isis.druid.generator.Contract.Field;
import edu.vanderbilt.isis.druid.generator.Contract.Name;
import edu.vanderbilt.isis.druid.generator.GeneratorException;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class ContractXmlParser {
	final Logger logger;

	public ContractXmlParser(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * @param logger
	 * @param contractFile
	 * @return
	 * @throws GeneratorException
	 */
	public Contract parseFile(final File contractFile,
			final Map<String, Contract.Mode> mode) throws GeneratorException {

		final File contractPath = contractFile;
		try {
			if (contractFile == null) {
				throw new GeneratorException("no contract specified");
			}
			if (!contractFile.exists()) {
				throw new GeneratorException("invalid contract specified "
						+ contractFile);
			}
			logger.info("contract: {}", contractPath.getCanonicalPath());
		} catch (IOException e1) {
			throw new GeneratorException("bad path for contract "
					+ contractFile);
		}
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		final Document contractXml;
		try {
			final DocumentBuilder db = dbf.newDocumentBuilder();
			contractXml = db.parse(contractPath);
			// logger.info("Namespace: {}", contractXml.getNamespaceURI());
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
		final Contract.Root root = parseRoot(de);
		logger.info("contract {}", root);
		return new Contract(logger, root, mode);
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
	public Contract.Root parseRoot(final Element xml) {
		final Map<String, Contract.Mode> mode_set = new HashMap<String, Contract.Mode>();
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
					relation_set.add(parseRelation(currentElement));
					continue;
				}
				if ("sponsor".equals(tagName)) {
					sponsor = currentElement.getAttribute("name");
					continue;
				}
				if ("mode".equals(tagName)) {
					final Contract.Mode mode = parseMode(currentElement);
					mode_set.put(mode.getType(), mode);
					continue;
				}
			}
		}
		final Contract.Root root = new Contract.Root(name, mode_set, sponsor,
				relation_set);
		// logger.info("root element {}", root);
		return root;
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
	public Contract.Relation parseRelation(final Element xml) {
		final Name name = extract_attr_name(xml, "name");

		final List<Contract.Field> field_set = new ArrayList<Contract.Field>();
		final List<Contract.Key> key_set = new ArrayList<Contract.Key>();
		final Map<String, Contract.Mode> mode_set = new HashMap<String, Contract.Mode>();
		final List<Contract.Message> message_set = new ArrayList<Contract.Message>();

		NodeList nodeList = xml.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element currentElement = (Element) currentNode;
				final String tagName = currentElement.getTagName();
				if ("field".equals(tagName)) {
					field_set.add(parseField(currentElement));
					continue;
				}
				if ("key".equals(tagName)) {
					key_set.add(parseKey(currentElement));
					continue;
				}
				if ("message".equals(tagName)) {
					message_set.add(parseMessage(currentElement));
					continue;
				}
				if ("mode".equals(tagName)) {
					final Contract.Mode mode = parseMode(currentElement);
					mode_set.put(mode.getType(), mode);
					continue;
				}
			}
		}
		final Contract.Relation relation = new Contract.Relation(logger,name,
				mode_set, field_set, key_set, message_set);
		// logger.info("relation element {}", relation);
		return relation;
	}

	/**
	 * The relation mode is an optionally repeated element. <code>
      ...
      <mode type="isAmmo" value="foo">some text</mode> 
     * </code
	 * 
	 * 
	 * @param xmldtype
	 * @return
	 */
	public Contract.Mode parseMode(Element xml) {
		final String type = xml.getAttribute("type");
		final Contract.Mode rm = new Contract.Mode(type, extract_attr_name(xml,
				"value"), xml.getTextContent());
		// logger.info("relation mode element {}", rm);
		return rm;
	}

	private Contract.Name extract_attr_name(final Element xml,
			final String attrName) {
		final String value = xml.getAttribute(attrName);

		if (!value.isEmpty()) {
			return new Contract.Name(value);
		}

		final NamedNodeMap attrMap = xml.getAttributes();
		final int attrCount = attrMap.getLength();
		final StringBuilder sb = new StringBuilder();
		for (int ix = 0; ix < attrCount; ix++) {
			final Attr attr = (Attr) attrMap.item(ix);
			sb.append(attr.getName()).append(":").append(attr.getNodeValue())
					.append(" ");
		}
		logger.warn("attr {} map {}", attrCount, sb.toString());
		if (!value.isEmpty()) {
			return new Contract.Name(value);
		}
		return new Contract.Name("");
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
	public Contract.Field parseField(final Element xml) {
		final String initial = xml.getAttribute("default");

		final List<Contract.Enumeration> enum_set = new ArrayList<Contract.Enumeration>();

		final NodeList nodeList = xml.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element currentElement = (Element) currentNode;
				final String tagName = currentElement.getTagName();
				if ("enum".equals(tagName)) {
					enum_set.add(parseEnumeration(currentElement));
					continue;
				}
			}
		}
		final String type = xml.getAttribute("type");
		Contract.Field field = null;
		try {
			field = new Contract.Field(extract_attr_name(xml, "name"), type,
					initial, xml.getTextContent(), enum_set);
			// logger.info("field element {}", field);
		} catch (Exception ex) {
			logger.error(
					"field element failed on Type: {}, unsupported Field Type.",
					type);
		}
		return field;
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

	public Contract.Key parseKey(final Element xml) {
		final Name keyName = extract_attr_name(xml, "name");
		final List<Contract.KeyFieldRef> field_set = new ArrayList<Contract.KeyFieldRef>();

		final NodeList nodeList = xml.getChildNodes();
		for (int ix = 0; ix < nodeList.getLength(); ix++) {
			final Node currentNode = nodeList.item(ix);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element currentElement = (Element) currentNode;
				final String tagName = currentElement.getTagName();
				if ("ref".equals(tagName)) {
					field_set.add(new Contract.KeyFieldRef(extract_attr_name(
							currentElement, "field")));
				}
			}
		}
		final Contract.Key key = new Contract.Key(keyName, field_set);
		// logger.info("key element {}", key);
		return key;
	}

	public Contract.Message parseMessage(final Element xml) {
		// logger.info("message element {}", xml);
		return null;
	}

	public Contract.Enumeration parseEnumeration(final Element xml) {
		final Contract.Name key = new Contract.Name(xml.getAttribute("key"));
		final int ordinal = Integer.parseInt(xml.getAttribute("value"));
		final Contract.Enumeration enumeration = new Contract.Enumeration(key,
				ordinal);
		// logger.info("enumeration element {}", enumeration);
		return enumeration;
	}
}
