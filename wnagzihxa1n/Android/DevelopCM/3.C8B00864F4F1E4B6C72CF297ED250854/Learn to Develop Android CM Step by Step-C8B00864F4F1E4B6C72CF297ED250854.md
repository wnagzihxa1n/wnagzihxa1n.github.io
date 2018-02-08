# Learn to Develop Android CM Step by Step-C8B00864F4F1E4B6C72CF297ED250854

**Author：wnagzihxa1n
Mail：tudouboom@163.com**

## 0x00 前言
填上的坑，我又给挖出来了:)

另外加上了Java层的模拟器检测，检测的东西非常多。。。。。。

- https://github.com/toToCW/AndroidCrackMes/blob/master/3.C8B00864F4F1E4B6C72CF297ED250854

## 0x01 Description
由于主题是检测模拟器，所以模拟器检测部分比较多

有一些检测目前早就被模拟器的开发商绕过去了，比如IMEI，大多模拟器都支持修改，手机号也支持修改
```
package com.wnagzihxain.application;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by wnagzihxain on 2017/5/5 0005.
 */

public class Emulator_Finder {
    private final static String TAG = "toT0C";
    private static String[] Default_IMEI = {
            "000000000000000"
    };
    private static String[] Default_IMSI = {
            "310260000000000"
    };
    private static String[] Default_Drivers = {
            "goldfish"
    };
    private static String[] Default_OperatorName = {
            "android"
    };
    private static String[] Deafult_phoneNumbers = {
            "15555215554", "15555215556","15555215582", "15555215584",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580"
    };
    private static String[] Default_Files = {
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props"
    };
    private static String[] Default_Pipes = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"
    };

    //检测IMEI
    public static boolean CheckIMEI(Context context) {
        boolean result = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        Log.i(TAG, "Start Calling CheckIMEI() : " + IMEI);
        for (String imei : Default_IMEI) {
            if (IMEI.equalsIgnoreCase(imei)) {
                Log.i(TAG, "Find Emulator : " + imei);
                result = true;
            }
        }
        return result;
    }

    //检测IMSI
    public static boolean CheckIMSI(Context context) {
        boolean result = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        Log.i(TAG, "Start Calling CheckIMEI() : " + IMSI);
        for (String imsi : Default_IMSI) {
            if (IMSI.equalsIgnoreCase(imsi)) {
                Log.i(TAG, "Find Emulator : " + imsi);
                result = true;
            }
        }
        return result;
    }

    //检测驱动
    public static boolean CheckDriver(Context context) {
        boolean result = false;
        File file = new File("/proc/tty/drivers");
        if (file.exists() && file.canRead()) {
            byte[] data = new byte[1024];
            try {
                InputStream inputStream = new FileInputStream(file);
                inputStream.read(data);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String Drivers = new String(data);
            Log.i(TAG, "Start Calling CheckDriver() : " + Drivers);
            for (String driver : Default_Drivers) {
                if (Drivers.indexOf(driver) != -1) {
                    Log.i(TAG, "Find Emulator : " + driver);
                    result = true;
                }
            }
        }
        return result;
    }

    //检测运营商
    public static boolean CheckOperatorName(Context context) {
        boolean result = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String operatorName = telephonyManager.getNetworkOperatorName();
        Log.i(TAG, "Start Calling CheckOperator() : " + operatorName);
        for (String operatorname : Default_OperatorName) {
            if (operatorName.equalsIgnoreCase(operatorname)) {
                Log.i(TAG, "Find Emulator : " + operatorname);
                result = true;
            }
        }
        return result;
    }

    //检测手机号
    public static boolean CheckPhoneNumber(Context context) {
        boolean result = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();
        Log.i(TAG, "Start Calling CheckPhoneNumber() : " + phoneNumber);
        for (String phonenumber : Deafult_phoneNumbers) {
            if (phoneNumber.equalsIgnoreCase(phonenumber)) {
                Log.i(TAG, "Find Emulator : " + phonenumber);
                result = true;
            }
        }
        return result;
    }

    //检测模拟器的特征文件
    public static boolean CheckFiles(Context context) {
        boolean result = false;
        Log.i(TAG, "Start Calling CheckFiles()");
        for (String fileName : Default_Files) {
            File file = new File(fileName);
            if (file.exists()) {
                Log.i(TAG, "Find Emulator : " + fileName);
                result = true;
            }
        }
        return result;
    }

    //检测硬件信息
    public static boolean CheckBuild(Context context) {
        boolean result = false;
        Log.i(TAG, "Start Calling CheckBuild()");
        String BOARD = android.os.Build.BOARD;
        String BOOTLOADER = android.os.Build.BOOTLOADER;
        String BRAND = android.os.Build.BRAND;
        String DEVICE = android.os.Build.DEVICE;
        String HARDWARE = android.os.Build.HARDWARE;
        String MODEL = android.os.Build.MODEL;
        String PRODUCT = android.os.Build.PRODUCT;
        if (BOARD == "unknown"
                || BOOTLOADER == "unknown"
                || BRAND == "generic"
                || DEVICE == "generic"
                || MODEL == "sdk"
                || PRODUCT == "sdk"
                || HARDWARE == "goldfish") {
            result = true;
        }
        return result;
    }

    //检测Pipes
    public static boolean CheckPipes(Context context) {
        boolean result = false;
        Log.i(TAG, "Start Calling CheckPipes()");
        for (String pipe : Default_Pipes) {
            File file = new File(pipe);
            if (file.exists()) {
                Log.i(TAG, "Find Emulator : " + pipe);
                result = true;
            }
        }
        return result;
    }
}
```

校验算法我放到了native层，直接静态注册

本次的校验算法是我写的另一个用于比赛的简化版本，如果不纠结于编程解决问题那么就可以很快解决

首先将`jstring`转为C的字符串数组
```
char* GetStringUnicodeChars(JNIEnv *env, jstring Data)
{
    char *Result = NULL;
    jclass jClass_String = env->FindClass("java/lang/String");
    jstring encodeFormat = env->NewStringUTF("UTF-8");
    jmethodID jMethodID_getBytes = env->GetMethodID(jClass_String, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray jByteArray = (jbyteArray) env->CallObjectMethod(Data, jMethodID_getBytes, encodeFormat);
    jsize len = env->GetArrayLength(jByteArray);
    jbyte* temp = env->GetByteArrayElements(jByteArray, JNI_FALSE);
    if (len > 0) {
        Result = (char*) malloc(len + 1);
        memcpy(Result, temp, len);
        Result[len] = 0;
    }
    env->ReleaseByteArrayElements(jByteArray, temp, 0);
    return Result;
}
```

进行注册码合法性校验，限定在`[a-zA-Z0-9]`之间
```
bool RegCodeLegal(char* data) {
    char legalchars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    int len_data = strlen(data);
    int len_legalchars = strlen(legalchars);
    for (int i = 0; i < len_data; i++) {
        int index = -1;
        for (int j = 0; j < len_legalchars; j++) {
            if (data[i] == legalchars[j]) {
                index = j;
                break;
            }
        }
        if (index == -1) {
            //LOGI("%d : %c", i, data[i]);
            return false;
        }
    }
    return true;
}
```

校验分为两部分，先进行一个异或计算，然后再进行计算对比，在源码层还是很容易理解的
```
int len = strlen(RegCode_chars);
regcode_1 = (char*) malloc(len + 1);
for (int i = 0; i < len; i++) {
    regcode_1[i] = (char) ((RegCode_chars[i] + i) ^ 0x66);
}
regcode_1[len] = 0;

memset(regcode_2, 0, sizeof(regcode_2));
for (int i = 0; i < 4; i++) {
    regcode_2[i] = regcode_1[4 * i] + regcode_1[4 * i + 1] + regcode_1[4 * i + 2] + regcode_1[4 * i + 3];
}
if ((regcode_2[0] + regcode_2[2]) == (regcode_2[1] + regcode_2[3])) {
    return true;
}
return false;
```

关于如何想出的这个校验，我们可以先把`[a-zA-Z0-9]`都来一个16次的遍历
```
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyClass {
    static char[] legalchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

	public static void main(String[] args) {
		
		for (int i = 0; i < legalchars.length; i++) {
			System.out.print(legalchars[i] + " : ");
			for(int j = 0; j < 16; j++) {
				int temp = (legalchars[i] + j) ^ 0x66;
				System.out.printf("%d : %3d", j, temp);
				System.out.print(", ");
			}
			System.out.println("");
		}
	}
}
```

输出
```
A : 0 :  39, 1 :  36, 2 :  37, 3 :  34, 4 :  35, 5 :  32, 6 :  33, 7 :  46, 8 :  47, 9 :  44, 10 :  45, 11 :  42, 12 :  43, 13 :  40, 14 :  41, 15 :  54, 
B : 0 :  36, 1 :  37, 2 :  34, 3 :  35, 4 :  32, 5 :  33, 6 :  46, 7 :  47, 8 :  44, 9 :  45, 10 :  42, 11 :  43, 12 :  40, 13 :  41, 14 :  54, 15 :  55, 
C : 0 :  37, 1 :  34, 2 :  35, 3 :  32, 4 :  33, 5 :  46, 6 :  47, 7 :  44, 8 :  45, 9 :  42, 10 :  43, 11 :  40, 12 :  41, 13 :  54, 14 :  55, 15 :  52, 
D : 0 :  34, 1 :  35, 2 :  32, 3 :  33, 4 :  46, 5 :  47, 6 :  44, 7 :  45, 8 :  42, 9 :  43, 10 :  40, 11 :  41, 12 :  54, 13 :  55, 14 :  52, 15 :  53, 
E : 0 :  35, 1 :  32, 2 :  33, 3 :  46, 4 :  47, 5 :  44, 6 :  45, 7 :  42, 8 :  43, 9 :  40, 10 :  41, 11 :  54, 12 :  55, 13 :  52, 14 :  53, 15 :  50, 
F : 0 :  32, 1 :  33, 2 :  46, 3 :  47, 4 :  44, 5 :  45, 6 :  42, 7 :  43, 8 :  40, 9 :  41, 10 :  54, 11 :  55, 12 :  52, 13 :  53, 14 :  50, 15 :  51, 
G : 0 :  33, 1 :  46, 2 :  47, 3 :  44, 4 :  45, 5 :  42, 6 :  43, 7 :  40, 8 :  41, 9 :  54, 10 :  55, 11 :  52, 12 :  53, 13 :  50, 14 :  51, 15 :  48, 
H : 0 :  46, 1 :  47, 2 :  44, 3 :  45, 4 :  42, 5 :  43, 6 :  40, 7 :  41, 8 :  54, 9 :  55, 10 :  52, 11 :  53, 12 :  50, 13 :  51, 14 :  48, 15 :  49, 
I : 0 :  47, 1 :  44, 2 :  45, 3 :  42, 4 :  43, 5 :  40, 6 :  41, 7 :  54, 8 :  55, 9 :  52, 10 :  53, 11 :  50, 12 :  51, 13 :  48, 14 :  49, 15 :  62, 
J : 0 :  44, 1 :  45, 2 :  42, 3 :  43, 4 :  40, 5 :  41, 6 :  54, 7 :  55, 8 :  52, 9 :  53, 10 :  50, 11 :  51, 12 :  48, 13 :  49, 14 :  62, 15 :  63, 
K : 0 :  45, 1 :  42, 2 :  43, 3 :  40, 4 :  41, 5 :  54, 6 :  55, 7 :  52, 8 :  53, 9 :  50, 10 :  51, 11 :  48, 12 :  49, 13 :  62, 14 :  63, 15 :  60, 
L : 0 :  42, 1 :  43, 2 :  40, 3 :  41, 4 :  54, 5 :  55, 6 :  52, 7 :  53, 8 :  50, 9 :  51, 10 :  48, 11 :  49, 12 :  62, 13 :  63, 14 :  60, 15 :  61, 
M : 0 :  43, 1 :  40, 2 :  41, 3 :  54, 4 :  55, 5 :  52, 6 :  53, 7 :  50, 8 :  51, 9 :  48, 10 :  49, 11 :  62, 12 :  63, 13 :  60, 14 :  61, 15 :  58, 
N : 0 :  40, 1 :  41, 2 :  54, 3 :  55, 4 :  52, 5 :  53, 6 :  50, 7 :  51, 8 :  48, 9 :  49, 10 :  62, 11 :  63, 12 :  60, 13 :  61, 14 :  58, 15 :  59, 
O : 0 :  41, 1 :  54, 2 :  55, 3 :  52, 4 :  53, 5 :  50, 6 :  51, 7 :  48, 8 :  49, 9 :  62, 10 :  63, 11 :  60, 12 :  61, 13 :  58, 14 :  59, 15 :  56, 
P : 0 :  54, 1 :  55, 2 :  52, 3 :  53, 4 :  50, 5 :  51, 6 :  48, 7 :  49, 8 :  62, 9 :  63, 10 :  60, 11 :  61, 12 :  58, 13 :  59, 14 :  56, 15 :  57, 
Q : 0 :  55, 1 :  52, 2 :  53, 3 :  50, 4 :  51, 5 :  48, 6 :  49, 7 :  62, 8 :  63, 9 :  60, 10 :  61, 11 :  58, 12 :  59, 13 :  56, 14 :  57, 15 :   6, 
R : 0 :  52, 1 :  53, 2 :  50, 3 :  51, 4 :  48, 5 :  49, 6 :  62, 7 :  63, 8 :  60, 9 :  61, 10 :  58, 11 :  59, 12 :  56, 13 :  57, 14 :   6, 15 :   7, 
S : 0 :  53, 1 :  50, 2 :  51, 3 :  48, 4 :  49, 5 :  62, 6 :  63, 7 :  60, 8 :  61, 9 :  58, 10 :  59, 11 :  56, 12 :  57, 13 :   6, 14 :   7, 15 :   4, 
T : 0 :  50, 1 :  51, 2 :  48, 3 :  49, 4 :  62, 5 :  63, 6 :  60, 7 :  61, 8 :  58, 9 :  59, 10 :  56, 11 :  57, 12 :   6, 13 :   7, 14 :   4, 15 :   5, 
U : 0 :  51, 1 :  48, 2 :  49, 3 :  62, 4 :  63, 5 :  60, 6 :  61, 7 :  58, 8 :  59, 9 :  56, 10 :  57, 11 :   6, 12 :   7, 13 :   4, 14 :   5, 15 :   2, 
V : 0 :  48, 1 :  49, 2 :  62, 3 :  63, 4 :  60, 5 :  61, 6 :  58, 7 :  59, 8 :  56, 9 :  57, 10 :   6, 11 :   7, 12 :   4, 13 :   5, 14 :   2, 15 :   3, 
W : 0 :  49, 1 :  62, 2 :  63, 3 :  60, 4 :  61, 5 :  58, 6 :  59, 7 :  56, 8 :  57, 9 :   6, 10 :   7, 11 :   4, 12 :   5, 13 :   2, 14 :   3, 15 :   0, 
X : 0 :  62, 1 :  63, 2 :  60, 3 :  61, 4 :  58, 5 :  59, 6 :  56, 7 :  57, 8 :   6, 9 :   7, 10 :   4, 11 :   5, 12 :   2, 13 :   3, 14 :   0, 15 :   1, 
Y : 0 :  63, 1 :  60, 2 :  61, 3 :  58, 4 :  59, 5 :  56, 6 :  57, 7 :   6, 8 :   7, 9 :   4, 10 :   5, 11 :   2, 12 :   3, 13 :   0, 14 :   1, 15 :  14, 
Z : 0 :  60, 1 :  61, 2 :  58, 3 :  59, 4 :  56, 5 :  57, 6 :   6, 7 :   7, 8 :   4, 9 :   5, 10 :   2, 11 :   3, 12 :   0, 13 :   1, 14 :  14, 15 :  15, 
a : 0 :   7, 1 :   4, 2 :   5, 3 :   2, 4 :   3, 5 :   0, 6 :   1, 7 :  14, 8 :  15, 9 :  12, 10 :  13, 11 :  10, 12 :  11, 13 :   8, 14 :   9, 15 :  22, 
b : 0 :   4, 1 :   5, 2 :   2, 3 :   3, 4 :   0, 5 :   1, 6 :  14, 7 :  15, 8 :  12, 9 :  13, 10 :  10, 11 :  11, 12 :   8, 13 :   9, 14 :  22, 15 :  23, 
c : 0 :   5, 1 :   2, 2 :   3, 3 :   0, 4 :   1, 5 :  14, 6 :  15, 7 :  12, 8 :  13, 9 :  10, 10 :  11, 11 :   8, 12 :   9, 13 :  22, 14 :  23, 15 :  20, 
d : 0 :   2, 1 :   3, 2 :   0, 3 :   1, 4 :  14, 5 :  15, 6 :  12, 7 :  13, 8 :  10, 9 :  11, 10 :   8, 11 :   9, 12 :  22, 13 :  23, 14 :  20, 15 :  21, 
e : 0 :   3, 1 :   0, 2 :   1, 3 :  14, 4 :  15, 5 :  12, 6 :  13, 7 :  10, 8 :  11, 9 :   8, 10 :   9, 11 :  22, 12 :  23, 13 :  20, 14 :  21, 15 :  18, 
f : 0 :   0, 1 :   1, 2 :  14, 3 :  15, 4 :  12, 5 :  13, 6 :  10, 7 :  11, 8 :   8, 9 :   9, 10 :  22, 11 :  23, 12 :  20, 13 :  21, 14 :  18, 15 :  19, 
g : 0 :   1, 1 :  14, 2 :  15, 3 :  12, 4 :  13, 5 :  10, 6 :  11, 7 :   8, 8 :   9, 9 :  22, 10 :  23, 11 :  20, 12 :  21, 13 :  18, 14 :  19, 15 :  16, 
h : 0 :  14, 1 :  15, 2 :  12, 3 :  13, 4 :  10, 5 :  11, 6 :   8, 7 :   9, 8 :  22, 9 :  23, 10 :  20, 11 :  21, 12 :  18, 13 :  19, 14 :  16, 15 :  17, 
i : 0 :  15, 1 :  12, 2 :  13, 3 :  10, 4 :  11, 5 :   8, 6 :   9, 7 :  22, 8 :  23, 9 :  20, 10 :  21, 11 :  18, 12 :  19, 13 :  16, 14 :  17, 15 :  30, 
j : 0 :  12, 1 :  13, 2 :  10, 3 :  11, 4 :   8, 5 :   9, 6 :  22, 7 :  23, 8 :  20, 9 :  21, 10 :  18, 11 :  19, 12 :  16, 13 :  17, 14 :  30, 15 :  31, 
k : 0 :  13, 1 :  10, 2 :  11, 3 :   8, 4 :   9, 5 :  22, 6 :  23, 7 :  20, 8 :  21, 9 :  18, 10 :  19, 11 :  16, 12 :  17, 13 :  30, 14 :  31, 15 :  28, 
l : 0 :  10, 1 :  11, 2 :   8, 3 :   9, 4 :  22, 5 :  23, 6 :  20, 7 :  21, 8 :  18, 9 :  19, 10 :  16, 11 :  17, 12 :  30, 13 :  31, 14 :  28, 15 :  29, 
m : 0 :  11, 1 :   8, 2 :   9, 3 :  22, 4 :  23, 5 :  20, 6 :  21, 7 :  18, 8 :  19, 9 :  16, 10 :  17, 11 :  30, 12 :  31, 13 :  28, 14 :  29, 15 :  26, 
n : 0 :   8, 1 :   9, 2 :  22, 3 :  23, 4 :  20, 5 :  21, 6 :  18, 7 :  19, 8 :  16, 9 :  17, 10 :  30, 11 :  31, 12 :  28, 13 :  29, 14 :  26, 15 :  27, 
o : 0 :   9, 1 :  22, 2 :  23, 3 :  20, 4 :  21, 5 :  18, 6 :  19, 7 :  16, 8 :  17, 9 :  30, 10 :  31, 11 :  28, 12 :  29, 13 :  26, 14 :  27, 15 :  24, 
p : 0 :  22, 1 :  23, 2 :  20, 3 :  21, 4 :  18, 5 :  19, 6 :  16, 7 :  17, 8 :  30, 9 :  31, 10 :  28, 11 :  29, 12 :  26, 13 :  27, 14 :  24, 15 :  25, 
q : 0 :  23, 1 :  20, 2 :  21, 3 :  18, 4 :  19, 5 :  16, 6 :  17, 7 :  30, 8 :  31, 9 :  28, 10 :  29, 11 :  26, 12 :  27, 13 :  24, 14 :  25, 15 : 230, 
r : 0 :  20, 1 :  21, 2 :  18, 3 :  19, 4 :  16, 5 :  17, 6 :  30, 7 :  31, 8 :  28, 9 :  29, 10 :  26, 11 :  27, 12 :  24, 13 :  25, 14 : 230, 15 : 231, 
s : 0 :  21, 1 :  18, 2 :  19, 3 :  16, 4 :  17, 5 :  30, 6 :  31, 7 :  28, 8 :  29, 9 :  26, 10 :  27, 11 :  24, 12 :  25, 13 : 230, 14 : 231, 15 : 228, 
t : 0 :  18, 1 :  19, 2 :  16, 3 :  17, 4 :  30, 5 :  31, 6 :  28, 7 :  29, 8 :  26, 9 :  27, 10 :  24, 11 :  25, 12 : 230, 13 : 231, 14 : 228, 15 : 229, 
u : 0 :  19, 1 :  16, 2 :  17, 3 :  30, 4 :  31, 5 :  28, 6 :  29, 7 :  26, 8 :  27, 9 :  24, 10 :  25, 11 : 230, 12 : 231, 13 : 228, 14 : 229, 15 : 226, 
v : 0 :  16, 1 :  17, 2 :  30, 3 :  31, 4 :  28, 5 :  29, 6 :  26, 7 :  27, 8 :  24, 9 :  25, 10 : 230, 11 : 231, 12 : 228, 13 : 229, 14 : 226, 15 : 227, 
w : 0 :  17, 1 :  30, 2 :  31, 3 :  28, 4 :  29, 5 :  26, 6 :  27, 7 :  24, 8 :  25, 9 : 230, 10 : 231, 11 : 228, 12 : 229, 13 : 226, 14 : 227, 15 : 224, 
x : 0 :  30, 1 :  31, 2 :  28, 3 :  29, 4 :  26, 5 :  27, 6 :  24, 7 :  25, 8 : 230, 9 : 231, 10 : 228, 11 : 229, 12 : 226, 13 : 227, 14 : 224, 15 : 225, 
y : 0 :  31, 1 :  28, 2 :  29, 3 :  26, 4 :  27, 5 :  24, 6 :  25, 7 : 230, 8 : 231, 9 : 228, 10 : 229, 11 : 226, 12 : 227, 13 : 224, 14 : 225, 15 : 238, 
z : 0 :  28, 1 :  29, 2 :  26, 3 :  27, 4 :  24, 5 :  25, 6 : 230, 7 : 231, 8 : 228, 9 : 229, 10 : 226, 11 : 227, 12 : 224, 13 : 225, 14 : 238, 15 : 239, 
0 : 0 :  86, 1 :  87, 2 :  84, 3 :  85, 4 :  82, 5 :  83, 6 :  80, 7 :  81, 8 :  94, 9 :  95, 10 :  92, 11 :  93, 12 :  90, 13 :  91, 14 :  88, 15 :  89, 
1 : 0 :  87, 1 :  84, 2 :  85, 3 :  82, 4 :  83, 5 :  80, 6 :  81, 7 :  94, 8 :  95, 9 :  92, 10 :  93, 11 :  90, 12 :  91, 13 :  88, 14 :  89, 15 :  38, 
2 : 0 :  84, 1 :  85, 2 :  82, 3 :  83, 4 :  80, 5 :  81, 6 :  94, 7 :  95, 8 :  92, 9 :  93, 10 :  90, 11 :  91, 12 :  88, 13 :  89, 14 :  38, 15 :  39, 
3 : 0 :  85, 1 :  82, 2 :  83, 3 :  80, 4 :  81, 5 :  94, 6 :  95, 7 :  92, 8 :  93, 9 :  90, 10 :  91, 11 :  88, 12 :  89, 13 :  38, 14 :  39, 15 :  36, 
4 : 0 :  82, 1 :  83, 2 :  80, 3 :  81, 4 :  94, 5 :  95, 6 :  92, 7 :  93, 8 :  90, 9 :  91, 10 :  88, 11 :  89, 12 :  38, 13 :  39, 14 :  36, 15 :  37, 
5 : 0 :  83, 1 :  80, 2 :  81, 3 :  94, 4 :  95, 5 :  92, 6 :  93, 7 :  90, 8 :  91, 9 :  88, 10 :  89, 11 :  38, 12 :  39, 13 :  36, 14 :  37, 15 :  34, 
6 : 0 :  80, 1 :  81, 2 :  94, 3 :  95, 4 :  92, 5 :  93, 6 :  90, 7 :  91, 8 :  88, 9 :  89, 10 :  38, 11 :  39, 12 :  36, 13 :  37, 14 :  34, 15 :  35, 
7 : 0 :  81, 1 :  94, 2 :  95, 3 :  92, 4 :  93, 5 :  90, 6 :  91, 7 :  88, 8 :  89, 9 :  38, 10 :  39, 11 :  36, 12 :  37, 13 :  34, 14 :  35, 15 :  32, 
8 : 0 :  94, 1 :  95, 2 :  92, 3 :  93, 4 :  90, 5 :  91, 6 :  88, 7 :  89, 8 :  38, 9 :  39, 10 :  36, 11 :  37, 12 :  34, 13 :  35, 14 :  32, 15 :  33, 
9 : 0 :  95, 1 :  92, 2 :  93, 3 :  90, 4 :  91, 5 :  88, 6 :  89, 7 :  38, 8 :  39, 9 :  36, 10 :  37, 11 :  34, 12 :  35, 13 :  32, 14 :  33, 15 :  46, 
```

校验算法有一个相加后对比，最简单的办法就是找到16个一样的数字
```
for (int i = 0; i < 4; i++) {
    regcode_2[i] = regcode_1[4 * i] + regcode_1[4 * i + 1] + regcode_1[4 * i + 2] + regcode_1[4 * i + 3];
}
if ((regcode_2[0] + regcode_2[2]) == (regcode_2[1] + regcode_2[3])) {
    return true;
}
```

这里就是我在前面说的**不要局限在写代码遍历**，直接用眼睛找，虽然有可能是找不到的

我们来随便找一组，就挑第一排
```
0 :  39
1 :  36
2 :  37
3 :  34

8 :  47
9 :  44
10 :  45
11 :  42

sum = 324
```

然后就在对应的位数里面挑出和为324的八个数就行
```
4 :  40 : J
5 :  33 : B
6 :  40 : H
7 :  31 : r

12 :  48 : J
13 :  50 : G
14 :  31 : k
15 :  51 : F
```

这个校验是可以再加限制条件的，那靠眼睛找是要瞎掉的

在主Activity调用模拟器检测，检测到直接退出
```
private void CallDetectMeyhod() {
    if (Emulator_Finder.CheckIMEI(context)
            || Emulator_Finder.CheckIMSI(context)
            || Emulator_Finder.CheckDriver(context)
            || Emulator_Finder.CheckOperatorName(context)
            || Emulator_Finder.CheckPhoneNumber(context)
            || Emulator_Finder.CheckFiles(context)
            || Emulator_Finder.CheckBuild(context)
            || Emulator_Finder.CheckPipes(context)) {
        System.exit(0);
    }
}
```

## 0x02 小结
Have Fun:)









































