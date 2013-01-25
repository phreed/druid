// ST:BODY:start
package transapps.pli.orm;

import java.util.ArrayList;

// ST:package:inline
import transapps.pli.orm.provider.ContentDescriptor;
// ST:package:complete

import android.content.ContentValues;
import android.database.Cursor;

public class LocationsCreator {

  public static ContentValues getCVfromLocations(final LocationsData data) {
    ContentValues rValue = new ContentValues();
    // ST:putField:inline
    rValue.put(ContentDescriptor.Locations.Cols.NAME_NAME, data.name); 
    rValue.put(ContentDescriptor.Locations.Cols.LAT_NAME, data.lat); 
    rValue.put(ContentDescriptor.Locations.Cols.LON_NAME, data.lon); 
    rValue.put(ContentDescriptor.Locations.Cols.ALTITUDE_NAME, data.altitude); 
    rValue.put(ContentDescriptor.Locations.Cols.ACCURACY_NAME, data.accuracy); 
    rValue.put(ContentDescriptor.Locations.Cols.CREATED_NAME, data.created); 
    rValue.put(ContentDescriptor.Locations.Cols.MODIFIED_NAME, data.modified); 
    rValue.put(ContentDescriptor.Locations.Cols.HOPS_NAME, data.hops); 
    rValue.put(ContentDescriptor.Locations.Cols.DELTA_LOCATIONS_NAME, data.deltaLocations); 
    // ST:putField:complete
    return rValue;
  }

  /**
   * Get LocationsData object from ContentValues object
   * 
   * @param cv
   * @return
   */
  // I'm not even sure this is used/usable, but here in case it is...
  public static LocationsData CvToLocationsData(final ContentValues cv) {
    // set default values

    // ST:setValue:inline
    String name = "";
    if (cv.containsKey(ContentDescriptor.Locations.Cols.NAME_NAME)) {
      name = cv.getAsString(ContentDescriptor.Locations.Cols.NAME_NAME);
    } 

    long lat = 0;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.LAT_NAME)) {
      lat = cv.getAsLong(ContentDescriptor.Locations.Cols.LAT_NAME);
    } 

    long lon = 0;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.LON_NAME)) {
      lon = cv.getAsLong(ContentDescriptor.Locations.Cols.LON_NAME);
    } 

    long altitude = 0;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.ALTITUDE_NAME)) {
      altitude = cv.getAsLong(ContentDescriptor.Locations.Cols.ALTITUDE_NAME);
    } 

    long accuracy = 0;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.ACCURACY_NAME)) {
      accuracy = cv.getAsLong(ContentDescriptor.Locations.Cols.ACCURACY_NAME);
    } 

    long created = now;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.CREATED_NAME)) {
      created = cv.getAsLong(ContentDescriptor.Locations.Cols.CREATED_NAME);
    } 

    long modified = now;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.MODIFIED_NAME)) {
      modified = cv.getAsLong(ContentDescriptor.Locations.Cols.MODIFIED_NAME);
    } 

    long hops = 0;
    if (cv.containsKey(ContentDescriptor.Locations.Cols.HOPS_NAME)) {
      hops = cv.getAsLong(ContentDescriptor.Locations.Cols.HOPS_NAME);
    } 

    byte[] deltaLocations = "";
    if (cv.containsKey(ContentDescriptor.Locations.Cols.DELTA_LOCATIONS_NAME)) {
      deltaLocations = cv.getAsByte(ContentDescriptor.Locations.Cols.DELTA_LOCATIONS_NAME);
    } 
    // ST:setValue:complete

    // construct the returned object
    LocationsData rValue = 
       new LocationsData(name ,lat ,lon ,altitude ,accuracy ,created ,modified ,hops ,deltaLocations );

    return rValue;
  }

  /**
   * Get all of the LocationsData from the passed in cursor.
   * 
   * @param cursor
   *            passed in cursor
   * @return ArrayList<LocationsData\> The set of LocationsData
   */
  public static ArrayList<LocationsData> getLocationsDataArrayListFromCursor(
      Cursor cursor) {
    ArrayList<LocationsData> rValue = new ArrayList<LocationsData>();
    if (cursor != null) {
      cursor.moveToFirst();
      do {
        rValue.add(getLocationsDataFromCursor(cursor));
      } while (cursor.moveToNext() == true);
    }
    return rValue;
  }

  /**
   * Get the first LocationsData from the passed in cursor.
   * 
   * @param cursor
   *            passed in cursor
   * @return LocationsData object
   */
  public static LocationsData getLocationsDataFromCursor(Cursor cursor) {

    // ST:getValue:inline
    String name = cursor.getString(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.NAME_NAME)); 
    long lat = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.LAT_NAME)); 
    long lon = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.LON_NAME)); 
    long altitude = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.ALTITUDE_NAME)); 
    long accuracy = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.ACCURACY_NAME)); 
    long created = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.CREATED_NAME)); 
    long modified = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.MODIFIED_NAME)); 
    long hops = cursor.getLong(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.HOPS_NAME)); 
    byte[] deltaLocations = cursor.getBlob(cursor 
            .getColumnIndex(ContentDescriptor.Locations.Cols.DELTA_LOCATIONS_NAME)); 
    // ST:getValue:complete

    // construct the returned object
    LocationsData rValue = 
       new LocationsData(name ,lat ,lon ,altitude ,accuracy ,created ,modified ,hops ,deltaLocations );

    return rValue;
  }
}
// ST:BODY:end