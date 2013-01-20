package com.walkernation.multiple.ui.dataOne;

import com.walkernation.multiple.orm.MultipleResolver;

public interface OnOpenWindowInterface {
	public void openEditLocationFragment(int index);

	public void openViewLocationFragment(int index);

	public void openCreateLocationFragment();

	public void openListLocationsFragment();

	public MultipleResolver getMultipleResolver();
	// ADD HERE FOR EACH : activity/Fragment
	// implement in LocationActivityBase.java
}
