package com.luyu.parser.DexFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DexEncodedMethod {
    private DexMethod dexMethod;
    private String accessFlags;
    private DexCode dexCode;

    private int[] accessFlagsArray = {0x00000001, 0x00000002, 0x00000004,
            0x00000008, 0x00000010, 0x00000020, 0x00000040, 0x00000080,
            0x00000100, 0x00000400, 0x00000800, 0x00001000, 0x00010000, 0x00020000};

//    private String[] accessFlagsString = {"ACC_PUBLIC", "ACC_PRIVATE", "ACC_PROTECTED",
//            "ACC_STATIC", "ACC_FINAL", "ACC_SYNCHRONIZED", "ACC_BRIDGE", "ACC_VARARGS",
//            "ACC_NATIVE", "ACC_ABSTRACT", "ACC_STRICT", "ACC_SYNTHETIC", "ACC_CONSTRUCTOR", "ACC_DECLARED_SYNCHRONIZED"};

    private String[] accessFlagsString = {"public", "private", "protected",
            "static", "final", "synchronized", "bridge", "varargs",
            "native", "abstract", "strict", "synthetic", "constructor", "declared_synchronized"};

    public void setAccessFlags(int accessFlags) {
        List<String> stringBuilder = new ArrayList<>();
        if (accessFlags == 0) {
            this.accessFlags = null;
            return;
        }
        for (int i = accessFlagsArray.length - 1; i >= 0; i--) {
            if (accessFlags >= accessFlagsArray[i]) {
                stringBuilder.add(accessFlagsString[i]);
                accessFlags = accessFlags - accessFlagsArray[i];
            }
        }
        Collections.reverse(stringBuilder);
        StringBuilder result = new StringBuilder("");
        for (String list : stringBuilder) {
            result.append(list);
            result.append(" ");
        }
        this.accessFlags = result.toString().trim();
    }

    public String getAccessFlags() {
        if (this.accessFlags == null) {
            return "";
        }
        return accessFlags;
    }

    public DexCode getDexCode() {
        return dexCode;
    }

    public DexMethod getDexMethod() {
        return dexMethod;
    }

    public void setDexCode(DexCode dexCode) {
        this.dexCode = dexCode;
    }

    public void setDexMethod(DexMethod dexMethod) {
        this.dexMethod = dexMethod;
    }
}
