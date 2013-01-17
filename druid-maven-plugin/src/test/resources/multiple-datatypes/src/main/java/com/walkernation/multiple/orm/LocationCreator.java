package com.walkernation.multiple.orm;

import java.util.ArrayList;

import com.walkernation.multiple.provider.ContentDescriptor;

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
		if (cv.containsKey(ContentDescriptor.Location.Cols.LAT_NAME)) {
			latitude = cv.getAsLong(ContentDescriptor.Location.Cols.LAT_NAME);
		}
		if (cv.containsKey(ContentDescriptor.Location.Cols.LONG_NAME)) {
			longitude = cv.getAsLong(ContentDescriptor.Location.Cols.LONG_NAME);
		}
		if (cv.containsKey(ContentDescriptor.Location.Cols.HEIGHT_NAME)) {
			height = cv.getAsLong(ContentDescriptor.Location.Cols.HEIGHT_NAME);
		}
		if (cv.containsKey(ContentDescriptor.Location.Cols.USER_ID_NAME)) {
			userid = cv.getAsLong(ContentDescriptor.Location.Cols.USER_ID_NAME);
		}
		// construct the returned object
		LocationData rValue = new LocationData(latitude, longitude, height,
				userid);
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