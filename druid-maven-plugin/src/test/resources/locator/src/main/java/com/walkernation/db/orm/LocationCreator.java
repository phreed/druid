package com.walkernation.db.orm;

import java.util.ArrayList;

import com.walkernation.db.provider.ContentDescriptor;

import android.content.ContentValues;
import android.database.Cursor;

public class LocationCreator {

	public static ContentValues getCVfromLocation(final LocationData data) {
		ContentValues rValue = new ContentValues();

		/*
		 * this is how you would check objects that are being stored ========>
		 * if (data.<member> != null) { rValue.put(<key>, data.<data_member>);}
		 */

		rValue.put(ContentDescriptor.Location.Cols.LAT_NAME, data.latitude);
		rValue.put(ContentDescriptor.Location.Cols.LONG_NAME, data.longitude);
		rValue.put(ContentDescriptor.Location.Cols.HEIGHT_NAME, data.height);
		rValue.put(ContentDescriptor.Location.Cols.USER_ID_NAME, data.userID);

		return rValue;
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
	 * Get the first LocationData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return LocationData object
	 */
	public static LocationData getLocationDataFromCursor(Cursor cursor) {

		long latitude = cursor.getLong(cursor
				.getColumnIndex(ContentDescriptor.Location.Cols.LAT_NAME));
		long longitude = cursor.getLong(cursor
				.getColumnIndex(ContentDescriptor.Location.Cols.LONG_NAME));
		long height = cursor.getLong(cursor
				.getColumnIndex(ContentDescriptor.Location.Cols.HEIGHT_NAME));
		long user_id = cursor.getLong(cursor
				.getColumnIndex(ContentDescriptor.Location.Cols.USER_ID_NAME));

		LocationData location = new LocationData(latitude, longitude, height,
				user_id);

		return location;
	}
}
