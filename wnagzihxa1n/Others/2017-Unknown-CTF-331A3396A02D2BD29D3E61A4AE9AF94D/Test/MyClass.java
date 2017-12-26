public class MyClass {
	
	static byte[] Compare_KEY = new byte[]{31, 102, 14, 97, 48, 123, 11, 104, 22, 114, 47, 95, 16, 117, 62, 107, 29, 65, 26, 100, 12, 111, 15, 100};
	static char[] Xor_KEY = "sicnuisasher".toCharArray();
	
	public static void main(String[] args) throws Exception {
		for(int i = 0; i < Compare_KEY.length; i += 2) {
			byte temp = Compare_KEY[i];
			Compare_KEY[i] = Compare_KEY[i + 1];
			Compare_KEY[i + 1] = temp;
		}
		
		int j = 0;
		for(int i = 1; i < Compare_KEY.length; i += 2) {
			Compare_KEY[i] = (byte)(Compare_KEY[i] ^ Xor_KEY[j++]);
		}
		
		System.out.println(new String(Compare_KEY, "utf-8"));
	}
}
