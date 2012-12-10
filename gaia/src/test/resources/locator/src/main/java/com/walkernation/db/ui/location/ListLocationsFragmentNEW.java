package com.walkernation.db.ui.location;

/**
 * @author Michael A. Walker
 * @date 2012.10.15
 * 
 * This is a Custom Fragment that lists all the LocationData(s) stored in the 
 * related ContentProiver. It stores state information in a RetainedFragment
 * that survives the UI fragment being rotated/ destroyed. 
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.walkernation.db.R;
import com.walkernation.db.orm.LocationData;
import com.walkernation.db.provider.ContentDescriptor;
import com.walkernation.db.provider.LocationDataArrayAdapter;
import com.walkernation.db.provider.LocationDataDBAdaptor;

public class ListLocationsFragmentNEW extends ListFragment {

	// URI for access to the ContentProvider
	final static Uri uri = ContentDescriptor.Location.CONTENT_URI;
	// TAG for logging
	private static final String LOG_TAG = ListLocationsFragmentNEW.class
			.getCanonicalName();

	// fragment that stores the 'data'
	RetainedFragment mRetainedFragment;

	boolean mDualPane;
	int mCurCheckPosition = 0;
	OnOpenWindowInterface mOpener;

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
		checkDualPanel(); // important to setup fragments right.
		setRetainInstance(false);
	}

	/**
	 * The system calls this when it's time for the fragment to draw its user
	 * interface for the first time. To draw a UI for your fragment, you must
	 * return a View from this method that is the root of your fragment's
	 * layout. You can return null if the fragment does not provide a UI.
	 * 
	 * Provide default implementation to return a simple list view. Subclasses
	 * can override to replace with their own layout. If doing so, the returned
	 * view hierarchy must have a ListView whose id is android.R.id.list and can
	 * optionally have a sibling view id android.R.id.empty that is to be shown
	 * when the list is empty.
	 * 
	 * If you are overriding this method with your own custom content, consider
	 * including the standard layout list_content in your layout file, so that
	 * you continue to retain all of the standard behavior of ListFragment. In
	 * particular, this is currently the only way to have the built-in
	 * indeterminant progress state be shown.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreateView");
		View view = inflater.inflate(R.layout.location_listview, container,
				false);
		// get the ListView that will be displayed
		ListView lv = (ListView) view.findViewById(android.R.id.list);

		// customize the ListView in whatever desired ways.
		lv.setBackgroundColor(Color.CYAN);
		// return the parent view
		return view;
	}

	public boolean checkDualPanel() {
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			mDualPane = true;
			return true;
		} else {
			mDualPane = false;
			return false;
		}
	}

	/**
	 * Called when the fragment's activity has been created and this fragment's
	 * view hierarchy instantiated. It can be used to do final initialization
	 * once these pieces are in place, such as retrieving views or restoring
	 * state. It is also useful for fragments that use
	 * setRetainInstance(boolean) to retain their instance, as this callback
	 * tells the fragment when it is fully associated with the new activity
	 * instance.
	 */

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String RetainmentFragmentTag = "ListLocationFragmentsRetainment";
		FragmentManager fm = getFragmentManager();

		// Check to see if we have retained the worker fragment.
		mRetainedFragment = (RetainedFragment) fm
				.findFragmentByTag(RetainmentFragmentTag);

		// If not retained (or first time running), we need to create it.
		if (mRetainedFragment == null) {
			mRetainedFragment = new RetainedFragment();
			// Tell it who it is working with.
			mRetainedFragment.setTargetFragment(this, 0);
			fm.beginTransaction().add(mRetainedFragment, RetainmentFragmentTag)
					.commit();

			// set the client object (getActivty doesn't work in that fragment,
			// could push this to the constructor by passing context, or
			// something,
			// but this works fine for now
			mRetainedFragment.setClient(getActivity().getContentResolver()
					.acquireContentProviderClient(uri));
			// create the custom array adapter that will make the custom row
			// layouts
			mRetainedFragment.setAA(new LocationDataArrayAdapter(getActivity(),
					R.layout.location_listview_custom_row, mRetainedFragment
							.getArray()));
		}

		// update the back end data.
		mRetainedFragment.updateLocationLocationData();

		setListAdapter(mRetainedFragment.getAA());

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
				mRetainedFragment.updateLocationLocationData();
			}
		});

	}

	/**
	 * This method will be called when an item in the list is selected.
	 * Subclasses should override. Subclasses can call
	 * getListView().getItemAtPosition(position) if they need to access the data
	 * associated with the selected item.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		mOpener.openViewLocationFragment((int) mRetainedFragment.locationData
				.get(position).userID);
	}

	public static class RetainedFragment extends Fragment {
		// URI to connect to the ContentProvider of the LocationData(s)
		final static Uri uri = ContentDescriptor.Location.CONTENT_URI;
		private static final String LOG_TAG = RetainedFragment.class
				.getCanonicalName();

		// Collection<T> which stores the ListView's LocationData(s)
		private ArrayList<LocationData> locationData;
		// Custom ArrayAdapter that allows the rows of the ListView to show
		// customized layouts
		private LocationDataArrayAdapter aa;

		// ContentProviderClient is a NON thread safe means to access a Content
		// Provider that is much more efficient when connecting to the SAME
		// Content
		// Provider over and over.
		ContentProviderClient client;

		public RetainedFragment() {
			Log.d(LOG_TAG, "constructor");
			locationData = new ArrayList<LocationData>();

		}

		public void setClient(ContentProviderClient cli) {
			client = cli;
		}

		public ArrayList<LocationData> getArray() {
			return locationData;
		}

		/**
		 * update the List of LocationData(s) synconized to prevent nonthread
		 * safe client from causing issues if called externally (might not have
		 * to do this, just being extra safe)
		 */
		synchronized public void updateLocationLocationData() {
			Log.d(LOG_TAG, "updateLocationLocationData");
			try {

				Cursor allRows;
				allRows = client.query(uri, null, null, null, null);
				locationData.clear();
				locationData.addAll(getArrayList(allRows));
				aa.notifyDataSetChanged();
			} catch (RemoteException e) {
				Log.e(LOG_TAG,
						"Error connecting to Content Provider" + e.getMessage());
				e.printStackTrace();
			}
		}

		public void setAA(LocationDataArrayAdapter locationDataArrayAdapter) {
			aa = locationDataArrayAdapter;
		}

		public LocationDataArrayAdapter getAA() {
			Log.d(LOG_TAG, "getAA");
			return aa;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			Log.d(LOG_TAG, "onActivityCreated");
			super.onActivityCreated(savedInstanceState);

		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.d(LOG_TAG, "onCreate");
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		/**
		 * Convert Cursor to ArrayList &lt; LocationData &gt;
		 * 
		 * @param cursor
		 * @return
		 */
		private static ArrayList<LocationData> getArrayList(Cursor cursor) {
			// create container to return.
			ArrayList<LocationData> rValue = new ArrayList<LocationData>();
			// return empty container if cursor is empty
			if (cursor.getCount() == 0) {
				return rValue;
			}
			// get all the LocationData(s) from the cursor and put in the
			// ArrayList
			cursor.moveToFirst();
			do {
				rValue.add(LocationDataDBAdaptor
						.getLocationDataFromCursor(cursor));
			} while (cursor.moveToNext());

			return rValue;
		}
	}

}
