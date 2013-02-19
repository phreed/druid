package edu.vanderbilt.isis.druid.generator.datatypes;

public class LONG_BASE_TYPE extends BaseDataType {

	@Override
	public String getDefaultJavaValue() {
		return "0";
	}

	@Override
	public String getDefaultUIValue() {
		return "0";
	}

	@Override
	public String wrapperClass() {
		// TODO Auto-generated method stub
		return "Long";
	}

	@Override
	String SqlDataType() {
		return "INTEGER";
	}

	@Override
	String getJavaDataType() {
		return "long";
	}

	@Override
	String ReadParcelMethod() {
		return "readLong";
	}

	@Override
	String WriteParcelMethod() {
		return "writeLong";
	}

	@Override
	String ReadFromCursorMethod() {
		return "getLong";
	}

	@Override
	String CreationViewType() {
		return "EditText";
	}

	@Override
	String EditViewType() {
		return "EditText";
	}

	@Override
	String ViewViewType() {
		return "TextView";
	}

	@Override
	String CustomRowViewType() {
		return "TextView";
	}

	@Override
	String CreationViewTypeAbbrv() {
		return "ET";
	}

	@Override
	String EditViewTypeAbbrv() {
		return "ET";
	}

	@Override
	String ViewViewTypeAbbrv() {
		return "TV";
	}

	@Override
	String CustomRowViewTypeAbbrv() {
		return "TV";
	}

}
