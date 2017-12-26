# Learn to Develop Android CM Step by Step-08199CDAC10B05FB828F9D68461F0045

**Author：wnagzihxain
Mail：tudouboom@163.com**

## 0x00 前言
挖一个坑，又给填上了。。。。。。
- https://github.com/toToCW/AndroidCrackMes/tree/master/2.08199CDAC10B05FB828F9D68461F0045

## 0x01 Description
想Flag是一件很蛋疼的事情。。。。。。，现在长这样，以后就不一定了。。。。。。
```
flag{A1_Y0_Bu_Cu0_Y0}
```

算法选择一个简单的异或操作，直接跟一个数组异或就可以得出Flag

先把Flag转为`byte`数组，至于`XOR_KEY`为什么我只选择`0x66`，估计世界上只有一个人知道
```
public class MyClass {
    final static String Flag = "flag{A1_Y0_Bu_Cu0_Y0}";
    private static byte XOR_KEY = 0x66;
    
	private static void printHex(byte[] Data) {
    	for(int i = 0; i < Data.length; i++) {
    		String temp = Integer.toHexString(Data[i] & 0xFF);
    		if(temp.length() == 1) {
    			System.out.print("0x0" + temp);
    		} else {
    			System.out.print("0x" + temp);
			}
    		if(i != Data.length - 1) {
    			System.out.print(", ");
    		}
    	}
    	System.out.println("");
	}
    
	public static void main(String[] args) {
		byte[] Flag_byte = Flag.getBytes();
		for (int i = 0; i < Flag_byte.length; i++) {
			Flag_byte[i] = (byte) (Flag_byte[i] ^ XOR_KEY);
		}
		printHex(Flag_byte);
	}
}
```

输出
```
0x00, 0x0a, 0x07, 0x01, 0x1d, 0x27, 0x57, 0x39, 0x3f, 0x56, 0x39, 0x24, 0x13, 0x39, 0x25, 0x13, 0x56, 0x39, 0x3f, 0x56, 0x1b
```

我们将这个数组命名为`RegCode`
```
private static byte[] RegCode = {0x00, 0x0a, 0x07, 0x01, 0x1d, 0x27, 0x57, 0x39, 0x3f, 0x56, 0x39, 0x24, 0x13, 0x39, 0x25, 0x13, 0x56, 0x39, 0x3f, 0x56, 0x1b};
```

那么我们如何在输入的数据中获取到`XOR_KEY`呢？而且`byte`理论上最大到`0xFF`，那么解题者只需要强行遍历`0x00 - 0xFF`作为`XOR_KEY`，找到有意义的数据即可

所以我设了一个幌子，这个`random_xor_key`还是能够充当搅屎棍的~~~
```
String RegCode_input = et_RegCode.getText().toString();
int random_xor_key = (int) (Math.random() * RegCode_input.length());
XOR_KEY = RegCode_input.getBytes()[random_xor_key];
```

为了不让解题者太容易看出Flag，也为了**留一个坑**，我选择加上MD5算法，这样既不太明显暴露Flag，也不至于太难，毕竟MD5的Java代码直接拷贝就可以用

校验的时候，单独写一个函数，先异或，然后MD5计算
```
private static boolean CheckReg() {
    boolean result = false;
    byte[] RegCode_stored = new byte[RegCode.length];
    for (int i = 0; i < RegCode.length; i++) {
        RegCode_stored[i] = (byte) (RegCode[i] ^ XOR_KEY);
    }
    if (Encrypt_MD5((new String(RegCode_stored))).equals(Check_MD5)) {
        result = true;
    }
    return result;
}
```

MD5实现
```
private static String Encrypt_MD5(String RegCode_XOR) {
    StringBuffer stringBuffer = new StringBuffer("");
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = messageDigest.digest(RegCode_XOR.getBytes());
        for (byte b : bytes) {
            String temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append(0);
            }
            stringBuffer.append(temp);
        }
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return stringBuffer.toString();
}
```

进行校验的MD5值初始化，当然拿去解密是肯定解不出来的
```
final static String Check_MD5 = "3f766e701a20641253c35cd24ed3d85c";
```

最后为了让这个CM看起来正常一点，我限制了输入注册码的长度
```
if ((RegCode_input.length() == 16) && CheckReg()) {
    Toast.makeText(MainActivity.this, "N1ce, it's th3 c0rrect Flag", Toast.LENGTH_LONG).show();
} else {
    Toast.makeText(MainActivity.this, "S0rry, try aga1n", Toast.LENGTH_LONG).show();
}
```

前面的`random_xor_key`，，简单粗暴的绕过方法就是输入`ffffffffffffffff`，这样无论怎么随机，都只会抽到`f`也就是`0x66`

## 0x02 小结
Have Fun:)


