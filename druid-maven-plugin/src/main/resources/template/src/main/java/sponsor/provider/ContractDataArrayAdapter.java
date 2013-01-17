package com.walkernation.db.provider;

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

import com.walkernation.db.R;
import com.walkernation.db.orm.LocationData;

public class LocationDataArrayAdapter extends ArrayAdapter<LocationData> {

	private static final String LOG_TAG = LocationDataArrayAdapter.class
			.getCanonicalName();

	int resource;

	public LocationDataArrayAdapter(Context _context, int _resource,
			List<LocationData> _items) {
		super(_context, _resource, _items);
		Log.d(LOG_TAG, "constructor()");
		resource = _resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(LOG_TAG, "getView()");
		LinearLayout todoView = null;
		try {

			LocationData item = getItem(position);

			long latitude = item.latitude;
			long longitude = item.longitude;
			long height = item.height;
			long user_id = item.userID;

			if (convertView == null) {
				todoView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(inflater);
				vi.inflate(resource, todoView, true);
			} else {
				todoView = (LinearLayout) convertView;
			}

			TextView userID = (TextView) todoView.findViewById(R.id.user_id);
			TextView latitudeTextView = (TextView) todoView
					.findViewById(R.id.latitude);
			TextView longitudeTextView = (TextView) todoView
					.findViewById(R.id.longitude);
			TextView heightTextView = (TextView) todoView
					.findViewById(R.id.height);

			userID.setText("" + user_id);
			latitudeTextView.setText("" + latitude);
			longitudeTextView.setText("" + longitude);
			heightTextView.setText("" + height);

		} catch (Exception e) {
			Toast.makeText(getContext(), "exception in ArrayAdpter: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		return todoView;
	}

}