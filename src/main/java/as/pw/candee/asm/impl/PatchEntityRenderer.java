package as.pw.candee.asm.impl;
import as.pw.candee.asm.api.ClassPatch;
import as.pw.candee.asm.api.MappingName;
import as.pw.candee.utils.ASMUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
public class PatchEntityRenderer extends ClassPatch {
        public PatchEntityRenderer() {
                super("net.minecraft.client.renderer.EntityRenderer", "buq");
        }
        public byte[] transform(byte[] bytes) {
                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(bytes);
                classReader.accept(classNode, 0);
                MappingName method = new MappingName("updateCameraAndRender", "a", "updateCameraAndRender");
                String desc = "(FJ)V";
                for (MethodNode it : classNode.methods) {
                        if (method.equalName(it.name) && it.desc.equals("(FJ)V")) {
                                patchUpdateCameraAndRender(it);
                        }
                }
                ClassWriter writer = new ClassWriter(0);
                classNode.accept(writer);
                return writer.toByteArray();
        }
        public void patchUpdateCameraAndRender(MethodNode methodNode) {
                AbstractInsnNode target = ASMUtil.findMethodInsn(methodNode, 182, "biq", "a", "(F)V");
                if (target != null) {
                        InsnList insnList = new InsnList();
                        insnList.add(new VarInsnNode(23, 1));
                        insnList.add(new MethodInsnNode(184, Type.getInternalName(getClass()), "updateCameraAndRenderHook", "(F)V", false));
                        methodNode.instructions.insert(target, insnList);
                }
        }
        public static void updateCameraAndRenderHook(float partialTicks) {}
}