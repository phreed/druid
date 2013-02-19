package edu.vanderbilt.isis.druid.generator.datatypes;

public class ShortType extends INTEGER_BASE_TYPE {

	@Override
	String getJavaDataType() {
		return "short";
	}

	@Override
	public String ReadParcelMethod() {
		return "readLong";
	}

	@Override
	public String WriteParcelMethod() {
		return "writeLong";
	}

	@Override
	public String ReadFromCursorMethod() {
		// TODO Auto-generated method stub
		return "getLong";
	}
	
}
