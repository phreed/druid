package edu.vanderbilt.isis.druid.generator.datatypes;

public class BoolType extends BaseDataType {

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
		return "INTEGER";
	}

	@Override
	String getJavaDataType() {
		return "boolean";
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
		return "ToggleButton";
	}

	@Override
	String EditViewType() {
		return "ToggleButton";
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
	public String getUiViewDisplayFieldLinkToXMLTemplate() {
		return "" + field.getName().getCamel()
				+ "TB = (ToggleButton) getView().findViewById(R.id."
				+ relation.getName().getSnake() + "_create_value_"
				+ field.getName().getSnake() + ");";
	}

	@Override
	public String getUiCreateConvertValuesFromViewsToJavaTemplate() {
		return "if (" + field.getName().getCamel() + "Boolean == true) {\n    "
				+ field.getName().getCamel() + " = 1;\n} else {\n    "
				+ field.getName().getCamel() + " = 0;\n}";
	}

	@Override
	public String getUiViewSetDisplayViewFromOrmObjectTemplate() {
		return "final String " + field.getName().getCamel()
				+ "String;\nif (Integer.valueOf("
				+ relation.getName().getCamel() + "Data."
				+ field.getName().getCamel() + ") == 0){\n    "
				+ field.getName().getCamel()
				+ "String = \"False\";\n} else {\n    "
				+ field.getName().getCamel() + "String = \"True\";\n}\n"
				+ field.getName().getCamel() + "TV.setText("
				+ field.getName().getCamel() + "String);";
	}

	@Override
	public String getUiCreateDisplayFieldDeclarationTemplate() {
		return "ToggleButton " + field.getName().getCamel() + "TB;";
	}

	public String getUiViewDisplayFieldViewToDefaultTemplate() {
		return "";
	}

	@Override
	public String wrapperClass() {
		// TODO Auto-generated method stub
		return "Boolean";
	}
	// " + field.getName().getCamel() + "
}
