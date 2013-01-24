package com.walkernation.multiple.orm;

import java.io.Serializable;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class DataOneData implements Serializable, Parcelable {

	/**
	 * generated serial version id
	 */
	private static final long serialVersionUID = -1320113528675037131L;

	public int _id;
	public byte byteName;
	public short shortName;
	public int intName;
	public long longName;
	public float floatName;
	public double doubleName;
	public String stringName = "";
	public boolean booleanName;

	public DataOneData() {
		_id = -1; // TODO change to negative 1?
		byteName = 0;
		shortName = 0;
		intName = 0;
		longName = 0;
		floatName = 0;
		doubleName = 0;
		stringName = "";
		booleanName = false;
	}

	public DataOneData(final int _id, final byte byteName,
			final short shortName, final int intName, final long longName,
			final float floatName, final double doubleName,
			final String stringName, final boolean booleanName) {
		this._id = _id;
		this.byteName = byteName;
		this.shortName = shortName;
		this.intName = intName;
		this.longName = longName;
		this.floatName = floatName;
		this.doubleName = doubleName;
		this.stringName = stringName;
		this.booleanName = booleanName;

	}

	public DataOneData(final byte byteName, final short shortName,
			final int intName, final long longName, final float floatName,
			final double doubleName, final String stringName,
			final boolean booleanName) {
		this.byteName = byteName;
		this.shortName = shortName;
		this.intName = intName;
		this.longName = longName;
		this.floatName = floatName;
		this.doubleName = doubleName;
		this.stringName = stringName;
		this.booleanName = booleanName;

	}

	public ContentValues getCV() {
		return DataOneCreator.getCVfromDataOne(this);
	}

	public DataOneData clone() {
		return new DataOneData(_id, byteName, shortName, intName, longName,
				floatName, doubleName, stringName, booleanName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// write a parcel to memory
	// the order of input & reading have to be EXACT
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeByte(byteName);
		dest.writeInt(shortName);
		dest.writeInt(intName);
		dest.writeLong(longName);
		dest.writeFloat(floatName);
		dest.writeDouble(doubleName);
		dest.writeString(stringName);
		dest.writeByte((byte) (booleanName ? 1 : 0));
	}

	// just has to be in this class to implement parcelable, just has to be
	// stock code with changes for data type.
	public static final Parcelable.Creator<DataOneData> CREATOR = new Parcelable.Creator<DataOneData>() {
		public DataOneData createFromParcel(Parcel in) {
			return new DataOneData(in);
		}

		public DataOneData[] newArray(int size) {
			return new DataOneData[size];
		}
	};

	// create a DataOne object from the parcelable data
	// the order of input & reading have to be EXACT
	private DataOneData(Parcel in) {
		_id = in.readInt();
		byteName = in.readByte();
		shortName = (short) in.readInt();
		intName = in.readInt();
		longName = in.readLong();
		floatName = in.readFloat();
		doubleName = in.readDouble();
		stringName = in.readString();
		booleanName = (in.readByte() == 1);
	}

	@Override
	public String toString() {
		String rValue = "id => " + _id + ", byte => " + shortName + ", int =>"
				+ intName + ", long =>" + longName + ", float =>" + floatName
				+ ", double =>" + doubleName + ", string =>" + stringName
				+ ", bool =>" + booleanName;
		return rValue;
	}
}
