package com.luyu.parser.DexFile;

import com.luyu.parser.Utils;

public class DexStringId {
    private int stringDataOff;

    public static int getSize() { return 4; }
    public int getStringDataOff() { return stringDataOff; }
    public void setStringDataOff(int stringDataOff) { this.stringDataOff = stringDataOff; }

    @Override
    public String toString() { return Utils.byte2HexString(Utils.int2Byte(stringDataOff)) + "\n"; }
}
