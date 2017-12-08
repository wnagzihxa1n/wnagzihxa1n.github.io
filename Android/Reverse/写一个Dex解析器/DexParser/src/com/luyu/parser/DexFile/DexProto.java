package com.luyu.parser.DexFile;

import java.util.List;

public class DexProto {
    private String protoShorty;          /* index into stringIds for shorty descriptor */
    private String protoReturnType;      /* index into typeIds list for return type */
    private List<String> protoParameters;      /* file offset to type_list for parameter types */

    public List<String> getProtoParameters() { return protoParameters; }
    public String  getProtoReturnType() { return protoReturnType; }
    public String getProtoShorty() { return protoShorty; }
    public void setProtoParameters(List<String> protoParameters) { this.protoParameters = protoParameters; }
    public void setProtoReturnType(String protoReturnType) { this.protoReturnType = protoReturnType; }
    public void setProtoShorty(String protoShorty) { this.protoShorty = protoShorty; }
}
