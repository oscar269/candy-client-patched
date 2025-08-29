 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.MathUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.math.BlockPos;
 public class Flight extends Module {
     public final Setting<FlyMode> mode;
     public final Setting<Float> speed;
     public final Setting<Boolean> viewBob;
     public final Setting<Float> bobAmount;
     public final Setting<Boolean> damageBoost;
     public final Setting<Integer> jpSpeed;
     public final Setting<Integer> teleportTime;
     private double startY;
     private boolean onDamage = false;
     private final Timer timer = new Timer();
     public Flight() {
         super("Flight", Categories.MOVEMENT, false, false);
         this.mode = register(new Setting("Mode", FlyMode.CREATIVE));
         this.speed = register(new Setting("Speed", 0.1F, 5.0F, 0.0F, v -> (this.mode.getValue() == FlyMode.CREATIVE)));
         this.viewBob = register(new Setting("ViewBob", Boolean.FALSE, v -> (this.mode.getValue() == FlyMode.CREATIVE)));
         this.bobAmount = register(new Setting("BobAmount", 0.1F, 2.0F, 0.0F, v -> (this.viewBob.getValue() && this.mode.getValue() == FlyMode.CREATIVE)));
         this.damageBoost = register(new Setting("DamageBoost", Boolean.FALSE, v -> (this.mode.getValue() == FlyMode.CREATIVE)));
         this.jpSpeed = register(new Setting("TeleportAmount", 5, 20, 0, v -> (this.mode.getValue() == FlyMode._2B2TJB)));
         this.teleportTime = register(new Setting("TeleportTime", 750, 5000, 0, v -> (this.mode.getValue() == FlyMode._2B2TJB)));
     }
     public void onEnable() {
         if (mc.player == null)
             return;
         super.onEnable();
         BlockPos startPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         this.startY = mc.player.posY;
         this.onDamage = false;
         if (this.mode.getValue() == FlyMode.CREATIVE && this.damageBoost.getValue()) {
             if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 3.0001D, 0.0D)).isEmpty()) {
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 3.0001D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
             }
             mc.player.setPosition(mc.player.posX, mc.player.posY + 0.42D, mc.player.posZ);
             this.onDamage = true;
         }
     } public void onTick() {
         double[] mv;
         double yaw;
         double pitch;
         if (mc.player == null || mc.world == null)
             return;
         switch (this.mode.getValue()) {
             case CREATIVE:
                 if (this.damageBoost.getValue() && !this.onDamage)
                     break;    mc.player.setVelocity(0.0D, 0.0D, 0.0D);
                 mc.player.jumpMovementFactor = this.speed.getValue();
                 mv = MathUtil.directionSpeed(this.speed.getValue());
                 if (mc.player.movementInput.moveStrafe != 0.0F || mc.player.movementInput.moveForward != 0.0F) {
                     if (this.viewBob.getValue()) mc.player.cameraYaw = this.bobAmount.getValue() / 10.0F;
                     mc.player.motionX = mv[0];
                     mc.player.motionZ = mv[1];
                 }
                 if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY += this.speed.getValue();
                 if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.player.motionY -= this.speed.getValue();
                 break;
             case JUMP:
                 if (mc.player.posY < this.startY) mc.player.jump();
                 break;
             case _2B2TJB:
                 yaw = Math.cos(Math.toRadians((mc.player.rotationYaw + 90.0F)));
                 pitch = Math.sin(Math.toRadians((mc.player.rotationYaw + 90.0F)));
                 mc.player.setPosition(mc.player.posX + this.jpSpeed.getValue() * yaw, mc.player.posY, mc.player.posZ + this.jpSpeed
.getValue() * pitch);
                 if (this.timer.passedMs(this.teleportTime.getValue())) {
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 30.0D, mc.player.posZ);
                     this.timer.reset();
                 }
                 mc.player.motionY = 0.0D;
                 break;
         }
     }
     public void onDisable() {
         super.onDisable();
         mc.player.motionX = 0.0D;
         mc.player.motionY = 0.0D;
         mc.player.motionZ = 0.0D;
     }
     public enum FlyMode
     {
         CREATIVE,
         JUMP,
         _2B2TJB
             }
 }