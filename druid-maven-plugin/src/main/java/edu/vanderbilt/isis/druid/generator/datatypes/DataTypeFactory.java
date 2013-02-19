package edu.vanderbilt.isis.druid.generator.datatypes;

import java.util.HashMap;

public class DataTypeFactory {

	public static BaseDataType getType(String str) throws Exception {
		System.out.println("\n\n" + str + "\n\n");
		return DataTypes.getType(str);
	}

	static private enum DataTypes {
		// **** Old AMMO supported types **** //
		NULL("NULL"), //
		BOOL("BOOL"), //
		BLOB("BLOB"), //
		FLOAT("FLOAT"), //
		INTEGER("INTEGER"), //
		LONG("LONG"), //
		TEXT("TEXT"), //
		REAL("REAL"), //
		FK("FK"), //
		GUID("GUID"), //
		EXCLUSIVE("EXCLUSIVE"), //
		INCLUSIVE("INCLUSIVE"), //
		TIMESTAMP("TIMESTAMP"), //
		SHORT("SHORT"), //
		FILE("FILE"), //
		SERIAL("SERIAL"), //
		// **** New DRUID supported types 1.0 **** //
		DOUBLE("DOUBLE"), //
		STRING("STRING"); //

		final static HashMap<String, DataTypes> map;

		static {
			map = new HashMap<String, DataTypes>();
			// **** Old AMMO supported types **** //
			map.put(NULL.getText(), NULL);
			map.put(BOOL.getText(), BOOL);
			map.put(BLOB.getText(), BLOB);
			map.put(FLOAT.getText(), FLOAT);
			map.put(INTEGER.getText(), INTEGER);
			map.put(LONG.getText(), LONG);
			map.put(TEXT.getText(), TEXT);
			map.put(REAL.getText(), REAL);
			map.put(FK.getText(), FK);
			map.put(GUID.getText(), GUID);
			map.put(EXCLUSIVE.getText(), EXCLUSIVE);
			map.put(INCLUSIVE.getText(), INCLUSIVE);
			map.put(TIMESTAMP.getText(), TIMESTAMP);
			map.put(SHORT.getText(), SHORT);
			map.put(FILE.getText(), FILE);
			map.put(SERIAL.getText(), SERIAL);
			// **** New DRUID supported types 1.0 **** //
			map.put(DOUBLE.getText(), DOUBLE);
			map.put(STRING.getText(), STRING);

		}

		static public BaseDataType getType(String str) throws Exception {

			switch (getDataTypeCase(str)) {
			// **** Old AMMO supported types **** //
			case NULL: {
				return new NullType();
			}
			case BOOL: {
				// System.out.println("               FOUND A BOOL ");
				return new BoolType();
			}
			case BLOB: {
				return new BlobType();
			}
			case INTEGER: {
				return new IntegerType();
			}
			case LONG: {
				return new LongType();
			}
			case TEXT: { // return StringType
				return new StringType();
			}
			case REAL: { // return FloatType
				return new FloatType();
			}
			case FK: {
				return new FKType();
			}
			case GUID: {
				return new GUIDType();
			}
			case EXCLUSIVE: {
				return new ExclusiveType();
			}
			case INCLUSIVE: {
				return new InclusiveType();
			}
			case TIMESTAMP: {
				return new TimestampType();
			}
			case SHORT: {
				return new ShortType();
			}
			case FILE: {
				return new FileType();
			}
			case FLOAT: {
				return new FloatType();
			}
			// **** New DRUID supported types 1.0 **** //
			case STRING: {
				return new StringType();
			}
			case DOUBLE: {
				return new DoubleType();
			}
			case SERIAL: {
				return new SerialType();
			}
			default:
				return null;

			}// end switch statement
		}

		static DataTypes getDataTypeCase(String str) throws Exception {
			DataTypes rValue = map.get(str);
			if (rValue == null) {
				throw new Exception("Error in Contract : " + str
						+ " is not a valid DataType");
			}
			return rValue;
		}

		private final String text;

		DataTypes(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

}
