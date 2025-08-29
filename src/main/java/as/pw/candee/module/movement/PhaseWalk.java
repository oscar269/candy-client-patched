 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.MovementUtil;
 import as.pw.candee.utils.PlayerUtil;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.util.math.Vec2f;
 import net.minecraft.util.math.Vec3d;
 public class PhaseWalk
     extends Module
 {
     public final Setting<Float> multiplier;
     public final Setting<Float> downSpeed;
     public final Setting<Boolean> shiftLimit;
     public final Setting<Boolean> voidSafe;
     public final Setting<Integer> voidY;
     private boolean sneakedFlag;
     private boolean phaseSuffix;
     private long startTime = 0L;
     public PhaseWalk() {
         super("PhaseWalk", Categories.MOVEMENT, false, false);
         this.multiplier = register(new Setting("SpeedMultiplier", 1.0F, 5.0F, 0.1F));
         this.downSpeed = register(new Setting("DownSpeed", 0.1F, 2.0F, 0.05F));
         this.shiftLimit = register(new Setting("ShiftLimit", Boolean.TRUE));
         this.voidSafe = register(new Setting("VoidSafe", Boolean.TRUE));
         this.voidY = register(new Setting("VoidY", 60, 256, 0, v -> this.voidSafe.getValue()));
     }
     public void onEnable() {
         this.sneakedFlag = false;
         this.phaseSuffix = false;
         this.startTime = 0L;
     }
     public void onDisable() {
         this.phaseSuffix = false;
     }
     public void onTick() {
         EntityPlayerSP player = mc.player;
         if (player == null || mc.world == null)
             return;
         boolean isInside = PlayerUtil.isInsideBlock();
         if (isInside) {
             if (this.startTime == 0L) this.startTime = System.currentTimeMillis();
             this.phaseSuffix = true;
             player.noClip = true;
             player.fallDistance = 0.0F;
             Vec3d input = MovementUtil.getMoveInputs(player);
             double mul = this.multiplier.getValue();
             Vec2f moveVec = MovementUtil.toWorld(new Vec2f((float)input.x, (float)input.z), player.rotationYaw);
             player.motionX = moveVec.x * mul;
             player.motionZ = moveVec.y * mul;
             player.motionY = 0.0D;
             if (player.movementInput.sneak) {
                 if (this.shiftLimit.getValue()) {
                     if (!this.sneakedFlag) {
                         if (this.voidSafe.getValue() && player.posY - this.downSpeed.getValue() <= this.voidY.getValue()) {
                             player.setPosition(player.posX, this.voidY.getValue() + 1.0D, player.posZ);
                         } else {
                             player.setPosition(player.posX, player.posY - this.downSpeed.getValue(), player.posZ);
                         }
                     }
                 } else {
                     player.setPosition(player.posX, player.posY - this.downSpeed.getValue(), player.posZ);
                 }
                 this.sneakedFlag = true;
             } else if (this.sneakedFlag) {
                 this.sneakedFlag = false;
             }
         } else {
             this.phaseSuffix = false;
             player.noClip = false;
             if (this.startTime != 0L) {
                 this.startTime = 0L;
             }
         }
         if (this.voidSafe.getValue() && player.posY <= this.voidY.getValue()) {
             player.setPosition(player.posX, this.voidY.getValue() + 1.0D, player.posZ);
             this.phaseSuffix = true;
         }
     }
 }