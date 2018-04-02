import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;

import jeb.api.IScript;
import jeb.api.JebInstance;
import jeb.api.ast.*;
import jeb.api.dex.Dex;
import jeb.api.dex.DexMethod;
import jeb.api.ui.JavaView;
import jeb.api.ui.JebUI;
import jeb.api.ui.View;
import sun.reflect.generics.tree.ClassSignature;

public class JavaDeObfuscator implements IScript {

    private static String decodeMethodSignature = "Lcom/wnagzihxa1n/obfuscatordemo/Util;->decStr([B)Ljava/lang/String;";
    private JebInstance jeb = null;
    private Constant.Builder cstBuilder = null;

    @Override
    public void run(JebInstance jebInstance) {
        jeb = jebInstance;
        cstBuilder = new Constant.Builder(jeb);
        JebUI jebUI = jeb.getUI();
        JavaView javaView = (JavaView) jebUI.getView(View.Type.JAVA);
        Dex dex = jeb.getDex();
        int methodCount = dex.getMethodCount();
        jeb.print("methodCount : " + Integer.toString(methodCount));
        for (int i = 0; i < methodCount; i++) {
            DexMethod dexMethod = dex.getMethod(i);
//            if (dexMethod.getSignature(true).contains(decodeSignature)) {
//                jeb.print(dexMethod.getSignature(true));
//            }
            if (!dexMethod.getSignature(true).contains(decodeMethodSignature)) {
                continue;
            }
            int methodIndex = dexMethod.getIndex();
            String methodSignature = dexMethod.getSignature(true);
            jeb.print(methodSignature);
            List<Integer> methodReferences = dex.getMethodReferences(methodIndex);
            if (methodReferences == null) {
                continue;
            }
            for (Integer refIdx : methodReferences) {
                if (refIdx == null) {
                    break;
                }
                DexMethod refDexMethod = dex.getMethod(refIdx);
                jeb.print("#    " + refDexMethod.getSignature(true));
                jeb.decompileMethod(refDexMethod.getSignature(true));
                jeb.api.ast.Method decompileMethodTree = jeb.getDecompiledMethodTree(refDexMethod.getSignature(true));
                List<IElement> sublElements = decompileMethodTree.getSubElements();
                replaceMethod(sublElements, decompileMethodTree);
            }
        }
        jeb.print("Finish");
    }

    private void replaceMethod(List<IElement> elements, IElement parentElement) {
        for (IElement element : elements) {
//            jeb.print("        " + element.toString());
            if (element instanceof Call) {
                Call call = (Call) element;
                jeb.api.ast.Method method = call.getMethod();
                if (method.getSignature().equals(decodeMethodSignature)) {
                    jeb.print("##    " + method.getSignature());
                    List<IExpression> arguments = call.getArguments();
                    NewArray arg = (NewArray) arguments.get(0);
                    List encByte = arg.getInitialValues();
                    if (encByte == null) {
                        continue;
                    }
                    int arraySize = encByte.size();
                    byte[] encByteArray = new byte[arraySize];
                    for (int i = 0; i < arraySize; i++) {
                        encByteArray[i] = ((Constant)encByte.get(i)).getByte();
                    }
                    String decStr = null;
                    decStr = descryptStr(encByteArray);
                    if (decStr != null) {
                        jeb.print(decStr);
                        parentElement.replaceSubElement(element, cstBuilder.buildString(decStr));
                    }
                    continue;
                }
            }
            List<IElement> subElements = element.getSubElements();
            replaceMethod(subElements, element);
        }
    }

    private String descryptStr(byte[] data) {
        return new String(data);
    }
}
