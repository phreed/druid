package com.walkernation.db;

import com.walkernation.db.ui.ActivityLauncherObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class FragmentHoldingActivity extends FragmentActivity {

	ActivityLauncherObject launcher;

	@Override
	protected void onCreate(Bundle arg0) {

		launcher = new ActivityLauncherObject(this);

		super.onCreate(arg0);

	}
}
