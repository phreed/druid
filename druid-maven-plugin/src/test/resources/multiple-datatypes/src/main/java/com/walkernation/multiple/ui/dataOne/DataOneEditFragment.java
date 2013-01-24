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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.walkernation.multiple.R;
import com.walkernation.multiple.orm.DataOneData;
import com.walkernation.multiple.orm.MultipleResolver;

public class DataOneEditFragment extends Fragment {

	final static public String LOG_TAG = DataOneEditFragment.class
			.getCanonicalName();
	// variable for passing around row index
	final static public String rowIdentifyerTAG = "index";

	// EditText(s) used
	EditText byteNameET;
	EditText shortNameET;
	EditText intNameET;
	EditText longNameET;
	EditText floatNameET;
	EditText doubleNameET;
	EditText stringNameET;
	ToggleButton booleanNameTB;

	// Button(s) used
	Button saveButton;
	Button resetButton;
	Button cancelButton;

	// parent Activity
	OnOpenWindowInterface mOpener;
	// custom ContentResolver wrapper.
	MultipleResolver resolver;

	// listener to button presses.
	// TODO determine/label pattern.
	OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dataone_edit_button_save:
				doSaveButtonClick();
				break;
			case R.id.dataone_edit_button_reset:
				doResetButtonClick();
				break;
			case R.id.dataone_edit_button_cancel:
				doCancelButtonClick();
				break;
			default:
				break;
			}
		}
	};

	public static DataOneEditFragment newInstance(int index) {
		DataOneEditFragment f = new DataOneEditFragment();
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get the Buttons
		saveButton = (Button) getView().findViewById(
				R.id.dataone_edit_button_save);
		resetButton = (Button) getView().findViewById(
				R.id.dataone_edit_button_reset);
		cancelButton = (Button) getView().findViewById(
				R.id.dataone_edit_button_cancel);
		// Get the EditTexts
		byteNameET = (EditText) getView().findViewById(R.id.byteValue);
		// byteNameET.setKeyListener(null); // disable changing
		shortNameET = (EditText) getView().findViewById(R.id.shortValue);
		intNameET = (EditText) getView().findViewById(R.id.intValue);
		longNameET = (EditText) getView().findViewById(R.id.longValue);
		floatNameET = (EditText) getView().findViewById(R.id.floatValue);
		doubleNameET = (EditText) getView().findViewById(R.id.doubleValue);
		stringNameET = (EditText) getView().findViewById(R.id.stringValue);
		booleanNameTB = (ToggleButton) getView().findViewById(
				R.id.booleanValueToggleButton);
		// setup the onClick callback methods
		saveButton.setOnClickListener(myOnClickListener);
		resetButton.setOnClickListener(myOnClickListener);
		cancelButton.setOnClickListener(myOnClickListener);
		// set the EditTexts to this Location's Values
		setValuesToDefault();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.data_one_edit_fragment,
				container, false);
		container.setBackgroundColor(Color.CYAN);
		return view;
	}

	public void doResetButtonClick() {
		setValuesToDefault();
	}

	public void doSaveButtonClick() {
		Toast.makeText(getActivity(), "Updated.", Toast.LENGTH_SHORT).show();
		DataOneData location = makeLocationDataFromUI();
		if (location != null) {
			try {
				resolver.updateDataOneWithID(location);
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

	public DataOneData makeLocationDataFromUI() {

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
		byte byteNameValue = (byte) (Integer.valueOf(byteEditable.toString()) % 128);
		short shortNameValue = Short.valueOf(shortEditable.toString());
		int intNameValue = Integer.valueOf(intEditable.toString());
		long longNameValue = Long.valueOf(longEditable.toString());
		float floatNameValue = Float.valueOf(floatEditable.toString());
		double doubleNameValue = Double.valueOf(doubleEditable.toString());
		String stringNameValue = stringEditable.toString();
		boolean booleanNameValue = (Integer.valueOf(booleanEditable.toString()) == 0) ? false
				: true;

		// return new DataOneData object with
		return new DataOneData(getUniqueKey(), byteNameValue, shortNameValue,
				intNameValue, longNameValue, floatNameValue, doubleNameValue,
				stringNameValue, booleanNameValue);

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

		DataOneData dataOneData;
		try {
			dataOneData = resolver.getDataOneDataViaRowID(getUniqueKey());
		} catch (RemoteException e) {
			Log.d(LOG_TAG, "" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		if (dataOneData != null) {
			Log.d(LOG_TAG, "setValuesToDefualt :" + dataOneData.toString());
			// set the EditTexts to the current values
			byteNameET.setText(String.valueOf(dataOneData.byteName));
			shortNameET.setText(String.valueOf(dataOneData.shortName));
			intNameET.setText(String.valueOf(dataOneData.intName));
			longNameET.setText(String.valueOf(dataOneData.longName));
			floatNameET.setText(String.valueOf(dataOneData.floatName));
			doubleNameET.setText(String.valueOf(dataOneData.doubleName));
			stringNameET.setText(dataOneData.stringName);
			booleanNameTB.setChecked(dataOneData.booleanName);
			return true;
		}
		return false;
	}

	public int getUniqueKey() {
		return getArguments().getInt(rowIdentifyerTAG, 0);
	}

}
