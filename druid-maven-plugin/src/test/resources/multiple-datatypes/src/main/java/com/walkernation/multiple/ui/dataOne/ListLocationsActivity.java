package com.walkernation.multiple.ui.dataOne;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.walkernation.db.R;

// "FragmentActivity" is the Activity Version needed for support library 
public class ListLocationsActivity extends LocationActivityBase {
	private static final String LOG_TAG = ListLocationsActivity.class
			.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
		promptOnBackPressed = true;
		// set the Layout of main Activity.
		// (contains only the fragment holder)
		setContentView(R.layout.main);
		LocationListFragment fragment;
		String imageFragmentTag = "imageFragmentTag";
		if (savedInstanceState == null) {
			fragment = new LocationListFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.locations, fragment, imageFragmentTag).commit();
		}

	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			event.startTracking();
			return true;		
		}
		return super.onKeyLongPress(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}
}
