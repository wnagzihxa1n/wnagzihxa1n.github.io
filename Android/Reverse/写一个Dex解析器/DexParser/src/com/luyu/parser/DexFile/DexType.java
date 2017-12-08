package com.luyu.parser.DexFile;

public class DexType {
    private String type;

    public String getType() {
        switch (type) {
            case "V": return "void";
            case "Z": return "boolean";
            case "B": return "byte";
            case "S": return "short";
            case "C": return "char";
            case "I": return "int";
            case "J": return "long";
            case "F": return "float";
            case "D": return "double";
        }
        return this.type;
    }
    public void setType(String type) { this.type = type; }
}
