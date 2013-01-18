package com.walkernation.multiple.ui.dataOne;

import android.os.Bundle;

public class ViewOpenDataActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			int index = getIntent().getExtras().getInt(
					ViewDataOneFragment.rowIdentifyerTAG);

			ViewDataOneFragment details = ViewDataOneFragment
					.newInstance(index);

			details.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}