package edu.vanderbilt.isis.druid.generator.datatypes;

import edu.vanderbilt.isis.druid.generator.Contract.Field;
import edu.vanderbilt.isis.druid.generator.Contract.Relation;

public abstract class BaseDataType {

	// parental stuff
	Relation relation;
	Field field;

	public void setRelation(Relation rel) {
		relation = rel;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Relation getRelation() {
		return relation;
	}

	public Field getField() {
		return field;
	}

	abstract String getDefaultJavaValue();

	abstract String getDefaultUIValue();

	abstract String wrapperClass();

	/**
	 * what SQLite data Type should this 'Type' be stored as?
	 * 
	 * @return
	 */
	abstract String SqlDataType();

	/**
	 * what Java data Type should this 'Type' be stored as?
	 * 
	 * @return
	 */
	abstract String getJavaDataType();

	/**
	 * What 'method' should be used when reading this type from a parcel
	 * 
	 * @return
	 */
	abstract String ReadParcelMethod();

	/**
	 * What 'method' should be used when writing this type to a parcel
	 * 
	 * @return
	 */
	abstract String WriteParcelMethod();

	/**
	 * what 'method' should be used to read this 'type' from a cursor
	 * 
	 * @return
	 */
	abstract String ReadFromCursorMethod();

	/**
	 * What is the 'Type' of View that should be used on the Creation fragment
	 * 
	 * @return
	 */
	abstract String CreationViewType();

	/**
	 * What is the 'Type' of View that should be used on the Edit fragment
	 * 
	 * @return
	 */
	abstract String EditViewType();

	/**
	 * What is the 'Type' of View that should be used on the View fragment
	 * 
	 * @return
	 */
	abstract String ViewViewType();

	/**
	 * What is the 'Type' of View that should be used on the CustomRow Adaptor
	 * in the ListView
	 * 
	 * @return
	 */
	abstract String CustomRowViewType();

	/**
	 * Abbreviation for the View Type for Creation
	 * 
	 * @return
	 */
	abstract String CreationViewTypeAbbrv();

	/**
	 * Abbreviation for the View Type for Edit
	 * 
	 * @return
	 */
	abstract String EditViewTypeAbbrv();

	/**
	 * Abbreviation for the View Type for View
	 * 
	 * @return
	 */
	abstract String ViewViewTypeAbbrv();

	/**
	 * Abbreviation for the View Type for CustomRow
	 * 
	 * @return
	 */
	abstract String CustomRowViewTypeAbbrv();

	/** ORM **/

	public String getOrmDataObjectConstructReadParcelTemplate() {
		String rValue = field.getName().getCamel() + " = in."
				+ ReadParcelMethod() + "();";
		return rValue;
	}

	public String getOrmDataObjectConstructWriteParcelTemplate() {
		return "dest." + WriteParcelMethod() + "(" + field.getName().getCamel()
				+ ");";
	}

	public String getOrmDataObjectCosntructorParametersTemplate() {
		return "this." + field.getName().getCamel() + " = "
				+ field.getName().getCamel();
	}

	public String getOrmDataObjectToStringTemplate() {
		return "\" " + field.getName().getCamel() + ": \" + "
				+ field.getName().getCamel();
	}

	public String getOrmDataObjectConstructCloneTemplate() {
		return getOrmDataObjectCosntructorParametersTemplate();
	}

	public String getOrmDataObjectConstructorAssignmentsTemplate() {
		return "";
	}

	public String getOrmDataObjectPutDeclarationTemplate() {
		return "public " + getJavaDataType() + " " + field.getName().getCamel()
				+ ";";
	}

	public String getOrmCreatorputFieldIntoCVTemplate() {
		return "rValue.put(ContentDescriptor."
				+ relation.getName().getBactrian() + ".Cols."
				+ field.getName().getCobra() + ", data."
				+ field.getName().getCamel() + ");";
	}

	public String getOrmCreatorGetFieldFromCursorTemplate() {
		return getJavaDataType() + " " + field.getName().getCamel()
				+ " = cursor." + ReadFromCursorMethod()
				+ "(cursor.getColumnIndex(ContentDescriptor."
				+ relation.getName().getBactrian() + ".Cols."
				+ field.getName().getCobra() + "));";
	}

	public String getOrmConstructorParameters() {
		return getJavaDataType() + " " + field.getName().getCamel();
	}

	public String getOrmDataDeclaration() {
		return "";
	}

	public String getProviderSQLTableCreationFieldAndType() {
		return "";
	}

	public String getPutJavaTypeDeclaration() {
		String rValue = "";
		if (getIsThisFieldInUI(field) == true) {
			rValue = getJavaDataType() + " " + field.getName().getCamel()
					+ " = item." + field.getName().getCamel() + ";";
		}
		return rValue;
	}

	public String getPutDisplayViewDeclarations() {
		String rValue = "";
		if (getIsThisFieldInUI(field) == true) {
			rValue = CustomRowViewType() + " " + field.getName().getCamel()
					+ CustomRowViewTypeAbbrv() + " = (" + CustomRowViewType()
					+ ") todoView.findViewById(R.id."
					+ relation.getName().getSnake() + "_listview_custom_row_"
					+ field.getName().getSnake() + "_" + CustomRowViewType()
					+ ");";
		}
		return rValue;
	}

	public String getPutSetValuesToDisplay() {
		String rValue = "";
		if (getIsThisFieldInUI(field) == true) {
			rValue = field.getName().getCamel() + CustomRowViewTypeAbbrv()
					+ ".setText(\"\" + " + field.getName().getCamel() + ");\n";
		}
		return rValue;
	}

	/** UI package **/

	public boolean getIsThisFieldInUI(Field field) {
		if (relation.getKeyMap().get("ui").getFields()
				.contains(field.getName().getNorm()) == true) {
			return true;
		} else {
			return false;
		}
	}

	public String getUiViewDisplayFieldDeclarationTemplate() {
		return "";
	}

	public String getUiViewDisplayFieldLinkToXMLTemplate() {
		return "";
	}

	public String getUiViewDisplayFieldViewToDefaultTemplate() {
		return "" + field.getName().getCamel() + "ET.setText(\"\"+ "
				+ getDefaultUIValue() + ");";
	}

	public String getUiViewSetDisplayViewFromOrmObjectTemplate() {
		return "";
	}

	public String getUiCreateDisplayFieldDeclarationTemplate() {
		return "";
	}

	public String getUiCreateDisplayFieldLinkToXMLTemplate() {
		return "" + field.getName().getCamel() + "" + CreationViewTypeAbbrv()
				+ " = (" + CreationViewType()
				+ ") getView().findViewById(R.id."
				+ relation.getName().getSnake() + "_create_value_"
				+ field.getName().getSnake() + ");";
	}

	public String getUiCreateDisplayFieldViewToDefaultTemplate() {
		return "" + field.getName().getCamel() + "" + CreationViewTypeAbbrv()
				+ ".setText(\"\"+ " + getDefaultJavaValue() + ");";
	}

	public String getUiCreateGetValuesFromViewsTemplate() {
		return "Editable " + field.getName().getCamel() + "Createable = "
				+ field.getName().getCamel() + "" + CreationViewTypeAbbrv()
				+ ".getText();";
	}

	public String getUiCreateJavaFieldDeclarationAndInitializationTemplate() {
		return "" + getJavaDataType() + " " + field.getName().getCamel()
				+ " = " + getJavaDataType() + ";";
	}

	public String getUiCreateConvertValuesFromViewsToJavaTemplate() {
		return "" + field.getName().getCamel() + " = " + wrapperClass()
				+ ".valueOf(" + field.getName().getCamel()
				+ "Editable.toString());";
	}

	public String getUiCreateOrmConstructorFieldNameTemplate() {
		return "";
	}

	public String getUiEditDisplayFieldDeclarationTemplate() {
		return "";
	}

	public String getUiEditDisplayFieldLinkToXMLTemplate() {
		return "";
	}

	public String getUiEditGetValuesFromViewsTemplate() {
		return "";
	}

	public String getUiEditJavaFieldDeclarationAndInitializationTemplate() {
		return "";
	}

	public String getUiEditConvertValuesFromViewsToJavaTemplate() {
		return "";
	}

	public String getUiEditOrmConstructorFieldNameTemplate() {
		return "";
	}

	public String getUiEditSetDisplayViewFromOrmObjectTemplate() {
		return "";
	}

}
