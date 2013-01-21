package com.walkernation.multiple.ui.dataOne;


import android.os.Bundle;

public class DataOneCreateActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			DataOneCreateFragment creator = DataOneCreateFragment
					.newInstance();

			creator.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, creator).commit();
		}
	}
}