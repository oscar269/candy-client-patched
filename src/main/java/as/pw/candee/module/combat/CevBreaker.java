 package as.pw.candee.module.combat;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.exploit.InstantMine;
 import as.pw.candee.module.exploit.SilentPickel;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.CrystalUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
 import net.minecraft.network.play.client.CPacketUseEntity;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;

 public class CevBreaker
     extends Module
 {
     private Integer lockedTargetId = null;
     private BlockPos lockedHead = null;
     private int lockTicks = 0;
     public CevBreaker() {
         super("CevBreaker", Categories.COMBAT, false, false);
         this.preDelay = register(new Setting("BlockDelay", 0.0F, 20.0F, 0.0F));
         this.crystalDelay = register(new Setting("CrystalDelay", 0.0F, 20.0F, 0.0F));
         this.breakDelay = register(new Setting("BreakDelay", 0.0F, 20.0F, 0.0F));
         this.attackDelay = register(new Setting("AttackDelay", 3.0F, 20.0F, 0.0F));
         this.endDelay = register(new Setting("EndDelay", 0.0F, 20.0F, 0.0F));
         this.range = register(new Setting("Range", 10.0F, 20.0F, 1.0F));
         this.tick = register(new Setting("Tick", Boolean.TRUE));
         this.toggle = register(new Setting("Toggle", Boolean.TRUE));
         this.noSwingBlock = register(new Setting("NoSwingBlock", Boolean.TRUE));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.FALSE));
         this.packetCrystal = register(new Setting("PacketCrystal", Boolean.TRUE));
         this.instantBreak = register(new Setting("InstantBreak", Boolean.FALSE));
         this.toggleSilentPickel = register(new Setting("ToggleSilentPickel", Boolean.FALSE));
         this.packetBreak = register(new Setting("PacketBreak", Boolean.TRUE));
         this.offHandBreak = register(new Setting("OffhandBreak", Boolean.TRUE));
         this.skip = register(new Setting("Skip", Boolean.FALSE));
         this.breakAttempts = register(new Setting("BreakAttempts", 7, 20, 1));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.blockColor = register(new Setting("Color", new Color(250, 0, 200, 50)));
         this.outline = register(new Setting("Outline", Boolean.FALSE));
         this.width = register(new Setting("Width", 2.0F, 5.0F, 0.1F, v -> this.outline.getValue()));
         this.old = null;
         this.done = false;
         this.based = false;
         this.pickelSlot = -1;
         this.attempts = 0;
         this.endTimer = null;
     }
     public void onEnable() {
         reset();
         if (this.toggleSilentPickel.getValue()) setToggleSilentPickel(true);
     }
     public void onDisable() {
         if (this.toggleSilentPickel.getValue()) setToggleSilentPickel(false);
     }
     public void setToggleSilentPickel(boolean toggle) {
         Module silent = CandeePlusRewrite.m_module.getModuleWithClass(SilentPickel.class);
         if (toggle) { silent.enable(); } else { silent.disable(); }
     }
     public void onTick() {
         if (this.tick.getValue()) doCB();
     }
     public void onUpdate() {
         if (!this.tick.getValue()) doCB();
     }
     public void doCB() {
         if (nullCheck())
             return;    try {
             if (!findMaterials()) {
                 if (this.toggle.getValue()) {
                     sendMessage("Cannot find materials! disabling...");
                     disable();
                 }
                 return;
             }
             if (this.lockedTargetId == null) {
                 target = findTarget();
                 if (isNull(target)) {
                     if (this.toggle.getValue()) {
                         sendMessage("Cannot find target! disabling...");
                         disable();
                     }
                     return;
                 }
                 if (FriendManager.isFriend(target.getName())) {
                     reset();
                     return;
                 }
                 this.lockedTargetId = target.getEntityId();
                 this.lockTicks = 20;
             } else {
                 Entity e = mc.world.getEntityByID(this.lockedTargetId);
                 target = (e instanceof EntityPlayer) ? (EntityPlayer)e : null;
                 if (isNull(target) || FriendManager.isFriend(target.getName())) {
                     reset();
                     return;
                 }
             }
             if (isNull(this.base) && !findSpace(target)) {
                 if (this.toggle.getValue()) {
                     sendMessage("Cannot find space! disabling...");
                     disable();
                 }
                 return;
             }
             BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
             BlockPos headPos = targetPos.add(0, 2, 0);
             if (this.lockedHead == null)
             { this.lockedHead = headPos;
                 this.lockTicks = 20; }
             else if (this.lockTicks > 0) { this.lockTicks--; }
             if (this.headMode) {
                 this.builtTrap = true;
                 tryPlaceCrystalImmediate(this.lockedHead);
             }
             if (this.blockTimer == null) this.blockTimer = new Timer();
             if (!this.builtTrap) {
                 if (BlockUtil.getBlock(this.base) == Blocks.AIR && !this.based && this.blockTimer.passedX(this.preDelay.getValue())) {
                     setItem(this.obbySlot);
                     placeBlock(this.base, Boolean.FALSE);
                     this.blockTimer.reset();
                 }
                 if (BlockUtil.getBlock(this.base.add(0, 1, 0)) == Blocks.AIR && !this.based && this.blockTimer.passedX(this.preDelay.getValue())) {
                     setItem(this.obbySlot);
                     placeBlock(this.base.add(0, 1, 0), Boolean.FALSE);
                     this.blockTimer.reset();
                 }
                 if (BlockUtil.getBlock(this.lockedHead) == Blocks.AIR && this.blockTimer.passedX(this.preDelay.getValue())) {
                     setItem(this.obbySlot);
                     placeBlock(this.lockedHead, this.packetPlace.getValue());
                     this.blockTimer = null;
                     this.builtTrap = true;
                     tryPlaceCrystalImmediate(this.lockedHead);
                 }
             }
             if (this.builtTrap && !this.base.equals(this.old) && this.skip.getValue()) this.placedCrystal = true;
             if (this.crystalTimer == null && this.builtTrap) this.crystalTimer = new Timer();
             if (this.builtTrap && !this.placedCrystal && this.crystalTimer.passedX(this.crystalDelay.getValue())) {
                 if (CrystalUtil.canPlaceCrystal(this.lockedHead)) {
                     if (this.crystalSlot != 999) setItem(this.crystalSlot);
                     EnumHand hand = (this.crystalSlot != 999) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                     mc.playerController.updateController();
                     mc.rightClickDelayTimer = 0;
                     if (this.packetCrystal.getValue()) {
                         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.lockedHead, EnumFacing.UP, hand, 0.5F, 1.0F, 0.5F));
                     } else {
                         mc.playerController.processRightClickBlock(mc.player, mc.world, this.lockedHead, EnumFacing.UP, new Vec3d(0.5D, 1.0D, 0.5D), hand);
                     }
                     if (!this.noSwingBlock.getValue()) mc.player.swingArm(hand);
                     this.placedCrystal = true;
                     this.crystalTimer.reset();
                 } else {
                     this.crystalTimer.reset();
                 }
             }
             if (this.breakTimer == null && this.placedCrystal) this.breakTimer = new Timer();
             if (this.placedCrystal && !this.brokeBlock && this.breakTimer.passedX(this.breakDelay.getValue())) {
                 setItem(this.pickelSlot);
                 if (BlockUtil.getBlock(this.lockedHead) == Blocks.AIR) {
                     this.brokeBlock = true;
                 }
                 if (!breaking) {
                     if (!this.instantBreak.getValue()) {
                         if (!this.noSwingBlock.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
                         mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.lockedHead, EnumFacing.DOWN));
                         mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.lockedHead, EnumFacing.DOWN));
                     } else {
                         if (!this.noSwingBlock.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
                         CandeePlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
                         InstantMine.startBreak(this.lockedHead, EnumFacing.DOWN);
                     }
                     breaking = true;
                 }
             }
             if (this.brokeBlock && !this.base.equals(this.old) && this.skip.getValue()) this.attackedCrystal = true;
             if (this.attackTimer == null && this.brokeBlock) this.attackTimer = new Timer();
             if (this.brokeBlock && !this.attackedCrystal) {
                 breaking = false;
                 if (this.attackTimer.passedX(this.attackDelay.getValue())) {
                     BlockPos plannedCrystalPos = this.lockedHead.add(0, 1, 0);
                     Entity crystal = mc.world.loadedEntityList.stream().filter(e -> e instanceof net.minecraft.entity.item.EntityEnderCrystal).filter(e -> (e.getDistance(plannedCrystalPos.getX() + 0.5D, plannedCrystalPos.getY(), plannedCrystalPos.getZ() + 0.5D) < 2.0D)).min(Comparator.comparing(c -> c.getDistance(target))).orElse(null);
                     if (crystal == null) {
                         if (this.attempts >= this.breakAttempts.getValue()) {
                             this.attackedCrystal = true;
                             this.attempts = 0;
                         } else {
                             this.attempts++;
                             this.attackTimer.reset();
                         }
                         return;
                     }
                     EnumHand hand2 = this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                     if (this.packetBreak.getValue()) {
                         mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                     } else {
                         mc.playerController.attackEntity(mc.player, crystal);
                         mc.player.swingArm(hand2);
                     }
                     this.attackedCrystal = true;
                     this.attempts = 0;
                 }
             }
             if (this.endTimer == null && this.attackedCrystal) this.endTimer = new Timer();
             if (this.attackedCrystal && !this.done && this.endTimer.passedX(this.endDelay.getValue())) {
                 this.done = true;
                 this.old = new BlockPos(this.base.getX(), this.base.getY(), this.base.getZ());
                 reset();
             }
             restoreItem();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     }
     private void tryPlaceCrystalImmediate(BlockPos headPos) {
         if (this.placedCrystal)
             return;    if (!CrystalUtil.canPlaceCrystal(headPos))
             return;    if (this.crystalSlot != 999) setItem(this.crystalSlot);
         EnumHand hand = (this.crystalSlot != 999) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
         mc.playerController.updateController();
         mc.rightClickDelayTimer = 0;
         if (this.packetCrystal.getValue()) {
             mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(headPos, EnumFacing.UP, hand, 0.5F, 1.0F, 0.5F));
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, headPos, EnumFacing.UP, new Vec3d(0.5D, 1.0D, 0.5D), hand);
         }
         if (!this.noSwingBlock.getValue()) mc.player.swingArm(hand);
         this.placedCrystal = true;
         if (this.crystalTimer == null) this.crystalTimer = new Timer();
         this.crystalTimer.reset();
     }
     public void reset() {
         this.base = null;
         this.builtTrap = false;
         this.placedCrystal = false;
         this.brokeBlock = false;
         this.attackedCrystal = false;
         this.done = false;
         this.headMode = false;
         this.based = false;
         this.crystalSlot = -1;
         this.obbySlot = -1;
         this.pickelSlot = -1;
         this.attempts = 0;
         target = null;
         this.blockTimer = null;
         this.crystalTimer = null;
         this.breakTimer = null;
         this.attackTimer = null;
         this.endTimer = null;
         breaking = false;
         this.lockedTargetId = null;
         this.lockedHead = null;
         this.lockTicks = 0;
     }
     public void onRender3D() {
         try {
             if (isNull(target))
                 return;    BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
             BlockPos headPos = targetPos.add(0, 2, 0);
             RenderUtil3D.drawBox(headPos, 1.0D, this.blockColor.getValue(), 63);
             if (this.outline.getValue()) RenderUtil3D.drawBoundingBox(headPos, 1.0D, this.width.getValue(), this.blockColor.getValue());
         } catch (Exception ignored) {}
     }
     public void setItem(int slot) {
         mc.player.inventory.currentItem = slot;
         mc.playerController.updateController();
     }
     public void restoreItem() {}
     public boolean findSpace(EntityPlayer player) {
         BlockPos targetPos = new BlockPos(player.posX, player.posY, player.posZ);
         BlockPos headPos = targetPos.add(0, 2, 0);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         List<BlockPos> posess = new ArrayList<>();
         if (BlockUtil.getBlock(headPos) == Blocks.OBSIDIAN) {
             this.headMode = true;
             this.base = targetPos;
             return true;
         }
         if (BlockUtil.canPlaceBlock(headPos)) {
             this.based = true;
             this.base = targetPos;
             return true;
         }
         for (BlockPos offset : offsets) {
             BlockPos basePos = targetPos.add(offset);
             if ((BlockUtil.getBlock(basePos) == Blocks.OBSIDIAN || BlockUtil.getBlock(basePos) == Blocks.BEDROCK) &&
                 BlockUtil.canPlaceBlockFuture(basePos.add(0, 1, 0)) &&
                 BlockUtil.canPlaceBlockFuture(basePos.add(0, 2, 0)) &&
                 BlockUtil.getBlock(headPos) == Blocks.AIR) {
                 posess.add(basePos.add(0, 1, 0));
             }
         }
         this
             .base = posess.stream().filter(p -> (mc.player.getDistance(p.getX(), p.getY(), p.getZ()) <= this.range.getValue())).max(Comparator.comparing(PlayerUtil::getDistanceI)).orElse(null);
         if (this.base == null) return false;
         this.headMode = false;
         return true;
     }
     public EntityPlayer findTarget() {
         EntityPlayer looking;
         List<EntityPlayer> candidates = new ArrayList<>(mc.world.playerEntities);
         candidates.removeIf(p ->
                 (p == null || p == mc.player || p.isDead || FriendManager.isFriend(p.getName()) || mc.player.getDistance(p) > this.targetRange.getValue()));
         if (candidates.isEmpty()) return null;
         switch (this.targetType.getValue()) {
             case Nearest:
                 return candidates.stream()
                     .min(Comparator.comparingDouble(p -> mc.player.getDistance(p)))
                     .orElse(null);
             case Looking:
                 looking = PlayerUtil.getLookingPlayer(this.targetRange.getValue());
                 if (looking != null &&
                     !FriendManager.isFriend(looking.getName()) && candidates
                     .contains(looking)) {
                     return looking;
                 }
                 return null;
             case Best:
                 return candidates.stream()
                     .filter(this::findSpace)
                     .min(Comparator.comparingDouble(PlayerUtil::getDistance))
                     .orElse(null);
         }
         return null;
     }
     public boolean findMaterials() {
         this.crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
         this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         this.pickelSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
         if (itemCheck(this.crystalSlot) && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) this.crystalSlot = 999;
         return (!itemCheck(this.crystalSlot) && !itemCheck(this.obbySlot) && !itemCheck(this.pickelSlot));
     }
     public boolean itemCheck(int slot) {
         return (slot == -1);
     }
     public boolean isNull(Object o) {
         return (o == null);
     }
     public static boolean breaking = false; public final Setting<Float> preDelay; public final Setting<Float> crystalDelay; public final Setting<Float> breakDelay; public final Setting<Float> attackDelay; public final Setting<Float> endDelay; public final Setting<Float> range; public final Setting<Boolean> tick; public final Setting<Boolean> toggle; public final Setting<Boolean> noSwingBlock; public final Setting<Boolean> packetPlace;
     public void placeBlock(BlockPos pos, Boolean packet) {
         BlockUtil.placeBlock(pos, packet);
         if (!this.noSwingBlock.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
     }
     public final Setting<Boolean> packetCrystal; public final Setting<Boolean> instantBreak; public final Setting<Boolean> toggleSilentPickel; public final Setting<Boolean> packetBreak; public final Setting<Boolean> offHandBreak; public final Setting<Boolean> skip; public final Setting<Integer> breakAttempts; public final Setting<Float> targetRange; public final Setting<Target> targetType;
     public final Setting<Color> blockColor;
     public static EntityPlayer target = null; public final Setting<Boolean> outline; public final Setting<Float> width; public BlockPos base; public BlockPos old; public boolean builtTrap; public boolean placedCrystal; public boolean brokeBlock; public boolean attackedCrystal; public boolean done; public boolean headMode; public boolean based; public int crystalSlot; public int obbySlot; public int pickelSlot; public int attempts; public Timer blockTimer; public Timer crystalTimer;
     public Timer breakTimer;
     public Timer attackTimer;
     public Timer endTimer;
     private static final int LOCK_TICKS_DEFAULT = 20;
     public enum Target { Nearest,
         Looking,
         Best
}
 }