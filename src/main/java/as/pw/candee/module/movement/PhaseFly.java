 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.MathUtil;
 import net.minecraft.client.entity.EntityPlayerSP;
 public class PhaseFly extends Module {
     public final Setting<Float> speed;
     public final Setting<Float> vSpeed;
     public final Setting<Boolean> onlyInBlock;
     public PhaseFly() {
         super("PhaseFly", Categories.MOVEMENT, false, false);
         this.speed = register(new Setting("Speed", 1.0F, 5.0F, 0.1F));
         this.vSpeed = register(new Setting("VerticalSpeed", 1.0F, 5.0F, 0.1F));
         this.onlyInBlock = register(new Setting("OnlyInBlock", Boolean.FALSE));
     }
     public void onUpdate() {
         if (nullCheck())
             return;
         EntityPlayerSP player = mc.player;
         boolean insideBlock = isInsideBlock(player);
         if (this.onlyInBlock.getValue() && !insideBlock) {
             player.noClip = false;
             return;
         }
         player.noClip = true;
         player.onGround = false;
         player.fallDistance = 0.0F;
         double[] mv = MathUtil.directionSpeed(this.speed.getValue());
         player.motionX = mv[0];
         player.motionZ = mv[1];
         double v = this.vSpeed.getValue();
         if (player.movementInput.jump) {
             player.motionY = v;
         } else if (player.movementInput.sneak) {
             player.motionY = -v;
         } else {
             player.motionY = 0.0D;
         }
     }
     public void onDisable() {
         if (mc.player != null) {
             mc.player.noClip = false;
         }
     }
     private boolean isInsideBlock(EntityPlayerSP player) {
         return (player.noClip || !player.world.getCollisionBoxes(player, player.getEntityBoundingBox()).isEmpty());
     }
 }