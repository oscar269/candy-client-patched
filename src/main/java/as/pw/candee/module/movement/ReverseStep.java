 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.utils.EntityUtil;
 import net.minecraft.client.entity.EntityPlayerSP;
 public class ReverseStep
     extends Module
 {
     public ReverseStep() {
         super("ReverseStep", Categories.MOVEMENT, false, false);
     }
     public void onUpdate() {
         if (mc.player.onGround) {
             EntityPlayerSP player = mc.player;
             if (!EntityUtil.isInLiquid())
                 player.motionY--;
         }
     }
 }