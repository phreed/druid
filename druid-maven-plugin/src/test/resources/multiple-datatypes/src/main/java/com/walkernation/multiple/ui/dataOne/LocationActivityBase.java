package com.walkernation.multiple.ui.dataOne;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.walkernation.multiple.R;

/**
 * Base class for all activities
 * 
 * @author Michael A. Walker
 * 
 */
public class LocationActivityBase extends FragmentActivity implements
		OnOpenWindowInterface {

	boolean promptOnBackPressed = false;
	DataOneListFragment fragment;
	private static final String LOG_TAG = LocationActivityBase.class
			.getCanonicalName();
	boolean mDualPane;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	public void onBackPressed() {
		if (promptOnBackPressed == true) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Closing Activity")
					.setMessage("Are you sure you want to close this activity?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							}).setNegativeButton("No", null).show();
		} else {
			super.onBackPressed();
		}
	}

	private boolean determineDualPane() {
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			mDualPane = true;
			return true;
		} else {
			mDualPane = false;
			return false;
		}
	}

	public void openViewLocationFragment(int index) {
		Log.d(LOG_TAG, "openViewLocationFragment(" + index + ")");
		if (determineDualPane()) {

			Fragment test = getSupportFragmentManager().findFragmentById(
					R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != DataOneViewFragment.class) {
				DataOneViewFragment details = DataOneViewFragment
						.newInstance(index);

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.
				ft.replace(R.id.details, details);

			} else {
				// Check what fragment is shown, replace if needed.
				DataOneViewFragment details = (DataOneViewFragment) getSupportFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null || details.getUniqueKey() != index) {
					// Make new fragment to show this selection.
					details = DataOneViewFragment.newInstance(index);

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
			Intent intent = newViewLocationIntent(this, index);
			startActivityForResult(intent, 0);
			// startActivity(intent);
		}
	}

	public void openEditLocationFragment(final int index) {
		Log.d(LOG_TAG, "openEditLocationFragment(" + index + ")");
		if (determineDualPane()) {

			Fragment test = getSupportFragmentManager().findFragmentById(
					R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != DataOneEditFragment.class) {
				DataOneEditFragment editor = DataOneEditFragment
						.newInstance(index);

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, editor);

			} else {
				// Check what fragment is shown, replace if needed.
				DataOneEditFragment editor = (DataOneEditFragment) getSupportFragmentManager()
						.findFragmentById(R.id.details);
				if (editor == null || editor.getUniqueKey() != index) {
					// Make new fragment to show this selection.
					editor = DataOneEditFragment.newInstance(index);

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
			Intent intent = newEditLocationIntent(this, index);
			startActivityForResult(intent, 0);
			// startActivity(intent);
		}
	}

	public void test() {
		Toast.makeText(getApplicationContext(),
				" => " + fragment.isAdded() + " " + fragment.isAdded(),
				Toast.LENGTH_SHORT).show();
	}

	public void openCreateLocationFragment() {
		Log.d(LOG_TAG, "openCreateLocationFragment");
		if (determineDualPane()) {

			Fragment test = getSupportFragmentManager().findFragmentById(
					R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != DataOneCreateFragment.class) {
				DataOneCreateFragment details = DataOneCreateFragment
						.newInstance();

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			} else {
				// Check what fragment is shown, replace if needed.
				DataOneCreateFragment details = (DataOneCreateFragment) getSupportFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null) {
					// Make new fragment to show this selection.
					details = DataOneCreateFragment.newInstance();

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
			Intent intent = newCreateLocationIntent(this);
			startActivityForResult(intent, 0);
			// startActivity(intent);
		}
	}

	@Override
	public void openListLocationsFragment() {
		Log.d(LOG_TAG, "openCreateLocationFragment");
		if (determineDualPane()) {
			// already displayed
			Fragment test = getSupportFragmentManager().findFragmentByTag(
					"imageFragmentTag");
			if (test != null) {
				DataOneListFragment t = (DataOneListFragment) test;
				t.updateLocationLocationData();
			}

		} else {

			/*
			 * Don't need to do anything, just close the currect activity ontop
			 * of the list
			 */
			// finish();

			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			// Intent intent = newListLocationIntent(this);
			// startActivity(intent);
		}
	}

	/*************************************************************************/
	/*
	 * Create Intents for Intents
	 */
	/*************************************************************************/

	public static Intent newViewLocationIntent(Activity activity, int index) {
		Intent intent = new Intent();
		intent.setClass(activity, DataOneViewActivity.class);
		intent.putExtra(DataOneViewFragment.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newEditLocationIntent(Activity activity, int index) {
		Intent intent = new Intent();
		intent.setClass(activity, DataOneEditActivity.class);
		intent.putExtra(DataOneEditFragment.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newListLocationIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, DataOneListActivity.class);
		return intent;
	}

	public static Intent newCreateLocationIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, DataOneCreateActivity.class);
		return intent;
	}

}
