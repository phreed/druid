package main.java.com.walkernation.db.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Vladimir Vivien (http://vladimirvivien.com/)
 */
public class ContentDescriptor {
	// utility variables
	public static final String AUTHORITY = "com.walkernation.db.locationsprovider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final UriMatcher URI_MATCHER = buildUriMatcher();

	private ContentDescriptor() {
	};

	// register identifying URIs for Restaurant entity
	// the TOKEN value is associated with each URI registered
	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = AUTHORITY;
		matcher.addURI(authority, Location.PATH, Location.PATH_TOKEN);
		matcher.addURI(authority, Location.PATH_FOR_ID,
				Location.PATH_FOR_ID_TOKEN);

		return matcher;
	}

	// Define a static class that represents description of stored content
	// entity.
	// Here we define Restaurant
	public static class Location {
		// an identifying name for entity
		public static final String NAME = "main_table";

		// define a URI paths to access entity
		// BASE_URI/restaurants - for list of restaurants
		// BASE_URI/restaurants/* - retreive specific restaurant by id
		// the toke value are used to register path in matcher (see above)
		public static final String PATH = "locations";
		public static final int PATH_TOKEN = 100;
		public static final String PATH_FOR_ID = "location/*";
		public static final int PATH_FOR_ID_TOKEN = 200;

		// URI for all content stored as Restaurant entity
		// BASE_URI + PATH ==>
		// "content://com.favrestaurant.contentprovider/restaurants";
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		// define content mime type for entity
		public static final String CONTENT_TYPE_DIR = "com.walkernation.cursor.dir/com.walkernation.db";
		public static final String CONTENT_ITEM_TYPE = "com.walkernation.cursor.item/com.walkernation.db";

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