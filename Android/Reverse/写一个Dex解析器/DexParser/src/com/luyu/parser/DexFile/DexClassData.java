package com.luyu.parser.DexFile;

import java.util.ArrayList;
import java.util.List;

public class DexClassData {
    private int staticFieldSize;
    private int instanceFieldSize;
    private int directMethodSize;
    private int virtualMethodSize;
    private List<DexEncodedField> dexStaticFieldList = new ArrayList<>();
    private List<DexEncodedField> dexInstanceFieldList = new ArrayList<>();
    private List<DexEncodedMethod> dexDirectMethodList = new ArrayList<>();
    private List<DexEncodedMethod> dexVirtualMethodList = new ArrayList<>();

    public int getInstanceFieldSize() {
        return instanceFieldSize;
    }

    public int getDirectMethodSize() {
        return directMethodSize;
    }

    public int getStaticFieldSize() {
        return staticFieldSize;
    }

    public int getVirtualMethodSize() {
        return virtualMethodSize;
    }

    public List<DexEncodedField> getDexInstanceFieldList() {
        return dexInstanceFieldList;
    }

    public List<DexEncodedField> getDexStaticFieldList() {
        return dexStaticFieldList;
    }

    public List<DexEncodedMethod> getDexDirectMethodList() {
        return dexDirectMethodList;
    }

    public List<DexEncodedMethod> getDexVirtualMethodList() {
        return dexVirtualMethodList;
    }

    public void setDexStaticFieldList(List<DexEncodedField> dexStaticFieldList) {this.dexStaticFieldList = dexStaticFieldList;}

    public void setDexInstanceFieldList(List<DexEncodedField> dexInstanceFieldList) {this.dexInstanceFieldList = dexInstanceFieldList;}

    public void setDexDirectMethodList(List<DexEncodedMethod> dexDirectMethodList) {this.dexDirectMethodList = dexDirectMethodList;}

    public void setDexVirtualMethodList(List<DexEncodedMethod> dexVirtualMethodList) {
        this.dexVirtualMethodList = dexVirtualMethodList;
    }

    public void setDirectMethodSize(int directMethodSize) {
        this.directMethodSize = directMethodSize;
    }

    public void setInstanceFieldSize(int instanceFieldSize) {
        this.instanceFieldSize = instanceFieldSize;
    }

    public void setStaticFieldSize(int staticFieldSize) {
        this.staticFieldSize = staticFieldSize;
    }

    public void setVirtualMethodSize(int virtualMethodSize) {
        this.virtualMethodSize = virtualMethodSize;
    }
}
