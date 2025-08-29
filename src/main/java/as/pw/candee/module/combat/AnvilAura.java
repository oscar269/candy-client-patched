 package as.pw.candee.module.combat;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.exploit.InstantMine;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.block.BlockAnvil;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 public class AnvilAura
     extends Module
 {
     public final Setting<Float> targetRange;
     public final Setting<Float> placeRange;
     public final Setting<Boolean> rotate;
     public final Setting<Boolean> packet;
     public final Setting<Integer> placeDelay;
     public final Setting<Integer> breakDelay;
     public final Timer placeTimer = new Timer(); public final Setting<Integer> stackHeight; public final Setting<Float> maxTargetSpeed; public final Setting<Boolean> instantBreak; public final Setting<Boolean> packetBreak; public final Setting<Boolean> autoPlace; public final Setting<Boolean> continuousBreak;
     public final Timer breakTimer = new Timer();
     public BlockPos currentTarget = null;
     public BlockPos breakingPos = null;
     public EntityPlayer targetPlayer = null;
     private int oldSlot = -1;
     private boolean isBreaking = false;
     public AnvilAura() {
         super("AnvilAura", Categories.COMBAT, false, false);
         this.targetRange = register(new Setting("Target Range", 5.0F, 0.0F, 10.0F));
         this.placeRange = register(new Setting("Place Range", 5.0F, 0.0F, 10.0F));
         this.rotate = register(new Setting("Rotate", Boolean.TRUE));
         this.packet = register(new Setting("Packet", Boolean.TRUE));
         this.placeDelay = register(new Setting("Place Delay", 100, 0, 1000));
         this.breakDelay = register(new Setting("Break Delay", 0, 0, 500));
         this.stackHeight = register(new Setting("Stack Height", 5, 1, 15));
         this.maxTargetSpeed = register(new Setting("Max Target Speed", 10.0F, 0.0F, 30.0F));
         this.instantBreak = register(new Setting("InstantBreak", Boolean.TRUE));
         this.packetBreak = register(new Setting("PacketBreak", Boolean.TRUE));
         this.autoPlace = register(new Setting("Auto Place", Boolean.TRUE));
         this.continuousBreak = register(new Setting("Continuous Break", Boolean.TRUE));
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         this.targetPlayer = findTarget();
         if (this.targetPlayer == null) {
             reset();
             return;
         }
         BlockPos playerPos = new BlockPos(this.targetPlayer.posX, this.targetPlayer.posY, this.targetPlayer.posZ);
                 BlockPos headPos = playerPos.up(2);
         if (this.autoPlace.getValue() && this.placeTimer.passedMs(this.placeDelay.getValue())) {
             placeAnvilStack(headPos);
         }
         if (this.continuousBreak.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue())) {
             breakFeetAnvil(playerPos);
         }
     }
     private EntityPlayer findTarget() {
         EntityPlayer bestTarget = null;
         double bestDistance = Double.MAX_VALUE;
         for (EntityPlayer player : mc.world.playerEntities) {
             if (player == mc.player ||
                 player.isDead || player.getHealth() <= 0.0F ||
                 FriendManager.isFriend(player))
                 continue;
             double distance = PlayerUtil.getDistance(player);
             if (distance > this.targetRange.getValue() ||
                 getSpeed(player) > this.maxTargetSpeed.getValue())
                 continue;
             if (distance < bestDistance) {
                 bestDistance = distance;
                 bestTarget = player;
             }
         }
         return bestTarget;
     }
     private void placeAnvilStack(BlockPos headPos) {
         int anvilSlot = InventoryUtil.findHotbarBlockWithClass(BlockAnvil.class);
         if (anvilSlot == -1)
             return;
         int obsidianSlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (obsidianSlot != -1) {
             placeSupportBlocks(headPos, obsidianSlot);
         }
         boolean needsPlacement = false; int i;
         for (i = 0; i < this.stackHeight.getValue(); i++) {
             BlockPos checkPos = headPos.up(i);
             if (mc.world.isAirBlock(checkPos) && canPlace(checkPos)) {
                 needsPlacement = true;
                 break;
             }
         }
         if (!needsPlacement)
             return;
         this.oldSlot = mc.player.inventory.currentItem;
         InventoryUtil.switchToHotbarSlot(anvilSlot, false);
         for (i = 0; i < this.stackHeight.getValue(); i++) {
             BlockPos placePos = headPos.up(i);
             if (mc.world.isAirBlock(placePos) && canPlace(placePos)) {
                 BlockUtil.placeBlock(placePos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                 this.placeTimer.reset();
                 break;
             }
         }
         if (this.oldSlot != -1) {
             InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
         }
     }
     private void placeSupportBlocks(BlockPos basePos, int slot) {
         this.oldSlot = mc.player.inventory.currentItem;
         InventoryUtil.switchToHotbarSlot(slot, false);
         if (!mc.world.isAirBlock(basePos) || canPlace(basePos)) {
             if (this.oldSlot != -1) {
                 InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
             }
             return;
         }
         BlockPos feetPos = new BlockPos(this.targetPlayer.posX, this.targetPlayer.posY, this.targetPlayer.posZ);
         int maxHeight = feetPos.getY() + 2;
         BlockPos[] horizontalOffsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         BlockPos farthestOffset = null;
         double farthestDistance = -1.0D;
         for (BlockPos offset : horizontalOffsets) {
             BlockPos posToCheck = feetPos.add(offset);
             double distance = mc.player.getDistance(posToCheck.getX() + 0.5D, posToCheck.getY(), posToCheck.getZ() + 0.5D);
             if (distance > farthestDistance) {
                 farthestDistance = distance;
                 farthestOffset = offset;
             }
         }
         if (farthestOffset == null) {
             if (this.oldSlot != -1) {
                 InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
             }
             return;
         }
         BlockPos supportPos = basePos.add(farthestOffset).down(1);
         for (int i = 0; i < this.stackHeight.getValue(); i++) {
             BlockPos placePos = supportPos.up(i);
             if (placePos.getY() > maxHeight)
                 break;
             if (mc.world.isAirBlock(placePos) && canPlace(placePos)) {
                 BlockUtil.placeBlock(placePos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                 this.placeTimer.reset();
                 break;
             }
         }
         if (this.oldSlot != -1) {
             InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
         }
     }
     private void breakFeetAnvil(BlockPos feetPos) {
         if (!(BlockUtil.getBlock(feetPos) instanceof BlockAnvil)) {
             return;
         }
         boolean hasAnvilsAbove = false;
         for (int i = 1; i <= this.stackHeight.getValue(); i++) {
             if (BlockUtil.getBlock(feetPos.up(i)) instanceof BlockAnvil) {
                 hasAnvilsAbove = true;
                 break;
             }
         }
         if (!hasAnvilsAbove) {
             return;
         }
         this.breakingPos = feetPos;
         int pickaxeSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
         if (pickaxeSlot == -1)
             return;
         int currentSlot = mc.player.inventory.currentItem;
         InventoryUtil.switchToHotbarSlot(pickaxeSlot, false);
         try {
             Module instantMineModule = CandeePlusRewrite.m_module.getModuleWithClass(InstantMine.class);
             if (this.instantBreak.getValue() && instantMineModule != null) {
                 if (!this.isBreaking) {
                     mc.player.swingArm(EnumHand.MAIN_HAND);
                     instantMineModule.enable();
                     InstantMine.startBreak(feetPos, EnumFacing.DOWN);
                     this.isBreaking = true;
                 }
                 if (mc.world.isAirBlock(feetPos)) {
                     this.isBreaking = false;
                     InstantMine.resetMining();
                 }
             } else {
                 mc.player.swingArm(EnumHand.MAIN_HAND);
                 if (this.packetBreak.getValue()) {
                     mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
                     mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
                 }
                 else {
                     mc.playerController.clickBlock(feetPos, EnumFacing.DOWN);
                 }
             }
             this.breakTimer.reset();
         }
         catch (Exception e) {
             mc.player.swingArm(EnumHand.MAIN_HAND);
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, feetPos, EnumFacing.DOWN));
         }
         InventoryUtil.switchToHotbarSlot(currentSlot, false);
     }
     private boolean canPlace(BlockPos pos) {
         if (!BlockUtil.canPlaceBlock(pos)) return false;
         return (mc.player.getDistance(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D) <= this.placeRange.getValue());
     }
     private double getSpeed(EntityPlayer player) {
         double dx = player.posX - player.prevPosX;
         double dz = player.posZ - player.prevPosZ;
         return Math.sqrt(dx * dx + dz * dz) * 20.0D;
     }
     private void reset() {
         this.currentTarget = null;
         this.breakingPos = null;
         this.targetPlayer = null;
         this.isBreaking = false;
         try {
             InstantMine.resetMining();
         } catch (Exception ignored) {}
     }
     public void onDisable() {
         reset();
         if (this.oldSlot != -1) {
             InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
             this.oldSlot = -1;
         }
     }
 }