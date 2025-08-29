 package as.pw.candee.asm;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.asm.api.ClassPatch;
 import as.pw.candee.asm.impl.PatchEntityRenderer;
 import net.minecraft.launchwrapper.IClassTransformer;
 public class CandyTransformer
     implements IClassTransformer
 {
     public static final List<ClassPatch> patches;
     public byte[] transform(String name, String transformedName, byte[] bytes) {
         if (bytes == null) {
             return null;
         }
         for (ClassPatch it : patches) {
             if (it.className.equals(transformedName)) {
                 return it.transform(bytes);
             }
         }
         return bytes;
     }
     static {
         (patches = new ArrayList<>()).add(new PatchEntityRenderer());
     }
 }