package com.walkernation.multiple.orm;

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

import com.walkernation.multiple.provider.ContentDescriptor;

/**
 * encapsulation of the ContentProviderClient for a single URI
 * <p>
 * Uses ContentResolver instead of ContentProviderClient or other mechanism to
 * simplify code and to make this object thread safe.
 * 
 * @author Michael A. Walker
 * 
 */
public class MultipleResolver {

	private ContentResolver cr;
	private Uri dataOneURI = ContentDescriptor.DataTypeOne.CONTENT_URI;
	private Uri dataTwoURI = ContentDescriptor.DataTypeTwo.CONTENT_URI;

	public MultipleResolver(Activity activity) {
		cr = activity.getContentResolver();
	}

	public ContentProviderResult[] applyBatch(
			final ArrayList<ContentProviderOperation> operations)
			throws RemoteException, OperationApplicationException {
		return cr.applyBatch(ContentDescriptor.AUTHORITY, operations);
	}

	/*
	 * Bulk Insert for each ORM Data Type
	 */
	public int bulkInsertDataOne(final ArrayList<DataOneData> data)
			throws RemoteException {
		ContentValues[] values = new ContentValues[data.size()];
		int index = 0;
		for (DataOneData dataData : data) {
			values[index] = dataData.getCV();
			++index;
		}
		return cr.bulkInsert(dataOneURI, values);
	}

	public int bulkInsertDataTwo(final ArrayList<DataTwoData> data)
			throws RemoteException {
		ContentValues[] values = new ContentValues[data.size()];
		int index = 0;
		for (DataTwoData dataData : data) {
			values[index] = dataData.getCV();
			++index;
		}
		return cr.bulkInsert(dataTwoURI, values);
	}

	/*
	 * Delete for each ORM Data Type
	 */
	public int deleteDataOne(final String selection,
			final String[] selectionArgs) throws RemoteException {
		return cr.delete(dataOneURI, selection, selectionArgs);
	}

	public int deleteDataTwo(final String selection,
			final String[] selectionArgs) throws RemoteException {
		return cr.delete(dataTwoURI, selection, selectionArgs);
	}

	/*
	 * Get Type for each ORM Data Type
	 */
	public String getType() throws RemoteException {
		return cr.getType(dataTwoURI);
	}

	/*
	 * Insert for each ORM Data Type
	 */
	public Uri insert(final DataOneData dataOneObject) throws RemoteException {
		ContentValues tempCV = dataOneObject.getCV();
		tempCV.remove(ContentDescriptor.DataTypeOne.ColumnNames.ID);
		return cr.insert(dataOneURI, tempCV);
	}

	public Uri insert(final DataTwoData locationData) throws RemoteException {
		return cr.insert(dataTwoURI, locationData.getCV());
	}

	/*
	 * access files from the content provider
	 */
	public AssetFileDescriptor openAssetFileDescriptor(final Uri uri,
			final String mode) throws RemoteException, FileNotFoundException {
		return cr.openAssetFileDescriptor(uri, mode);
	}

	public ParcelFileDescriptor openFileDescriptor(final Uri uri,
			final String mode) throws RemoteException, FileNotFoundException {
		return cr.openFileDescriptor(uri, mode);
	}

	/*
	 * Query for each ORM Data Type
	 */

	public ArrayList<DataOneData> queryDataOneData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cr.query(dataOneURI, projection, selection,
				selectionArgs, sortOrder);
		// make return object
		ArrayList<DataOneData> rValue = new ArrayList<DataOneData>();
		// convert cursor to reutrn object
		rValue.addAll(DataOneCreator.getDataOneDataArrayListFromCursor(result));
		// return 'return object'
		return rValue;
	}

	public ArrayList<DataTwoData> queryDataTwoData(final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) throws RemoteException {
		// query the C.P.
		Cursor result = cr.query(dataTwoURI, projection, selection,
				selectionArgs, sortOrder);
		// make return object
		ArrayList<DataTwoData> rValue = new ArrayList<DataTwoData>();
		// convert cursor to reutrn object
		rValue.addAll(DataTwoCreator.getDataTwoDataArrayListFromCursor(result));
		// return 'return object'
		return rValue;
	}

	/*
	 * Update for each ORM Data Type
	 */

	public int updateDataOneData(final DataOneData values,
			final String selection, final String[] selectionArgs)
			throws RemoteException {
		return cr.update(dataOneURI, values.getCV(), selection, selectionArgs);
	}

	public int updateDataTwoData(final DataTwoData values,
			final String selection, final String[] selectionArgs)
			throws RemoteException {
		return cr.update(dataTwoURI, values.getCV(), selection, selectionArgs);
	}

	/*
	 * Sample extensions of above for customized additional methods for classes
	 * that extand this one
	 */

	public ArrayList<DataOneData> getAllDataOneData() throws RemoteException {
		return queryDataOneData(null, null, null, null);
	}

	public ArrayList<DataTwoData> getAllDataTwoData() throws RemoteException {
		return queryDataTwoData(null, null, null, null);
	}

	public DataOneData getDataOneDataViaRowID(final int rowID)
			throws RemoteException {
		String[] selectionArgs = { String.valueOf(rowID) };
		ArrayList<DataOneData> results = queryDataOneData(null,
				ContentDescriptor.DataTypeOne.ColumnNames.ID + "= ?",
				selectionArgs, null);
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public DataTwoData getDataTwoDataViaRowID(final int rowID)
			throws RemoteException {
		String[] selectionArgs = { String.valueOf(rowID) };
		ArrayList<DataTwoData> results = queryDataTwoData(null,
				ContentDescriptor.DataTypeOne.ColumnNames.ID + "=",
				selectionArgs, null);
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public int deleteDataTwoOfID(long userID) throws RemoteException {
		String[] selectionArgs = new String[] { String.valueOf(userID) };
		return deleteDataTwo("_id=?", selectionArgs);
	}

	public int deleteAllDataOneWithRowID(long rowID) throws RemoteException {
		String[] args = { String.valueOf(rowID) };
		return deleteDataOne(ContentDescriptor.DataTypeOne.ColumnNames.ID
				+ " = ? ", args);
	}

	public int deleteAllDataTwoWithRowID(long rowID) throws RemoteException {
		String[] args = { String.valueOf(rowID) };
		return deleteDataTwo(ContentDescriptor.DataTypeTwo.ColumnNames.ID
				+ " = ? ", args);
	}

	public int updateDataOneWithID(DataOneData dataOne) throws RemoteException {
		String selection = "user_id = ?";
		String[] selectionArgs = { String.valueOf(dataOne._id) };
		return updateDataOneData(dataOne, selection, selectionArgs);
	}

	public int updateDataTwoWithID(DataOneData dataTwo) throws RemoteException {
		String selection = "user_id = ?";
		String[] selectionArgs = { String.valueOf(dataTwo._id) };
		return updateDataOneData(dataTwo, selection, selectionArgs);
	}

}
