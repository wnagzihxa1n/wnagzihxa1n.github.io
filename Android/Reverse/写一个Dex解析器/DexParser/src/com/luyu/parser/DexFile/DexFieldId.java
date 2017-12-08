package com.luyu.parser.DexFile;

public class DexFieldId {
    private short classIdx;           /* index into typeIds list for defining class */
    private short typeIdx;            /* index into typeIds for field type */
    private int nameIdx;            /* index into stringIds for field name */

    public static int getSize() { return 8; }
    public short getClassIdx() { return classIdx; }
    public short getTypeIdx() { return typeIdx; }
    public int getNameIdx() { return nameIdx; }
    public void setClassIdx(short classIdx) { this.classIdx = classIdx; }
    public void setTypeIdx(short typeIdx) { this.typeIdx = typeIdx; }
    public void setNameIdx(int nameIdx) { this.nameIdx = nameIdx; }
}
