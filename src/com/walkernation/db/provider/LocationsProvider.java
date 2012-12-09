package com.walkernation.db.provider;

import com.walkernation.db.orm.LocationData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class LocationsProvider extends ContentProvider {

	private static final String LOG_TAG = LocationsProvider.class
			.getCanonicalName();

	LocationDataDBAdaptor mDB;
	public static String AUTHORITY = "com.walkernation.db.locationsprovider";
	static String BASE_PATH = "location";
	private final static String myURI = "content://" + AUTHORITY + "/"
			+ BASE_PATH;
	public final static Uri CONTENT_URI = Uri.parse(myURI);

	// create constants used to differentiate between the different URI requests
	private static final int ALLROWS = ContentDescriptor.Location.PATH_TOKEN;
	private static final int SINGLE_ROW = ContentDescriptor.Location.PATH_FOR_ID_TOKEN;

	private static final UriMatcher uriMatcher = ContentDescriptor.URI_MATCHER;

	@Override
	public boolean onCreate() {
		Log.d(LOG_TAG, "onCreate");
		mDB = new LocationDataDBAdaptor(getContext());
		mDB.open();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		Log.d(LOG_TAG, "getType");
		switch (uriMatcher.match(uri)) {
		case ContentDescriptor.Location.PATH_TOKEN:
			return ContentDescriptor.Location.CONTENT_TYPE_DIR;
		case ContentDescriptor.Location.PATH_FOR_ID_TOKEN:
			return ContentDescriptor.Location.CONTENT_ITEM_TYPE;
		default:
			throw new UnsupportedOperationException("URI " + uri
					+ " is not supported.");
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(LOG_TAG, "query");

		final int match = uriMatcher.match(uri);
		switch (match) {
		// retrieve restaurant list
		case ContentDescriptor.Location.PATH_TOKEN: {
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(ContentDescriptor.Location.NAME);
			return builder.query(mDB.getDB(), projection, selection,
					selectionArgs, null, null, sortOrder);
		}
		default:
			return null;
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues value) {
		Log.d(LOG_TAG, "insert(ContentValues)");

		long rowID = mDB.insertLocation(value);
		if (rowID > 0) {
			return ContentUris.withAppendedId(CONTENT_URI, rowID);
		}
		throw new SQLException("Failed to Add new item into " + uri);
	}

	/**
	 * Don't know if this actually useful or not, but 'might' be, need to look
	 * more into content providers
	 * 
	 * @param uri
	 * @param value
	 * @return
	 */
	public Uri insert(Uri uri, LocationData value) {
		Log.d(LOG_TAG, "insert(LocationData)");
		long rowID = mDB.insertLocation(value);
		if (rowID > 0) {
			return ContentUris.withAppendedId(CONTENT_URI, rowID);
		}
		throw new SQLException("Failed to Add new item into " + uri);
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		Log.d(LOG_TAG, "delete" + whereClause + ", " + whereArgs);
		matchURI(uri, whereClause);
		int count = mDB.delete(whereClause, whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private void matchURI(Uri uri, String whereClause) {
		Log.d(LOG_TAG,
				"matchURI" + uri.toString() + ", " + uriMatcher.match(uri));
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			break;
		case SINGLE_ROW:
			whereClause = whereClause + "_id = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		Log.d(LOG_TAG, "update");
		int count;
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			count = mDB.update(values, whereClause, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
