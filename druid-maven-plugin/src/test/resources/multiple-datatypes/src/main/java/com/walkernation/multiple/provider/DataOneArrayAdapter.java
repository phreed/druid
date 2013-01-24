package com.walkernation.multiple.provider;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.walkernation.multiple.R;
import com.walkernation.multiple.orm.DataOneData;

public class DataOneArrayAdapter extends ArrayAdapter<DataOneData> {

	private static final String LOG_TAG = DataOneArrayAdapter.class
			.getCanonicalName();

	int resource;

	public DataOneArrayAdapter(Context _context, int _resource,
			List<DataOneData> _items) {
		super(_context, _resource, _items);
		Log.d(LOG_TAG, "constructor()");
		resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(LOG_TAG, "getView()");
		LinearLayout todoView = null;
		try {

			DataOneData item = getItem(position);

			// int _id = item._id;
			// byte byteName = item.byteName;
			short shortName = item.shortName;
			int intName = item.intName;
			long longName = item.longName;
			// float floatName = item.floatName;
			// double doubleName = item.doubleName;
			String stringName = item.stringName;
			// boolean booleanName = item.booleanName;

			if (convertView == null) {
				todoView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(inflater);
				vi.inflate(resource, todoView, true);
			} else {
				todoView = (LinearLayout) convertView;
			}

			TextView shortNameTV = (TextView) todoView
					.findViewById(R.id.shortValue);
			TextView intNameTV = (TextView) todoView.findViewById(R.id.intValue);
			TextView longNameTV = (TextView) todoView
					.findViewById(R.id.longValue);
			TextView stringNameTV = (TextView) todoView
					.findViewById(R.id.stringValue);

			shortNameTV.setText("" + shortName);
			intNameTV.setText("" + intName);
			longNameTV.setText("" + longName);
			stringNameTV.setText("" + stringName);

		} catch (Exception e) {
			Toast.makeText(getContext(),
					"exception in ArrayAdpter: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		return todoView;
	}
}