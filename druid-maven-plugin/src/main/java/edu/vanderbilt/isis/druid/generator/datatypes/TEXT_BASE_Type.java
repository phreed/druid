package edu.vanderbilt.isis.druid.generator.datatypes;

public class TEXT_BASE_Type extends BaseDataType {

	@Override
	public String getDefaultJavaValue() {
		return "\"\""; // aka: ""
	}

	@Override
	public String getDefaultUIValue() {
		return "\"\""; // aka: ""
	}

	@Override
	String SqlDataType() {
		return "TEXT";
	}

	@Override
	String getJavaDataType() {
		return "String";
	}

	@Override
	String ReadParcelMethod() {
		return "readString";
	}

	@Override
	String WriteParcelMethod() {
		return "writeString";
	}

	@Override
	String ReadFromCursorMethod() {
		return "getString";
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

	@Override
	String wrapperClass() {
		// TODO Auto-generated method stub
		return "String";
	}

}
