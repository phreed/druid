package com.walkernation.db.provider;

import java.util.ArrayList;

import com.walkernation.db.orm.LocationData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDataDBAdaptor {

	private static final String LOG_TAG = LocationDataDBAdaptor.class
			.getCanonicalName();

	private static final String DATABASE_NAME = "myDatabase.db";
	static final String DATABASE_TABLE_1 = "main_table";
	private static final int DATABASE_VERSION = 2;

	// the index (key) column name for use in where clauses.
	public static final String KEY_ID = "_id";

	// The name and column index of each column in your database
	public static final String LAT_NAME = "latitude";
	public static final String LONG_NAME = "longitude";
	public static final String HEIGHT_NAME = "height";
	public static final String USER_ID_NAME = "user_id";

	// the names and order of ALL columns, including internal use ones
	public static final String[] ALL_COLUMN_NAMES = { KEY_ID, LAT_NAME,
			LONG_NAME, HEIGHT_NAME, USER_ID_NAME };

	// the names and order of the public columns
	public static final String[] COLUMN_NAMES = { LAT_NAME, LONG_NAME,
			HEIGHT_NAME, USER_ID_NAME };

	// SQL Statement to create a new database.
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE_1 + " (" // start table
			+ KEY_ID + " integer primary key autoincrement, " // setup inc.
			+ LAT_NAME + " INTEGER," //
			+ LONG_NAME + " INTEGER," //
			+ HEIGHT_NAME + " INTEGER," //
			+ USER_ID_NAME + " INTEGER" //
			+ " );"; // end table

	// Variable to hold the database instance.
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private myDbHelper dbHelper;
	// if the DB is in memory or to file.
	public boolean MEMORY_ONLY_DB = false;

	/**
	 * constructor that accepts the context to be associated with
	 * 
	 * @param _context
	 */
	public LocationDataDBAdaptor(Context _context) {
		Log.d(LOG_TAG, "MyDBAdapter constructor");

		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	/**
	 * constructor that accepts the context to be associated with, and if this
	 * DB should be created in memory only.
	 * 
	 * @param _context
	 */
	public LocationDataDBAdaptor(Context _context, boolean memory_only_db) {
		Log.d(LOG_TAG, "MyDBAdapter constructor w/ mem only =" + memory_only_db);

		context = _context;
		MEMORY_ONLY_DB = memory_only_db;
		if (MEMORY_ONLY_DB == true) {
			dbHelper = new myDbHelper(context, null, null, DATABASE_VERSION);
		} else {
			dbHelper = new myDbHelper(context, DATABASE_NAME, null,
					DATABASE_VERSION);
		}
	}

	/**
	 * open the DB Get Memory or File version of DB, and write/read access or
	 * just read access if that is all that is possible.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public LocationDataDBAdaptor open() throws SQLException {
		Log.d(LOG_TAG, "open()");
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException ex) {
			db = dbHelper.getReadableDatabase();
		}
		return this;
	}

	/**
	 * Remove a row of the DB where the rowIndex matches.
	 * 
	 * @param rowIndex
	 *            row to remove from DB
	 * @return if the row was removed
	 */
	public int delete(long _id) {
		Log.d(LOG_TAG, "delete(" + _id + ") ");
		return db.delete(DATABASE_TABLE_1, KEY_ID + " = " + _id, null);
	}

	/**
	 * Delete row(s) that match the whereClause and whereArgs(if used).
	 * <p>
	 * the whereArgs is an String[] of values to substitute for the '?'s in the
	 * whereClause
	 * 
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(final String whereClause, final String[] whereArgs) {
		Log.d(LOG_TAG, "delete(" + whereClause + ") ");
		return db.delete(DATABASE_TABLE_1, whereClause, whereArgs);
	}

	/**
	 * Query the Database with the provided specifics.
	 * 
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public Cursor query(final String[] projection, final String selection,
			final String[] selectionArgs, final String sortOrder) {
		Log.d(LOG_TAG, "query: " + projection.toString() + ", " + selection
				+ ", " + selectionArgs.toString() + ", " + sortOrder);

		Cursor query = db.query(DATABASE_TABLE_1, projection, selection,
				selectionArgs, null, null, sortOrder);
		return query;
	}

	/**
	 * close the DB.
	 */
	public void close() {
		Log.d(LOG_TAG, "close()");
		db.close();
	}

	/**
	 * Start a transaction.
	 */
	public void startTransaction() {
		Log.d(LOG_TAG, "startTransaction()");
		db.beginTransaction();
	}

	/**
	 * End a transaction.
	 */
	public void endTransaction() {
		Log.d(LOG_TAG, "endTransaction()");
		db.endTransaction();
	}

	/**
	 * Get the underlying Database.
	 * 
	 * @return
	 */
	SQLiteDatabase getDB() {
		return db;
	}

	/**
	 * Insert a LocationDataobject into the DB.
	 * 
	 * @param location
	 *            data to add to DB
	 * @return the index in the table
	 */
	public long insertLocation(LocationData location) {
		Log.d(LOG_TAG, "insertLocation(LocationData)");
		return db.insert(DATABASE_TABLE_1, null, LocationDataToCV(location));
	}

	/**
	 * Insert a LocationData ContentValues into the DB.
	 * 
	 * @param location
	 * @return
	 */
	public long insertLocation(ContentValues location) {
		Log.d(LOG_TAG, "insertLocation(CV)");
		return db.insert(DATABASE_TABLE_1, null, location);
	}

	/**
	 * Get all the Locations stored in the DB
	 * 
	 * @return the Cursor results
	 */
	public Cursor getAllLocations() {
		Log.d(LOG_TAG, "getAllLocations()");
		return db.query(DATABASE_TABLE_1, ALL_COLUMN_NAMES, null, null, null,
				null, null);
	}

	/**
	 * get the LocationData at the specified row
	 * 
	 * @param rowIndex
	 *            row to get
	 * @return LocationData of the row
	 * @throws SQLException
	 * 
	 *             TODO: decide to return default LocationData, 'null', or
	 *             SQLException for when rowIndex isn't there
	 */
	public LocationData getLocation(long rowIndex) throws SQLException {
		Log.d(LOG_TAG, "getLocation(), row = " + rowIndex);

		Cursor cursor = db.query(true, DATABASE_TABLE_1, COLUMN_NAMES, KEY_ID
				+ "=" + rowIndex, null, null, null, null, null);

		if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
			throw new SQLException("No LocationData item found for row: "
					+ rowIndex);
		}

		// long latitude = cursor.getLong(cursor.getColumnIndex(LAT_NAME));
		// long longitude = cursor.getLong(cursor.getColumnIndex(LONG_NAME));
		// long height = cursor.getLong(cursor.getColumnIndex(HEIGHT_NAME));
		// long user_id = cursor.getLong(cursor.getColumnIndex(USER_ID_NAME));
		//
		// LocationData location = new LocationData(latitude, longitude, height,
		// user_id);

		// return location;

		return getLocationDataFromCursor(cursor);
	}

	/**
	 * Get the first LocationData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return LocationData object
	 */
	public static LocationData getLocationDataFromCursor(Cursor cursor) {

		long latitude = cursor.getLong(cursor.getColumnIndex(LAT_NAME));
		long longitude = cursor.getLong(cursor.getColumnIndex(LONG_NAME));
		long height = cursor.getLong(cursor.getColumnIndex(HEIGHT_NAME));
		long user_id = cursor.getLong(cursor.getColumnIndex(USER_ID_NAME));

		LocationData location = new LocationData(latitude, longitude, height,
				user_id);

		return location;
	}

	/**
	 * Get all of the LocationData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return ArrayList\<LocationData\> The set of LocationData
	 */
	public static ArrayList<LocationData> getLocationDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<LocationData> rValue = new ArrayList<LocationData>();
		if (cursor != null) {
			cursor.moveToFirst();
			do {
				rValue.add(getLocationDataFromCursor(cursor));
			} while (cursor.moveToNext() == true);
		}
		return rValue;
	}

	/**
	 * Update a location in the DB.
	 * 
	 * @param rowIndex
	 *            which row to update
	 * @param location
	 *            LocationData to put data
	 * @return if update was successful
	 */
	public boolean updateLocation(long rowIndex, LocationData location) {
		Log.d(LOG_TAG, "updateLocation(), row = " + rowIndex);
		// new CV to put into DB
		ContentValues updatedValues = LocationDataToCV(location);
		// which row to update
		String where = KEY_ID + "=" + rowIndex;
		// update the row in the DB
		return db.update(DATABASE_TABLE_1, updatedValues, where, null) > 0;
	}

	/**
	 * Update Value(s) in the DB.
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(ContentValues values, String whereClause,
			String[] whereArgs) {
		return db.update(DATABASE_TABLE_1, values, whereClause, whereArgs);
	}

	/**
	 * get appropriate ContentValues for this DB from LocationData object
	 * 
	 * @param data
	 * @return
	 */
	public static ContentValues LocationDataToCV(LocationData data) {
		ContentValues rValue = new ContentValues();

		/*
		 * this is how you would check objects that are being stored ========>
		 * if (data.<member> != null) { rValue.put(<key>, data.<data_member>);}
		 */

		rValue.put(LAT_NAME, data.latitude);
		rValue.put(LONG_NAME, data.longitude);
		rValue.put(HEIGHT_NAME, data.height);
		rValue.put(USER_ID_NAME, data.userID);

		return rValue;
	}

	/**
	 * Get LocationData object from ContentValues object
	 * 
	 * @param cv
	 * @return
	 */
	// I'm not even sure this is used/usable, but here in case it is...
	public static LocationData CvToLocationData(final ContentValues cv) {
		// set default values
		long latitude = 0;
		long longitude = 0;
		long height = 0;
		long userid = 0;

		// // can only do this because all 4 are long, but nice
		// // might be useful for VERY large objects, with lots of similar
		// // data type, make an iterator for loop for each data type
		// // will require a string[] of col names for that type
		// for (String cvColName : COLUMN_NAMES) {
		//
		// if (cv.containsKey(cvColName)) {
		// latitude = cv.getAsLong(cvColName);
		// }
		// }

		// check if CV contains keys, and if so, assign values
		if (cv.containsKey(LAT_NAME)) {
			latitude = cv.getAsLong(LAT_NAME);
		}
		if (cv.containsKey(LONG_NAME)) {
			longitude = cv.getAsLong(LONG_NAME);
		}
		if (cv.containsKey(HEIGHT_NAME)) {
			height = cv.getAsLong(HEIGHT_NAME);
		}
		if (cv.containsKey(USER_ID_NAME)) {
			userid = cv.getAsLong(USER_ID_NAME);
		}
		// construct the returned object
		LocationData rValue = new LocationData(latitude, longitude, height,
				userid);
		return rValue;
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			db.close();
		} catch (Exception e) {
			Log.d(LOG_TAG, "exception on finalize():" + e.getMessage());
		}
		super.finalize();
	}

	/**
	 * DB Helper Class.
	 * 
	 * @author mwalker
	 * 
	 */
	private static class myDbHelper extends SQLiteOpenHelper {

		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(LOG_TAG, "" + DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log version upgrade.
			Log.w(LOG_TAG + "DBHelper", "Upgrading from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// **** Upgrade DB ****
			// TODO: migrate data?? from old DB to new DB
			// drop old DB
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_1);
			// Create a new one.
			onCreate(db);

		}

	}

}
