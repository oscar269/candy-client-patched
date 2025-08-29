 package as.pw.candee.module.movement;
 import java.util.Comparator;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.util.math.BlockPos;
 public class HoleTP
     extends Module {
     public final Setting<Float> range;
     public final Setting<Boolean> stopMotion;
     public HoleTP() {
         super("HoleTP", Categories.MOVEMENT, false, false);
         this.range = register(new Setting("Range", 1.0F, 3.0F, 0.1F));
         this.stopMotion = register(new Setting("StopMotion", Boolean.FALSE));
     }
     public void onEnable() {
         BlockPos hole = CandeePlusRewrite.m_hole.calcHoles().stream().min(Comparator.comparing(p -> mc.player.getDistance(p.getX(), p.getY(), p.getZ()))).orElse(null);
         if (hole != null) {
             if (mc.player.getDistance(hole.getX(), hole.getY(), hole.getZ()) < this.range.getValue() + 1.5D) {
                 mc.player.setPosition(hole.getX() + 0.5D, mc.player.posY, hole.getZ() + 0.5D);
                 if (this.stopMotion.getValue()) {
                     mc.player.motionX = 0.0D;
                     mc.player.motionZ = 0.0D;
                 }
                 mc.player.motionY = -3.0D;
                 sendMessage("Accepting teleport...");
             } else {
                 sendMessage("Out of range! disabling...");
             }
         } else {
             sendMessage("Not found hole! disabling...");
         }
         disable();
     }
 }