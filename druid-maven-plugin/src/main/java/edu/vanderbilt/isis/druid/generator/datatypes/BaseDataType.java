package edu.vanderbilt.isis.druid.generator.datatypes;

import edu.vanderbilt.isis.druid.generator.Contract.Field;
import edu.vanderbilt.isis.druid.generator.Contract.Relation;

public class BaseDataType {

	// parental stuff
	Relation relation;
	Field field;

	// for use in the other values
	String javaType;
	String defaultJavaValue;
	String defaultUIValue;

	/** ORM package **/
	// orm data object
	String ormDataObjectConstructReadParcelTemplate;
	String ormDataObjectConstructWriteParcelTemplate;
	String ormDataObjectCosntructorParametersTemplate;
	String ormDataObjectToStringTemplate;
	String ormDataObjectConstructCloneTemplate;
	String ormDataObjectConstructorAssignmentsTemplate;
	String ormDataObjectPutDeclarationTemplate;
	// orm creator
	String ormCreatorputFieldIntoCVTemplate;
	String ormCreatorGetFieldFromCursorTemplate;
	// orm constructor
	String ormConstructToString;
	String ormConstructClone;
	String ormConstructorAsignments;
	String ormConstructorParameters;
	String ormDataDeclaration;

	/** PROVIDER package **/
	// DB adaptor
	String providerSQLTableCreationFieldAndType;

	/** UI **/
	// view
	String putDeclaration;
	String putTextViewRowIDDeclarations;
	String putTextViewDeclarations;
	String putSetValuesToDisplay;
	// view
	String uiViewDisplayFieldDeclarationTemplate;
	String uiViewDisplayFieldLinkToXMLTemplate;
	String uiViewDisplayFieldViewToDefaultTemplate;
	String uiViewSetDisplayViewFromOrmObjectTemplate;
	// create
	String uiCreateDisplayFieldDeclarationTemplate;
	String uiCreateDisplayFieldLinkToXMLTemplate;
	String uiCreateDisplayFieldViewToDefaultTemplate;
	String uiCreateGetValuesFromViewsTemplate;
	String uiCreateJavaFieldDeclarationAndInitializationTemplate;
	String uiCreateConvertValuesFromViewsToJavaTemplate;
	String uiCreateOrmConstructorFieldNameTemplate;
	// edit
	String uiEditDisplayFieldDeclarationTemplate;
	String uiEditDisplayFieldLinkToXMLTemplate;
	String uiEditGetValuesFromViewsTemplate;
	String uiEditJavaFieldDeclarationAndInitializationTemplate;
	String uiEditConvertValuesFromViewsToJavaTemplate;
	String uiEditOrmConstructorFieldNameTemplate;
	String uiEditSetDisplayViewFromOrmObjectTemplate;

	public Relation getRelation() {
		return relation;
	}

	public Field getField() {
		return field;
	}

	public String getJavaType() {
		return javaType;
	}

	public String getDefaultJavaValue() {
		return defaultJavaValue;
	}

	public String getDefaultUIValue() {
		return defaultUIValue;
	}

	public String getOrmDataObjectConstructReadParcelTemplate() {
		return ormDataObjectConstructReadParcelTemplate;
	}

	public String getOrmDataObjectConstructWriteParcelTemplate() {
		return ormDataObjectConstructWriteParcelTemplate;
	}

	public String getOrmDataObjectCosntructorParametersTemplate() {
		return ormDataObjectCosntructorParametersTemplate;
	}

	public String getOrmDataObjectToStringTemplate() {
		return ormDataObjectToStringTemplate;
	}

	public String getOrmDataObjectConstructCloneTemplate() {
		return ormDataObjectConstructCloneTemplate;
	}

	public String getOrmDataObjectConstructorAssignmentsTemplate() {
		return ormDataObjectConstructorAssignmentsTemplate;
	}

	public String getOrmDataObjectPutDeclarationTemplate() {
		return ormDataObjectPutDeclarationTemplate;
	}

	public String getOrmCreatorputFieldIntoCVTemplate() {
		return ormCreatorputFieldIntoCVTemplate;
	}

	public String getOrmCreatorGetFieldFromCursorTemplate() {
		return ormCreatorGetFieldFromCursorTemplate;
	}

	public String getOrmConstructToString() {
		return ormConstructToString;
	}

	public String getOrmConstructClone() {
		return ormConstructClone;
	}

	public String getOrmConstructorAsignments() {
		return ormConstructorAsignments;
	}

	public String getOrmConstructorParameters() {
		return ormConstructorParameters;
	}

	public String getOrmDataDeclaration() {
		return ormDataDeclaration;
	}

	public String getProviderSQLTableCreationFieldAndType() {
		return providerSQLTableCreationFieldAndType;
	}

	public String getPutDeclaration() {
		return putDeclaration;
	}

	public String getPutTextViewRowIDDeclarations() {
		return putTextViewRowIDDeclarations;
	}

	public String getPutTextViewDeclarations() {
		return putTextViewDeclarations;
	}

	public String getPutSetValuesToDisplay() {
		return putSetValuesToDisplay;
	}

	public String getUiViewDisplayFieldDeclarationTemplate() {
		return uiViewDisplayFieldDeclarationTemplate;
	}

	public String getUiViewDisplayFieldLinkToXMLTemplate() {
		return uiViewDisplayFieldLinkToXMLTemplate;
	}

	public String getUiViewDisplayFieldViewToDefaultTemplate() {
		return uiViewDisplayFieldViewToDefaultTemplate;
	}

	public String getUiViewSetDisplayViewFromOrmObjectTemplate() {
		return uiViewSetDisplayViewFromOrmObjectTemplate;
	}

	public String getUiCreateDisplayFieldDeclarationTemplate() {
		return uiCreateDisplayFieldDeclarationTemplate;
	}

	public String getUiCreateDisplayFieldLinkToXMLTemplate() {
		return uiCreateDisplayFieldLinkToXMLTemplate;
	}

	public String getUiCreateDisplayFieldViewToDefaultTemplate() {
		return uiCreateDisplayFieldViewToDefaultTemplate;
	}

	public String getUiCreateGetValuesFromViewsTemplate() {
		return uiCreateGetValuesFromViewsTemplate;
	}

	public String getUiCreateJavaFieldDeclarationAndInitializationTemplate() {
		return uiCreateJavaFieldDeclarationAndInitializationTemplate;
	}

	public String getUiCreateConvertValuesFromViewsToJavaTemplate() {
		return uiCreateConvertValuesFromViewsToJavaTemplate;
	}

	public String getUiCreateOrmConstructorFieldNameTemplate() {
		return uiCreateOrmConstructorFieldNameTemplate;
	}

	public String getUiEditDisplayFieldDeclarationTemplate() {
		return uiEditDisplayFieldDeclarationTemplate;
	}

	public String getUiEditDisplayFieldLinkToXMLTemplate() {
		return uiEditDisplayFieldLinkToXMLTemplate;
	}

	public String getUiEditGetValuesFromViewsTemplate() {
		return uiEditGetValuesFromViewsTemplate;
	}

	public String getUiEditJavaFieldDeclarationAndInitializationTemplate() {
		return uiEditJavaFieldDeclarationAndInitializationTemplate;
	}

	public String getUiEditConvertValuesFromViewsToJavaTemplate() {
		return uiEditConvertValuesFromViewsToJavaTemplate;
	}

	public String getUiEditOrmConstructorFieldNameTemplate() {
		return uiEditOrmConstructorFieldNameTemplate;
	}

	public String getUiEditSetDisplayViewFromOrmObjectTemplate() {
		return uiEditSetDisplayViewFromOrmObjectTemplate;
	}

}
