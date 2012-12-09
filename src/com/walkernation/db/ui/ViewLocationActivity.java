package com.walkernation.db.ui;


import android.os.Bundle;

public class ViewLocationActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			int index = getIntent().getExtras().getInt(
					ViewLocationFragment.rowIdentifyerTAG);

			ViewLocationFragment details = ViewLocationFragment
					.newInstance(index);

			details.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}