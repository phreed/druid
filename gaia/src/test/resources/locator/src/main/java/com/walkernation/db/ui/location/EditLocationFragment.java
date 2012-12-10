package main.java.com.walkernation.db.ui.location;

import main.java.com.walkernation.db.orm.LocationData;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.walkernation.db.R;

public class EditLocationFragment extends LocationFragmentBase {

	final static public String LOG_TAG = EditLocationFragment.class
			.getCanonicalName();
	final static public String rowIdentifyerTAG = "index";

	EditText userIdTV;
	EditText latitudeTV;
	EditText longitudeTV;
	EditText heightTV;

	Button saveButton;
	Button resetButton;
	Button cancelButton;

	OnOpenWindowInterface mOpener;

	OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edit_location_button_save:
				doSaveButtonClick();
				break;
			case R.id.edit_location_button_reset:
				doResetButtonClick();
				break;
			case R.id.edit_location_button_cancel:
				doCancelButtonClick();
				break;
			default:
				break;
			}
		}
	};

	public static EditLocationFragment newInstance(int index) {
		EditLocationFragment f = new EditLocationFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt(rowIdentifyerTAG, index);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOpener = (OnOpenWindowInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnOpenWindowListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get the Buttons
		saveButton = (Button) getView().findViewById(
				R.id.edit_location_button_save);
		resetButton = (Button) getView().findViewById(
				R.id.edit_location_button_reset);
		cancelButton = (Button) getView().findViewById(
				R.id.edit_location_button_cancel);
		// Get the EditTexts
		userIdTV = (EditText) getView().findViewById(R.id.userID);
		userIdTV.setKeyListener(null);
		latitudeTV = (EditText) getView().findViewById(R.id.latitude);
		longitudeTV = (EditText) getView().findViewById(R.id.longitude);
		heightTV = (EditText) getView().findViewById(R.id.height);
		// set the EditTexts to this Location's Values
		setValuesToDefault();
		// setup the onClick callback methods
		saveButton.setOnClickListener(myOnClickListener);
		resetButton.setOnClickListener(myOnClickListener);
		cancelButton.setOnClickListener(myOnClickListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.location_edit_fragment,
				container, false);
		container.setBackgroundColor(Color.CYAN);
		return view;
	}

	public void doResetButtonClick() {
		setValuesToDefault();
	}

	public void doSaveButtonClick() {
		Toast.makeText(getActivity(), "Updated.", Toast.LENGTH_SHORT).show();
		LocationData location = makeLocationDataFromUI();
		if (location != null) {
			try {
				updateLocationWithUserID(location);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			mOpener.openViewLocationFragment(getUniqueKey());
		} else {
			getActivity().finish(); // same as hitting 'back' button
		}
	}

	public LocationData makeLocationDataFromUI() {
		LocationData rValue = null;

		Editable longStr = longitudeTV.getText();
		Editable latStr = latitudeTV.getText();
		Editable heightStr = heightTV.getText();

		if ((longStr.length() > 0) && (longStr.length() > 0)
				&& (longStr.length() > 0)) {
			long userID = getUniqueKey();
			long longitude = Long.valueOf(longStr.toString());
			long latitude = Long.valueOf(latStr.toString());
			long height = Long.valueOf(heightStr.toString());
			rValue = new LocationData(latitude, longitude, height, userID);
		}

		return rValue;
	}

	public void doCancelButtonClick() {
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			// put
			mOpener.openViewLocationFragment(getUniqueKey());
		} else {
			getActivity().finish(); // same as hitting 'back' button
		}

	}

	public boolean setValuesToDefault() {
		LocationData location;
		try {
			location = getLocationDataForUserID(getUniqueKey());
		} catch (RemoteException e) {
			Log.d(LOG_TAG, "" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		if (location != null) {
			// set the EditTexts to the current values
			userIdTV.setText(String.valueOf(location.userID));
			latitudeTV.setText(String.valueOf(location.latitude));
			longitudeTV.setText(String.valueOf(location.longitude));
			heightTV.setText(String.valueOf(location.height));
			return true;
		}
		return false;
	}

	public int getUniqueKey() {
		return getArguments().getInt(rowIdentifyerTAG, 0);
	}

}
