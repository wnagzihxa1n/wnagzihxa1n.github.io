package com.luyu.parser.DexFile;

import java.util.List;

public class DexClass {
    private String className;
    private String accessFlags;
    private String superClass;
    private List<String> interfaces;
    private String sourceFile;
    private String[] annotations;
    private DexClassData dexClassData;
    private int staticValuesOff;

    private int[] accessFlagsArray = {0x00000001, 0x00000010, 0x00000020,
            0x00000200, 0x00000400, 0x00002000, 0x00004000};
    private String[] accessFlagsString = {"ACC_PUBLIC", "ACC_FINAL", "ACC_SUPER",
            "ACC_INTERFACE", "ACC_ABSTRACT", "ACC_ANNOTATION", "ACC_ENUM"};
//    private String[] accessFlagString = {"public", "final", "super",
//            "interface", "abstract", "annotation", "enum"};

    public String getClassName() { return className; }
    public String getAccessFlags() { return accessFlags; }
    public String getSuperClass() { return superClass; }
    public List<String> getInterfaces() { return interfaces; }
    public String getSourceFile() { return sourceFile; }
    public DexClassData getDexClassData() { return dexClassData; }

    public void setClassName(String className) { this.className = className; }
    public void setAccessFlags(int accessFlags) {
        if (accessFlags == 0) {
            this.accessFlags = null;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = accessFlagsArray.length - 1; i >= 0; i--) {
            if (accessFlags >= accessFlagsArray[i]) {
                stringBuilder.append(accessFlagsString[i]);
                stringBuilder.append(" ");
                accessFlags = accessFlags - accessFlagsArray[i];
            }
        }
        this.accessFlags = stringBuilder.toString();
    }
    public void setSuperClass(String superclass) { this.superClass = superclass; }
    public void setInterfaces(List<String> interfaces) { this.interfaces = interfaces; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    public void setDexClassData(DexClassData dexClassData) { this.dexClassData = dexClassData; }
}
