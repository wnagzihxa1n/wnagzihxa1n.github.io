package com.luyu.parser.DexFile;

public class DexClassDef {
    private int classIdx;           /* index into typeIds for this class */
    private int accessFlags;
    private int superclassIdx;      /* index into typeIds for superclass */
    private int interfacesOff;      /* file offset to DexTypeList */
    private int sourceFileIdx;      /* index into stringIds for source file name */
    private int annotationsOff;     /* file offset to annotations_directory_item */
    private int classDataOff;       /* file offset to class_data_item */
    private int staticValuesOff;    /* file offset to DexEncodedArray */

    public static int getSize() { return 32; }
    public int getClassIdx() { return classIdx; }
    public int getAccessFlags() { return accessFlags; }
    public int getSuperclassIdx() { return superclassIdx; }
    public int getInterfacesOff() { return interfacesOff; }
    public int getSourceFileIdx() { return sourceFileIdx; }
    public int getAnnotationsOff() { return annotationsOff; }
    public int getClassDataOff() { return classDataOff; }
    public int getStaticValuesOff() { return staticValuesOff; }
    public void setClassIdx(int classIdx) { this.classIdx = classIdx; }
    public void setAccessFlags(int accessFlags) { this.accessFlags = accessFlags; }
    public void setAnnotationsOff(int annotationsOff) { this.annotationsOff = annotationsOff; }
    public void setClassDataOff(int classDataOff) { this.classDataOff = classDataOff; }
    public void setInterfacesOff(int interfacesOff) { this.interfacesOff = interfacesOff; }
    public void setSourceFileIdx(int sourceFileIdx) { this.sourceFileIdx = sourceFileIdx; }
    public void setStaticValuesOff(int staticValuesOff) { this.staticValuesOff = staticValuesOff; }
    public void setSuperclassIdx(int superclassIdx) { this.superclassIdx = superclassIdx; }
}
