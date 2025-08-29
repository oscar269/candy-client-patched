 package as.pw.candee.module.combat;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.HoleUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.block.BlockTrapDoor;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 public class AutoSelfFill
     extends Module {
     public final Setting<Float> offset;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> packetPlace;
     public final Setting<Float> detectRange;
     public final Setting<Integer> placeIntervalMs;
     private EnumHand oldhand;
     private int oldslot;
     private final Timer placeTimer = new Timer();
     public AutoSelfFill() {
         super("AutoSelfFill", Categories.COMBAT, false, false);
         this.offset = register(new Setting("Offset", 0.6F, 1.0F, 0.0F));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.TRUE));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.TRUE));
         this.detectRange = register(new Setting("DetectRange", 4.0F, 10.0F, 1.0F));
         this.placeIntervalMs = register(new Setting("PlaceInterval", 200, 1000, 0));
         this.oldhand = null;
         this.oldslot = -1;
     }
     public void onTick() {
         if (nullCheck())
             return;
         BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         boolean inHole = (HoleUtil.isObbyHole(playerPos) || HoleUtil.isBedrockHole(playerPos) || HoleUtil.isSafeHole(playerPos));
         if (!inHole) {
             return;
         }
         EntityPlayer target = mc.world.playerEntities.stream().filter(p -> (p != mc.player)).filter(p -> !FriendManager.isFriend(p)).filter(p -> (mc.player.getDistance(p) <= this.detectRange.getValue())).findFirst().orElse(null);
         if (target == null)
             return;
         int slot = InventoryUtil.findHotbarBlockWithClass(BlockTrapDoor.class);
         if (slot == -1) {
             sendMessage("Cannot find TrapDoor! disabling");
             disable();
             return;
         }
         if (!this.placeTimer.passedMs(this.placeIntervalMs.getValue()))
             return;
         BlockPos trapPos = findPlacePosAround(playerPos);
         if (trapPos == null)
             return;
         setItem(slot);
         double x = mc.player.posX;
         double y = mc.player.posY;
         double z = mc.player.posZ;
         mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + this.offset.getValue(), z, mc.player.onGround));
         EnumFacing face = getFacingTowardPlayer(trapPos, playerPos);
         if (face == null) face = EnumFacing.UP;
         BlockUtil.rightClickBlock(trapPos, face, new Vec3d(0.5D, 0.6D, 0.5D), this.packetPlace.getValue());
         mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
         restoreItem();
         this.placeTimer.reset();
     }
     private BlockPos findPlacePosAround(BlockPos base) {
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         for (BlockPos off : offsets) {
             BlockPos pos = base.add(off);
             if (mc.world.isAirBlock(pos) &&
                 entityClear(pos) &&
                 BlockUtil.canPlaceBlock(pos)) {
                 BlockPos attach = base.add(off.getX(), -1, off.getZ());
                 if (!mc.world.isAirBlock(attach)) return pos;
                 attach = base.add(off.getX(), 1, off.getZ());
                 if (!mc.world.isAirBlock(attach)) return pos;
                 BlockPos sideA = pos.offset((off.getX() == 0) ? EnumFacing.EAST : EnumFacing.NORTH);
                 BlockPos sideB = pos.offset((off.getX() == 0) ? EnumFacing.WEST : EnumFacing.SOUTH);
                 if (!mc.world.isAirBlock(sideA) || !mc.world.isAirBlock(sideB)) return pos;
             }
         }    return null;
     }
     private EnumFacing getFacingTowardPlayer(BlockPos from, BlockPos playerPos) {
         for (EnumFacing f : EnumFacing.HORIZONTALS) {
             if (from.offset(f).equals(playerPos)) return f;
         }
         return null;
     }
     private boolean entityClear(BlockPos pos) {
         AxisAlignedBB box = new AxisAlignedBB(pos);
         return mc.world.getEntitiesWithinAABB(Entity.class, box, e ->
                 (e instanceof net.minecraft.entity.item.EntityEnderCrystal || e instanceof EntityPlayer)).isEmpty();
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
 }