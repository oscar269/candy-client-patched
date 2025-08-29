 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.EntityUtil;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 public class Step
     extends Module
 {
     public final Setting<Integer> height;
     public final Setting<Mode> mode;
     public final Setting<Boolean> pauseBurrow;
     public final Setting<Boolean> pauseSneak;
     private final double[] oneBlockOffset = new double[] { 0.42D, 0.753D }; public final Setting<Boolean> pauseWeb; public final Setting<Boolean> onlyMoving; public final Setting<Boolean> spoof; public final Setting<Integer> delay; public final Setting<Boolean> turnOff;
     private final double[] oneFiveOffset = new double[] { 0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D };
     private final double[] twoOffset = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D };
     private final double[] twoFiveOffset = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
     public Step() {
         super("Step", Categories.MOVEMENT, false, false);
         this.height = register(new Setting("Height", 2, 10, 1));
         this.mode = register(new Setting("Mode", Mode.Vanilla));
         this.pauseBurrow = register(new Setting("PauseBurrow", Boolean.TRUE));
         this.pauseSneak = register(new Setting("PauseSneak", Boolean.TRUE));
         this.pauseWeb = register(new Setting("PauseWeb", Boolean.TRUE));
         this.onlyMoving = register(new Setting("OnlyMoving", Boolean.TRUE));
         this.spoof = register(new Setting("Spoof", Boolean.TRUE));
         this.delay = register(new Setting("Delay", 3, 20, 0));
         this.turnOff = register(new Setting("Disable", Boolean.FALSE));
     }
     public static double[] forward(double speed) {
         float forward = mc.player.movementInput.moveForward;
         float side = mc.player.movementInput.moveStrafe;
         float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
         if (forward != 0.0F) {
             if (side > 0.0F) { yaw += ((forward > 0.0F) ? -45 : 45); }
             else if (side < 0.0F) { yaw += ((forward > 0.0F) ? 45 : -45); }
                side = 0.0F;
             forward = (forward > 0.0F) ? 1.0F : -1.0F;
         }
         double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
         double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
         double posX = forward * speed * cos + side * speed * sin;
         double posZ = forward * speed * sin - side * speed * cos;
         return new double[] { posX, posZ };
     }
     public void onUpdate() {
         if (nullCheck())
             return;
         BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         if ((mc.world.getBlockState(playerPos).getBlock() == Blocks.PISTON_HEAD || mc.world.getBlockState(playerPos).getBlock() == Blocks.OBSIDIAN || mc.world
             .getBlockState(playerPos).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(playerPos).getBlock() == Blocks.BEDROCK) && this.pauseBurrow.getValue()) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if ((mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.PISTON_HEAD || mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.OBSIDIAN || mc.world
             .getBlockState(playerPos.up()).getBlock() == Blocks.ENDER_CHEST || mc.world.getBlockState(playerPos.up()).getBlock() == Blocks.BEDROCK) && this.pauseBurrow.getValue()) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if (this.pauseWeb.getValue() && EntityUtil.isInWeb()) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if (this.pauseSneak.getValue() && mc.player.isSneaking()) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if (this.onlyMoving.getValue() && mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) {
             mc.player.stepHeight = 0.1F;
             return;
         }
         if (this.mode.getValue() == Mode.Normal) {
             mc.player.stepHeight = 0.6F;
             double[] dir = forward(0.1D);
             boolean one = false, onefive = false, two = false, twofive = false;
             AxisAlignedBB bb = mc.player.getEntityBoundingBox();
             if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.0D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 0.6D, dir[1])).isEmpty())
                 one = true;
             if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.4D, dir[1])).isEmpty())
                 onefive = true;
             if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.1D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 1.9D, dir[1])).isEmpty())
                 two = true;
             if (mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, bb.offset(dir[0], 2.4D, dir[1])).isEmpty()) {
                 twofive = true;
             }
             if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
                 if (one && this.height.getValue() >= 1.0F) {
                     for (double off : this.oneBlockOffset)
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ);
                 }
                 if (onefive && this.height.getValue() >= 1.5F) {
                     for (double off : this.oneFiveOffset)
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5D, mc.player.posZ);
                 }
                 if (two && this.height.getValue() >= 2.0F) {
                     for (double off : this.twoOffset)
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0D, mc.player.posZ);
                 }
                 if (twofive && this.height.getValue() >= 2.5F) {
                     for (double off : this.twoFiveOffset)
                         mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + off, mc.player.posZ, mc.player.onGround));
                     mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5D, mc.player.posZ);
                 }
             }
         }
         if (this.mode.getValue() == Mode.Vanilla) {
             mc.player.stepHeight = this.height.getValue();
         }
     }
     public void onDisable() {
         mc.player.stepHeight = 0.6F;
     }
     public enum Mode {
         Vanilla,
         Normal
             }
 }