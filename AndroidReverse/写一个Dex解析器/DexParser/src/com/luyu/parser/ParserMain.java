package com.luyu.parser;

import com.luyu.parser.DexFile.*;
import com.sun.deploy.uitoolkit.UIToolkit;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.ArrayList;
import java.util.List;

public class ParserMain {

    private static DexHeader dexHeader = new DexHeader();

    private static List<DexStringId> dexStringIdList = new ArrayList<>();
    private static List<String> dexStringList = new ArrayList<>();

    private static List<DexTypeId> dexTypeIdList = new ArrayList<>();
    private static List<DexType> dexTypeList = new ArrayList<>();

    private static List<DexProtoId> dexProtoIdList = new ArrayList<>();
    private static List<DexProto> dexProtoList = new ArrayList<>();

    private static List<DexFieldId> dexFieldIdList = new ArrayList<>();
    private static List<DexField> dexFieldList = new ArrayList<>();

    private static List<DexMethodId> dexMethodIdList = new ArrayList<>();
    private static List<DexMethod> dexMethodList = new ArrayList<>();

    private static List<DexClassDef> dexClassDefList = new ArrayList<>();
    private static List<DexClass> dexClassList = new ArrayList<>();

    public static DexHeader parseDexHeader(byte[] sourceDexFile) {
        dexHeader.setMagic(Utils.getBytes(sourceDexFile, 0, 8));
        dexHeader.setChecksum(Utils.byte2int(Utils.getBytes(sourceDexFile,8, 4)));
        dexHeader.setSignature(Utils.getBytes(sourceDexFile, 12, 20));
        dexHeader.setFileSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 32, 4)));
        dexHeader.setHeaderSize(Utils.byte2int(Utils.getBytes(sourceDexFile,36, 4)));
        dexHeader.setEndianTag(Utils.byte2int(Utils.getBytes(sourceDexFile,40, 4)));
        dexHeader.setLinkSize(Utils.byte2int(Utils.getBytes(sourceDexFile,44, 4)));
        dexHeader.setLinkOff(Utils.byte2int(Utils.getBytes(sourceDexFile,48, 4)));
        dexHeader.setMapOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 52, 4)));
        dexHeader.setStringIdsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 56, 4)));
        dexHeader.setStringIdsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 60, 4)));
        dexHeader.setTypeIdsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 64 , 4)));
        dexHeader.setTypeIdsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 68, 4)));
        dexHeader.setProtoIdsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 72, 4)));
        dexHeader.setProtoIdsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 76, 4)));
        dexHeader.setFieldIdsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 80, 4)));
        dexHeader.setFieldIdsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 84, 4)));
        dexHeader.setMethodIdsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 88, 4)));
        dexHeader.setMethodIdsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 92, 4)));
        dexHeader.setClassDefsSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 96, 4)));
        dexHeader.setClassDefsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 100, 4)));
        dexHeader.setDataSize(Utils.byte2int(Utils.getBytes(sourceDexFile, 104, 4)));
        dexHeader.setDataOff(Utils.byte2int(Utils.getBytes(sourceDexFile, 108, 4)));
        return dexHeader;
    }

    public static List<DexStringId> parseDexStringId(byte[] sourceDexFile) {
        int stringIdsOff = dexHeader.getStringIdsOff();
        int size = dexHeader.getStringIdsSize();
        int length = DexStringId.getSize();
        for (int i = 0; i < size; i++) {
            DexStringId dexStringId = new DexStringId();
            dexStringId.setStringDataOff(Utils.byte2int(Utils.getBytes(sourceDexFile, stringIdsOff + i * length, length)));
            dexStringIdList.add(dexStringId);
        }
        parseDexString(sourceDexFile);
        return dexStringIdList;
    }

    public static void parseDexString(byte[] sourceDexFile) {
        for (DexStringId dexStringId : dexStringIdList) {
            int stringDataOff = dexStringId.getStringDataOff();
            List<Byte> byteAryList = new ArrayList<Byte>();
            byte bytes = Utils.getBytes(sourceDexFile, stringDataOff, 1)[0];
            byte highBit = (byte)(bytes & 0x80);
            int index = 1;
            while(highBit != 0){
                bytes = Utils.getBytes(sourceDexFile, stringDataOff + index++, 1)[0];
                highBit = (byte)(bytes & 0x80);
            }
            byte b = Utils.getBytes(sourceDexFile, stringDataOff + index, 1)[0];
            byteAryList.clear();
            byteAryList.add(b);
            while (b != 0) {
                b = Utils.getBytes(sourceDexFile, stringDataOff + ++index, 1)[0];
                byteAryList.add(b);
            }
            byte[] byteAry = new byte[byteAryList.size()];
            for(int i = 0; i < byteAryList.size(); i++) {
                byteAry[i] = byteAryList.get(i);
            }
            try {
                String string = new String(byteAry, "UTF-8");
                dexStringList.add(string.trim());
//                System.out.println(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<DexTypeId> parseDexTypeId(byte[] sourceDexFile) {
        int typeIdsOff = dexHeader.getTypeIdsOff();
        int size = dexHeader.getTypeIdsSize();
        int length = DexTypeId.getSize();
        for (int i = 0; i < size; i++) {
            DexTypeId dexTypeId = new DexTypeId();
            dexTypeId.setDescriptorIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, typeIdsOff + i * length, length)));
            dexTypeIdList.add(dexTypeId);
        }
        parseDexType();
        return dexTypeIdList;
    }

    public static void parseDexType() {
        for (DexTypeId dexTypeId : dexTypeIdList) {
            DexType dexType = new DexType();
            dexType.setType(dexStringList.get(dexTypeId.getDescriptorIdx()));
            dexTypeList.add(dexType);
//            System.out.println(dexStringList.get(dexTypeId.getDescriptorIdx()));
        }
    }

    public static List<DexProtoId> parseDexProtoId(byte[] sourceDexFile) {
        int protoIdsOff = dexHeader.getProtoIdsOff();
        int size = dexHeader.getProtoIdsSize();
        int length = DexProtoId.getSize();
        for (int i = 0; i < size; i++) {
            DexProtoId dexProtoId = new DexProtoId();
            dexProtoId.setShortyIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, protoIdsOff + i * length, 4)));
            dexProtoId.setReturnTypeIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, protoIdsOff + i * length + 4, 4)));
            dexProtoId.setParametersOff(Utils.byte2int(Utils.getBytes(sourceDexFile, protoIdsOff + i * length + 8, 4)));
            dexProtoIdList.add(dexProtoId);
        }
        parseDexProto(sourceDexFile);
        return dexProtoIdList;
    }

    public static void parseDexProto(byte[] sourceDexFile) {
        for (DexProtoId dexProtoId : dexProtoIdList) {
            DexProto dexProto = new DexProto();
            dexProto.setProtoShorty(dexStringList.get(dexProtoId.getShortyIdx()));
            dexProto.setProtoReturnType(dexTypeList.get(dexProtoId.getReturnTypeIdx()).getType());
            List<String> paramList = new ArrayList<>();
            paramList.clear();
            if (dexProtoId.getParametersOff() == 0) {
                dexProto.setProtoParameters(paramList);
            } else {
                int paramSize = Utils.byte2int(Utils.getBytes(sourceDexFile, dexProtoId.getParametersOff(), 4));
                for (int i = 0; i < paramSize; i++) {
                    paramList.add(dexTypeList.get((int)Utils.byte2Short(Utils.getBytes(sourceDexFile, dexProtoId.getParametersOff() + 4 + i * 2, 2))).getType());
                }
                dexProto.setProtoParameters(paramList);
            }
//            System.out.println("-------------------------------------------------");
//            System.out.println("ShortyIdx       : " + dexProto.getProtoShorty());
//            System.out.println("ReturnTypeIdx   : " + dexProto.getProtoReturnType());
//            System.out.println("ProtoParameters : " + dexProto.getProtoParameters());
            dexProtoList.add(dexProto);
        }
    }

    public static List<DexFieldId> parseDexFieldId(byte[] sourceDexFile) {
        int fieldIdsOff = dexHeader.getFieldIdsOff();
        int size = dexHeader.getFieldIdsSize();
        int length = DexFieldId.getSize();
        for (int i = 0; i < size; i++) {
            DexFieldId dexFieldId = new DexFieldId();
            dexFieldId.setClassIdx(Utils.byte2Short(Utils.getBytes(sourceDexFile, fieldIdsOff + i * length, 2)));
            dexFieldId.setTypeIdx(Utils.byte2Short(Utils.getBytes(sourceDexFile, fieldIdsOff + i * length + 2, 2)));
            dexFieldId.setNameIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, fieldIdsOff + i * length + 4, 4)));
            dexFieldIdList.add(dexFieldId);
        }
        parseDexField();
        return dexFieldIdList;
    }

    public static void parseDexField() {
        for (DexFieldId dexFieldId : dexFieldIdList) {
            DexField dexField = new DexField();
            dexField.setFieldClass(dexTypeList.get((dexFieldId.getClassIdx())).getType());
            dexField.setFieldType(dexTypeList.get(dexFieldId.getTypeIdx()).getType());
            dexField.setFieldName(dexStringList.get(dexFieldId.getNameIdx()));
//            System.out.println("-------------------------------------------------");
//            System.out.println("ClassIdx : " + dexField.getFieldClass());
//            System.out.println("TypeIdx  : " + dexField.getFieldType());
//            System.out.println("NameIdx  : " + dexField.getFieldName());
            dexFieldList.add(dexField);
        }
    }

    public static List<DexMethodId> parseDexMethodId(byte[] sourceDexFile) {
        int methodIdsOff = dexHeader.getMethodIdsOff();
        int size = dexHeader.getMethodIdsSize();
        int length = DexMethodId.getSize();
        for (int i = 0; i < size; i++) {
            DexMethodId dexMethodId = new DexMethodId();
            dexMethodId.setClassIdx(Utils.byte2Short(Utils.getBytes(sourceDexFile, methodIdsOff + i * length, 2)));
            dexMethodId.setProtoIdx(Utils.byte2Short(Utils.getBytes(sourceDexFile, methodIdsOff + i * length + 2, 2)));
            dexMethodId.setNameIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, methodIdsOff + i * length + 4, 4)));
            dexMethodIdList.add(dexMethodId);
        }
        parseDexMethod();
        return dexMethodIdList;
    }

    public static void parseDexMethod() {
        for (DexMethodId dexMethodId : dexMethodIdList) {
            DexMethod dexMethod = new DexMethod();
            dexMethod.setMethodClass(dexTypeList.get(dexMethodId.getClassIdx()).getType());
            dexMethod.setMethodProto(dexProtoList.get(dexMethodId.getProtoIdx()));
            dexMethod.setMethodName(dexStringList.get(dexMethodId.getNameIdx()));
//            System.out.println("-------------------------------------------------");
//            System.out.println("MethodClass : " + dexMethod.getMethodClass());
//            System.out.println("MethodProto : " + dexMethod.getMethodProto().getProtoReturnType());
//            System.out.println("MethodName  : " + dexMethod.getMethodName());
            dexMethodList.add(dexMethod);
        }
    }

    public static List<DexClassDef> parseDexClassDef(byte[] sourceDexFile) {
        int classDefsOff = dexHeader.getClassDefsOff();
        int size = dexHeader.getClassDefsSize();
        int length = DexClassDef.getSize();
        for (int i = 0; i < size; i++) {
            DexClassDef dexClassDef = new DexClassDef();
            dexClassDef.setClassIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length, 4)));
            dexClassDef.setAccessFlags(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 4, 4)));
            dexClassDef.setSuperclassIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 8, 4)));
            dexClassDef.setInterfacesOff(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 12, 4)));
            dexClassDef.setSourceFileIdx(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 16, 4)));
            dexClassDef.setAnnotationsOff(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 20, 4)));
            dexClassDef.setClassDataOff(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 24, 4)));
            dexClassDef.setStaticValuesOff(Utils.byte2int(Utils.getBytes(sourceDexFile, classDefsOff + i * length + 28, 4)));
            dexClassDefList.add(dexClassDef);
        }
        parseDexClass(sourceDexFile);
        return dexClassDefList;
    }

    public static void parseDexClass(byte[] sourceDexFile) {
        int index = 0;
        for (DexClassDef dexClassDef : dexClassDefList) {
            DexClass dexClass = new DexClass();
            dexClass.setClassName(dexTypeList.get(dexClassDef.getClassIdx()).getType());
            dexClass.setAccessFlags(dexClassDef.getAccessFlags());
            dexClass.setSuperClass(dexTypeList.get(dexClassDef.getSuperclassIdx()).getType());

            //解析接口
            int interfaceSize = Utils.byte2int(Utils.getBytes(sourceDexFile, dexClassDef.getInterfacesOff(), 4));
            List<String> interfaceList = new ArrayList<>();
            interfaceList.clear();
            if (dexClassDef.getInterfacesOff() == 0) {
                dexClass.setInterfaces(interfaceList);
            } else {
                for (int i = 0; i < interfaceSize; i++) {
                    interfaceList.add(dexTypeList.get((int)Utils.byte2Short(Utils.getBytes(sourceDexFile, dexClassDef.getInterfacesOff() + 4 + i * 2, 2))).getType());
                    dexClass.setInterfaces(interfaceList);
                }
            }
            int sourceFileIdx = dexClassDef.getSourceFileIdx();
            dexClass.setSourceFile(sourceFileIdx == -1 ? "No_SourceFile" : dexStringList.get(sourceFileIdx));

            DexClassData dexClassData = new DexClassData();

            //解析DexClass
            int uleb128Size = 0;
            if (dexClassDef.getClassDataOff() == 0) {
                dexClass.setDexClassData(dexClassData);
            } else {
                byte[] uleb128Array = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff());
                uleb128Size = uleb128Array.length;
                dexClassData.setStaticFieldSize(Utils.decodeUleb128(uleb128Array));
                uleb128Array = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size);
                uleb128Size = uleb128Size + uleb128Array.length;
                dexClassData.setInstanceFieldSize(Utils.decodeUleb128(uleb128Array));
                uleb128Array = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size);
                uleb128Size = uleb128Size + uleb128Array.length;
                dexClassData.setDirectMethodSize(Utils.decodeUleb128(uleb128Array));
                uleb128Array = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size);
                uleb128Size = uleb128Size + uleb128Array.length;
                dexClassData.setVirtualMethodSize(Utils.decodeUleb128(uleb128Array));
                dexClass.setDexClassData(dexClassData);
            }

            int fieldOff = 0;
            int curr_index = 0;
            //解析DexStaticField
            List<DexEncodedField> dexStaticFieldList = new ArrayList<>();
            dexStaticFieldList.clear();
            int staticFieldSize = dexClass.getDexClassData().getStaticFieldSize();
            System.out.println("\nindex : " + index + " : ###############################################################");
//            System.out.println(index++ + "-------------------------------------------------");
            System.out.println("ClassName     : " + dexClass.getClassName().replace("L", "").replace(";", "").replace("/","."));
            System.out.println("AccessFlags   : " + dexClass.getAccessFlags());
            System.out.println("Superclass    : " + dexClass.getSuperClass());
            System.out.println("Interfaces    : " + dexClass.getInterfaces());
            System.out.println("SourceFile    : " + dexClass.getSourceFile());
//            System.out.println(index +  " ：" + staticFieldSize + "-------------------------------------------------");
            for (int i = 0; i < staticFieldSize; i++) {
                DexEncodedField dexEncodedField = new DexEncodedField();
                byte[] fieldArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + fieldOff);
                curr_index = Utils.decodeUleb128(fieldArray) + curr_index;
                fieldOff = fieldOff + fieldArray.length;
                byte[] accessArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + fieldOff);
                int accessSize = accessArray.length;
                dexEncodedField.setAccessFlags(Utils.decodeUleb128(accessArray));
                fieldOff = fieldOff + accessSize;
//                System.out.println("curr_index : " + curr_index);
//                System.out.println("[" + Utils.byte2HexString(fieldArray) + "]");
                DexField dexField = new DexField();
                dexField.setFieldClass(dexFieldList.get(curr_index).getFieldClass());
                dexField.setFieldName(dexFieldList.get(curr_index).getFieldName());
                dexField.setFieldType(dexFieldList.get(curr_index).getFieldType());
                dexEncodedField.setEncodedField(dexField);
                System.out.println("StaticField   : " + dexEncodedField.getAccessFlags()
                        + dexEncodedField.getEncodedField().getFieldType().replace("L", "").replace(";", "").replace("/",".")
                        + " " + dexEncodedField.getEncodedField().getFieldClass().replace("L", "").replace(";", "").replace("/", ".")
                        + "." + dexEncodedField.getEncodedField().getFieldName());
                dexStaticFieldList.add(dexEncodedField);
            }
            dexClassData.setDexStaticFieldList(dexStaticFieldList);

            //解析DexInstanceField
            curr_index = 0;
            int instanceFieldSize = dexClassData.getInstanceFieldSize();
            List<DexEncodedField> dexInstanceFieldList = new ArrayList<>();
            dexStaticFieldList.clear();
            for (int i = 0; i < instanceFieldSize; i++) {
//                System.out.println(index +  " ：" + instanceFieldSize + " ：" + i + "-------------------------------------------------");
                DexEncodedField dexEncodedField = new DexEncodedField();
                byte[] fieldArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + fieldOff);
                curr_index = Utils.decodeUleb128(fieldArray) + curr_index;
                fieldOff = fieldOff + fieldArray.length;
                byte[] accessArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + fieldOff);
                dexEncodedField.setAccessFlags(Utils.decodeUleb128(accessArray));
                fieldOff = fieldOff + accessArray.length;
//                System.out.println("curr_index : " + curr_index);
//                System.out.println("[" + Utils.byte2HexString(fieldArray) + "]");
                DexField dexField = new DexField();
                dexField.setFieldClass(dexFieldList.get(curr_index).getFieldClass());
                dexField.setFieldName(dexFieldList.get(curr_index).getFieldName());
                dexField.setFieldType(dexFieldList.get(curr_index).getFieldType());
                dexEncodedField.setEncodedField(dexField);
                System.out.println("InstanceField : " + dexEncodedField.getAccessFlags()
                        + dexEncodedField.getEncodedField().getFieldType().replace("L", "").replace(";", "").replace("/",".")
                        + " " + dexEncodedField.getEncodedField().getFieldClass().replace("L", "").replace(";", "").replace("/", ".")
                        + "." + dexEncodedField.getEncodedField().getFieldName());
                dexInstanceFieldList.add(dexEncodedField);
            }
            dexClassData.setDexInstanceFieldList(dexInstanceFieldList);

            //解析DexDirectMethod
            curr_index = 0;
            int methodOff = fieldOff;
            int directMethodSize = dexClassData.getDirectMethodSize();
            List<DexEncodedMethod> dexDirectMethodList = new ArrayList<>();
            dexDirectMethodList.clear();
            for (int i = 0; i < directMethodSize; i++) {
                DexEncodedMethod dexEncodedMethod = new DexEncodedMethod();
                byte[] methodArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                curr_index = Utils.decodeUleb128(methodArray) + curr_index;
                methodOff = methodOff + methodArray.length;
                byte[] accessArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                dexEncodedMethod.setAccessFlags(Utils.decodeUleb128(accessArray));
                methodOff = methodOff + accessArray.length;
                byte[] codeOffArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                methodOff = methodOff + codeOffArray.length;
//                System.out.println("curr_index : " + curr_index);
//                System.out.println(Utils.byte2HexString(methodArray));
                dexEncodedMethod.setDexMethod(dexMethodList.get(curr_index));
                String string = dexEncodedMethod.getAccessFlags()
                        + " " + dexEncodedMethod.getDexMethod().getMethodProto().getProtoReturnType().replace("L", "").replace(";", "").replace("/",".")
                        + " " + dexEncodedMethod.getDexMethod().getMethodClass().replace("L", "").replace(";", "").replace("/",".")
                        + "." + dexEncodedMethod.getDexMethod().getMethodName().replace("L", "").replace(";", "").replace("/",".");
                System.out.println("DirectMethod  : " + string.trim());
            }

            dexClassData.setDexDirectMethodList(dexDirectMethodList);

            //解析DexVirtualMethod
            curr_index = 0;
            int virtualMethodSize = dexClassData.getVirtualMethodSize();
            List<DexEncodedMethod> dexVirtualMethodList = new ArrayList<>();
            dexVirtualMethodList.clear();
            for (int i = 0; i < virtualMethodSize; i++) {
                DexEncodedMethod dexEncodedMethod = new DexEncodedMethod();
                byte[] methodArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                curr_index = Utils.decodeUleb128(methodArray) + curr_index;
                methodOff = methodOff + methodArray.length;
                byte[] accessArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                dexEncodedMethod.setAccessFlags(Utils.decodeUleb128(accessArray));
                methodOff = methodOff + accessArray.length;
                byte[] codeOffArray = Utils.readUleb128(sourceDexFile, dexClassDef.getClassDataOff() + uleb128Size + methodOff);
                methodOff = methodOff + codeOffArray.length;
//                System.out.println("curr_index : " + curr_index);
//                System.out.println(Utils.byte2HexString(methodArray));
                dexEncodedMethod.setDexMethod(dexMethodList.get(curr_index));
                String string = dexEncodedMethod.getAccessFlags()
                        + " " + dexEncodedMethod.getDexMethod().getMethodProto().getProtoReturnType().replace("L", "").replace(";", "").replace("/",".")
                        + " " + dexEncodedMethod.getDexMethod().getMethodClass().replace("L", "").replace(";", "").replace("/",".")
                        + "." + dexEncodedMethod.getDexMethod().getMethodName().replace("L", "").replace(";", "").replace("/",".");
                System.out.println("VirtualMethod : " + string.trim());
            }
            dexClassData.setDexVirtualMethodList(dexVirtualMethodList);

            if (index == 2) {
                break;
            }

            index++;

            dexClass.setDexClassData(dexClassData);
            dexClassList.add(dexClass);
        }
    }
}
