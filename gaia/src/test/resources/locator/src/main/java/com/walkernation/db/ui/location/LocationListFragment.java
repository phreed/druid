package com.walkernation.db.ui.location;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.walkernation.db.R;
import com.walkernation.db.orm.LocationData;
import com.walkernation.db.provider.LocationDataArrayAdapter;
import com.walkernation.db.provider.LocationDataDBAdaptor;

public class LocationListFragment extends
		LocationListFragmentBase {

	static final String LOG_TAG = LocationListFragment.class
			.getCanonicalName();

	@Override
	public View onCreateViewCustom(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.location_listview, container,
				false);
		// get the ListView that will be displayed
		ListView lv = (ListView) view.findViewById(android.R.id.list);

		// customize the ListView in whatever desired ways.
		lv.setBackgroundColor(Color.CYAN);
		// return the parent view
		return view;
	}

	@Override
	public void updateLocationLocationData() {
		Log.d(LOG_TAG, "updateLocationLocationData");
		try {
			locationData.clear();
			Cursor cursor = r.query(uri, null, null, null, null);
			ArrayList<LocationData> currentList = LocationDataDBAdaptor
					.getLocationDataArrayListFromCursor(cursor);
			locationData.addAll(currentList);
			aa.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e(LOG_TAG,
					"Error connecting to Content Provider" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreatedCustom(Bundle savedInstanceState) {
		// create the custom array adapter that will make the custom row
		// layouts
		aa = new LocationDataArrayAdapter(getActivity(),
				R.layout.location_listview_custom_row, locationData);

		// update the back end data.
		updateLocationLocationData();

		setListAdapter(aa);

		// TODO see why I have to manually "setOnClickListener"

		Button createNewButton = (Button) getView().findViewById(
				R.id.location_listview_create);
		createNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOpener.openCreateLocationFragment();
			}
		});

		Button refreshListButton = (Button) getView().findViewById(
				R.id.location_listview_refreshed);
		refreshListButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateLocationLocationData();
			}
		});
	}

	@Override
	public void onListItemClickCustom(ListView l, View v, int position, long id) {
		mOpener.openViewLocationFragment((int) locationData.get(position).userID);
	}

	@Override
	public void onCreateCustom() {
		r = getActivity().getContentResolver();

		locationData = new ArrayList<LocationData>();
	}

	/**
	 * 
	 * CUSTOMIZED VARIABLES
	 * 
	 */

	// NEW custom resolver (container around C.P.C.

	// Collection<T> which stores the ListView's LocationData(s)
	private ArrayList<LocationData> locationData;
	// Custom ArrayAdapter that allows the rows of the ListView to show
	// customized layouts
	private LocationDataArrayAdapter aa;

}