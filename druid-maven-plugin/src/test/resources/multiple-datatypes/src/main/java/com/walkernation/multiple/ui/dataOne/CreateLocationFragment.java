package com.walkernation.multiple.ui.dataOne;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.walkernation.db.R;
import com.walkernation.multiple.orm.LocationData;
import com.walkernation.multiple.provider.ContentDescriptor;

public class CreateLocationFragment extends LocationFragmentBase {

	public final static String LOG_TAG = CreateLocationFragment.class
			.getCanonicalName();
	final static Uri uri = ContentDescriptor.Location.CONTENT_URI;

	EditText userIdET;
	EditText latitudeET;
	EditText longitudeET;
	EditText heightET;

	Button buttonCreate;
	Button buttonClear;
	Button buttonCancel;

	int index;
	OnOpenWindowInterface mOpener;

	public final static String LOCATION = "location";

	public static CreateLocationFragment newInstance() {
		CreateLocationFragment f = new CreateLocationFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mOpener = (OnOpenWindowInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnOpenWindowListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		userIdET = (EditText) getView().findViewById(
				R.id.location_create_edittext_userID);
		latitudeET = (EditText) getView().findViewById(
				R.id.location_create_edittext_latitude);
		longitudeET = (EditText) getView().findViewById(
				R.id.location_create_edittext_longitude);
		heightET = (EditText) getView().findViewById(
				R.id.location_create_edittext_height);
		buttonClear = (Button) getView().findViewById(
				R.id.location_create_button_reset);
		buttonCancel = (Button) getView().findViewById(
				R.id.location_create_button_cancel);
		buttonCreate = (Button) getView().findViewById(
				R.id.location_create_button_save);

		buttonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userIdET.setText("");
				latitudeET.setText("");
				longitudeET.setText("");
				heightET.setText("");
			}
		});

		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getResources().getBoolean(R.bool.isTablet) == true) {
					// put
					mOpener.openViewLocationFragment(0);
				} else {
					getActivity().finish(); // same as hitting 'back' button
				}
			}
		});

		buttonCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int userID = Integer.valueOf(userIdET.getText().toString());
				int latitude = Integer.valueOf(latitudeET.getText().toString());
				int longitude = Integer.valueOf(longitudeET.getText()
						.toString());
				int hieght = Integer.valueOf(heightET.getText().toString());

				LocationData location = new LocationData(latitude, longitude,
						hieght, userID);
				try {
					insertNewLocation(location);
				} catch (RemoteException e) {
					Log.e(LOG_TAG,
							"Caught RemoteException => " + e.getMessage());
					e.printStackTrace();
				}

				// Toast.makeText(getActivity(), "Created...(not really)",
				// Toast.LENGTH_SHORT).show();
				if (getResources().getBoolean(R.bool.isTablet) == true) {
					// put
					mOpener.openViewLocationFragment(0);
				} else {
					getActivity().finish(); // same as hitting 'back' button
				}
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.location_creation_fragment,
				container, false);
		container.setBackgroundColor(Color.CYAN);
		return view;
	}

}
