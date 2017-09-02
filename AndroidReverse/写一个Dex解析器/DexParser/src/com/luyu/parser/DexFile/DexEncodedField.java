package com.luyu.parser.DexFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DexEncodedField {
    private DexField encodedField;
    private String accessFlags;

    private int[] accessFlagsArray = {0x00000001, 0x00000002, 0x00000004,
            0x00000008, 0x00000010, 0x00000040, 0x00000080, 0x00001000, 0x00004000};

//    private String[] accessFlagsString = {"ACC_PUBLIC", "ACC_PRIVATE", "ACC_PROTECTED",
//            "ACC_STATIC", "ACC_FINAL", "ACC_VOLATILE", "ACC_TRANSIENT", "ACC_SYNTHETIC", "ACC_ENUM"};
    private String[] accessFlagsString = {"public", "private", "protected",
            "static", "final", "volatile", "tansient", "synthetic", "enum"};


    public String getAccessFlags() {
        if (this.accessFlags == null) {
            return "";
        }
        return this.accessFlags;
    }

    public DexField getEncodedField() {
        return this.encodedField;
    }

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

    public void setEncodedField(DexField encodedField) {
        this.encodedField = encodedField;
    }
}
