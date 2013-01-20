package com.walkernation.multiple.ui.dataTwo;

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
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.walkernation.multiple.orm.LocationData;
import com.walkernation.multiple.provider.ContentDescriptor;
import com.walkernation.multiple.provider.DataOneArrayAdapter;

abstract public class LocationListFragmentBase extends ListFragment {

	/**
	 * 
	 * BOILERPLATE VARIABLES
	 * 
	 */

	// URI for access to the ContentProvider
	final static Uri uri = ContentDescriptor.Location.CONTENT_URI;

	// TAG for logging
	static final String LOG_TAG = LocationListFragmentBase.class
			.getCanonicalName();

	// boolean mDualPane;
	int mCurCheckPosition = 0;
	OnOpenWindowInterface mOpener;

	/**
	 * 
	 * BOILERPLATE METHODS
	 * 
	 */

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
		onCreateCustom();
		setRetainInstance(true);
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
		return onCreateViewCustom(inflater, container, savedInstanceState);
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
		Log.d(LOG_TAG, "onActivityCreated");
		onActivityCreatedCustom(savedInstanceState);
	}

	/**
	 * This method will be called when an item in the list is selected.
	 * Subclasses should override. Subclasses can call
	 * getListView().getItemAtPosition(position) if they need to access the data
	 * associated with the selected item.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(LOG_TAG, "onListItemClick");
		onListItemClickCustom(l, v, position, id);
	}

	/**
	 * 
	 * CUSTOMIZED VARIABLES
	 * 
	 */

	// NEW custom resolver (container around C.P.C.
	public ContentResolver r;

	// Collection<T> which stores the ListView's LocationData(s)
	ArrayList<LocationData> locationData;
	// Custom ArrayAdapter that allows the rows of the ListView to show
	// customized layouts
	DataOneArrayAdapter aa;

	/**
	 * 
	 * CUSTOM METHODS
	 * 
	 */

	public abstract View onCreateViewCustom(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	/**
	 * update the List of LocationData(s) synconized to prevent nonthread safe
	 * client from causing issues if called externally (might not have to do
	 * this, just being extra safe)
	 */
	public abstract void updateLocationLocationData();

	/**
	 * Custom onActivityCreated Implementation
	 * 
	 * @param savedInstanceState
	 */
	public abstract void onActivityCreatedCustom(Bundle savedInstanceState);

	/**
	 * Custom onListItemClick Listener implementation
	 * 
	 * @param l
	 * @param v
	 * @param position
	 * @param id
	 */
	public abstract void onListItemClickCustom(ListView l, View v,
			int position, long id);

	/**
	 * Custom onCreate implementation
	 */
	public abstract void onCreateCustom();

}
