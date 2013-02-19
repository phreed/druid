package edu.vanderbilt.isis.druid.generator.datatypes;

public class FloatType extends BaseDataType {

	@Override
	public String getDefaultJavaValue() {
		return "0";
	}

	@Override
	public String wrapperClass() {
		return "Float";
	}

	@Override
	public String getDefaultUIValue() {
		return "0.0";
	}

	@Override
	String SqlDataType() {
		return "REAL";
	}

	@Override
	String getJavaDataType() {
		return "float";
	}

	@Override
	String ReadParcelMethod() {
		return "readFloat";
	}

	@Override
	String WriteParcelMethod() {
		return "writeFloat";
	}

	@Override
	String ReadFromCursorMethod() {
		return "getFloat";
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
