package com.wnagzihxain.pack;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PackMain {
    private static String inAPKName = "SourceAPK.apk";
    private static String outAPKName = "pkg_packed.apk";
    public static void main(String[] args) {
        try {
            File inapk = new File(inAPKName);
            File outapk = new File(outAPKName);
            File tmpFolder = new File("apkDecompile");
            if (outapk.exists()) {
                outapk.delete();
                outapk.createNewFile();
            }
            if (tmpFolder.exists()) {
                tmpFolder.delete();
                tmpFolder.mkdirs();
            }
            if (!inapk.exists()) throw new Exception("Input APK file does not exist!!!");

            System.out.println("开始反编译 " + inapk.getAbsolutePath() + " 到 " + tmpFolder.getAbsolutePath());
            System.out.println("--------------------------------------------------------------");

            com.rover12421.shaka.cli.Main.main(new String[]{"d", "-s", inapk.getAbsolutePath(),
                    "-o", tmpFolder.getAbsolutePath(), "-f", "-df"});

            System.out.println("------------------------------反编译完成-----------------------\n");

            File assets = new File(tmpFolder, "assets");
            if (!assets.exists()) {
                assets.mkdirs();
            }
            File sourceDex = new File(tmpFolder, "classes.dex");
            File encryptedDex = new File(assets, "encryptedDex");

//            System.out.println("\nsourceDexPath : " + sourceDex.getAbsolutePath());
//            System.out.println("encryptedDexPath : " + encryptedDex.getAbsolutePath());

            System.out.println("开始加固");
            System.out.println("--------------------------------------------------------------");

            System.out.println("I: 加密 classes.dex...");
            encryptDex(sourceDex.getAbsolutePath(), encryptedDex.getAbsolutePath());
            System.out.println("I: classes.dex 加密完成...");

            System.out.println("I: 删除 classes.dex...");
            sourceDex.delete();
            System.out.println("I: classes.dex 删除完成...");

            System.out.println("I: 拷贝壳 Smali 文件...");
            File sourceSmaliFile = new File("smali");
            File targetSmaliFile = new File(tmpFolder, "smali");
            if (!targetSmaliFile.exists()) {
                targetSmaliFile.mkdirs();
            }

            File smaliProtectApp = new File(sourceSmaliFile, "ProtectApplication.smali");
            File smaliRefInvoke = new File(sourceSmaliFile, "RefInvoke.smali");
            File out_smaliProtectApp = new File(targetSmaliFile, "ProtectApplication.smali");
            File out_smaliRefInvoke = new File(targetSmaliFile, "RefInvoke.smali");
            InputStream in_protectApp = null, in_refInvoke = null;
            FileOutputStream out_protectApp = null, out_refInvoke = null;
            try {
                in_protectApp = new FileInputStream(smaliProtectApp);
                int len = in_protectApp.available();
                byte[] buffer = new byte[len];
                in_protectApp.read(buffer);
                out_protectApp = new FileOutputStream(out_smaliProtectApp);
                out_protectApp.write(buffer);
                out_protectApp.flush();

                in_refInvoke = new FileInputStream(smaliRefInvoke);
                len = in_refInvoke.available();
                byte[] buffer1 = new byte[len];
                in_refInvoke.read(buffer1);
                out_refInvoke = new FileOutputStream(out_smaliRefInvoke);
                out_refInvoke.write(buffer1);
                out_refInvoke.flush();
                System.out.println("I: 壳 Smali 文件拷贝完成...");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("I: 壳 Smali 文件拷贝失败...");
            } finally {
                try {
                    in_protectApp.close();
                    in_refInvoke.close();
                    out_protectApp.close();
                    out_refInvoke.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("I: 修改 AndroidManifest.xml...");
            modifyManifestXML(tmpFolder.getAbsolutePath() + "\\" +"AndroidManifest.xml");
            System.out.println("I: AndroidManifest.xml 修改完成...");

            System.out.println("------------------------------加固完成:)-----------------------\n");

            System.out.println("开始回编译");
            System.out.println("--------------------------------------------------------------");

            com.rover12421.shaka.cli.Main.main(new String[]{"b", tmpFolder.getAbsolutePath(), "-o", outapk.getAbsolutePath(), "-f"});
            System.out.println("I: 回编译成功...");
            System.out.println("I: 加固后的APK路径：" + outapk.getAbsolutePath());

            System.out.println("------------------------------回编译完成-----------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptDex(String sourceDexPath, String encryptedDexPath) {
        byte xor_key = 0x66;
        File sourceDex = new File(sourceDexPath);
        File encryptedDex = new File(encryptedDexPath);

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            inputStream = new FileInputStream(sourceDex);
            fileOutputStream = new FileOutputStream(encryptedDex);
            byte[] buffer = new byte[1024];
            int index = inputStream.read(buffer);
            while (true) {
                if (index == -1) {
                    break;
                }
                for (int i = 0; i < index; i++) {
                    buffer[i] ^= xor_key;
                }
                fileOutputStream.write(buffer, 0, index);
                index = inputStream.read(buffer);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void modifyManifestXML(String ManifestXMLPath) {
        String appKey = "APPLICATION_CLASS_NAME";
        String proxyApp = "com.wnagzihxain.sourceapk.ProtectApplication";
        String NAME_SPACE  = "http://schemas.android.com/apk/res/android";
        String NAME_PREFIX = "android";
        String META_DATA = "meta-data";
        String NAME = "name";
        String VALUE = "value";

        File manifestxml = new File(ManifestXMLPath);

        SAXBuilder saxBuilder = new SAXBuilder();
        String oldApplication = null;

        try {
            Document document = saxBuilder.build(manifestxml);
            Element element_root = document.getRootElement();
            Element element_application = element_root.getChild("application");
            Namespace namespace = Namespace.getNamespace(NAME_PREFIX, NAME_SPACE);
            Attribute attribute = element_application.getAttribute(NAME, namespace);
            if (attribute != null) {
                oldApplication = attribute.getValue();
                attribute.setValue(proxyApp);
            } else {
                element_application.setAttribute(NAME, proxyApp, namespace);
            }

            if (oldApplication != null) {
                element_application.addContent(new Element(META_DATA).setAttribute(NAME, appKey, namespace).setAttribute(VALUE, oldApplication, namespace));
            }

            XMLOutputter XMLOutput = new XMLOutputter();
            XMLOutput.output(document, new FileOutputStream(manifestxml));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



























