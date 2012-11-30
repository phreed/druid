package com.walkernation.db;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ActivityLauncherObject {

	public static final int LIST = 1;
	public static final int VIEW = 2;
	public static final int EDIT = 3;
	public static final int UPDATE = 4;

	private static final String LOG_TAG = ActivityLauncherObject.class
			.getCanonicalName();

	FragmentActivity mActivity;
	boolean mDualPane;

	public ActivityLauncherObject(FragmentActivity activity) {
		mActivity = activity;
	}

	public void openListLocationsFragment() {
		Log.d(LOG_TAG, "openCreateLocationFragment");
		if (determineDualPane()) {
			// already displayed
			Fragment test = mActivity.getSupportFragmentManager()
					.findFragmentByTag("imageFragmentTag");
			if (test != null) {
				ListLocationsFragment t = (ListLocationsFragment) test;
				t.mRetainedFragment.updateLocationLocationData();
			}

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newListLocationIntent(mActivity);
			mActivity.startActivity(intent);
		}
	}

	public void openEditLocationFragment(final int index) {
		Log.d(LOG_TAG, "openEditLocationFragment(" + index + ")");
		if (determineDualPane()) {

			Fragment test = mActivity.getSupportFragmentManager()
					.findFragmentById(R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != EditLocationFragment.class) {
				EditLocationFragment editor = EditLocationFragment
						.newInstance(index);

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, editor);

			} else {
				// Check what fragment is shown, replace if needed.
				EditLocationFragment editor = (EditLocationFragment) mActivity
						.getSupportFragmentManager().findFragmentById(
								R.id.details);
				if (editor == null || editor.getUniqueKey() != index) {
					// Make new fragment to show this selection.
					editor = EditLocationFragment.newInstance(index);

				}
				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, editor);

			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newEditLocationIntent(mActivity, index);
			mActivity.startActivity(intent);
		}
	}

	public void openCreateLocationFragment() {
		Log.d(LOG_TAG, "openCreateLocationFragment");
		if (determineDualPane()) {

			Fragment test = mActivity.getSupportFragmentManager()
					.findFragmentById(R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != CreateLocationFragment.class) {
				CreateLocationFragment details = CreateLocationFragment
						.newInstance();

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			} else {
				// Check what fragment is shown, replace if needed.
				CreateLocationFragment details = (CreateLocationFragment) mActivity
						.getSupportFragmentManager().findFragmentById(
								R.id.details);
				if (details == null) {
					// Make new fragment to show this selection.
					details = CreateLocationFragment.newInstance();

				}
				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newCreateLocationIntent(mActivity);
			mActivity.startActivity(intent);
		}
	}

	private boolean determineDualPane() {
		if (mActivity.getResources().getBoolean(R.bool.isTablet) == true) {
			mDualPane = true;
			return true;
		} else {
			mDualPane = false;
			return false;
		}
	}

	/*************************************************************************/
	/*
	 * Create Intents for Intents
	 */
	/*************************************************************************/

	public static Intent newViewLocationIntent(Activity activity, int index) {
		Intent intent = new Intent();
		intent.setClass(activity, ViewLocationActivity.class);
		intent.putExtra(ViewLocationFragment.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newEditLocationIntent(Activity activity, int index) {
		Intent intent = new Intent();
		intent.setClass(activity, EditLocationActivity.class);
		intent.putExtra(EditLocationFragment.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newListLocationIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, ListLocationsActivity.class);
		return intent;
	}

	public static Intent newCreateLocationIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, CreateLocationActivity.class);
		return intent;
	}

}
