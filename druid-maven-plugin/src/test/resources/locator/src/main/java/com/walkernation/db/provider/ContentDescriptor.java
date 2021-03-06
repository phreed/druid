package com.walkernation.db.provider;

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
	public static final String PROJECT_NAME = "db";
	public static final String PROVIDER_NAME = "locationsprovider";

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
		// add in single and
		matcher.addURI(AUTHORITY, Location.PATH, Location.PATH_TOKEN);
		matcher.addURI(AUTHORITY, Location.PATH_FOR_ID,
				Location.PATH_FOR_ID_TOKEN);

		return matcher;
	}

	// Define a static class that represents description of stored content
	// entity.
	// Here we define Restaurant
	public static class Location {
		// an identifying name for entity
		public static final String TABLE_NAME = "main_table";

		// define a URI paths to access entity
		// BASE_URI/restaurants - for list of locations
		// BASE_URI/restaurants/* - retrieve specific location by id
		// the token value are used to register path in matcher (see above)
		public static final String PATH = "locations";
		public static final int PATH_TOKEN = 100;
		public static final String PATH_FOR_ID = "location/*";
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
		public static final String[] ORM_COLUMN_NAMES = { Cols.LAT_NAME,
				Cols.LONG_NAME, Cols.HEIGHT_NAME, Cols.USER_ID_NAME };
		// the names and order of ALL columns, including internal use ones
		public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
				Cols.LAT_NAME, Cols.LONG_NAME, Cols.HEIGHT_NAME,
				Cols.USER_ID_NAME };

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			// The name and column index of each column in your database
			public static final String LAT_NAME = "latitude";
			public static final String LONG_NAME = "longitude";
			public static final String HEIGHT_NAME = "height";
			public static final String USER_ID_NAME = "user_id";
		}

	}
}