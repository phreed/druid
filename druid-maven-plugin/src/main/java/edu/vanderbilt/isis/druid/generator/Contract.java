package edu.vanderbilt.isis.druid.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.w3c.dom.NodeList;

import edu.vanderbilt.isis.druid.generator.datatypes.BaseDataType;
import edu.vanderbilt.isis.druid.generator.datatypes.DataTypeFactory;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class Contract {
    @SuppressWarnings("unused")
    final private Logger logger;

    private final Root root;
    private final Map<String,Mode> mode;
    
    public Root getRoot() {
        return this.root;
    }
    public List<Relation> getRelations() {
        return this.root.relations;
    }
    public Map<String,Mode> getMode() {
        return this.mode;
    }

    public Contract(final Logger logger, final Root root, final Map<String, Mode> mode) throws GeneratorException {
        this.logger = logger;
        this.root = root;
        this.mode = mode;
    }

    /**
     * convert the string to snake case
     * 
     * @param name
     * @return
     */
    static public String snake_case(final String name) {
        if (name == null) {
            return "<null-snake>";
        }
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
        if (name == null) {
            return "<null-camel>";
        }
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
    public static class Name {
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
        
        public String getPascal() {
            return capitalize(camel);
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == this) { return true; }
            if (!(obj instanceof Name)) {
                return false;
            }
            final Name that = (Name) obj;
            if (! this.norm.equals(that.norm)) {
                return false;
            }
            return true;
        }
        @Override
        public int hashCode() {
            return this.norm.hashCode();
        }
        
    }

    /**
     * roughly the same as a package.
     */
    public static class Sponsor {
        final private String base;
        final private String[] path;

        private Sponsor(final String base) {
            this.base = base;
            this.path = base.split("\\.");
        }

        public String getBase() {
            return this.base;
        }

        /**
         * Generate a relative path.
         * 
         * @return
         */
        public String getPath() {
            if (path.length < 1)
                return "";
            final StringBuilder sb = new StringBuilder();
            for (String node : this.path) {
                sb.append(node).append(File.separatorChar);
            }
            return sb.toString();
        }

        @Override
        public String toString() {
        return this.base;
    }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Sponsor)) {
                return false;
            }
            final Sponsor that = (Sponsor) obj;
            if (!this.base.equals(that.base)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return this.base.hashCode();
        }
    }

    /**
     * The root object
     */
    public static class Root {
        final private Name name;
        final private Sponsor sponsor;
        final private List<Relation> relations;
        final private Map<String, Mode> mode;

        public Name getName() {
            return name;
        }
        public Map<String,Mode> getMode() {
            return this.mode;
        }

        public Sponsor getSponsor() {
            return sponsor;
        }

        public List<Relation> getRelations() {
            return relations;
        }

        public Root(final Name name, final Map<String, Mode> mode_set, final String sponsor,
                List<Relation> relations) {
            this.name = name;
            this.mode = mode_set;
            this.sponsor = new Sponsor(sponsor);
            this.relations = relations;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            sb.append("<provider name=\"").append(name.norm).append("\" ")
                    .append(" sponsor=\"").append(sponsor).append("\">\n");
            for (Relation relation : this.relations) {
                sb.append(relation.toString());
            }
            sb.append("\n</provider>");
            return sb;
        }
    }

    public interface Predecessor {
        Name getName();
        Predecessor getPredecessor();
    }
    /**
     * Collect table information.
     */
    public static class Relation {
        final private Name name;
        final private Map<String, Mode> mode;
        final private List<Field> fields;
        final private List<Key> keys;
        final private Map<String,Key> keyMap;
        final private List<Message> messages;

        public Name getName() {
            return name;
        }

        public Map<String, Mode> getMode() {
            return mode;
        }

        public List<Field> getFields() {
            return fields;
        }

        public List<Key> getKeys() {
            return this.keys;
        }
        public Map<String, Key> getKeyMap() {
            return this.keyMap;
        }
        
        public List<Message> getMessages() {
            return this.messages;
        }


        public Relation(final Name name, final Map<String,Mode> mode,
                final List<Field> fields, final List<Key> key_set, 
                final List<Message> messages) {
            this.name = name;
            this.mode = mode;
            for (Field field : fields) {
                field.setParent(this);
            }
            this.fields = fields;
            this.keys = key_set;
            this.keyMap = new HashMap<String,Key>(key_set.size());
            for (Key key : key_set) {
                this.keyMap.put(key.name.norm, key);
            }
            this.messages = messages;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            sb.append("<relation name=").append(name.norm).append(">\n");
            for (Field field : this.fields) {
                sb.append(field.toString());
            }
            for (Key key : this.keys) {
                sb.append(key.toString());
            }
            for (Message message : this.messages) {
                if (message == null) continue;
                sb.append(message.toString());
            }
            sb.append("\n</relation>");
            return sb;
        }
    }

    /**
     * Carries the properties which turn on/off various things.
     * The mode is a map.
     */
    public static class Mode {
        private final Name value;
        private final String type;
        private final String description;

        public Name getValue() {
            return this.value;
        }

        public String getType() {
            return this.type;
        }

        public String getDescription() {
            return description;
        }

        public Mode(final String type, final Name value, 
                final String description) {
            this.value = value;
            this.type = type;
            this.description = description.trim();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            return sb.append("<mode type='").append(this.type)
                    .append("' value='").append(this.value).append("'>")
                    .append(description).append("</mode>");
        }
    }
    
    /**
     * A column in a table (relation).
     */
    public static class Field implements Predecessor {
        private final Name name;
        private final String dtype;
        private final BaseDataType dataType;
        private final String initial;
        private final String description;
        private final List<Enumeration> enums;
        private Relation parent;

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
        
        public BaseDataType getBaseDataType(){
        	return dataType;
        }
        
        public BaseDataType getDataType(){
        	return dataType;
        }
        
        /**
         * The object must be found in the list and 
         * it cannot be the first item for this to work.
         */
        public Predecessor getPredecessor() {
            final int ix = parent.fields.indexOf(this) - 1;
            if (ix < 0) {
                return null;
            }
            return parent.fields.get(ix);
        }

        public Field(final Name name, final String dtype,
                final String initial, final String description,
                final List<Enumeration> enum_set) throws Exception {
            this.name = name;
            this.dtype = dtype;
            this.dataType = DataTypeFactory.getType(dtype);
            this.initial = initial;
            this.description = description.trim();
            this.enums = enum_set;
        }
        public void setParent(final Relation parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            sb.append("<field name='").append(name.norm).append("' type='")
                    .append("'>");
            sb.append(description);
            for (Enumeration en : this.enums) {
                sb.append(en.toString());
            }
            sb.append("</field>\n");
            return sb;
        }
    }

    /**
     * A named set of field references.
     */
    public static class Key {
        private final Name name;
        private final List<KeyFieldRef> field_set;

        public Name getName() {
            return name;
        }
        
        public List<KeyFieldRef> getFields() {
            return this.field_set;
        }

        public Key(final Name name, final List<KeyFieldRef> field_set) {
            this.name = name;
            this.field_set = field_set;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            sb.append("<key name='").append(name.norm).append("'>");
            for (KeyFieldRef field : this.field_set) {
                field.toString(sb);
            }
            return sb.append("</key>");
        }
    }
    
    public static class KeyFieldRef {
        private final Name ref;

        public Name getRef() {
            return ref;
        }

        public KeyFieldRef(final Name ref) {
            this.ref = ref;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            return sb.append("<field ref='").append(ref.norm).append("'/>");
        }
       
    }
    

    public static class Message {
        private final Name encoding;
        private final List<MessageField> fields;

        public Name getName() {
            return encoding;
        }

        public Message(final Name encoding, final List<MessageField> fields) {
            this.encoding = encoding;
            this.fields = fields;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            sb.append("<message encoding='").append(encoding.norm).append("'>");
            for (MessageField field : this.fields) {
                field.toString(sb);
            }
            return sb.append("</message>");
        }
    }
    
    public static class MessageField {
        private final Name name;

        public Name getName() {
            return name;
        }

        public MessageField(final Name name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            return new StringBuilder().append("<field ref='").append(name.norm)
                    .append("' />");
        }
    }


    /**
     * A column in a table (relation).
     */
    public static class Enumeration {
        private final Name key;
        private final int ordinal;

        public Name getKey() {
            return key;
        }

        public String getOrdinal() {
            return Integer.toString(ordinal);
        }

        public Enumeration(final Name key, final int ordinal) {
            this.key = key;
            this.ordinal = ordinal;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            return this.toString(sb).toString();
        }
        public StringBuilder toString(final StringBuilder sb) {
            return sb.append("<enum key=\"").append(key)
                    .append('"').append(' ').append("value=\"").append(ordinal)
                    .append("\"/>\n");
        }
    }


}
