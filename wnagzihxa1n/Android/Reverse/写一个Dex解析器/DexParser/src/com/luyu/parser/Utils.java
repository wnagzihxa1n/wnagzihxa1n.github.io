package com.luyu.parser;

import com.luyu.parser.DexFile.DexHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static byte[] readDexFile(String dexFilePath) {
        byte[] buffer = new byte[1024];
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = new FileInputStream(dexFilePath);
            byteArrayOutputStream = new ByteArrayOutputStream();
            int index = 0;
            while ((index = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getBytes(byte[] sourceDexFile, int start, int len) {
        byte[] temp = new byte[len];
        if (sourceDexFile.length < start +len) {
            return temp;
        }
        for (int i = 0; i < len; i++) {
            temp[i] = sourceDexFile[start + i];
        }
        return temp;
    }

    public static int byte2int(byte[] ary) {
        int value = 0;
        if (ary.length == 1) {
            value = (int) ((ary[0] & 0xFF));
        } else if (ary.length == 2) {
            value = (int) ((ary[0] & 0xFF) | ((ary[1] << 8) & 0xFF00));
        } else if (ary.length == 3) {
            value = (int) ((ary[0] & 0xFF) | ((ary[1] << 8) & 0xFF00) | ((ary[2] << 16) & 0xFF0000));
        } else if (ary.length == 4) {
            value = (int) ((ary[0] & 0xFF) | ((ary[1] << 8) & 0xFF00) | ((ary[2] << 16) & 0xFF0000) | ((ary[3] << 24) & 0xFF000000));
        }
        return value;
    }

    public static byte[] int2Byte(int source) {
        int byteNum = (40 -Integer.numberOfLeadingZeros (source < 0 ? ~source : source)) / 8;
        byte[] byteArray = new byte[4];
        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (source >>> (n * 8));
        return (byteArray);
    }

    public static byte[] short2Byte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xFF).byteValue();
            temp = temp >> 8;
        }
        return b;
    }

    public static short byte2Short(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xFF);
        short s1 = (short) (b[1] & 0xFF);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    public static String byte2HexString(byte[] source) {
        StringBuilder stringBuilder = new StringBuilder();
        if (source == null || source.length <= 0) {
            return null;
        }
        for (byte b : source) {
            int temp = b & 0xFF;
            String str = Integer.toHexString(temp);
            if (str.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(str + " ");
        }
        return stringBuilder.toString().trim();
    }

    public static byte[] readUleb128(byte[] srcByte, int offset) {
        List<Byte> byteAryList = new ArrayList<Byte>();
        byte bytes = Utils.getBytes(srcByte, offset, 1)[0];
        byte highBit = (byte)(bytes & 0x80);
        byteAryList.add(bytes);
        offset++;
        while (highBit != 0){
            bytes = Utils.getBytes(srcByte, offset, 1)[0];
            highBit = (byte)(bytes & 0x80);
            offset++;
            byteAryList.add(bytes);
        }
        byte[] byteAry = new byte[byteAryList.size()];
        for(int i = 0; i < byteAryList.size(); i++) {
            byteAry[i] = byteAryList.get(i);
        }
        return byteAry;
    }

    public static int decodeUleb128(byte[] byteAry) {
        if (byteAry.length == 1) {
            return (byteAry[0] & 0x7F);
        }
        if (byteAry.length == 2) {
            return (byteAry[0] & 0x7F) | ((byteAry[1] & 0x7F) << 7);
        }
        if (byteAry.length == 3) {
            return (byteAry[0] & 0x7F) | ((byteAry[1] & 0x7F) << 7) | (byteAry[2] & 0x7F) << 14;
        }
        if (byteAry.length == 4) {
            return (byteAry[0] & 0x7F) | ((byteAry[1] & 0x7F) << 7) | (byteAry[2] & 0x7F) << 14 | (byteAry[3] & 0x7F) << 21;
        }
        if (byteAry.length == 5) {
            return (byteAry[0] & 0x7F) | ((byteAry[1] & 0x7F) << 7) | (byteAry[2] & 0x7F) << 14 | (byteAry[3] & 0x7F) << 21 | (byteAry[4] & 0x7F) << 28;
        }
        return 0;
    }
}
