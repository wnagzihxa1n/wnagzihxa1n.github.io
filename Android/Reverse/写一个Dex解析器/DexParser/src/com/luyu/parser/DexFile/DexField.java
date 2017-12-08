package com.luyu.parser.DexFile;

public class DexField {
    private String fieldClass;           /* index into typeIds list for defining class */
    private String fieldType;            /* index into typeIds for field type */
    private String fieldName;            /* index into stringIds for field name */

    public String getFieldClass() { return fieldClass; }
    public String getFieldName() { return fieldName; }
    public String getFieldType() { return fieldType; }
    public void setFieldClass(String fieldClass) { this.fieldClass = fieldClass; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
}
