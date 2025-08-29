package as.pw.candee.asm.api;
public class ClassPatch {
        public final String className;
        public final String notchName;
        public ClassPatch(String nameIn, String notchIn) {
                this.className = nameIn;
                this.notchName = notchIn;
        }
        public byte[] transform(byte[] bytes) {
                return new byte[0];
        }
        public boolean equalName(String name) {
                return (this.className.equals(name) || this.notchName.equals(name));
        }
}