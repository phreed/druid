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

	public byte byteName;
	public short shortName;
	public int intName;
	public long longName;
	public float floatName;
	public double doubleName;
	public String stringName = "";
	public boolean booleanName;

	public DataOneData() {
		byteName = 0;
		shortName = 0;
		intName = 0;
		longName = 0;
		floatName = 0;
		doubleName = 0;
		stringName = "";
		booleanName = false;
	}

	public DataOneData(final byte _byteName, final short _shortName,
			final int _intName, final long _longName, final float _floatName,
			final double _doubleName, final String _stringName,
			final boolean _booleanName) {

		byteName = _byteName;
		shortName = _shortName;
		intName = _intName;
		longName = _longName;
		floatName = _floatName;
		doubleName = _doubleName;
		stringName = _stringName;
		booleanName = _booleanName;

	}

	public ContentValues getCV() {
		return DataOneCreator.getCVfromDataOne(this);
	}

	public DataOneData clone() {
		return new DataOneData(byteName, shortName, intName, longName,
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
		byteName = in.readByte();
		shortName = (short) in.readInt();
		intName = in.readInt();
		longName = in.readLong();
		floatName = in.readFloat();
		doubleName = in.readDouble();
		stringName = in.readString();
		booleanName = (in.readByte() == 1);
	}

}
