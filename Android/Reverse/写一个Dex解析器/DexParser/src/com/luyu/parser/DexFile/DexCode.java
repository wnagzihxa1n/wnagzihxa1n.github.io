package com.luyu.parser.DexFile;

/**
 * Created by wnagzihxa1n on 2017/8/12 0012.
 */
public class DexCode {
    private short registersSize;
    private short insSize;
    private short outsSize;
    private short triesSize;
    private int debugInfoOff;       /* file offset to debug info stream */
    private int insnsSize;          /* size of the insns array, in u2 units */
    private int insns;
}
