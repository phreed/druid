package main.java.com.walkernation.db.orm;

import java.io.Serializable;

import main.java.com.walkernation.db.provider.LocationDataDBAdaptor;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationData implements Parcelable, Serializable {

	private static final long serialVersionUID = 5505248865103008665L;

	public long latitude;
	public long longitude;
	public long height;
	public long userID;

	public LocationData(long _latitude, long _longitude, long _height,
			long _userID) {
		latitude = _latitude;
		longitude = _longitude;
		height = _height;
		userID = _userID;
	}

	@Override
	public String toString() {
		return "User: " + userID + " @ lat/long: " + latitude + ", "
				+ longitude + " height:" + height;
	}

	public ContentValues getCV() {
		// use the LocationDataDBAdaptor class to simplify creation
		return LocationDataDBAdaptor.LocationDataToCV(this);
	}

	public LocationData clone() {
		return new LocationData(latitude, longitude, height, userID);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(userID);
		dest.writeLong(latitude);
		dest.writeLong(longitude);
		dest.writeLong(height);
	}

	public static final Parcelable.Creator<LocationData> CREATOR = new Parcelable.Creator<LocationData>() {
		public LocationData createFromParcel(Parcel in) {
			return new LocationData(in);
		}

		public LocationData[] newArray(int size) {
			return new LocationData[size];
		}
	};

	private LocationData(Parcel in) {
		userID = in.readLong();
		latitude = in.readLong();
		longitude = in.readLong();
		height = in.readLong();
	}
}
