package com.walkernation.db.orm;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.walkernation.db.provider.ContentDescriptor;

/**
 * encapsulation of the ContentProviderClient for a single URI
 * <p>
 * Uses ContentResolver instead of ContentProviderClient or other mechanism to
 * simplify code and to make this object thread safe.
 * 
 * @author Michael A. Walker
 * 
 */
public class LocationResolver {

	private ContentResolver cr;
	private Uri uri = ContentDescriptor.Location.CONTENT_URI;

	public LocationResolver(Activity activity) {
		cr = activity.getContentResolver();
	}

	public ContentProviderResult[] applyBatch(
			ArrayList<ContentProviderOperation> operations)
			throws RemoteException, OperationApplicationException {
		return cr.applyBatch(ContentDescriptor.AUTHORITY, operations);
	}

	public int bulkInsert(ArrayList<LocationData> locations)
			throws RemoteException {
		ContentValues[] values = new ContentValues[locations.size()];
		int index = 0;
		for (LocationData locationData : locations) {
			values[index] = locationData.getCV();
			++index;
		}
		return cr.bulkInsert(uri, values);
	}

	public int deleteLocationOfUserID(long userID) throws RemoteException {
		String[] selectionArgs = new String[] { String.valueOf(userID) };
		return cr.delete(uri, "user_id=?", selectionArgs);
	}

	public int delete(String selection, String[] selectionArgs)
			throws RemoteException {
		return cr.delete(uri, selection, selectionArgs);
	}

	public String getType() throws RemoteException {
		return cr.getType(uri);
	}

	public Uri insert(LocationData locationData) throws RemoteException {
		return cr.insert(uri, locationData.getCV());
	}

	public AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode)
			throws RemoteException, FileNotFoundException {
		return cr.openAssetFileDescriptor(uri, mode);
	}

	public ParcelFileDescriptor openFileDescriptor(Uri uri, String mode)
			throws RemoteException, FileNotFoundException {
		return cr.openFileDescriptor(uri, mode);
	}

	public ArrayList<LocationData> query(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cr.query(uri, projection, selection, selectionArgs,
				sortOrder);
		// make return object
		ArrayList<LocationData> rValue = new ArrayList<LocationData>();
		// convert cursor to reutrn object
		rValue.addAll(LocationCreator
				.getLocationDataArrayListFromCursor(result));
		// return 'return object'
		return rValue;
	}

	public int update(LocationData values, String selection,
			String[] selectionArgs) throws RemoteException {
		return cr.update(uri, values.getCV(), selection, selectionArgs);
	}

	public ArrayList<LocationData> getAllLocations() throws RemoteException {
		return query(null, null, null, null);
	}

	public LocationData getLocationOfUser(long userID) throws RemoteException {
		LocationData rValue = null;

		ArrayList<LocationData> list = query(
				ContentDescriptor.Location.ORM_COLUMN_NAMES,
				ContentDescriptor.Location.Cols.USER_ID_NAME + " = ?",
				new String[] { "" + userID }, null);

		if (list.size() > 0) {
			rValue = list.get(0);
		}
		return rValue;
	}
}
