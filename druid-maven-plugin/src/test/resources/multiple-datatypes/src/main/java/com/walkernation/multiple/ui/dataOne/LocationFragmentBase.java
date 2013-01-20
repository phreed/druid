package com.walkernation.multiple.ui.dataOne;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.walkernation.multiple.orm.MultipleResolver;

public class LocationFragmentBase extends Fragment {
	// LOG TAG, handles refactoring changes
	private static final String LOG_TAG = LocationFragmentBase.class
			.getCanonicalName();
	// URI to ContentProvider's info
	// final static Uri uri = ContentDescriptor.Location.CONTENT_URI;

	MultipleResolver resolver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreate");
		super.onCreate(savedInstanceState);

		resolver = new MultipleResolver(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
