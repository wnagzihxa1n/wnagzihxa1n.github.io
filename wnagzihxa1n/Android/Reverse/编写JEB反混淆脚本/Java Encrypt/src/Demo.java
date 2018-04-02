public class Demo {
    public static void main(String[] args) {
        String a = "1111111111";
        String b = "2222222222";
        String c = "3333333333";
        String d = "4444444444";
        String e = "5555555555";
        encStr(a);
        encStr(b);
        encStr(c);
        encStr(d);
        encStr(e);
    }

    private static void encStr(String data) {
        byte[] encByte = data.getBytes();
        System.out.print("{");
        for (byte b : encByte) {
            int temp = b & 0xFF;
            String t = Integer.toHexString(temp);
            if (t.length() == 1) {
                System.out.print("0x0" + t);
            } else {
                System.out.print("0x" + t);
            }
            System.out.print(", ");
        }
        System.out.println("}");
    }
}
