package as.pw.candee.asm;
import java.io.IOException;
import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
public class CandyAccessTransformer extends AccessTransformer {
        public CandyAccessTransformer() throws IOException {
                super("META-INF/candeeplusrewrite_at.cfg");
        }
}