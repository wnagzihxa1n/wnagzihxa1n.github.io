public class MyClass {
	
	static byte[] Compare_KEY = {0xB, 0x6F, 0x0E, 0x64, 0x09, 0x74, 0x11, 0x6F, 0x02, 0x6C, 
			0x01, 0x66, 0x5F, 0x57, 0x30, 0x5C, 0x73, 0x4A, 0x43, 0x6D, 0x75, 0x4A, 0x53, 0x42, 0x7D};
	static char[] strMOTAL = "mortal".toCharArray();
	
	public static void main(String[] args) throws Exception {
		int j = 0;
		for(int i = 0; i < Compare_KEY.length; i++) {
			if ((i % 2) == 1) {
				Compare_KEY[i] ^= 0x03;
			} else if (j <= 5) {
				Compare_KEY[i] ^= strMOTAL[j];
				j++;
			}
		}
		
		System.out.println(new String(Compare_KEY, "utf-8"));
	}
}
