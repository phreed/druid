package com.walkernation.multiple.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Michael A. Walker
 *         <p>
 *         based on the work by Vladimir Vivien (http://vladimirvivien.com/),
 *         which provides a very logical organization of the meta-data of the
 *         Database and Content Provider
 *         <p>
 *         This note might be moved to a 'Special Thanks' section once one is
 *         created and moved out of future test code.
 */
public class ContentDescriptor {

	/**
	 * Project Related Constants
	 */

	public static final String ORGANIZATIONAL_NAME = "com.walkernation";
	public static final String PROJECT_NAME = "multiple";
	public static final String PROVIDER_NAME = "provider";

	/**
	 * ConentProvider Related Constants
	 */
	public static final String AUTHORITY = ORGANIZATIONAL_NAME + "."
			+ PROJECT_NAME + "." + PROVIDER_NAME;
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final UriMatcher URI_MATCHER = buildUriMatcher();

	// register identifying URIs for Restaurant entity
	// the TOKEN value is associated with each URI registered
	private static UriMatcher buildUriMatcher() {

		// add default 'no match' result to matcher
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// add matches for DataTypeOne
		matcher.addURI(AUTHORITY, DataTypeOne.PATH, DataTypeOne.PATH_TOKEN);
		matcher.addURI(AUTHORITY, DataTypeOne.PATH_FOR_ID,
				DataTypeOne.PATH_FOR_ID_TOKEN);
		// add matches for DataTypeTwo
		matcher.addURI(AUTHORITY, DataTypeTwo.PATH, DataTypeTwo.PATH_TOKEN);
		matcher.addURI(AUTHORITY, DataTypeTwo.PATH_FOR_ID,
				DataTypeTwo.PATH_FOR_ID_TOKEN);

		return matcher;
	}

	/**
	 * Meta Data about data for this table
	 */
	public static class DataTypeOne {
		// an identifying name for entity
		public static final String TABLE_NAME = "DataTypeOne";

		// define a URI paths to access entity
		// BASE_URI/restaurants - for list of locations
		// BASE_URI/restaurants/* - retrieve specific location by id
		// the token value are used to register path in matcher (see above)
		public static final String PATH = TABLE_NAME + "s";
		public static final int PATH_TOKEN = 100;
		public static final String PATH_FOR_ID = TABLE_NAME + "/*";
		public static final int PATH_FOR_ID_TOKEN = 200;

		// URI for all content stored as Restaurant entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		// TODO figure out why 'db' is here...
		// might have been convention of example code I used previously
		// or might have some actual meaning
		private final static String MIME_TYPE_END = "db";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, excluding internal use ones
		public static final String[] ORM_COLUMN_NAMES = {
				ColumnNames.BYTE_NAME, ColumnNames.SHORT_NAME,
				ColumnNames.INT_NAME, ColumnNames.LONG_NAME,
				ColumnNames.FLOAT_NAME, ColumnNames.DOUBLE_NAME,
				ColumnNames.STRING_NAME, ColumnNames.BOOLEAN_NAME };
		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { ColumnNames.ID,
				ColumnNames.BYTE_NAME, ColumnNames.SHORT_NAME,
				ColumnNames.INT_NAME, ColumnNames.LONG_NAME,
				ColumnNames.FLOAT_NAME, ColumnNames.DOUBLE_NAME,
				ColumnNames.STRING_NAME, ColumnNames.BOOLEAN_NAME };

		// a static class to store columns in entity
		public static class ColumnNames {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database

			// 8-bit signed two's complement integer
			public static final String BYTE_NAME = "BYTE_NAME";
			// 16 bit signed two's complement integer
			public static final String SHORT_NAME = "SHORT_NAME";
			// 32 bit signed two's complement integer
			public static final String INT_NAME = "INT_NAME";
			// 64bit signed two's complement integer
			public static final String LONG_NAME = "LONG_NAME";
			// single-precision 32-bit IEEE 754 floating point
			public static final String FLOAT_NAME = "FLOAT_NAME";
			// double-precision 64-bit IEEE 754 floating point
			public static final String DOUBLE_NAME = "DOUBLE_NAME";
			// // flags that track true/false conditions
			// public static final String BOOLEAN_NAME = "BOOLEAN_NAME";
			// single 16-bit Unicode character
			// public static final String CHAR_NAME = "CHAR NAME";
			// // array of 8-bit signed two's complement integers
			// public static final String BYTE_ARRAY_NAME = "BYTE_ARRAY_NAME";
			// Java String class data
			public static final String STRING_NAME = "STRING_VARIABLE";

			public static final String BOOLEAN_NAME = "BOOLEAN_NAME";

			/**
			 * these two are commented out for now to focus on the others
			 * */
			// // a File
			// public static final String FILE_NAME = "FILE_NAME";
			// // a BLOB of binary data
			// public static final String BLOB_NAME = "BLOB_NAME";

		}

	}

	/**
	 * Meta Data about data for this table
	 */
	public static class DataTypeTwo {
		// an identifying name for entity
		public static final String TABLE_NAME = "DataTypeTwo";

		// define a URI paths to access entity
		// BASE_URI/restaurants - for list of locations
		// BASE_URI/restaurants/* - retrieve specific location by id
		// the token value are used to register path in matcher (see above)
		public static final String PATH = TABLE_NAME + "s";
		public static final int PATH_TOKEN = 300;
		public static final String PATH_FOR_ID = TABLE_NAME + "/*";
		public static final int PATH_FOR_ID_TOKEN = 400;

		// URI for all content stored as Restaurant entity
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		// TODO figure out why 'db' is here...
		// might have been convention of example code I used previously
		// or might have some actual meaning
		private final static String MIME_TYPE_END = "db";

		// define the MIME type of data in the content provider
		public static final String CONTENT_TYPE_DIR = ORGANIZATIONAL_NAME
				+ ".cursor.dir/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;
		public static final String CONTENT_ITEM_TYPE = ORGANIZATIONAL_NAME
				+ ".cursor.item/" + ORGANIZATIONAL_NAME + "." + MIME_TYPE_END;

		// the names and order of ALL columns, excluding internal use ones
		public static final String[] ORM_COLUMN_NAMES = {
				ColumnNames.BYTE_NAME, ColumnNames.SHORT_NAME,
				ColumnNames.INT_NAME, ColumnNames.LONG_NAME,
				ColumnNames.FLOAT_NAME, ColumnNames.DOUBLE_NAME,
				ColumnNames.STRING_NAME, ColumnNames.BOOLEAN_NAME };
		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { ColumnNames.ID,
				ColumnNames.BYTE_NAME, ColumnNames.SHORT_NAME,
				ColumnNames.INT_NAME, ColumnNames.LONG_NAME,
				ColumnNames.FLOAT_NAME, ColumnNames.DOUBLE_NAME,
				ColumnNames.STRING_NAME, ColumnNames.BOOLEAN_NAME };

		// a static class to store columns in entity
		public static class ColumnNames {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database

			// 8-bit signed two's complement integer
			public static final String BYTE_NAME = "BYTE_NAME";
			// 16 bit signed two's complement integer
			public static final String SHORT_NAME = "SHORT_NAME";
			// 32 bit signed two's complement integer
			public static final String INT_NAME = "INT_NAME";
			// 64bit signed two's complement integer
			public static final String LONG_NAME = "LONG_NAME";
			// single-precision 32-bit IEEE 754 floating point
			public static final String FLOAT_NAME = "FLOAT_NAME";
			// double-precision 64-bit IEEE 754 floating point
			public static final String DOUBLE_NAME = "DOUBLE_NAME";
			// // flags that track true/false conditions
			// public static final String BOOLEAN_NAME = "BOOLEAN_NAME";
			// single 16-bit Unicode character
			// public static final String CHAR_NAME = "CHAR NAME";
			// // array of 8-bit signed two's complement integers
			// public static final String BYTE_ARRAY_NAME = "BYTE_ARRAY_NAME";
			// Java String class data
			public static final String STRING_NAME = "STRING_VARIABLE";

			public static final String BOOLEAN_NAME = "BOOLEAN_NAME";

			/**
			 * these two are commented out for now to focus on the others
			 * */
			// // a File
			// public static final String FILE_NAME = "FILE_NAME";
			// // a BLOB of binary data
			// public static final String BLOB_NAME = "BLOB_NAME";

		}

	}

}