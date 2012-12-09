package com.walkernation.db.ui;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.walkernation.db.orm.LocationData;
import com.walkernation.db.provider.ContentDescriptor;
import com.walkernation.db.provider.LocationDataDBAdaptor;

public class LocationFragmentBase extends Fragment {
	// LOG TAG, handles refactoring changes
	private static final String LOG_TAG = LocationFragmentBase.class
			.getCanonicalName();
	// URI to ContentProvider's info
	final static Uri uri = ContentDescriptor.Location.CONTENT_URI;

	final static String[] columnProjection = {
			ContentDescriptor.Location.Cols.ID,
			ContentDescriptor.Location.Cols.USER_ID_NAME,
			ContentDescriptor.Location.Cols.LAT_NAME,
			ContentDescriptor.Location.Cols.LONG_NAME,
			ContentDescriptor.Location.Cols.HEIGHT_NAME };

	// Client to ContentProivder
	private ContentProviderClient mContentProviderClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		// obtain the client
		mContentProviderClient = getActivity().getContentResolver()
				.acquireContentProviderClient(uri);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// release the client
		mContentProviderClient.release();
	}

	/**
	 * Get the first Record with the provided userID.
	 * 
	 * @param userID
	 *            the userID to get
	 * @return Either the desired LocationData or 'null' if no Record was found
	 * @throws RemoteException
	 *             when ContentProvider wasn't accessible
	 */
	public LocationData getLocationDataForUserID(int userID)
			throws RemoteException {
		LocationData rValue = null;

		Cursor locationToDisplay;
		String[] whereArgs = new String[] { String.valueOf(userID) };
		locationToDisplay = mContentProviderClient.query(uri, columnProjection,
				"user_id=?", whereArgs, null);

		if (locationToDisplay.moveToFirst() == true) {
			rValue = LocationDataDBAdaptor
					.getLocationDataFromCursor(locationToDisplay);
		}

		return rValue;
	}

	/**
	 * Delete all Records with the userID of the supplied parameter.
	 * 
	 * @param userID
	 *            the userID of the rows to delete
	 * @return the number of rows deleted.
	 * @throws RemoteException
	 */
	public int deleteAllLocationsWithUserID(int userID) throws RemoteException {
		String[] selectionArgs = { String.valueOf(userID) };
		return mContentProviderClient.delete(uri, "user_id = ?", selectionArgs);
	}

	/**
	 * Delete ALL records in the ContentProvider.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int deleteAllLocations() throws RemoteException {
		return mContentProviderClient.delete(uri, null, null);
	}

	/**
	 * Create a new Record from the location parameter
	 * 
	 * @param location
	 *            data to create record from
	 * @return Uri of newly created record.
	 * @throws RemoteException
	 */
	public Uri insertNewLocation(LocationData location) throws RemoteException {
		return mContentProviderClient.insert(uri, location.getCV());
	}

	/**
	 * Insert a set of LocationData into the ContentProvider.
	 * 
	 * @param locations
	 *            An array of LocationData to add to the ContentProvider.
	 * 
	 * @return The number of values that were inserted.
	 * @throws RemoteException
	 */
	public int bulkInsertNewLocations(LocationData[] locations)
			throws RemoteException {
		ContentValues[] insertValues = new ContentValues[locations.length];
		for (int i = 0; i < insertValues.length; i++) {
			insertValues[i] = locations[i].getCV();
		}
		return mContentProviderClient.bulkInsert(uri, insertValues);
	}

	/**
	 * Update the location in the ContentProvider.
	 * 
	 * @param location
	 * @return The number of rows affected.
	 * @throws RemoteException
	 */
	public int updateLocationWithUserID(LocationData location)
			throws RemoteException {
		String selection = "user_id = ?";
		String[] selectionArgs = { String.valueOf(location.userID) };
		return mContentProviderClient.update(uri, location.getCV(), selection,
				selectionArgs);
	}

	/**
	 * Query the ContentProvider with the provided arguments.
	 * 
	 * @param columnProjection
	 *            columns to get, if 'null' all columns will be returned
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return Cursor to the matching query.
	 * @throws RemoteException
	 */
	public Cursor queryLocationContentProvider(String[] columnProjection,
			String selection, String[] selectionArgs, String sortOrder)
			throws RemoteException {
		// switch out 'null' columns for defined projection.
		String[] columns = null;
		if (columnProjection == null) {
			columns = LocationFragmentBase.columnProjection;
		} else {
			columns = columnProjection;
		}
		return mContentProviderClient.query(uri, columns, selection,
				selectionArgs, sortOrder);
	}

	/**
	 * Get the ContentProviderClient for any custom one-off interactions.
	 * 
	 * @return the ContentProviderClient to the ContentProvider
	 */
	public ContentProviderClient getContentProviderClient() {
		return mContentProviderClient;
	}
}
