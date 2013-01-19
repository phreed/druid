package com.walkernation.multiple.ui.dataOne;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.walkernation.multiple.orm.LocationData;
import com.walkernation.multiple.orm.LocationResolver;
import com.walkernation.multiple.orm.MultipleResolver;
import com.walkernation.multiple.provider.ContentDescriptor;

public class LocationFragmentBase extends Fragment {
	// LOG TAG, handles refactoring changes
	private static final String LOG_TAG = LocationFragmentBase.class
			.getCanonicalName();
	// URI to ContentProvider's info
	final static Uri uri = ContentDescriptor.Location.CONTENT_URI;

	MultipleResolver resolver;

	final static String[] columnProjection = {
			ContentDescriptor.Location.Cols.ID,
			ContentDescriptor.Location.Cols.USER_ID_NAME,
			ContentDescriptor.Location.Cols.LAT_NAME,
			ContentDescriptor.Location.Cols.LONG_NAME,
			ContentDescriptor.Location.Cols.HEIGHT_NAME };

	// Client to ContentProivder
	// private ContentProviderClient mContentProviderClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);

		resolver = new MultipleResolver(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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

		return resolver.getLocationOfUser(userID);

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
		// String[] selectionArgs = { String.valueOf(userID) };
		return resolver.deleteLocationOfUserID(userID);
		// return resolver.delete("user_id = ?", selectionArgs);
	}

	/**
	 * Delete ALL records in the ContentProvider.
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int deleteAllLocations() throws RemoteException {
		return resolver.delete(null, null);
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
		return resolver.insert(location);
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
	public int bulkInsertNewLocations(ArrayList<LocationData> locations)
			throws RemoteException {

		return resolver.bulkInsert(locations);
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
		return resolver.update(location, selection, selectionArgs);
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
	public ArrayList<LocationData> queryLocationContentProvider(
			String[] columnProjection, String selection,
			String[] selectionArgs, String sortOrder) throws RemoteException {
		// switch out 'null' columns for defined projection.
		String[] columns = null;
		if (columnProjection == null) {
			columns = LocationFragmentBase.columnProjection;
		} else {
			columns = columnProjection;
		}
		return resolver.query(columns, selection, selectionArgs, sortOrder);
	}

}
