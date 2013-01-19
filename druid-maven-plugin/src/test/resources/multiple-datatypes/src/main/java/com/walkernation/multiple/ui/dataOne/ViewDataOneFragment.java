package com.walkernation.multiple.ui.dataOne;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.walkernation.db.R;
import com.walkernation.multiple.orm.DataOneData;

public class ViewDataOneFragment extends LocationFragmentBase {
	// LOG TAG, handles refactoring changes
	private static final String LOG_TAG = ViewDataOneFragment.class
			.getCanonicalName();

	public final static String rowIdentifyerTAG = "index";

	private OnOpenWindowInterface mOpener;

	public final static String LOCATION = "location";

	TextView byteNameTV;
	TextView shortNameTV;
	TextView intNameTV;
	TextView longNameTV;
	TextView floatNameTV;
	TextView doubleNameTV;
	TextView stringNameTV;
	TextView booleanTV;

	// LocationData locationData;
	Button editButton;
	Button deleteButton;

	OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.button_location_view_to_delete:
				deleteButtonPressed();
				break;
			case R.id.button_location_view_to_edit:
				editButtonPressed();
				break;
			default:
				break;
			}
		}
	};

	public static ViewDataOneFragment newInstance(int index) {
		ViewDataOneFragment f = new ViewDataOneFragment();

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

		byteNameTV = (TextView) getView().findViewById(R.id.byteName);
		shortNameTV = (TextView) getView().findViewById(R.id.shortName);
		intNameTV = (TextView) getView().findViewById(R.id.intName);
		longNameTV = (TextView) getView().findViewById(R.id.longName);
		floatNameTV = (TextView) getView().findViewById(R.id.floatName);
		doubleNameTV = (TextView) getView().findViewById(R.id.doubleName);
		stringNameTV = (TextView) getView().findViewById(R.id.stringName);
		booleanTV = (TextView) getView().findViewById(R.id.booleanName);

		editButton = (Button) getView().findViewById(
				R.id.button_location_view_to_edit);
		deleteButton = (Button) getView().findViewById(
				R.id.button_location_view_to_delete);

		editButton.setOnClickListener(myOnClickListener);
		deleteButton.setOnClickListener(myOnClickListener);
		setUiToLocationData(getUniqueKey());
	}

	public void setUiToLocationData(int getUniqueKey) {
		DataOneData dataOneData = null;
		try { // TODO fix this to pull the right value by '_id' or something...
			dataOneData = null;// = getLocationDataForUserID(getUniqueKey);
		} catch (Exception e) {
			Log.e(LOG_TAG, "RemoteException Caught => " + e.getMessage());
			e.printStackTrace();
		}
		if (dataOneData == null) {
			getView().setVisibility(View.GONE);
		} else { // else it just displays empty screen
			byteNameTV.setText(String.valueOf(dataOneData.byteName));
			shortNameTV.setText(String.valueOf(dataOneData.shortName));
			intNameTV.setText(String.valueOf(dataOneData.intName));
			longNameTV.setText(String.valueOf(dataOneData.longName));
			floatNameTV.setText(String.valueOf(dataOneData.floatName));
			doubleNameTV.setText(String.valueOf(dataOneData.doubleName));
			stringNameTV.setText(String.valueOf(dataOneData.stringName));
			booleanTV.setText(String.valueOf(dataOneData.booleanName));
		}
	}

	private void editButtonPressed() {
		mOpener.openEditLocationFragment(getUniqueKey());
	}

	private void deleteButtonPressed() {
		String message;

		message = getResources().getString(
				R.string.location_view_deletion_dialog_message);

		new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.location_view_deletion_dialog_title)
				.setMessage(message)
				.setPositiveButton(R.string.location_view_deletion_dialog_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									deleteAllLocationsWithUserID(getUniqueKey());
								} catch (RemoteException e) {
									Log.e(LOG_TAG, "RemoteException Caught => "
											+ e.getMessage());
									e.printStackTrace();
								}
								mOpener.openListLocationsFragment();
								if (getResources().getBoolean(R.bool.isTablet) == true) {
									mOpener.openViewLocationFragment(-1);
								} else {
									getActivity().finish();
								}
							}

						})
				.setNegativeButton(R.string.location_view_deletion_dialog_no,
						null).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.location_view_fragment,
				container, false);
		container.setBackgroundColor(Color.CYAN);
		return view;
	}

	public int getUniqueKey() {
		return getArguments().getInt(rowIdentifyerTAG, 0);
	}

}
