package com.luyu.parser.DexFile;

public class DexHeader {
    private byte[] magic = new byte[8];           /* includes version number */
    private int checksum;           /* adler32 checksum */
    private byte[] signature = new byte[20]; /* SHA-1 hash */
    private int fileSize;           /* length of entire file */
    private int headerSize;         /* offset to start of next section */
    private int endianTag;
    private int linkSize;
    private int linkOff;
    private int mapOff;
    private int stringIdsSize;
    private int stringIdsOff;
    private int typeIdsSize;
    private int typeIdsOff;
    private int protoIdsSize;
    private int protoIdsOff;
    private int fieldIdsSize;
    private int fieldIdsOff;
    private int methodIdsSize;
    private int methodIdsOff;
    private int classDefsSize;
    private int classDefsOff;
    private int dataSize;
    private int dataOff;

    public static int getLength() { return 0x70; }

    public void setMagic(byte[] magic) { this.magic = magic; }
    public byte[] getMagic() { return magic; }

    public void setChecksum(int checksum) { this.checksum = checksum; }
    public int getChecksum() { return checksum; }

    public void setSignature(byte[] signature) { this.signature = signature; }
    public byte[] getSignature() { return signature; }

    public void setFileSize(int fileSize) { this.fileSize = fileSize; }
    public int getFileSize() { return fileSize; }

    public void setHeaderSize(int headerSize) { this.headerSize = headerSize; }
    public int getHeaderSize() { return headerSize; }

    public void setEndianTag(int endianTag) { this.endianTag = endianTag; }
    public int getEndianTag() { return endianTag; }

    public void setLinkSize(int linkSize) { this.linkSize = linkSize; }
    public int getLinkSize() { return linkSize; }

    public void setLinkOff(int linkOff) { this.linkOff = linkOff; }
    public int getLinkOff() { return linkOff; }

    public void setMapOff(int mapOff) { this.mapOff = mapOff; }
    public int getMapOff() { return mapOff; }

    public void setStringIdsSize(int stringIdsSize) { this.stringIdsSize = stringIdsSize; }
    public int getStringIdsSize() { return stringIdsSize; }

    public void setStringIdsOff(int stringIdsOff) { this.stringIdsOff = stringIdsOff; }
    public int getStringIdsOff() { return stringIdsOff; }

    public void setTypeIdsSize(int typeIdsSize) { this.typeIdsSize = typeIdsSize; }
    public int getTypeIdsSize() { return typeIdsSize; }

    public void setTypeIdsOff(int typeIdsOff) { this.typeIdsOff = typeIdsOff; }
    public int getTypeIdsOff() { return typeIdsOff; }

    public void setProtoIdsSize(int protoIdsSize) { this.protoIdsSize = protoIdsSize; }
    public int getProtoIdsSize() { return protoIdsSize; }

    public void setProtoIdsOff(int protoIdsOff) { this.protoIdsOff = protoIdsOff; }
    public int getProtoIdsOff() { return protoIdsOff; }

    public void setFieldIdsSize(int fieldIdsSize) { this.fieldIdsSize = fieldIdsSize; }
    public int getFieldIdsSize() { return fieldIdsSize; }

    public void setFieldIdsOff(int fieldIdsOff) { this.fieldIdsOff = fieldIdsOff; }
    public int getFieldIdsOff() { return fieldIdsOff; }

    public int getMethodIdsSize() { return methodIdsSize; }
    public void setMethodIdsSize(int methodIdsSize) { this.methodIdsSize = methodIdsSize; }

    public void setMethodIdsOff(int methodIdsOff) { this.methodIdsOff = methodIdsOff; }
    public int getMethodIdsOff() { return methodIdsOff; }

    public void setClassDefsSize(int classDefsSize) { this.classDefsSize = classDefsSize; }
    public int getClassDefsSize() { return classDefsSize; }

    public void setClassDefsOff(int classDefsOff) { this.classDefsOff = classDefsOff; }
    public int getClassDefsOff() { return classDefsOff; }

    public void setDataSize(int dataSize) { this.dataSize = dataSize; }
    public int getDataSize() { return dataSize; }

    public void setDataOff(int dataOff) { this.dataOff = dataOff; }
    public int getDataOff() { return dataOff; }

    @Override
    public String toString() {
        return "Magic           : " + new String(magic) + "\n"
            + "CheckSum        : " + checksum + "\n"
            + "Signature       : " + signature.toString() + "\n"
            + "FileSize        : " + fileSize + "\n"
            + "HeaderSize      : " + headerSize + "\n"
            + "EndianTag       : " + endianTag + "\n"
            + "LinkSize        : " + linkSize + "\n"
            + "LinkOff         : " + linkOff + "\n"
            + "MapOff          : " + mapOff + "\n"
            + "StringIdsSize   : " + stringIdsSize + "\n"
            + "StringIdsOff    : " + stringIdsOff + "\n"
            + "TypeIdsSize     : " + typeIdsSize + "\n"
            + "TyprIdsOff      : " + typeIdsOff + "\n"
            + "ProtoIdsSize    : " + protoIdsSize + "\n"
            + "ProtoIdsOff     : " + protoIdsOff + "\n"
            + "FieldIdsSize    : " + fieldIdsSize + "\n"
            + "FieldIdsOff     : " + fieldIdsOff + "\n"
            + "MethodIdsSize   : " + methodIdsSize + "\n"
            + "MethodIdsOff    : " + methodIdsOff + "\n"
            + "ClassDefSize    : " + classDefsSize + "\n"
            + "ClassDefOff     : " + classDefsOff + "\n"
            + "DataSize        : " + dataSize + "\n"
            + "DataOff         : " + dataOff + "\n";
    }
}


