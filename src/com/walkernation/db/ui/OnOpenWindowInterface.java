package com.walkernation.db.ui;

public interface OnOpenWindowInterface {
	public void openEditLocationFragment(int index);

	public void openViewLocationFragment(int index);

	public void openCreateLocationFragment();

	public void openListLocationsFragment();

	// ADD HERE FOR EACH : activity/Fragment
	// implement in LocationActivityBase.java
}
