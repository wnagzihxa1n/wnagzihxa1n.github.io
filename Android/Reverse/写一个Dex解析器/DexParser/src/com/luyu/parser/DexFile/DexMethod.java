package com.luyu.parser.DexFile;

public class DexMethod {
    private String methodClass;           /* index into typeIds list for defining class */
    private DexProto methodProto;           /* index into protoIds for method prototype */
    private String methodName;            /* index into stringIds for method name */

    public DexProto getMethodProto() { return methodProto; }
    public String getMethodClass() { return methodClass; }
    public String getMethodName() { return methodName; }
    public void setMethodClass(String methodClass) { this.methodClass = methodClass; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    public void setMethodProto(DexProto methodProto) { this.methodProto = methodProto; }
}
