package com.walkernation.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class LocationsProvider extends ContentProvider {

	// logger TAG
	private static final String LOG_TAG = LocationsProvider.class
			.getCanonicalName();

	// Local backend DB
	LocationDataDBAdaptor mDB;

	// shorten variable names from C.D. for easier readability
	public final static Uri CONTENT_URI = ContentDescriptor.Location.CONTENT_URI;
	public static String AUTHORITY = ContentDescriptor.AUTHORITY;
	private static final int ALLROWS = ContentDescriptor.Location.PATH_TOKEN;
	private static final int SINGLE_ROW = ContentDescriptor.Location.PATH_FOR_ID_TOKEN;
	private static final UriMatcher uriMatcher = ContentDescriptor.URI_MATCHER;

	@Override
	public boolean onCreate() {
		Log.d(LOG_TAG, "onCreate()");
		mDB = new LocationDataDBAdaptor(getContext());
		mDB.open();
		return true;
	}

	@Override
	public String getType(Uri uri) {
		Log.d(LOG_TAG, "getType()");
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
		Log.d(LOG_TAG, "query()");
		final int match = uriMatcher.match(uri);
		switch (match) {
		case ALLROWS: {
			SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
			builder.setTables(ContentDescriptor.Location.TABLE_NAME);
			return builder.query(mDB.getDB(), projection, selection,
					selectionArgs, null, null, sortOrder);
		}
		case SINGLE_ROW: {
			// TODO look into how (see if even needed) to implement this.
			return null;
		}
		default:
			return null;
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues value) {
		Log.d(LOG_TAG, "insert()");
		long rowID = mDB.insert(ContentDescriptor.Location.TABLE_NAME, value);
		if (rowID > -1) {
			Uri insertedID = ContentUris.withAppendedId(
					ContentDescriptor.Location.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(insertedID, null);
			return ContentUris.withAppendedId(CONTENT_URI, rowID);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		Log.d(LOG_TAG, "delete()");
		matchURI(uri, whereClause);
		int count = mDB.delete(ContentDescriptor.Location.TABLE_NAME,
				whereClause, whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private void matchURI(Uri uri, String whereClause) {
		Log.d(LOG_TAG, "matchURI()");
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			break;
		case SINGLE_ROW:
			whereClause = whereClause + ContentDescriptor.Location.Cols.ID
					+ " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		Log.d(LOG_TAG, "update()");
		int count;
		switch (uriMatcher.match(uri)) {
		case ALLROWS:

			count = mDB.update(ContentDescriptor.Location.TABLE_NAME, values,
					whereClause, whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}