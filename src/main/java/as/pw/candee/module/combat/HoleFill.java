 package as.pw.candee.module.combat;
 import java.util.List;
 import java.util.stream.Collectors;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 public class HoleFill
     extends Module {
     public final Setting<Float> range;
     public final Setting<Integer> place;
     public final Setting<Boolean> toggle;
     public final Setting<Boolean> packetPlace;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> autoMode;
     public final Setting<Float> detectRange;
     public final Setting<Integer> placeIntervalMs;
     private EnumHand oldhand;
     private int oldslot;
     private final Timer placeTimer = new Timer();
     public HoleFill() {
         super("HoleFill", Categories.COMBAT, false, false);
         this.range = register(new Setting("Range", 6.0F, 12.0F, 1.0F));
         this.place = register(new Setting("Place", 2, 10, 1));
         this.toggle = register(new Setting("Toggle", Boolean.FALSE));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.FALSE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.FALSE));
         this.autoMode = register(new Setting("AutoMode", Boolean.FALSE));
         this.detectRange = register(new Setting("DetectRange", 4.0F, 10.0F, 1.0F));
         this.placeIntervalMs = register(new Setting("PlaceInterval", 75, 300, 0));
         this.oldhand = null;
         this.oldslot = -1;
     }
     public void onTick() {
         List<BlockPos> holes;
         if (nullCheck())
             return;
         int slot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (slot == -1) {
             restoreItem();
             if (this.toggle.getValue()) disable();
             return;
         }
         if (this.autoMode.getValue()) {
             holes = CandeePlusRewrite.m_hole.getHoles().stream().filter(h -> (PlayerUtil.getDistance(h) < this.range.getValue())).filter(HoleFill::isAnyEntityInBlock).filter(this::isPlaceable).filter(h -> !isFriendNear(h, this.detectRange.getValue())).filter(h -> { for (EntityPlayer p : mc.world.playerEntities) { if (p == mc.player || FriendManager.isFriend(p)) continue;    double dx = h.getX() + 0.5D; double dy = h.getY() + 0.5D; double dz = h.getZ() + 0.5D; if (p.getDistance(dx, dy, dz) < this.detectRange.getValue()) return true;    }    return false; }).collect(Collectors.toList());
         }
         else {
             holes = CandeePlusRewrite.m_hole.getHoles().stream().filter(h -> (PlayerUtil.getDistance(h) < this.range.getValue())).filter(HoleFill::isAnyEntityInBlock).filter(this::isPlaceable).filter(h -> !isFriendNear(h, this.detectRange.getValue())).collect(Collectors.toList());
         }
         int counter = 0;
         if (!holes.isEmpty()) setItem(slot);
         for (BlockPos hole : holes) {
             if (counter >= this.place.getValue())
                 break;    if (!this.placeTimer.passedMs(this.placeIntervalMs.getValue()) ||
                 !isPlaceable(hole) ||
                 isFriendNear(hole, this.detectRange.getValue()))
                 continue;    BlockUtil.placeBlock(hole, this.packetPlace.getValue());
             this.placeTimer.reset();
             counter++;
         }
         if (!this.autoMode.getValue() && holes.isEmpty() && this.toggle.getValue()) {
             restoreItem();
             disable();
             return;
         }
         restoreItem();
     }
     public boolean isPlaceable(BlockPos pos) {
         if (!mc.world.isAirBlock(pos) && !mc.world.getBlockState(pos).getMaterial().isReplaceable()) return false;
        return isAnyEntityInBlock(pos);
             }
     public static boolean isAnyEntityInBlock(BlockPos pos) {
         AxisAlignedBB box = new AxisAlignedBB(pos);
         List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(null, box);
         for (Entity e : list) {
             if ((!(e instanceof EntityPlayer) || e != mc.player) &&
                 !e.isDead && e.getEntityBoundingBox().intersects(box)) return false;
         }
         return true;
     }
     private boolean isFriendNear(BlockPos pos, double range) {
         double cx = pos.getX() + 0.5D;
         double cy = pos.getY() + 0.5D;
         double cz = pos.getZ() + 0.5D;
         for (EntityPlayer p : mc.world.playerEntities) {
             if (p != mc.player &&
                 FriendManager.isFriend(p) &&
                 p.getDistance(cx, cy, cz) <= range) return true;
         }
         return false;
     }
     public void setItem(int slot) {
         if (slot < 0 || slot > 8)
             return;    if (this.silentSwitch.getValue()) {
             if (mc.player.inventory.currentItem == slot)
                 return;    this.oldhand = null;
             if (mc.player.isHandActive()) this.oldhand = mc.player.getActiveHand();
             if (this.oldslot == -1) this.oldslot = mc.player.inventory.currentItem;
             mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
             if (mc.player.inventory.currentItem == slot)
                 return;    mc.player.inventory.currentItem = slot;
             mc.playerController.updateController();
         }
     }
     public void restoreItem() {
         if (!this.silentSwitch.getValue())
             return;    if (this.oldslot != -1) {
             mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
             this.oldslot = -1;
         }
         if (this.oldhand != null) {
             mc.player.setActiveHand(this.oldhand);
             this.oldhand = null;
         }
     }
 }