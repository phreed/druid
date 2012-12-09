package com.walkernation.db.orm;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.walkernation.db.provider.LocationDataDBAdaptor;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

/**
 * encapsulation of the ContentProviderClient for a single URI
 * 
 * @author rangerz
 * 
 */
public class LocationResolver {

	private ContentProviderClient cpc;
	private Uri uri = null; // TODO ??? fix this to right 'uri' & make final
							// static ???

	public LocationResolver(Activity activity, Uri uri) {
		this.uri = uri;
		cpc = activity.getContentResolver().acquireContentProviderClient(uri);
	}

	public ContentProviderResult[] applyBatch(
			ArrayList<ContentProviderOperation> operations)
			throws RemoteException, OperationApplicationException {
		return cpc.applyBatch(operations);
	}

	public int bulkInsert(ArrayList<LocationData> locations)
			throws RemoteException {
		ContentValues[] values = new ContentValues[locations.size()];
		int index = 0;
		for (LocationData locationData : locations) {
			values[index] = locationData.getCV();
			++index;
		}
		return cpc.bulkInsert(uri, values);
	}

	public int delete(String selection, String[] selectionArgs)
			throws RemoteException {
		return cpc.delete(uri, selection, selectionArgs);
	}

	public ContentProvider getLocalContentProvider() {
		// TODO decide if this should be provided or not... maybe not
		return cpc.getLocalContentProvider();
	}

	public String getType() throws RemoteException {
		return cpc.getType(uri);
	}

	public Uri insert(LocationData locationData) throws RemoteException {
		return cpc.insert(uri, locationData.getCV());
	}

	public AssetFileDescriptor openAssetFile(Uri uri, String mode)
			throws RemoteException, FileNotFoundException {
		return cpc.openAssetFile(uri, mode);
	}

	public ParcelFileDescriptor openFile(Uri uri, String mode)
			throws RemoteException, FileNotFoundException {
		return cpc.openFile(uri, mode);
	}

	public ArrayList<LocationData> query(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cpc.query(uri, projection, selection, selectionArgs,
				sortOrder);
		// make return object
		ArrayList<LocationData> rValue = new ArrayList<LocationData>();
		// convert cursor to reutrn object
		rValue.addAll(LocationDataDBAdaptor
				.getLocationDataArrayListFromCursor(result));
		// return 'return object'
		return rValue;
	}

	public boolean release() {
		return cpc.release();
	}

	public int update(LocationData values, String selection,
			String[] selectionArgs) throws RemoteException {
		return cpc.update(uri, values.getCV(), selection, selectionArgs);
	}

}
