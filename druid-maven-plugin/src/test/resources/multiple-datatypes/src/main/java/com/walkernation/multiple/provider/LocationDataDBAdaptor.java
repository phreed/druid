package com.walkernation.multiple.provider;

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

	private static final String DATABASE_NAME = "myDatabase2.db";

	static final int DATABASE_VERSION = 10;

	/**
	 * Short Constant names (to improve readability)
	 */
	// for DataTypeOne
	private static final String DATA_ONE_TABLE_NAME = ContentDescriptor.DataTypeOne.TABLE_NAME;
	private static final String ONE_KEY_ID = ContentDescriptor.DataTypeOne.ColumnNames.ID;
	private static final String ONE_BYTE_NAME = ContentDescriptor.DataTypeOne.ColumnNames.BYTE_NAME;
	private static final String ONE_SHORT_NAME = ContentDescriptor.DataTypeOne.ColumnNames.SHORT_NAME;
	private static final String ONE_INT_NAME = ContentDescriptor.DataTypeOne.ColumnNames.INT_NAME;
	private static final String ONE_LONG_NAME = ContentDescriptor.DataTypeOne.ColumnNames.LONG_NAME;
	private static final String ONE_FLOAT_NAME = ContentDescriptor.DataTypeOne.ColumnNames.FLOAT_NAME;
	private static final String ONE_DOUBLE_NAME = ContentDescriptor.DataTypeOne.ColumnNames.DOUBLE_NAME;
	private static final String ONE_STRING_NAME = ContentDescriptor.DataTypeOne.ColumnNames.STRING_NAME;
	private static final String ONE_BOOLEAN_NAME = ContentDescriptor.DataTypeOne.ColumnNames.BOOLEAN_NAME;

	// for DataTypeTwo
	private static final String DATA_TWO_TABLE_NAME = ContentDescriptor.DataTypeTwo.TABLE_NAME;
	private static final String TWO_KEY_ID = ContentDescriptor.DataTypeTwo.ColumnNames.ID;
	private static final String TWO_BYTE_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.BYTE_NAME;
	private static final String TWO_SHORT_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.SHORT_NAME;
	private static final String TWO_INT_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.INT_NAME;
	private static final String TWO_LONG_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.LONG_NAME;
	private static final String TWO_FLOAT_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.FLOAT_NAME;
	private static final String TWO_DOUBLE_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.DOUBLE_NAME;
	private static final String TWO_STRING_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.STRING_NAME;
	private static final String TWO_BOOLEAN_NAME = ContentDescriptor.DataTypeTwo.ColumnNames.BOOLEAN_NAME;

	// SQL Statement to create a new database.
	private static final String TABLE_DATA_ONE_CREATE = "create table "
			+ DATA_ONE_TABLE_NAME + " (" // start table
			+ ONE_KEY_ID + " integer primary key autoincrement, " // setup inc.
			+ ONE_BYTE_NAME + " INTEGER," //
			+ ONE_SHORT_NAME + " INTEGER," //
			+ ONE_INT_NAME + " INTEGER," //
			+ ONE_LONG_NAME + " INTEGER," //
			+ ONE_FLOAT_NAME + " REAL," //
			+ ONE_DOUBLE_NAME + " REAL," //
			+ ONE_STRING_NAME + " TEXT," //
			+ ONE_BOOLEAN_NAME + " INTEGER" // will store as 0 or 1, and convert
			+ " );"; // end table

	// SQL Statement to create a new database.
	private static final String TABLE_DATA_TWO_CREATE = "create table "
			+ DATA_TWO_TABLE_NAME + " (" // start table
			+ TWO_KEY_ID + " integer primary key autoincrement, " // setup inc.
			+ TWO_BYTE_NAME + " INTEGER," //
			+ TWO_SHORT_NAME + " INTEGER," //
			+ TWO_INT_NAME + " INTEGER," //
			+ TWO_LONG_NAME + " INTEGER," //
			+ TWO_FLOAT_NAME + " REAL," //
			+ TWO_DOUBLE_NAME + " REAL," //
			+ TWO_STRING_NAME + " TEXT," //
			+ TWO_BOOLEAN_NAME + " INTEGER" // will store as 0 or 1, and convert
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
	 * Insert a LocationData ContentValues into the DB.
	 * 
	 * @param location
	 * @return
	 */
	public long insert(final String table, final ContentValues location) {
		Log.d(LOG_TAG, "insertLocation(CV)");
		return db.insert(table, null, location);
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
			try {
				db = dbHelper.getReadableDatabase();
				Log.e(LOG_TAG, "**************ONLY ABLE TO GET READABLE DB.");
			} catch (SQLException ex2) {
				Log.e(LOG_TAG, "**************unable ABLE TO GET READABLE DB.");
				throw ex2;
			}
		}
		if (db.isReadOnly()){
			Log.e(LOG_TAG, "DB IS READ ONLY");
		}
		Log.e(LOG_TAG, "************** MADE IT TO END.");
		return this;
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
			Log.d(LOG_TAG, "DBHelper: creation of new database."
					+ TABLE_DATA_ONE_CREATE);
			db.execSQL(TABLE_DATA_ONE_CREATE);

			// db.execSQL(TABLE_DATA_TWO_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log version upgrade.
			Log.w(LOG_TAG + "DBHelper", "Upgrading from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// **** Upgrade DB ****
			// TODO: migrate data?? from old DB to new DB
			// drop old DB
			db.execSQL("DROP TABLE IF EXISTS " + DATA_ONE_TABLE_NAME);
			// db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_TWO_CREATE);

			// Create a new one.
			onCreate(db);

		}

	}

	public SQLiteDatabase getDB() {
		return db;
	}

}
