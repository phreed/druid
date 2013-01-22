
package edu.vanderbilt.isis.druid.generator;

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

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class Contract {
    final private Logger logger;

    public final Root root;

    public static Contract newInstance(final Logger logger, final File contractFile)
            throws GeneratorException {
        return new Contract(logger, contractFile);
    }

    public Contract(final Logger logger, final File contractFile) throws GeneratorException {
        this.logger = logger;

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
        this.root = Root.newInstance(de);
        logger.trace("contract {}", this.root);
    }
    
    /**
     * convert the string to snake case
     * 
     * @param name
     * @return
     */
    static public String snake_case(final String name) {
        final StringBuilder sb = new StringBuilder();
        for (String seg : name.split(" ")) {
            sb.append(seg.toLowerCase());
            sb.append('_');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * convert the string to camel case
     * 
     * @param name
     * @return
     */
    static public String camelCase(final String name) {
        if (name.length() < 1)
            return "";
        final StringBuilder sb = new StringBuilder();
        final String[] sl = name.split(" ");
        if (sl.length < 1)
            return "";
        sb.append(sl[0].toLowerCase());
        for (int ix = 1; ix < sl.length; ++ix) {
            if (sl[ix].length() < 1)
                continue;
            sb.append(sl[ix].substring(0, 1).toUpperCase());
            if (sl[ix].length() > 0)
                sb.append(sl[ix].substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * Used for multiple presentation types.
     */
    static class Name {
        private final String norm;
        private final String camel;
        private final String snake;

        public String getNorm() {
            return norm;
        }

        public String getCamel() {
            return camel;
        }

        public String getSnake() {
            return snake;
        }

        private Name(final String norm, final String camel, final String snake) {
            this.norm = norm;
            this.camel = camel;
            this.snake = snake;
        }

        public Name(final String name) {
            this(name, camelCase(name), snake_case(name));
        }

        public Name(final NodeList seq) {
            this(seq.item(0).getTextContent());
        }

        private static String capitalize(String str) {
            if (str.length() < 1)
                return "";
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        @Override
        public String toString() {
            return this.norm;
        }

        public String getCobra() {
            return snake.toUpperCase();
        }

        public String getBactrian() {
            return capitalize(camel);
        }
    }

    static class Sponsor {
        final private String base;
        final private String[] path;
        
        private Sponsor(final String base) {
            this.base = base;
            this.path = base.split("\\.");

        }
        
        public String getBase() {
            return this.base;
        }
        
        public File getPath(final File basedir) {
            File wip = basedir.getAbsoluteFile();
            for (String node : this.path){
                wip = new File(wip, node);
            }
            return wip;
        }
    }
    /**
     * The root object
     */
    static class Root {
        final private Name name;
        final private Sponsor sponsor;
        final private List<Relation> relations;

        public Name getName() {
            return name;
        }

        public Sponsor getSponsor() {
            return sponsor;
        }

        public List<Relation> getRelations() {
            return relations;
        }

        private Root(final Name name, final String sponsor,
                List<Relation> relations) {
            this.name = name;
            this.sponsor = new Sponsor(sponsor);
            this.relations = relations;
        }

        static public Root newInstance(final Element xml) {
            String sponsor = "";
            final NodeList sl = xml.getElementsByTagName("sponsor");
            for (int ix = 0; ix < sl.getLength();) {
                Element el = (Element) sl.item(ix);
                sponsor = el.getAttribute("name");
                break;
            }

            final List<Relation> relation_set = new ArrayList<Relation>();
            final NodeList nl = xml.getElementsByTagName("relation");
            for (int ix = 0; ix < nl.getLength(); ++ix) {
                final Relation relation = Relation.newInstance((Element) nl
                        .item(ix));
                relation_set.add(relation);
            }

            return new Root(extract_name(xml, "name"), sponsor,
                    relation_set);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<provider name=\"").append(name.norm).append("\" ")
                    .append(" sponsor=\"").append(sponsor).append("\">\n");
            for (Relation relation : this.relations) {
                sb.append(relation.toString());
            }
            sb.append("\n</provider>");
            return sb.toString();
        }
    }

    /**
     * Collect table information.
     */
    static class Relation {
        final private Name name;
        final private RMode mode;
        final private List<Field> fields;
        final private List<FieldRef> keycols;

        public Name getName() {
            return name;
        }

        public RMode getMode() {
            return mode;
        }

        public List<Field> getFields() {
            return fields;
        }

        public List<FieldRef> getKeycols() {
            return keycols;
        }

        private Relation(final Name name, final RMode mode,
                final List<Field> fields, final List<FieldRef> keycols) {
            this.name = name;
            this.mode = mode;
            this.fields = fields;
            this.keycols = keycols;
        }

        static public Relation newInstance(final Element xml) {
            final List<Field> field_set = new ArrayList<Field>();
            final NodeList nl = xml.getElementsByTagName("field");
            for (int ix = 0; ix < nl.getLength(); ++ix) {
                field_set.add(Field.newInstance((Element) nl.item(ix)));
            }

            // process the key columns
            final List<FieldRef> keycol_set = new ArrayList<FieldRef>();
            final NodeList kl = xml.getElementsByTagName("key");
            for (int ix = 0; ix < kl.getLength(); ++ix) {
                NodeList rl = xml.getElementsByTagName("ref");
                for (int jx = 0; jx < rl.getLength(); ++jx) {
                    keycol_set.add(FieldRef.newInstance((Element) rl.item(jx)));
                }
            }

            RMode mode = null;
            final NodeList ml = xml.getElementsByTagName("mode");
            for (int ix = 0; ix < ml.getLength();) {
                mode = RMode.newInstance((Element) ml.item(ix));
                break;
            }

            return new Relation(extract_name(xml, "name"), mode, field_set,
                    keycol_set);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("<relation name=").append(name.norm).append(">\n");
            for (Field field : this.fields) {
                sb.append(field.toString());
            }
            for (FieldRef ref : this.keycols) {
                sb.append(ref.toString());
            }
            sb.append("\n</relation>");
            return sb.toString();
        }
    }

    static class RMode {
        private final Name name;
        private final String dtype;
        private final String description;

        public Name getName() {
            return name;
        }

        public String getDtype() {
            return dtype;
        }

        public String getDescription() {
            return description;
        }

        private RMode(final Name name, final String dtype,
                final String description) {
            this.name = name;
            this.dtype = dtype;
            this.description = description.trim();
        }

        static public RMode newInstance(Element xml) {
            final String type = xml.getAttribute("type");
            return new RMode(extract_name(xml, "name"), type,
                    xml.getTextContent());
        }

        @Override
        public String toString() {
            return new StringBuilder().append("<mode name='").append(name.norm)
                    .append("' type='").append(dtype).append("'>")
                    .append(description).append("</field>").toString();
        }
    }

    static public Name extract_name(final Element xml, final String attr) {
        return new Name(xml.getAttribute(attr));
    }

    static public RMode extract_mode(final Element xml) {
        return null;
    }

    /**
     * A column in a table (relation).
     */
    static class Field {
        private final Name name;
        private final String dtype;
        private final String initial;
        private final String description;
        private final List<Enumeration> enums;

        public Name getName() {
            return name;
        }

        public String getDtype() {
            return dtype;
        }

        public String getDefault() {
            return initial;
        }

        public String getDescription() {
            return description;
        }

        public List<Enumeration> getEnums() {
            return enums;
        }

        private Field(final Name name, final String dtype,
                final String initial, final String description,
                final List<Enumeration> enum_set) {
            this.name = name;
            this.dtype = dtype;
            this.initial = initial;
            this.description = description.trim();
            this.enums = enum_set;
        }

        static public Field newInstance(final Element xml) {
            final String initial = xml.getAttribute("default");

            final List<Enumeration> enum_set = new ArrayList<Enumeration>();
            final NodeList enum_list = xml.getElementsByTagName("enum");
            for (int ix = 0; ix < enum_list.getLength(); ++ix) {
                enum_set.add(Enumeration.newInstance((Element) enum_list
                        .item(ix)));
            }

            final String type = xml.getAttribute("type");
            return new Field(extract_name(xml, "name"), type, initial,
                    xml.getTextContent(), enum_set);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("<field name='").append(name.norm).append("' type='")
                    .append("'>");
            sb.append(description);
            for (Enumeration en : this.enums) {
                sb.append(en.toString());
            }
            sb.append("</field>\n");
            return sb.toString();
        }
    }

    static class FieldRef {
        private final Name name;

        public Name getName() {
            return name;
        }

        private FieldRef(final Name name) {
            this.name = name;
        }

        static public FieldRef newInstance(final Element xml) {
            return new FieldRef(extract_name(xml, "field"));
        }

        @Override
        public String toString() {
            return new StringBuilder().append("<ref field='").append(name.norm)
                    .append("' />").toString();
        }
    }

    /**
     * A column in a table (relation).
     */
    static class Enumeration {
        private final Name key;
        private final int ordinal;

        public Name getKey() {
            return key;
        }

        public String getOrdinal() {
            return Integer.toString(ordinal);
        }

        private Enumeration(final Name key, final int ordinal) {
            this.key = key;
            this.ordinal = ordinal;
        }

        static public Enumeration newInstance(final Element xml) {
            Name key = new Name(xml.getAttribute("key"));
            int ordinal = Integer.parseInt(xml.getAttribute("value"));
            return new Enumeration(key, ordinal);
        }

        @Override
        public String toString() {
            return new StringBuilder().append("<enum key=\"").append(key)
                    .append('"').append(' ').append("value=\"").append(ordinal)
                    .append("\"/>\n").toString();
        }
    }

}
