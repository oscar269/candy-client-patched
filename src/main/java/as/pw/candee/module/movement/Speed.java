 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.Vec3d;
 import net.minecraftforge.fml.common.Mod.EventHandler;
 public class Speed extends Module {
     public final Setting<SpeedMode> mode;
     public final Setting<Float> multiplier;
     public final Setting<Boolean> forceSprint;
     public final Setting<Boolean> enableOnJump;
     public final Setting<Boolean> resetMotion;
     public final Setting<Boolean> fastJump;
     public final Setting<Boolean> jumpSkip;
     private boolean lastActive = false;
     public Speed() {
         super("Speed", Categories.MOVEMENT, false, false);
         this.mode = register(new Setting("Mode", SpeedMode.STRAFE));
         this.multiplier = register(new Setting("Multiplier", 2.0F, 5.0F, 0.0F));
         this.forceSprint = register(new Setting("Force Sprint", Boolean.TRUE));
         this.enableOnJump = register(new Setting("Enable On Jump", Boolean.FALSE));
         this.resetMotion = register(new Setting("Reset Motion", Boolean.TRUE));
         this.fastJump = register(new Setting("Fast Jump", Boolean.TRUE));
         this.jumpSkip = register(new Setting("Jump Skip", Boolean.TRUE));
     }
     public void onEnable() {
         super.onEnable();
     }
     public void onDisable() {
         super.onDisable();
         resetMotion();
     }
     private void resetMotion() {
         if (!this.resetMotion.getValue() || !this.lastActive || mc.player == null) {
             return;
         }
         this.lastActive = false;
         mc.player.motionX = 0.0D;
         mc.player.motionZ = 0.0D;
     }
     @EventHandler
     public void onTick() {
         if (mc.player == null || mc.world == null)
             return;
         EntityPlayerSP entityPlayerSP = mc.player;
         Vec3d input = getMoveInputs(entityPlayerSP);
         if (this.forceSprint.getValue() && (input.x != 0.0D || input.z != 0.0D)) {
             entityPlayerSP.setSprinting(true);
         }
         if (this.enableOnJump.getValue() && !mc.gameSettings.keyBindJump.isKeyDown()) {
             resetMotion();
             return;
         }
         if (entityPlayerSP.isInWater() || entityPlayerSP.isInLava() || entityPlayerSP.isOnLadder()) {
             return;
         }
         this.lastActive = true;
         Vec3d moveInputs = getMoveInputs(entityPlayerSP);
         moveInputs = moveInputs.subtract(0.0D, moveInputs.y, 0.0D).normalize();
         float yaw = entityPlayerSP.rotationYaw;
         Vec3d motion = toWorld(moveInputs.x, moveInputs.z, yaw);
         AxisAlignedBB box = entityPlayerSP.getEntityBoundingBox().offset(motion.x / 10.0D, 0.0D, motion.z / 10.0D);
         if (this.fastJump.getValue() && mc.gameSettings.keyBindJump.isKeyDown() && entityPlayerSP.onGround) {
             entityPlayerSP.motionY = 0.42D;
         }
         if (this.jumpSkip.getValue() && mc.world
             .collidesWithAnyBlock(box) &&
             !mc.world.collidesWithAnyBlock(box.offset(0.0D, 0.25D, 0.0D)) && entityPlayerSP.motionY > 0.0D) {
             double maxHeight = getMaxHeight(box.offset(0.0D, 0.25D, 0.0D));
             entityPlayerSP.setPosition(entityPlayerSP.posX + motion.x / 10.0D, maxHeight, entityPlayerSP.posZ + motion.z / 10.0D);
             entityPlayerSP.motionY = 0.0D;
         }
         if (this.mode.getValue() == SpeedMode.VANILLA) {
             float value = this.multiplier.getValue();
             entityPlayerSP.motionX = motion.x * value;
             entityPlayerSP.motionZ = motion.z * value;
         } else if (this.mode.getValue() == SpeedMode.STRAFE) {
             Vec3d vec = new Vec3d(entityPlayerSP.motionX, 0.0D, entityPlayerSP.motionZ);
             double mul = Math.max(vec.distanceTo(Vec3d.ZERO), entityPlayerSP.isSneaking() ? 0.0D : 0.1D);
             if (!mc.gameSettings.keyBindForward.isKeyDown() && entityPlayerSP.onGround) {
                 mul *= 1.18D;
             } else if (!mc.gameSettings.keyBindForward.isKeyDown()) {
                 mul *= 1.025D;
             }
             entityPlayerSP.motionX = motion.x * mul;
             entityPlayerSP.motionZ = motion.z * mul;
         }
     }
     private Vec3d getMoveInputs(EntityPlayer player) {
         double forward = 0.0D;
         double strafe = 0.0D;
         if (mc.gameSettings.keyBindForward.isKeyDown()) forward++;
         if (mc.gameSettings.keyBindBack.isKeyDown()) forward--;
         if (mc.gameSettings.keyBindLeft.isKeyDown()) strafe++;
         if (mc.gameSettings.keyBindRight.isKeyDown()) strafe--;
         return new Vec3d(strafe, 0.0D, forward);
     }
     private Vec3d toWorld(double x, double z, float yaw) {
         double radians = Math.toRadians(yaw);
         double cos = Math.cos(radians);
         double sin = Math.sin(radians);
         return new Vec3d(x * cos - z * sin, 0.0D, x * sin + z * cos);
     }
     private double getMaxHeight(AxisAlignedBB box) {
         return box.maxY;
     }
     public enum SpeedMode {
         VANILLA,
         STRAFE
             }
 }