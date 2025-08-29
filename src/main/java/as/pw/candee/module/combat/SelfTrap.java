package as.pw.candee.module.combat;
import java.util.HashMap;
import java.util.Map;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.Timer;
import net.minecraft.util.math.BlockPos;
public class SelfTrap
        extends Module {
    private boolean isSneaking;

    public SelfTrap() {
                super("SelfTrap", Categories.COMBAT, false, false);
        Setting<Integer> blocksPerTick = register(new Setting("BlocksPerTick", 8, 1, 20));
        Setting<Integer> delay = register(new Setting("Delay", 50, 0, 250));
        Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.TRUE));
        Setting<Integer> disableTime = register(new Setting("DisableTime", 200, 50, 300));
        Setting<Boolean> disable = register(new Setting("AutoDisable", Boolean.TRUE));
        Setting<Boolean> packet = register(new Setting("PacketPlace", Boolean.FALSE));
        Timer offTimer = new Timer();
        Timer timer = new Timer();
        Map<BlockPos, Integer> retries = new HashMap<>();
        Timer retryTimer = new Timer();
        int blocksThisTick = 0;
        boolean hasOffhand = false;
        }
}