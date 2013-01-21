package com.walkernation.multiple.ui.dataOne;

import android.os.Bundle;

public class DataOneEditActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			int index = getIntent().getExtras().getInt(
					DataOneEditFragment.rowIdentifyerTAG);

			DataOneEditFragment editor = DataOneEditFragment
					.newInstance(index);

			editor.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, editor).commit();
		}
	}

}