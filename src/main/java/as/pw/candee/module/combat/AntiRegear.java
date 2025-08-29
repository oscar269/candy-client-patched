 package as.pw.candee.module.combat;
 import java.awt.Color;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
 public class AntiRegear
     extends Module
 {
     public final Setting<Boolean> outline;
     public final Setting<Integer> red;
     public final Setting<Integer> green;
     public final Setting<Integer> blue;
     public final Setting<Integer> alpha;
     public final Setting<Integer> outlineAlpha;
     public final Setting<Boolean> render;
     public final Setting<Boolean> pickOnly;
     public final Setting<Boolean> enderChest;
     public final Setting<Float> range;
     public final Setting<Float> delay;
     public final Setting<Float> wallRange;
     public final Setting<Float> targetRange;
     public final Setting<RotationMode> rotationMode;
     public final Setting<Boolean> strict;
     private final Timer timer = new Timer();
     private boolean isBreakingBlock = false;
     private BlockPos breakingPos = null;
     public AntiRegear() {
         super("AntiRegear", Categories.COMBAT, false, false);
         this.outline = register(new Setting("Outline", Boolean.TRUE));
         this.red = register(new Setting("Red", 176, 255, 0));
         this.green = register(new Setting("Green", 118, 255, 0));
         this.blue = register(new Setting("Blue", 255, 255, 0));
         this.alpha = register(new Setting("Alpha", 60, 255, 0));
         this.outlineAlpha = register(new Setting("O-Alpha", 255, 255, 0));
         this.render = register(new Setting("Render", Boolean.TRUE));
         this.pickOnly = register(new Setting("Pickaxe Only", Boolean.FALSE));
         this.enderChest = register(new Setting("E-Chest Support", Boolean.FALSE));
         this.range = register(new Setting("Range", 5.0F, 6.0F, 1.0F));
         this.delay = register(new Setting("Delay", 1.0F, 5.0F, 0.0F));
         this.wallRange = register(new Setting("Wall-Range", 3.0F, 6.0F, 0.0F));
         this.targetRange = register(new Setting("Enemy-Range", 5.0F, 12.0F, 0.0F));
         this.rotationMode = register(new Setting("Rotation Mode", RotationMode.Vanilla));
         this.strict = register(new Setting("Strict", Boolean.TRUE));
     }
     public void onRender3D() {
         if (nullCheck()) {
             return;
         }
         if (!this.render.getValue() || !this.isBreakingBlock || this.breakingPos == null)
             return;    Block block = mc.world.getBlockState(this.breakingPos).getBlock();
         if (block instanceof net.minecraft.block.BlockShulkerBox || (this.enderChest.getValue() && block instanceof net.minecraft.block.BlockEnderChest)) {
             Color boxColor = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
             RenderUtil3D.drawBox(this.breakingPos, 1.0D, boxColor, this.alpha.getValue());
             if (this.outline.getValue()) {
                 Color outlineColor = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue());
                 RenderUtil3D.drawBoundingBox(this.breakingPos, 1.0D, 1.0F, outlineColor);
             }
         }
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         if (!this.timer.passedMs((long)(this.delay.getValue() * 1000.0F)))
             return;    if (this.strict.getValue() && !mc.player.onGround)
             return;
         this.isBreakingBlock = false;
         this.breakingPos = null;
         BlockPos playerPos = mc.player.getPosition();
         for (BlockPos pos : BlockPos.getAllInBox(playerPos
                 .add(-this.range.getValue(), -this.range.getValue(), -this.range.getValue()), playerPos
                 .add(this.range.getValue(), this.range.getValue(), this.range.getValue()))) {
             Block block = mc.world.getBlockState(pos).getBlock();
             if (!(block instanceof net.minecraft.block.BlockShulkerBox) && (!this.enderChest.getValue() || !(block instanceof net.minecraft.block.BlockEnderChest)))
                 continue;    if (this.pickOnly.getValue() && !(mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemPickaxe))
                 return;    if ((isBlockBehindWall(pos) && pos.distanceSq(playerPos) > (this.wallRange.getValue() * this.wallRange.getValue())) ||
                 !isEnemyNearby(pos))
                 continue;    faceBlock(pos);
             mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
             mc.player.swingArm(EnumHand.MAIN_HAND);
             this.timer.reset();
             this.isBreakingBlock = true;
             this.breakingPos = pos;
         }
     }
     private boolean isBlockBehindWall(BlockPos pos) {
         Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
         Vec3d posVec = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
         RayTraceResult result = mc.world.rayTraceBlocks(eyesPos, posVec, false, true, false);
         return (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && !result.getBlockPos().equals(pos));
     }
     private boolean isEnemyNearby(BlockPos pos) {
         for (EntityPlayer player : mc.world.playerEntities) {
             if (player != mc.player &&
                 !FriendManager.isFriend(player.getName()) &&
                 player.getDistanceSq(pos) <= (this.targetRange.getValue() * this.targetRange.getValue())) return true;
         }
         return false;
     }
     private void faceBlock(BlockPos pos) {
         Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
         Vec3d posVec = new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
         double diffX = posVec.x - eyesPos.x;
         double diffY = posVec.y - eyesPos.y;
         double diffZ = posVec.z - eyesPos.z;
         double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
         float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
         float pitch = (float)-(Math.atan2(diffY, diffXZ) * 180.0D / Math.PI);
         if (this.rotationMode.getValue() == RotationMode.Vanilla) {
             mc.player.rotationYaw = yaw;
             mc.player.rotationPitch = pitch;
         }
     }
     public enum RotationMode {
         Vanilla
             }
 }