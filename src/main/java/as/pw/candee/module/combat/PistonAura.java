 package as.pw.candee.module.combat;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.MathUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
 import net.minecraft.network.play.client.CPacketUseEntity;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.util.math.Vec3i;
 import org.apache.logging.log4j.Level;
 public class PistonAura extends Module {
     public final Setting<Float> preDelay;
     public final Setting<Float> pistonDelay;
     public final Setting<Float> crystalDelay;
     public final Setting<Float> redstoneDelay;
     public final Setting<Float> pushDelay;
     public final Setting<Float> breakDelay;
     public final Setting<Float> targetRange;
     public final Setting<Target> targetType;
     public final Setting<Float> range;
     public final Setting<RedStone> redStoneType;
     public final Setting<Boolean> antiBlock;
     public final Setting<Boolean> noSwingBlock;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> packetPlace;
     public final Setting<Boolean> packetCrystal;
     public final Setting<Boolean> packetBreak;
     public final Setting<Boolean> offHandBreak;
     public final Setting<Boolean> sidePiston;
     public final Setting<Boolean> tick;
     public final Setting<Boolean> toggle;
     public final Setting<Boolean> trap;
     public final Setting<Float> trapDelay;
     public final Setting<Boolean> breakSync;
     public final Setting<Float> maxDelay;
     public final Setting<Integer> breakAttempts;
     public final Setting<Integer> maxY;
     public final Setting<Boolean> render;
     public final Setting<Color> crystalColor;
     public final Setting<Color> pistonColor;
     public final Setting<Color> redstoneColor;
     public final Setting<Boolean> line;
     public final Setting<Float> width;
     public final Setting<Boolean> debug;
     public final List<BlockPos> debugPosess;
     public int oldslot;
     public EnumHand oldhand;
     public static EntityPlayer target;
     public BlockPos pistonPos;
     public BlockPos crystalPos;
     public BlockPos redStonePos;
     public boolean placedPiston;
     public boolean placedCrystal;
     public boolean placedRedStone;
     public boolean waitedPiston;
     public boolean brokeCrystal;
     public boolean builtTrap;
     public boolean done;
     public boolean retrying;
     public boolean digging;
     public Timer pistonTimer;
     public Timer crystalTimer;
     public Timer redStoneTimer;
     public Timer pistonCrystalTimer;
     public Timer breakTimer;
     public Timer preTimer;
     public Timer trapTimer;
     public Timer syncTimer;
     public int pistonSlot;
     public int crystalSlot;
     public int redstoneSlot;
     public int obbySlot;
     public int pickelSlot;
     public int trapTicks;
     public int attempts;
     public BlockPos oldPiston;
     public BlockPos oldRedstone;
     public int tmpSlot;
     public PistonAura() {
         super("PistonAura", Categories.COMBAT, false, false);
         this.preDelay = register(new Setting("BlockDelay", 0.0F, 25.0F, 0.0F));
         this.pistonDelay = register(new Setting("PistonDelay", 0.0F, 25.0F, 0.0F));
         this.crystalDelay = register(new Setting("CrystalDelay", 0.0F, 25.0F, 0.0F));
         this.redstoneDelay = register(new Setting("RedStoneDelay", 0.0F, 25.0F, 0.0F));
         this.pushDelay = register(new Setting("PushDelay", 0.0F, 25.0F, 0.0F));
         this.breakDelay = register(new Setting("BreakDelay", 5.0F, 25.0F, 0.0F));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.range = register(new Setting("Range", 10.0F, 20.0F, 1.0F));
         this.redStoneType = register(new Setting("Redstone", RedStone.Both));
         this.antiBlock = register(new Setting("AntiBlock", Boolean.TRUE));
         this.noSwingBlock = register(new Setting("NoSwingBlock", Boolean.FALSE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.FALSE));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.TRUE));
         this.packetCrystal = register(new Setting("PacketCrystal", Boolean.TRUE));
         this.packetBreak = register(new Setting("PacketBreak", Boolean.TRUE));
         this.offHandBreak = register(new Setting("OffhandBreak", Boolean.TRUE));
         this.sidePiston = register(new Setting("SidePiston", Boolean.FALSE));
         this.tick = register(new Setting("Tick", Boolean.TRUE));
         this.toggle = register(new Setting("Toggle", Boolean.TRUE));
         this.trap = register(new Setting("Trap", Boolean.FALSE));
         this.trapDelay = register(new Setting("TrapDelay", 3.0F, 25.0F, 0.0F, s -> this.trap.getValue()));
         this.breakSync = register(new Setting("BreakSync", Boolean.TRUE));
         this.maxDelay = register(new Setting("MaxDelay", 50.0F, 100.0F, 1.0F, s -> this.breakSync.getValue()));
         this.breakAttempts = register(new Setting("BreakAttempts", 7, 20, 1));
         this.maxY = register(new Setting("MaxY", 2, 4, 1));
         this.render = register(new Setting("Render", Boolean.TRUE));
         this.crystalColor = register(new Setting("Crystal Color", new Color(250, 0, 200, 50), s -> this.render.getValue()));
         this.pistonColor = register(new Setting("Piston Color", new Color(40, 170, 245, 50), s -> this.render.getValue()));
         this.redstoneColor = register(new Setting("RedStone Color", new Color(252, 57, 50, 50), s -> this.render.getValue()));
         this.line = register(new Setting("Line", Boolean.FALSE, s -> this.render.getValue()));
         this.width = register(new Setting("Line Width", 2.0F, 5.0F, 0.1F, s -> (this.line.getValue() && this.render.getValue())));
         this.debug = register(new Setting("Debug", Boolean.FALSE));
         this.debugPosess = new ArrayList<>();
         this.oldslot = -1;
         this.oldhand = null;
         this.trapTicks = 0;
         this.attempts = 0;
         this.oldRedstone = null;
     }
     public void onEnable() {
         reset();
     }
     public void onUpdate() {
         if (!this.tick.getValue()) doPA();
     }
     public void onTick() {
         if (this.tick.getValue()) doPA();
     }
     public void doPA() {
         if (nullCheck())
             return;    try {
             if (!findMaterials()) {
                 if (this.toggle.getValue()) { sendMessage("Cannot find materials! disabling..."); disable(); }
                 return;
             }
             target = findTarget();
             if (isNull(target) || FriendManager.isFriend(target)) {
                 if (this.toggle.getValue()) { sendMessage("Cannot find target! disabling..."); disable(); }
                 return;
             }
             if ((isNull(this.pistonPos) || isNull(this.crystalPos) || isNull(this.redStonePos)) &&
                 !findSpace(target, this.redStoneType.getValue())) {
                 if (this.toggle.getValue()) { sendMessage("Cannot find space! disabling..."); disable(); }
                 return;
             }
             if (this.preTimer == null) this.preTimer = new Timer();
             if (this.preTimer.passedX(this.preDelay.getValue()) && !prepareBlock()) {
                 restoreItem();
                 return;
             }
             if (this.trapTimer == null) this.trapTimer = new Timer();
             if (!this.trap.getValue()) this.builtTrap = true;
             BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
             if (BlockUtil.getBlock(targetPos.add(0, 2, 0)) == Blocks.OBSIDIAN || this.pistonPos
                 .getY() >= targetPos.add(0, 2, 0).getY()) {
                 this.builtTrap = true;
             }
             if (!this.builtTrap && this.trapTimer.passedX(this.trapDelay.getValue())) {
                 BlockPos offset = new BlockPos(this.crystalPos.getX() - targetPos.getX(), 0, this.crystalPos.getZ() - targetPos.getZ());
                 BlockPos trapBase = targetPos.add(offset.getX() * -1, 0, offset.getZ() * -1);
                 if (this.trapTicks == 0 && BlockUtil.getBlock(trapBase) == Blocks.AIR) {
                     setItem(this.obbySlot);
                     placeBlock(trapBase, Boolean.FALSE);
                     this.trapTimer = new Timer();
                     this.trapTicks = 1;
                 } else {
                     this.trapTicks = 1;
                 }
                 if (this.trapTicks == 1) {
                     setItem(this.obbySlot);
                     placeBlock(trapBase.add(0, 1, 0), Boolean.FALSE);
                     this.trapTimer = new Timer();
                     this.trapTicks = 2;
                 }
                 if (this.trapTicks == 2) {
                     setItem(this.obbySlot);
                     placeBlock(trapBase.add(0, 2, 0), Boolean.FALSE);
                     this.trapTimer = new Timer();
                     this.trapTicks = 3;
                 }
                 if (this.trapTicks == 3) {
                     setItem(this.obbySlot);
                     placeBlock(targetPos.add(0, 2, 0), Boolean.FALSE);
                     this.trapTimer = new Timer();
                     this.trapTicks = 4;
                     this.builtTrap = true;
                 }
                 restoreItem();
                 return;
             }
             if (this.pistonTimer == null && this.builtTrap) this.pistonTimer = new Timer();
             if (this.builtTrap && !this.placedPiston && this.pistonTimer.passedX(this.pistonDelay.getValue())) {
                 setItem(this.pistonSlot);
                 float[] angle = MathUtil.calcAngle(new Vec3d(this.crystalPos), new Vec3d(targetPos));
                 mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0] + 180.0F, angle[1], true));
                 placePiston(this.pistonPos, targetPos, this.packetPlace.getValue());
                 this.placedPiston = true;
             }
             if (this.crystalTimer == null && this.placedPiston) this.crystalTimer = new Timer();
             if (this.placedPiston && !this.placedCrystal && this.crystalTimer.passedX(this.crystalDelay.getValue())) {
                 if (this.crystalSlot != 999) setItem(this.crystalSlot);
                 EnumHand hand = (this.crystalSlot != 999) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                 if (this.packetCrystal.getValue()) {
                     mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.crystalPos, EnumFacing.DOWN, hand, 0.0F, 0.0F, 0.0F));
                 } else {
                     mc.playerController.processRightClickBlock(mc.player, mc.world, this.crystalPos, EnumFacing.DOWN, new Vec3d(0.0D, 0.0D, 0.0D), hand);
                 }
                 this.placedCrystal = true;
             }
             if (this.redStoneTimer == null && this.placedCrystal) this.redStoneTimer = new Timer();
             if (this.placedCrystal && !this.placedRedStone && this.redStoneTimer.passedX(this.redstoneDelay.getValue())) {
                 setItem(this.redstoneSlot);
                 placeBlock(this.redStonePos, this.packetPlace.getValue());
                 this.placedRedStone = true;
             }
             if (this.pistonCrystalTimer == null && this.placedRedStone) this.pistonCrystalTimer = new Timer();
             if (this.placedRedStone && !this.waitedPiston && this.pistonCrystalTimer.passedX(this.pushDelay.getValue())) {
                 this.waitedPiston = true;
             }
             if (this.retrying) {
                 setItem(this.pickelSlot);
                 if (!this.digging) {
                     mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.redStonePos, EnumFacing.DOWN));
                     this.digging = true;
                 }
                 if (this.digging && BlockUtil.getBlock(this.redStonePos) == Blocks.AIR) {
                     mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.redStonePos, EnumFacing.DOWN));
                     this.placedCrystal = false;
                     this.placedRedStone = false;
                     this.waitedPiston = false;
                     this.brokeCrystal = false;
                     this.done = false;
                     this.digging = false;
                     this.retrying = false;
                     this.crystalTimer = null;
                     this.redStoneTimer = null;
                     this.pistonCrystalTimer = null;
                     this.breakTimer = null;
                     this.attempts = 0;
                 }
                 restoreItem();
                 return;
             }
             if (this.waitedPiston && !this.brokeCrystal) {
                 Entity crystal = mc.world.loadedEntityList.stream().filter(e2 -> e2 instanceof net.minecraft.entity.item.EntityEnderCrystal).filter(e2 -> (target.getDistance(e2) < 5.0F)).min(Comparator.comparing(e2 -> target.getDistance(e2))).orElse(null);
                 if (crystal == null) {
                     if (this.attempts < this.breakAttempts.getValue()) {
                         this.attempts++;
                     } else {
                         this.attempts = 0;
                         this.digging = false;
                         this.retrying = true;
                     }
                     restoreItem();
                     return;
                 }
                 EnumHand hand2 = this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                 if (this.packetBreak.getValue()) {
                     mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                 } else {
                     mc.playerController.attackEntity(mc.player, crystal);
                     mc.player.swingArm(hand2);
                 }
                 this.brokeCrystal = true;
             }
             if (this.breakTimer == null && this.brokeCrystal) this.breakTimer = new Timer();
             if (this.brokeCrystal && !this.done && this.breakTimer.passedX(this.breakDelay.getValue())) {
                 this.done = true;
             }
             if (this.done) {
                 if ((BlockUtil.getBlock(this.redStonePos) != Blocks.REDSTONE_BLOCK && BlockUtil.getBlock(this.redStonePos) != Blocks.REDSTONE_TORCH) ||
                     !this.breakSync.getValue()) {
                     reset();
                 } else {
                     if (this.syncTimer == null) this.syncTimer = new Timer();
                     if (this.syncTimer.passedDms(this.maxDelay.getValue()) && this.maxDelay.getValue() != -1.0F) {
                         reset();
                     }
                     else {
                         Entity crystal = mc.world.loadedEntityList.stream().filter(e2 -> e2 instanceof net.minecraft.entity.item.EntityEnderCrystal).filter(e2 -> (target.getDistance(e2) < 5.0F)).min(Comparator.comparing(e2 -> target.getDistance(e2))).orElse(null);
                         if (crystal == null) {
                             restoreItem();
                             return;
                         }
                         EnumHand hand2 = this.offHandBreak.getValue() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                         if (this.packetBreak.getValue()) {
                             mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                         } else {
                             mc.playerController.attackEntity(mc.player, crystal);
                             mc.player.swingArm(hand2);
                         }
                         this.breakTimer = null;
                         this.done = false;
                     }
                 }
             }
             restoreItem();
         } catch (Exception e) {
             CandeePlusRewrite.Log(Level.ERROR, e.getMessage());
         }
     }
     public void onRender3D() {
         try {
             if (!this.render.getValue())
                 return;    if (isNull(this.pistonPos) || isNull(this.crystalPos) || isNull(this.redStonePos))
                 return;
             if (this.line.getValue()) {
                 RenderUtil3D.drawBoundingBox(this.crystalPos, 1.0D, this.width.getValue(), convert(this.crystalColor.getValue()));
                 RenderUtil3D.drawBoundingBox(this.pistonPos, 1.0D, this.width.getValue(), convert(this.pistonColor.getValue()));
                 RenderUtil3D.drawBoundingBox(this.redStonePos, 1.0D, this.width.getValue(), convert(this.redstoneColor.getValue()));
             } else {
                 RenderUtil3D.drawBox(this.crystalPos, 1.0D, this.crystalColor.getValue(), 63);
                 RenderUtil3D.drawBox(this.pistonPos, 1.0D, this.pistonColor.getValue(), 63);
                 RenderUtil3D.drawBox(this.redStonePos, 1.0D, this.redstoneColor.getValue(), 63);
             }
             if (this.debug.getValue()) {
                 for (BlockPos pos : this.debugPosess) {
                     if (pos == null)
                         continue;    RenderUtil3D.drawBoundingBox(pos, 1.0D, this.width.getValue(), new Color(230, 230, 230));
                 }
             }
         } catch (Exception ignored) {}
     }
     public Color convert(Color c) {
         return new Color(c.getRed(), c.getGreen(), c.getBlue(), 240);
     }
     public static EnumFacing getFacingToTarget(BlockPos pistonPos, BlockPos targetPos) {
         int dx = targetPos.getX() - pistonPos.getX();
         int dz = targetPos.getZ() - pistonPos.getZ();
         if (Math.abs(dx) > Math.abs(dz))
             return (dx > 0) ? EnumFacing.EAST : EnumFacing.WEST;
         if (dz != 0) {
             return (dz > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
         }
         return EnumFacing.UP;
     }
     public void placePiston(BlockPos pos, BlockPos targetPos, boolean usePacket) {
         BlockUtil.placeBlock(pos, usePacket);
         if (!this.noSwingBlock.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
     }
     public boolean findSpace(EntityPlayer target, RedStone type) {
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         List<PistonAuraPos> poses = new ArrayList<>();
         for (BlockPos offset : offsets) {
             for (int y = 0; y <= this.maxY.getValue(); y++) {
                 PistonAuraPos pos = new PistonAuraPos();
                 BlockPos cPos = base.add(offset.getX(), y, offset.getZ());
                 if (BlockUtil.getBlock(cPos) == Blocks.ENDER_CHEST) {
                     sendmsg("x");
                 } else if ((BlockUtil.getBlock(cPos) != Blocks.OBSIDIAN && BlockUtil.getBlock(cPos) != Blocks.BEDROCK) ||
                     BlockUtil.getBlock(cPos.add(0, 1, 0)) != Blocks.AIR ||
                     BlockUtil.getBlock(cPos.add(0, 2, 0)) != Blocks.AIR) {
                     sendmsg("y");
                 } else if (mypos.getX() == cPos.getX() && mypos.getZ() == cPos.getZ()) {
                     sendmsg("a");
                 } else {
                     pos.setCrystal(cPos);
                     List<BlockPos> pistonOffsets = new ArrayList<>();
                     pistonOffsets.add(new BlockPos(1, 0, 0));
                     pistonOffsets.add(new BlockPos(-1, 0, 0));
                     pistonOffsets.add(new BlockPos(0, 0, 1));
                     pistonOffsets.add(new BlockPos(0, 0, -1));
                     if (!this.antiBlock.getValue()) pistonOffsets.add(new BlockPos(0, 0, 0));
                     BlockPos pistonBase = base.add(offset.getX() * 2, y + 1, offset.getZ() * 2);
                     List<BlockPos> pistonPoses = new ArrayList<>();
                     for (BlockPos poff : pistonOffsets) {
                         BlockPos pPos = pistonBase.add(poff);
                         if (BlockUtil.getBlock(pPos) != Blocks.AIR) {
                             sendmsg("b"); continue;
                         }
                         BlockPos checkPos_c = pPos.add(offset.getX() * -1, offset.getY(), offset.getZ() * -1);
                         BlockPos checkPos_r = pPos.add(offset.getX(), offset.getY(), offset.getZ());
                         if (mypos.getDistance(pPos.getX(), pPos.getY(), pPos.getZ()) < 3.6D + (pPos.getY() - mypos.getY() + 1) && pPos
                             .getY() > mypos.getY() + 1) {
                             sendmsg("++"); continue;
                         }    if (BlockUtil.getBlock(checkPos_c) != Blocks.AIR ||
                             BlockUtil.getBlock(checkPos_r) != Blocks.AIR || (pPos
                             .getX() == cPos.getX() && pPos.getZ() == cPos.getZ()) || (mypos
                             .getX() == pPos.getX() && mypos.getZ() == pPos.getZ()) || (mypos
                             .getX() == checkPos_r.getX() && mypos.getZ() == checkPos_r.getZ()) || (mypos
                             .getX() == checkPos_c.getX() && mypos.getZ() == checkPos_c.getZ())) {
                             sendmsg("d = " + checkPos_c); continue;
                         }
                         pistonPoses.add(pPos);
                     }
                     pos.setPiston(pistonPoses.stream()
                             .min(Comparator.comparing(p -> mypos.getDistance(p.getX(), p.getY(), p.getZ())))
                             .orElse(null));
                     if (isNull(pos.piston)) {
                         sendmsg("e");
                     } else {
                         BlockPos redstonePos;
                         if ((cPos.getX() != pos.piston.getX() && cPos.getZ() != pos.piston.getZ()) || (pos
                             .piston.getX() - cPos.getX() != 0 && pos.piston.getZ() - cPos.getZ() == 0) || (pos
                             .piston.getZ() - cPos.getZ() != 0 && pos.piston.getX() - cPos.getX() == 0 && (offset
                             .getX() != 0 || offset.getZ() != 0))) {
                             redstonePos = pos.piston.add(offset);
                         } else {
                             redstonePos = pos.piston.add(new BlockPos(pos.piston.getX() - pos.crystal.getX(), 0, pos.piston.getZ() - pos.crystal.getZ()));
                         }
                         if (BlockUtil.getBlock(redstonePos) != Blocks.AIR) {
                             sendmsg("f");
                         } else {
                             pos.setRedStone(redstonePos);
                             poses.add(pos);
                         }
                     }
                 }
             }
         }
         if (poses.isEmpty()) return false;
         PistonAuraPos bestPos = poses.stream().filter(p -> (p.getMaxRange() <= this.range.getValue())).min(Comparator.comparing(PistonAuraPos::getMaxRange)).orElse(null);
         if (bestPos == null) return false;
         this.pistonPos = bestPos.piston;
         this.crystalPos = bestPos.crystal;
         this.redStonePos = bestPos.redstone;
         return true;
     }
     public EntityPlayer findTarget() {
         EntityPlayer t = null;
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         players.removeIf(p -> (p == mc.player || FriendManager.isFriend(p) || p.isDead || p.getHealth() <= 0.0F));
         players.removeIf(p -> (mc.player.getDistance(p) > this.targetRange.getValue()));
         if (this.targetType.getValue() == Target.Nearest) {
             t = players.stream().min(Comparator.comparingDouble(mc.player::getDistance)).orElse(null);
         } else if (this.targetType.getValue() == Target.Looking) {
             EntityPlayer looking = PlayerUtil.getLookingPlayer(this.targetRange.getValue());
             if (looking != null && !FriendManager.isFriend(looking) && players.contains(looking)) {
                 t = looking;
             } else {
                 t = players.stream().min(Comparator.comparingDouble(mc.player::getDistance)).orElse(null);
             }
         } else if (this.targetType.getValue() == Target.Best) {
             t = players.stream().filter(p -> findSpace(p, this.redStoneType.getValue())).min(Comparator.comparingDouble(mc.player::getDistance)).orElse(null);
         }
         return t;
     }
     public boolean findMaterials() {
         this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.PISTON);
         this.crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
         int redstoneBlock = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
         int redstoneTorch = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_TORCH);
         this.obbySlot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         this.pickelSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
         if (itemCheck(this.crystalSlot) && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
             this.crystalSlot = 999;
         }
         if (itemCheck(this.pistonSlot)) {
             this.pistonSlot = InventoryUtil.findHotbarBlock(Blocks.STICKY_PISTON);
         }
         if (this.redStoneType.getValue() == RedStone.Block) this.redstoneSlot = redstoneBlock;
         if (this.redStoneType.getValue() == RedStone.Torch) this.redstoneSlot = redstoneTorch;
         if (this.redStoneType.getValue() == RedStone.Both) {
             this.redstoneSlot = redstoneTorch;
             if (itemCheck(this.redstoneSlot)) this.redstoneSlot = redstoneBlock;
         }
         return (!itemCheck(this.crystalSlot) && !itemCheck(this.obbySlot) && !itemCheck(this.pickelSlot) &&
             !itemCheck(this.redstoneSlot) && !itemCheck(this.pistonSlot));
     }
     public boolean itemCheck(int slot) {
         return (slot == -1);
     }
     public boolean prepareBlock() {
         BlockPos targetPos = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos crystal = this.crystalPos;
         BlockPos piston = this.pistonPos.add(0, -1, 0);
         BlockPos redstone = this.redStonePos.add(0, -1, 0);
         if (BlockUtil.getBlock(crystal) == Blocks.AIR) {
             setItem(this.obbySlot);
             placeBlock(crystal, this.packetPlace.getValue());
             if (this.preDelay.getValue() != 0.0F) this.preTimer.reset();
             return false;
         }
         if (!BlockUtil.hasNeighbour(piston)) {
             setItem(this.obbySlot);
             BlockPos base = crystal.add(crystal.getX() - targetPos.getX(), 0, crystal.getZ() - targetPos.getZ());
             placeBlock(base, this.packetPlace.getValue());
             if (this.preDelay.getValue() != 0.0F) this.preTimer.reset();
             return false;
         }
         if (BlockUtil.getBlock(piston) == Blocks.AIR) {
             setItem(this.obbySlot);
             placeBlock(piston, this.packetPlace.getValue());
             if (this.preDelay.getValue() != 0.0F) this.preTimer.reset();
             return false;
         }
         if (BlockUtil.getBlock(redstone) == Blocks.AIR && (piston.getX() != redstone.getX() || piston.getZ() != redstone.getZ())) {
             setItem(this.obbySlot);
             placeBlock(redstone, this.packetPlace.getValue());
             if (this.preDelay.getValue() != 0.0F) this.preTimer.reset();
             return false;
         }
         return true;
     }
     public Vec3i getOffset(BlockPos base, int x, int z) {
         return new Vec3i(base.getX() * x, 0, base.getZ() * z);
     }
     public boolean isNull(Object b) {
         return (b == null);
     }
     public void setTmp() {
         this.tmpSlot = mc.player.inventory.currentItem;
     }
     public void updateItem() {
         mc.player.inventory.currentItem = this.tmpSlot;
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
     public void sendmsg(String s) {
         if (this.debug.getValue()) sendMessage(s);
     }
     public void placeBlock(BlockPos pos, Boolean packet) {
         BlockUtil.placeBlock(pos, packet);
         if (!this.noSwingBlock.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
     }
     public void reset() {
         this.oldPiston = this.pistonPos;
         this.oldRedstone = this.redStonePos;
         target = null;
         this.pistonPos = null;
         this.crystalPos = null;
         this.redStonePos = null;
         this.placedPiston = false;
         this.placedCrystal = false;
         this.placedRedStone = false;
         this.waitedPiston = false;
         this.brokeCrystal = false;
         this.builtTrap = false;
         this.done = false;
         this.digging = false;
         this.retrying = false;
         this.pistonTimer = null;
         this.crystalTimer = null;
         this.redStoneTimer = null;
         this.pistonCrystalTimer = null;
         this.breakTimer = null;
         this.preTimer = null;
         this.trapTimer = null;
         this.syncTimer = null;
         this.pistonSlot = -1;
         this.crystalSlot = -1;
         this.redstoneSlot = -1;
         this.obbySlot = -1;
         this.pickelSlot = -1;
         this.trapTicks = 0;
         this.attempts = 0;
     }
     public enum Target { Nearest, Looking, Best}
     public enum RedStone { Block, Torch, Both}
     public static class PistonAuraPos { private BlockPos piston;
         private BlockPos crystal;
         private BlockPos redstone;
         public void setPiston(BlockPos piston) { this.piston = piston; }
         public void setCrystal(BlockPos crystal) { this.crystal = crystal; }
         public void setRedStone(BlockPos redstone) { this.redstone = redstone; }
         public BlockPos getCrystalPos() { return this.crystal; }
         public BlockPos getPistonPos() { return this.piston; } public BlockPos getRedstone() {
             return this.redstone;
         }
         public double getMaxRange() {
             double p = PlayerUtil.getDistanceI(this.piston);
             double c = PlayerUtil.getDistanceI(this.crystal);
             double r = PlayerUtil.getDistanceI(this.redstone);
             return Math.max(Math.max(p, c), r);
         } }
 }