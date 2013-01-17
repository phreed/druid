package com.walkernation.multiple.provider;

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
	public final static Uri CONTENT_URI = ContentDescriptor.DataTypeOne.CONTENT_URI;
	public static String AUTHORITY = ContentDescriptor.AUTHORITY;
	private static final int DATA_ONE_ALL_ROWS = ContentDescriptor.DataTypeOne.PATH_TOKEN;
	private static final int DATA_ONE_SINGLE_ROW = ContentDescriptor.DataTypeOne.PATH_FOR_ID_TOKEN;

	private static final int DATA_TWO_ALL_ROWS = ContentDescriptor.DataTypeTwo.PATH_TOKEN;
	private static final int DATA_TWO_SINGLE_ROW = ContentDescriptor.DataTypeTwo.PATH_FOR_ID_TOKEN;
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
		case DATA_ONE_ALL_ROWS:
			return ContentDescriptor.DataTypeOne.CONTENT_TYPE_DIR;
		case DATA_ONE_SINGLE_ROW:
			return ContentDescriptor.DataTypeOne.CONTENT_ITEM_TYPE;
		case DATA_TWO_ALL_ROWS:
			return ContentDescriptor.DataTypeTwo.CONTENT_TYPE_DIR;
		case DATA_TWO_SINGLE_ROW:
			return ContentDescriptor.DataTypeTwo.CONTENT_ITEM_TYPE;

		default:
			throw new UnsupportedOperationException("URI " + uri
					+ " is not supported.");
		}
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) {
		Log.d(LOG_TAG, "query()");
		String modifiedSelection = selection;
		final int match = uriMatcher.match(uri);
		switch (match) {
		case DATA_ONE_SINGLE_ROW: {
			modifiedSelection = modifiedSelection
					+ ContentDescriptor.DataTypeOne.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
		}
		case DATA_ONE_ALL_ROWS: {
			return query(uri, ContentDescriptor.DataTypeOne.TABLE_NAME,
					projection, modifiedSelection, selectionArgs, sortOrder);
		}
		case DATA_TWO_SINGLE_ROW: {
			modifiedSelection = modifiedSelection
					+ ContentDescriptor.DataTypeOne.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
		}
		case DATA_TWO_ALL_ROWS: {
			return query(uri, ContentDescriptor.DataTypeTwo.TABLE_NAME,
					projection, modifiedSelection, selectionArgs, sortOrder);
		}
		default:
			return null;
		}

	}

	private Cursor query(final Uri uri, final String tableName,
			final String[] projection, final String selection,
			final String[] selectionArgs, final String sortOrder) {

		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(tableName);
		return builder.query(mDB.getDB(), projection, selection, selectionArgs,
				null, null, sortOrder);
	}

	@Override
	public Uri insert(Uri uri, ContentValues value) {
		Log.d(LOG_TAG, "insert()");

		final int match = uriMatcher.match(uri);
		switch (match) {
		case DATA_ONE_SINGLE_ROW:
		case DATA_ONE_ALL_ROWS: {
			long rowID = mDB.insert(ContentDescriptor.DataTypeOne.TABLE_NAME,
					value);
			if (rowID > -1) {
				Uri insertedID = ContentUris.withAppendedId(
						ContentDescriptor.DataTypeOne.CONTENT_URI, rowID);
				getContext().getContentResolver()
						.notifyChange(insertedID, null);
				return ContentUris.withAppendedId(CONTENT_URI, rowID);
			}
			return null;
		}

		case DATA_TWO_SINGLE_ROW:
		case DATA_TWO_ALL_ROWS: {
			long rowID = mDB.insert(ContentDescriptor.DataTypeTwo.TABLE_NAME,
					value);
			if (rowID > -1) {
				Uri insertedID = ContentUris.withAppendedId(
						ContentDescriptor.DataTypeTwo.CONTENT_URI, rowID);
				getContext().getContentResolver()
						.notifyChange(insertedID, null);
				return ContentUris.withAppendedId(CONTENT_URI, rowID);
			}
			return null;
		}

		default:
			return null;
		}

	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		Log.d(LOG_TAG, "delete()");

		switch (uriMatcher.match(uri)) {
		case DATA_ONE_SINGLE_ROW:
			whereClause = whereClause
					+ ContentDescriptor.DataTypeOne.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
			// no break here on purpose
		case DATA_ONE_ALL_ROWS: {
			return deleteAndNotify(uri,
					ContentDescriptor.DataTypeOne.TABLE_NAME, whereClause,
					whereArgs);
		}

		case DATA_TWO_SINGLE_ROW:
			whereClause = whereClause
					+ ContentDescriptor.DataTypeTwo.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
			// no break here on purpose
		case DATA_TWO_ALL_ROWS: {
			return deleteAndNotify(uri,
					ContentDescriptor.DataTypeTwo.TABLE_NAME, whereClause,
					whereArgs);
		}

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

	}

	private int deleteAndNotify(final Uri uri, final String tableName,
			final String whereClause, final String[] whereArgs) {
		int count = mDB.delete(tableName, whereClause, whereArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		Log.d(LOG_TAG, "update()");

		switch (uriMatcher.match(uri)) {
		case DATA_ONE_SINGLE_ROW:
			whereClause = whereClause
					+ ContentDescriptor.DataTypeOne.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
		case DATA_ONE_ALL_ROWS: {
			return updateAndNotify(uri,
					ContentDescriptor.DataTypeOne.TABLE_NAME, values,
					whereClause, whereArgs);

		}
		case DATA_TWO_SINGLE_ROW:
			whereClause = whereClause
					+ ContentDescriptor.DataTypeTwo.ColumnNames.ID + " = "
					+ uri.getLastPathSegment();
		case DATA_TWO_ALL_ROWS: {
			return updateAndNotify(uri,
					ContentDescriptor.DataTypeTwo.TABLE_NAME, values,
					whereClause, whereArgs);
		}
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	private int updateAndNotify(final Uri uri, final String tableName,
			final ContentValues values, final String whereClause,
			final String[] whereArgs) {
		int count = mDB.update(ContentDescriptor.DataTypeOne.TABLE_NAME,
				values, whereClause, whereArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}
}