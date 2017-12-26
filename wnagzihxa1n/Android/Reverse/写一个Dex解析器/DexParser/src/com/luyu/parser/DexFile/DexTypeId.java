package com.luyu.parser.DexFile;

public class DexTypeId {
    int descriptorIdx;

    public static int getSize() { return 4; }
    public int getDescriptorIdx() { return descriptorIdx; }
    public void setDescriptorIdx(int descriptorIdx) { this.descriptorIdx = descriptorIdx; }
}
