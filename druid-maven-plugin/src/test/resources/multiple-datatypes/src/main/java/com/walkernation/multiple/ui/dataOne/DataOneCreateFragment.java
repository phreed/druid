package com.walkernation.multiple.ui.dataOne;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.walkernation.multiple.R;
import com.walkernation.multiple.orm.DataOneData;
import com.walkernation.multiple.orm.MultipleResolver;

public class DataOneCreateFragment extends Fragment {

	public final static String LOG_TAG = DataOneCreateFragment.class
			.getCanonicalName();

	// EditText(s) used
	EditText byteNameET;
	EditText shortNameET;
	EditText intNameET;
	EditText longNameET;
	EditText floatNameET;
	EditText doubleNameET;
	EditText stringNameET;
	ToggleButton booleanNameTB;

	Button buttonCreate;
	Button buttonClear;
	Button buttonCancel;

	// int index;
	OnOpenWindowInterface mOpener;
	MultipleResolver resolver;

	public final static String LOCATION = "location";

	public static DataOneCreateFragment newInstance() {
		DataOneCreateFragment f = new DataOneCreateFragment();
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
			resolver = new MultipleResolver(activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnOpenWindowListener");
		}
	}

	@Override
	public void onDetach() {
		mOpener = null;
		resolver = null;
		super.onDetach();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the EditTexts
		byteNameET = (EditText) getView().findViewById(R.id.byteNameCreate);
		shortNameET = (EditText) getView().findViewById(R.id.shortNameCreate);
		intNameET = (EditText) getView().findViewById(R.id.intNameCreate);
		longNameET = (EditText) getView().findViewById(R.id.longNameCreate);
		floatNameET = (EditText) getView().findViewById(R.id.floatNameCreate);
		doubleNameET = (EditText) getView().findViewById(R.id.doubleNameCreate);
		stringNameET = (EditText) getView().findViewById(R.id.stringNameCreate);
		booleanNameTB = (ToggleButton) getView().findViewById(
				R.id.booleanNameToggleButtonCreate);

		buttonClear = (Button) getView().findViewById(
				R.id.dataone_create_button_reset);
		buttonCancel = (Button) getView().findViewById(
				R.id.dataone_create_button_reset);
		buttonCreate = (Button) getView().findViewById(
				R.id.dataone_create_button_save);

		buttonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				byteNameET.setText("");
				shortNameET.setText("");
				longNameET.setText("");
				floatNameET.setText("");
				doubleNameET.setText("");
				stringNameET.setText("");
				booleanNameTB.setChecked(false);
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
				// local Editables
				Editable byteEditable = byteNameET.getText();
				Editable shortEditable = shortNameET.getText();
				Editable intEditable = intNameET.getText();
				Editable longEditable = longNameET.getText();
				Editable floatEditable = floatNameET.getText();
				Editable doubleEditable = doubleNameET.getText();
				Editable stringEditable = doubleNameET.getText();
				Editable booleanEditable = doubleNameET.getText();

				// pull values from Editables
				byte byteNameValue = Byte.valueOf(byteEditable.toString());
				short shortNameValue = Short.valueOf(shortEditable.toString());
				int intNameValue = Integer.valueOf(intEditable.toString());
				long longNameValue = Long.valueOf(longEditable.toString());
				float floatNameValue = Float.valueOf(floatEditable.toString());
				double doubleNameValue = Double.valueOf(doubleEditable
						.toString());
				String stringNameValue = stringEditable.toString();
				boolean booleanNameValue = (Integer.valueOf(booleanEditable
						.toString()) == 0) ? false : true;

				// new DataOneData object with above info
				DataOneData newData = new DataOneData(-1, byteNameValue,
						shortNameValue, intNameValue, longNameValue,
						floatNameValue, doubleNameValue, stringNameValue,
						booleanNameValue);
				// insert it through Resolver to be put into ContentProvider
				try {
					resolver.insert(newData);
				} catch (RemoteException e) {
					Log.e(LOG_TAG,
							"Caught RemoteException => " + e.getMessage());
					e.printStackTrace();
				}
				// return back to proper state
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
		View view = inflater.inflate(R.layout.data_one_creation_fragment,
				container, false);
		container.setBackgroundColor(Color.CYAN);
		return view;
	}

}
