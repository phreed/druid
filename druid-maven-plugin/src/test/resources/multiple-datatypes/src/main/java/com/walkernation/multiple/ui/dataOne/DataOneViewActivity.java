package com.walkernation.multiple.ui.dataOne;

import android.os.Bundle;

public class DataOneViewActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			int index = getIntent().getExtras().getInt(
					DataOneViewFragment.rowIdentifyerTAG);

			DataOneViewFragment details = DataOneViewFragment
					.newInstance(index);

			details.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}