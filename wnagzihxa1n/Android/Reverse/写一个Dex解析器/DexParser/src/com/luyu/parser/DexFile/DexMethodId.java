package com.luyu.parser.DexFile;

public class DexMethodId {
    private short classIdx;           /* index into typeIds list for defining class */
    private short protoIdx;           /* index into protoIds for method prototype */
    private int nameIdx;            /* index into stringIds for method name */

    public static int getSize() { return 8; }
    public short getClassIdx() { return classIdx; }
    public short getProtoIdx() { return protoIdx; }
    public int getNameIdx() { return nameIdx; }
    public void setClassIdx(short classIdx) { this.classIdx = classIdx; }
    public void setProtoIdx(short protoIdx) { this.protoIdx = protoIdx; }
    public void setNameIdx(int nameIdx) { this.nameIdx = nameIdx; }
}
