package edu.vanderbilt.isis.druid.generator.datatypes;

public class INTEGER_BASE_TYPE extends BaseDataType {

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
		return "Integer";
	}

	@Override
	String SqlDataType() {
		return "INTEGER";
	}

	@Override
	String getJavaDataType() {
		return "int";
	}

	@Override
	String ReadParcelMethod() {
		return "readInt";
	}

	@Override
	String WriteParcelMethod() {
		return "writeInt";
	}

	@Override
	String ReadFromCursorMethod() {
		return "getInt";
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
