package com.luyu.parser;

import com.luyu.parser.DexFile.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static String dexFilePath = "classes2.dex";
    private static DexHeader dexHeader = new DexHeader();
    private static List<DexStringId> dexStringIdList = new ArrayList<DexStringId>();
    private static List<DexTypeId> dexTypeIdList = new ArrayList<>();
    private static List<DexProtoId> dexProtoIdList = new ArrayList<>();
    private static List<DexFieldId> dexFieldIdList = new ArrayList<>();
    private static List<DexMethodId> dexMethodIdList = new ArrayList<>();
    private static List<DexClassDef> dexClassDefList = new ArrayList<>();
    private static List<DexClass> dexClassList = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        byte[] sourceDexFile = Utils.readDexFile(dexFilePath);
        dexHeader = ParserMain.parseDexHeader(sourceDexFile);
        System.out.println(dexHeader.toString());
        dexStringIdList = ParserMain.parseDexStringId(sourceDexFile);
        dexTypeIdList = ParserMain.parseDexTypeId(sourceDexFile);
        dexProtoIdList = ParserMain.parseDexProtoId(sourceDexFile);
        dexFieldIdList = ParserMain.parseDexFieldId(sourceDexFile);
        dexMethodIdList = ParserMain.parseDexMethodId(sourceDexFile);
        dexClassDefList = ParserMain.parseDexClassDef(sourceDexFile);
    }
}
