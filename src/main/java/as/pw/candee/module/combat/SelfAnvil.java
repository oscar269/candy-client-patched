 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import net.minecraft.entity.Entity;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 public class SelfAnvil extends Module {
     public final Setting<Boolean> packetPlace;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> entityCheck;
     public final Setting<Boolean> crystalOnly;
     public BlockPos basePos;
     public int stage;
     private EnumHand oldhand;
     private int oldslot;
     public SelfAnvil() {
         super("SelfAnvil", Categories.COMBAT, false, false);
         this.packetPlace = register(new Setting("PacketPlace", Boolean.TRUE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.TRUE));
         this.entityCheck = register(new Setting("EntityCheck", Boolean.TRUE));
         this.crystalOnly = register(new Setting("CrystalOnly", Boolean.FALSE, v -> this.entityCheck.getValue()));
         this.oldhand = null;
         this.oldslot = -1;
     }
     public void onEnable() {
         this.basePos = null;
         this.stage = 0;
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         int obby = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         int anvil = InventoryUtil.findHotbarBlock(Blocks.ANVIL);
         if (obby == -1 || anvil == -1) {
             sendMessage("Cannot find materials! disabling");
             disable();
             return;
         }
         BlockPos anvilPos = PlayerUtil.getPlayerPos().add(0, 2, 0);
         if (!BlockUtil.hasNeighbour(anvilPos)) {
             if ((this.basePos = findPos()) == null) {
                 sendMessage("Cannot find space! disabling");
                 disable();
                 return;
             }
             setItem(obby);
             BlockPos pos0 = this.basePos.add(0, 1, 0);
             BlockPos pos2 = this.basePos.add(0, 2, 0);
             BlockUtil.placeBlock(pos0, this.packetPlace.getValue());
             BlockUtil.rightClickBlock(pos0, EnumFacing.UP, this.packetPlace.getValue());
             setItem(anvil);
             EnumFacing facing = null;
             for (EnumFacing f : EnumFacing.values()) {
                 if (pos2.add(f.getDirectionVec()).equals(anvilPos)) {
                     facing = f;
                 }
             }
             BlockUtil.rightClickBlock(anvilPos, facing, this.packetPlace.getValue());
             restoreItem();
             disable();
         } else {
             setItem(anvil);
             BlockUtil.placeBlock(anvilPos, this.packetPlace.getValue());
             restoreItem();
             disable();
         }
     }
     public BlockPos findPos() {
         BlockPos playerPos = PlayerUtil.getPlayerPos();
         BlockPos lookingPos = playerPos.add(BlockUtil.getBackwardFacing(PlayerUtil.getLookingFacing()).getDirectionVec());
         List<BlockPos> possiblePlacePositions = new ArrayList<>();
         BlockPos[] array = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
                 for (BlockPos offset : array) {
             BlockPos pos = playerPos.add(offset);
             if (BlockUtil.getBlock(pos) != Blocks.AIR &&
                 BlockUtil.canRightClickForPlace(pos)) {
                 BlockPos pos2 = pos.add(0, 1, 0);
                 if (entityCheck(pos2)) {
                     BlockPos pos3 = pos2.add(0, 1, 0);
                     if (entityCheck(pos3)) {
                         BlockPos anvil = playerPos.add(0, 2, 0);
                         if (entityCheck(anvil)) {
                             possiblePlacePositions.add(pos);
                         }
                     }
                 }
             }
         }
         return possiblePlacePositions.stream().min(Comparator.comparing(b -> lookingPos.getDistance(b.getX(), b.getY(), b.getZ()))).orElse(null);
     }
     public boolean entityCheck(BlockPos pos) {
         if (!this.entityCheck.getValue()) {
             return true;
         }
         for (Entity e : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
             if (!(e instanceof net.minecraft.entity.item.EntityEnderCrystal) && this.crystalOnly.getValue()) {
                 continue;
             }
             return false;
         }
         return true;
     }
     public void setItem(int slot) {
         if (this.silentSwitch.getValue()) {
             this.oldhand = null;
             if (mc.player.isHandActive()) {
                 this.oldhand = mc.player.getActiveHand();
             }
             this.oldslot = mc.player.inventory.currentItem;
             mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
             mc.player.inventory.currentItem = slot;
             mc.playerController.updateController();
         }
     }
     public void restoreItem() {
         if (this.oldslot != -1 && this.silentSwitch.getValue()) {
             if (this.oldhand != null) {
                 mc.player.setActiveHand(this.oldhand);
             }
             mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
             this.oldslot = -1;
             this.oldhand = null;
         }
     }
 }