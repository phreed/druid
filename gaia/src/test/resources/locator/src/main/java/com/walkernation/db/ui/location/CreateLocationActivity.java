package main.java.com.walkernation.db.ui.location;


import android.os.Bundle;

public class CreateLocationActivity extends LocationActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			CreateLocationFragment creator = CreateLocationFragment
					.newInstance();

			creator.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, creator).commit();
		}
	}
}