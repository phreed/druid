package edu.vanderbilt.isis.druid.generator.datatypes;

public class BlobType extends BaseDataType {

	String typeName = "blob";

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return typeName;
	}

	@Override
	public String getDefaultJavaValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultUIValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String SqlDataType() {
		// TODO Auto-generated method stub
		return "BLOB";
	}

	@Override
	String getJavaDataType() {
		return "byte[]";
	}

	@Override
	String ReadParcelMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String WriteParcelMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String ReadFromCursorMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String CreationViewType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String EditViewType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String ViewViewType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String CustomRowViewType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String CreationViewTypeAbbrv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String EditViewTypeAbbrv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String ViewViewTypeAbbrv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String CustomRowViewTypeAbbrv() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String wrapperClass() {
		// TODO Auto-generated method stub
		return "";
	}

}
