package com.walkernation.multiple.ui.dataTwo;

import com.walkernation.multiple.orm.MultipleResolver;

public interface OnOpenWindowInterface {
	
	public MultipleResolver getMultipleResolver();
	
	public void openEditLocationFragment(int index);

	public void openViewLocationFragment(int index);

	public void openCreateLocationFragment();

	public void openListLocationsFragment();

	// ADD HERE FOR EACH : activity/Fragment
	// implement in LocationActivityBase.java
}
