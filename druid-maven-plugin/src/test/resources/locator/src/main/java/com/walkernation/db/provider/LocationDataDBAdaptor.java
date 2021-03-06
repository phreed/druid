package com.walkernation.db.provider;

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
	static final String DATABASE_TABLE_1 = ContentDescriptor.Location.TABLE_NAME;
	static final int DATABASE_VERSION = 10;

	// The SHORT name of each column in your table
	private static final String KEY_ID = ContentDescriptor.Location.Cols.ID;
	private static final String LAT_NAME = ContentDescriptor.Location.Cols.LAT_NAME;
	private static final String LONG_NAME = ContentDescriptor.Location.Cols.LONG_NAME;
	private static final String HEIGHT_NAME = ContentDescriptor.Location.Cols.HEIGHT_NAME;
	private static final String USER_ID_NAME = ContentDescriptor.Location.Cols.USER_ID_NAME;

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
	private boolean MEMORY_ONLY_DB = false;

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
	public int delete(final String table, long _id) {
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
	public int delete(final String table, final String whereClause,
			final String[] whereArgs) {
		Log.d(LOG_TAG, "delete(" + whereClause + ") ");
		return db.delete(table, whereClause, whereArgs);
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
	public Cursor query(final String table, final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) {
		Log.d(LOG_TAG, "query: " + projection.toString() + ", " + selection
				+ ", " + selectionArgs.toString() + ", " + sortOrder);

		Cursor query = db.query(table, projection, selection, selectionArgs,
				null, null, sortOrder);
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
	 * Insert a ContentValues into the DB.
	 * 
	 * @param location
	 * @return
	 */
	public long insert(final String table, final ContentValues cv) {
		Log.d(LOG_TAG, "insertLocation(CV)");
		return db.insert(table, null, cv);
	}

	/**
	 * Update Value(s) in the DB.
	 * 
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(final String table, final ContentValues values,
			final String whereClause, final String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
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

	public boolean isMemoryOnlyDB() {
		return MEMORY_ONLY_DB;
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