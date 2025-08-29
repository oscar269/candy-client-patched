 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 public class AutoPush
     extends Module
 {
     public final Setting<Float> preDelay;
     public final Setting<Float> placeDelay;
     public final Setting<Boolean> packetPlace;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Float> range;
     public final Setting<RedStone> redStoneType;
     public final Setting<Target> targetType;
     public final Setting<Float> targetRange;
     public EntityPlayer target;
     public int pistonSlot;
     public AutoPush() {
         super("AutoPush", Categories.COMBAT, false, false);
         this.preDelay = register(new Setting("BlockDelay", 0.0F, 25.0F, 0.0F));
         this.placeDelay = register(new Setting("PlaceDelay", 0.0F, 25.0F, 0.0F));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.FALSE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.FALSE));
         this.range = register(new Setting("Range", 10.0F, 20.0F, 1.0F));
         this.redStoneType = register(new Setting("Redstone", RedStone.Both));
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.target = null;
         this.obbySlot = -1;
         this.redstonePos = null;
         this.stage = 0;
         this.timer = null;
         this.oldslot = -1;
         this.oldhand = null;
         this.isTorch = false;
     }
     public int redstoneSlot; public int obbySlot; public BlockPos pistonPos; public BlockPos redstonePos; public int stage; public Timer preTimer; public Timer timer; public int oldslot; public EnumHand oldhand; public boolean isTorch;
     public void reset() {
         this.target = null;
         this.pistonSlot = -1;
         this.redstoneSlot = -1;
         this.obbySlot = -1;
         this.pistonPos = null;
         this.redstonePos = null;
         this.stage = 0;
         this.preTimer = null;
         this.timer = null;
         this.oldslot = -1;
         this.oldhand = null;
         this.isTorch = false;
     }
     public void onEnable() {
         reset();
     }
     public void onTick() {
         if (nullCheck())
             return;    if (!findMaterials()) {
             sendMessage("Cannot find materials! disabling...");
             disable();
             return;
         }
         this.target = findTarget();
         if (this.target == null) {
             sendMessage("Cannot find target! disabling...");
             disable();
             return;
         }
         if ((isNull(this.pistonPos) || isNull(this.redstonePos)) && !findSpace(this.target)) {
             sendMessage("Cannot find space! disabling...");
             disable();
             return;
         }
         if (this.preTimer == null) this.preTimer = new Timer();
         if (this.preTimer.passedX(this.preDelay.getValue()) && !prepareBlock()) {
             restoreItem();
             return;
         }
         if (this.timer == null) this.timer = new Timer();
         if (this.stage == 0 && this.timer.passedX(this.placeDelay.getValue())) {
             setItem(this.pistonSlot);
             Vec3d targetCenter = new Vec3d(this.target.posX, this.target.posY, this.target.posZ);
             float[] rotation = calculatePistonRotation(this.pistonPos, targetCenter);
             mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotation[0], rotation[1], mc.player.onGround));
             BlockUtil.placeBlock(this.pistonPos, this.packetPlace.getValue());
             this.stage = 1;
             this.timer.reset();
         }
         if (this.stage == 1 && this.timer.passedX(this.placeDelay.getValue())) {
             setItem(this.redstoneSlot);
             BlockUtil.placeBlock(this.redstonePos, this.packetPlace.getValue());
             this.stage = 2;
             disable();
             reset();
         }
         restoreItem();
     }
     public void setItem(int slot) {
         if (this.silentSwitch.getValue()) {
             this.oldhand = null;
             if (mc.player.isHandActive()) this.oldhand = mc.player.getActiveHand();
             this.oldslot = mc.player.inventory.currentItem;
             mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
             mc.player.inventory.currentItem = slot;
             mc.playerController.updateController();
         }
     }
     public void restoreItem() {
         if (this.oldslot != -1 && this.silentSwitch.getValue()) {
             if (this.oldhand != null) mc.player.setActiveHand(this.oldhand);
             mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
             this.oldslot = -1;
             this.oldhand = null;
         }
     }
     public boolean isNull(Object object) {
         return (object == null);
     }
     public boolean findSpace(EntityPlayer target) {
         BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         List<AutoPushPos> poses = new ArrayList<>();
         for (BlockPos offset : offsets) {
             AutoPushPos pos = new AutoPushPos();
             BlockPos base = targetPos.add(offset);
             if (BlockUtil.getBlock(base) != Blocks.AIR) {
                 BlockPos pistonPos = base.add(0, 1, 0);
                 if (BlockUtil.getBlock(pistonPos) == Blocks.AIR &&
                     !checkPos(pistonPos) && (
                     PlayerUtil.getDistance(pistonPos) >= 3.6D || pistonPos.getY() <= mypos.getY() + 1) &&
                     BlockUtil.getBlock(targetPos.add(offset.getX() * -1, 1, offset.getZ() * -1)) == Blocks.AIR) {
                     List<BlockPos> redstonePoses = new ArrayList<>();
                     List<BlockPos> roffsets = getBlockPos();
                     for (BlockPos roffset : roffsets) {
                         BlockPos redstonePos = pistonPos.add(roffset);
                         if ((redstonePos.getX() == targetPos.getX() && redstonePos.getZ() == targetPos.getZ()) ||
                             checkPos(redstonePos) ||
                             BlockUtil.getBlock(redstonePos) != Blocks.AIR)
                             continue;    redstonePoses.add(redstonePos);
                     }
                     BlockPos redstonePos2 = redstonePoses.stream().min(Comparator.comparing(b -> mc.player.getDistance(b.getX(), b.getY(), b.getZ()))).orElse(null);
                     if (redstonePos2 != null) {
                         pos.setPiston(pistonPos);
                         pos.setRedStone(redstonePos2);
                         poses.add(pos);
                     }
                 }
             }
         }
         AutoPushPos bestPos = poses.stream().filter(p -> (p.getMaxRange() <= this.range.getValue())).min(Comparator.comparing(AutoPushPos::getMaxRange)).orElse(null);
         if (bestPos != null) {
             this.pistonPos = bestPos.piston;
             this.redstonePos = bestPos.redstone;
             return true;
         }
         return false;
     }
     private List<BlockPos> getBlockPos() {
         List<BlockPos> roffsets = new ArrayList<>();
         roffsets.add(new BlockPos(1, 0, 0));
         roffsets.add(new BlockPos(-1, 0, 0));
         roffsets.add(new BlockPos(0, 0, 1));
         roffsets.add(new BlockPos(0, 0, -1));
         if (this.redStoneType.getValue() == RedStone.Block) roffsets.add(new BlockPos(0, 1, 0));
         return roffsets;
     }
     public EntityPlayer findTarget() {
         EntityPlayer target = null;
         List<EntityPlayer> players = mc.world.playerEntities;
         if (this.targetType.getValue() == Target.Nearest) {
             target = PlayerUtil.getNearestPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Looking) {
             target = PlayerUtil.getLookingPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Best)
         {
             target = players.stream().filter(p -> (p.getEntityId() != mc.player.getEntityId())).filter(p -> !FriendManager.isFriend(p.getName())).filter(this::findSpace).min(Comparator.comparing(PlayerUtil::getDistance)).orElse(null);
         }
         if (target != null && FriendManager.isFriend(target.getName())) return null;
         return target;
     }
     public boolean findMaterials() {
         this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.PISTON);
         int redstoneBlock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
         int redstoneTorch = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_TORCH);
         this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (itemCheck(this.pistonSlot)) this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.STICKY_PISTON);
         if (this.redStoneType.getValue() == RedStone.Block) {
             this.isTorch = false;
             this.redstoneSlot = redstoneBlock;
         }
         if (this.redStoneType.getValue() == RedStone.Torch) {
             this.isTorch = true;
             this.redstoneSlot = redstoneTorch;
         }
         if (this.redStoneType.getValue() == RedStone.Both) {
             this.isTorch = true;
             this.redstoneSlot = redstoneTorch;
             if (itemCheck(this.redstoneSlot)) {
                 this.isTorch = false;
                 this.redstoneSlot = redstoneBlock;
             }
         }
         return (!itemCheck(this.redstoneSlot) && !itemCheck(this.pistonSlot) && !itemCheck(this.obbySlot));
     }
     public boolean checkPos(BlockPos pos) {
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         return (pos.getX() == mypos.getX() && pos.getZ() == mypos.getZ());
     }
     public boolean itemCheck(int slot) {
         return (slot == -1);
     }
     public boolean prepareBlock() {
         BlockPos piston = this.pistonPos.add(0, -1, 0);
         BlockPos redstone = this.redstonePos.add(0, -1, 0);
         if (BlockUtil.getBlock(piston) == Blocks.AIR) {
             setItem(this.obbySlot);
             BlockUtil.placeBlock(piston, this.packetPlace.getValue());
             if (delayCheck()) return false;
         }
         if (BlockUtil.getBlock(redstone) == Blocks.AIR) {
             setItem(this.obbySlot);
             BlockUtil.placeBlock(redstone, this.packetPlace.getValue());
             return !delayCheck();
         }
         return true;
     }
     public float[] calculatePistonRotation(BlockPos pistonPos, Vec3d targetPos) {
         double deltaX = targetPos.x - pistonPos.getX() + 0.5D;
         double deltaY = targetPos.y - pistonPos.getY() + 0.5D;
         double deltaZ = targetPos.z - pistonPos.getZ() + 0.5D;
         double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
         float yaw = (float)Math.toDegrees(Math.atan2(-deltaX, deltaZ));
         float pitch = (float)Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));
         pitch = Math.max(-45.0F, Math.min(45.0F, pitch));
         return new float[] { yaw, pitch };
     }
     public boolean delayCheck() {
         return (this.preDelay.getValue() != 0.0F);
     }
     public enum Target {
         Nearest,
         Looking,
         Best
             }
     public enum RedStone {
         Block,
         Torch,
         Both
}
     public static class AutoPushPos {
         public BlockPos piston;
         public BlockPos redstone;
         public double getMaxRange() {
             if (this.piston == null || this.redstone == null) return 999999.0D;
             return Math.max(PlayerUtil.getDistance(this.piston), PlayerUtil.getDistance(this.redstone));
         }
         public void setPiston(BlockPos piston) {
             this.piston = piston;
         }
         public void setRedStone(BlockPos redstone) {
             this.redstone = redstone;
         }
     }
 }