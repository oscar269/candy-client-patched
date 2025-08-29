 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 public class FastStop
     extends Module
 {
     public FastStop() {
         super("FastStop", Categories.MOVEMENT, false, false);
     }
     public void onTick() {
         if (mc.player == null || mc.world == null)
             return;
         if (mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F) {
             mc.player.motionX = 0.0D;
             mc.player.motionZ = 0.0D;
         }
     }
 }