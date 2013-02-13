package edu.vanderbilt.isis.druid.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.stringtemplate.v4.AttributeRenderer;
import org.w3c.dom.NodeList;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class Contract {
	@SuppressWarnings("unused")
	final private Logger logger;

	public final Root root;

	public Contract(final Logger logger, final Root root)
			throws GeneratorException {
		this.logger = logger;
		this.root = root;
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
	}

	/**
	 * The root object
	 */
	public static class Root {
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

		public Root(final Name name, final String sponsor,
				List<Relation> relations) {
			this.name = name;
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
		final private RMode mode;
		final private List<Field> fields;
		final private List<FieldRef> keycols;
		final private List<Message> messages;
		final private List<FieldRef> uicols;
		final private ArrayList<String> dataTypes;

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

		public List<String> getDataTypes() {
			return dataTypes;
		}

		public List<Message> getMessages() {
			return this.messages;
		}

		public List<FieldRef> getUI() {
			return this.uicols;
		}

		public Relation(final Name name, final RMode mode,
				final List<Field> fields, final List<FieldRef> keycols,
				final List<Message> messages, List<FieldRef> uicols)
				throws Exception {
			this.name = name;
			this.mode = mode;

			this.dataTypes = new ArrayList<String>();
		//	System.out.println("\n\n\n");
			for (Field field : fields) {
			//	System.out.print(""+field.getDtype()+"");
				if (this.dataTypes.contains(field.getDtype()) == false) {
					this.dataTypes.add(field.getDtype());
				//	System.out.println("     was not found, was added");
				}else
				{
				//	System.out.println("     was found, was not added");
				}
			}
		//	System.out.println("\n\n\n");
			final ArrayList<Name> fieldNames = new ArrayList<Name>();
			for (Field field : fields) {
				field.setParent(this);
				fieldNames.add(field.getName());
			}
			this.fields = fields;
			// check every fieldRef
			eachFieldRef: for (FieldRef fieldRef : keycols) {
				// check to see if is abrv or full length.
				if (fieldNames.contains(fieldRef.getName()) == false) {
					for (Field field : fields) {
						// see if field has abrv that matches fieldRef
						if (field.abrv.getNorm().equals(
								fieldRef.getName().getNorm()) == true) {
							fieldRef = new FieldRef(field.getName());
							continue eachFieldRef;
						}
					}// end each field
					throw new Exception("Error with FieldRef '"
							+ fieldRef.getName()
							+ "', no matching Field found.");
				}// end check if field.name already
				fieldRef.setParent(this);
			}
			this.keycols = keycols;
			this.messages = messages;
			this.uicols = uicols;

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
			for (FieldRef ref : this.keycols) {
				sb.append(ref.toString());
			}
			for (Message message : this.messages) {
				if (message == null)
					continue;
				sb.append(message.toString());
			}
			sb.append("\n</relation>");
			return sb;
		}
	}

	public static class RMode {
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

		public RMode(final Name name, final String dtype,
				final String description) {
			this.name = name;
			this.dtype = dtype;
			this.description = description.trim();
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			return this.toString(sb).toString();
		}

		public StringBuilder toString(final StringBuilder sb) {
			return sb.append("<mode name='").append(name.norm)
					.append("' type='").append(dtype).append("'>")
					.append(description).append("</mode>");
		}
	}

	public static class UI {
		private final Name name;
		private final List<FieldRef> fields;
		private Relation parent;

		public List<FieldRef> getFields() {
			return fields;
		}

		public Name getName() {
			return this.name;
		}

		UI(List<FieldRef> fields, Name name) {
			this.fields = fields;
			this.name = name;
		}

		public void setParent(final Relation parent) {
			this.parent = parent;
		}

		public Relation getParent() {
			return this.parent;
		}

	}

	/**
	 * A column in a table (relation).
	 */
	public static class Field implements Predecessor {
		private final Name name;
		private final Name abrv;
		private final String dtype;
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

		public String getIsInKeys() {
			for (FieldRef ref : parent.keycols) {
				if (ref.getName().getSnake().equals(this.getName().getSnake())) {
					return "yes";
				}
			}
			return null;
		}

		/**
		 * The object must be found in the list and it cannot be the first item
		 * for this to work.
		 */
		public Predecessor getPredecessor() {
			final int ix = parent.fields.indexOf(this) - 1;
			if (ix < 0) {
				return null;
			}
			return parent.fields.get(ix);
		}

		public Field(final Name name, Name abrv, final String dtype,
				final String initial, final String description,
				final List<Enumeration> enum_set) {
			this.name = name;
			this.dtype = dtype;
			this.abrv = abrv;
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

	public static class FieldRef implements Predecessor {
		private final Name name;
		private Relation parent;

		public Name getName() {
			return name;
		}

		public Predecessor getPredecessor() {
			final int ix = parent.keycols.indexOf(this) - 1;
			System.out.println("\n\n\n Index in keycols: " + ix + "\n\n\n");
			if (ix < 0) {
				return null;
			}
			return parent.keycols.get(ix);

		}

		public void setParent(final Relation parent) {
			this.parent = parent;
		}

		public FieldRef(final Name name) {
			this.name = name;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			return this.toString(sb).toString();
		}

		public StringBuilder toString(final StringBuilder sb) {
			return sb.append("<ref field='").append(name.norm).append("'/>");
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
			return sb.append("<enum key=\"").append(key).append('"')
					.append(' ').append("value=\"").append(ordinal)
					.append("\"/>\n");
		}
	}

}
