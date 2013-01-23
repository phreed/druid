package com.walkernation.multiple.orm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

import com.walkernation.multiple.provider.ContentDescriptor;

public class DataOneCreator {

	public static ContentValues getCVfromDataOne(final DataOneData data) {
		ContentValues rValue = new ContentValues();

		/*
		 * this is how you would check objects that are being stored ========>
		 * if (data.<member> != null) { rValue.put(<key>, data.<data_member>);}
		 */

		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.ID, data._id);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.BYTE_NAME,
				data.byteName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.SHORT_NAME,
				data.shortName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.INT_NAME,
				data.intName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.LONG_NAME,
				data.longName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.FLOAT_NAME,
				data.floatName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.DOUBLE_NAME,
				data.doubleName);
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.STRING_NAME,
				data.stringName); // TODO determine how to handle 'null'
		rValue.put(ContentDescriptor.DataTypeOne.ColumnNames.BOOLEAN_NAME,
				data.booleanName);

		return rValue;
	}

	/**
	 * Get all of the LocationData from the passed in cursor.
	 * 
	 * @param cursor
	 *            passed in cursor
	 * @return ArrayList\<LocationData\> The set of LocationData
	 */
	public static ArrayList<DataOneData> getDataOneDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<DataOneData> rValue = new ArrayList<DataOneData>();
		if ((cursor != null) && (cursor.moveToFirst())) {
			do {
				rValue.add(getDataOneDataFromCursor(cursor));
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
	public static DataOneData getDataOneDataFromCursor(Cursor cursor) {

		int _id = 0;
		byte byteName = 0;
		short shortName = 0;
		int intName = 0;
		long longName = 0;
		float floatName = 0;
		double doubleName = 0;
		String stringName = "";
		boolean booleanName = false;

		if (cursor.moveToFirst() == false) {
			throw new UnsupportedOperationException(
					"Tried to read from empty Cursor.");
		}

		_id = cursor.getShort(cursor
				.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.ID));

		/*
		 * How to get a single byte stored (in a short's location) (wasteful of
		 * 1 byte)
		 * 
		 * TODO find a better way to do this....
		 */
		short tempShort = cursor
				.getShort(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.BYTE_NAME));
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putShort(tempShort);
		byteName = bb.get();

		shortName = cursor
				.getShort(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.SHORT_NAME));

		intName = cursor
				.getInt(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.INT_NAME));

		longName = cursor
				.getLong(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.LONG_NAME));

		longName = cursor
				.getLong(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.LONG_NAME));

		floatName = cursor
				.getFloat(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.FLOAT_NAME));

		doubleName = cursor
				.getDouble(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.DOUBLE_NAME));

		stringName = cursor
				.getString(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.STRING_NAME));

		short tempBoolShort = cursor
				.getShort(cursor
						.getColumnIndex(ContentDescriptor.DataTypeOne.ColumnNames.BOOLEAN_NAME));
		if (tempBoolShort == 1) {
			booleanName = true;
		}

		return new DataOneData(_id, byteName, shortName, intName, longName,
				floatName, doubleName, stringName, booleanName);

	}

}
