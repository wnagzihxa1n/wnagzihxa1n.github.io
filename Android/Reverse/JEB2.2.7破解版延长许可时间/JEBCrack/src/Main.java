import javax.print.attribute.standard.PrinterLocation;

import javassist.*;

public class Main {
	public static void main(String[] args) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath("E:\\workplace\\JEBCrack\\jeb.jar");
		CtClass ctClass = pool.get("com.pnfsoftware.jeb.client.Licensing");
		try{
			CtMethod ctMethod = ctClass.getDeclaredMethod("getExpirationTimestamp");
			String methodInfo= ctMethod.getMethodInfo().toString();
			System.out.println(methodInfo);
			ctMethod.setBody("return 2000000000;");
			ctClass.writeFile("E:\\");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
