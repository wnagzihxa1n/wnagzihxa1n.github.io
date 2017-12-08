package com.luyu.parser.DexFile;

public class DexProtoId {
    private int shortyIdx;          /* index into stringIds for shorty descriptor */
    private int returnTypeIdx;      /* index into typeIds list for return type */
    private int parametersOff;      /* file offset to type_list for parameter types */

    public static int getSize() { return 12; }
    public int getShortyIdx() { return shortyIdx; }
    public int getReturnTypeIdx() { return returnTypeIdx; }
    public int getParametersOff() { return parametersOff; }
    public void setShortyIdx(int shortyIdx) { this.shortyIdx = shortyIdx; }
    public void setReturnTypeIdx(int returnTypeIdx) { this.returnTypeIdx = returnTypeIdx; }
    public void setParametersOff(int parametersOff) { this.parametersOff = parametersOff; }
}
