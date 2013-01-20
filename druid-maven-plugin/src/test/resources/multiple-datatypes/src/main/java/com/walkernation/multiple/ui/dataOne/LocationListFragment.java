package com.walkernation.multiple.ui.dataOne;

import java.util.ArrayList;

import android.app.Activity;
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
import com.walkernation.multiple.orm.DataOneData;
import com.walkernation.multiple.orm.MultipleResolver;
import com.walkernation.multiple.provider.DataOneArrayAdapter;

public class LocationListFragment extends LocationListFragmentBase {

	static final String LOG_TAG = LocationListFragment.class.getCanonicalName();

	OnOpenWindowInterface mOpener;
	MultipleResolver resolver;
	ArrayList<DataOneData> dataOneData;

	@Override
	public void onAttach(Activity activity) {
		Log.d(LOG_TAG, "onAttach start");
		super.onAttach(activity);
		try {
			mOpener = (OnOpenWindowInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnOpenWindowListener" + e.getMessage());
		}
		Log.d(LOG_TAG, "onAttach end");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mOpener = null;
	}

	/**
	 * The system calls this when creating the fragment. Within your
	 * implementation, you should initialize essential components of the
	 * fragment that you want to retain when the fragment is paused or stopped,
	 * then resumed.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		resolver = new MultipleResolver(getActivity());
		dataOneData = new ArrayList<DataOneData>();
		setRetainInstance(true);
	}

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
			dataOneData.clear();

			ArrayList<LocationData> currentList = locationResolver
					.getAllLocations();

			dataOneData.addAll(currentList);
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
		aa = new DataOneArrayAdapter(getActivity(),
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
		mOpener.openViewLocationFragment((dataOneData.get(position))._id);
	}

	@Override
	public void onCreateCustom() {

	}

	/**
	 * 
	 * CUSTOMIZED VARIABLES
	 * 
	 */

	private DataOneArrayAdapter aa;

}
