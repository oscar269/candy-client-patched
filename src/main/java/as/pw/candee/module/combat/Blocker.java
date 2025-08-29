 package as.pw.candee.module.combat;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.stream.Collectors;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.CrystalUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketUseEntity;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 public class Blocker
     extends Module {
     public final Setting<Boolean> piston;
     public final Setting<Boolean> crystalSync;
     public final Setting<Boolean> breakCrystalPA;
     public final Setting<Boolean> teleportPA;
     public final Setting<Integer> limitPA;
     public final Setting<Float> crystalDelayPA;
     public final Setting<Float> range;
     public final Setting<Integer> maxY;
     public final Setting<Boolean> cev;
     public final Setting<Float> crystalDelayCEV;
     public final Setting<Boolean> teleportCEV;
     public final Setting<Integer> limitCEV;
     public final Setting<Boolean> civ;
     public final Setting<Float> crystalDelayCIV;
     public final Setting<Float> placeDelay;
     public final Setting<Boolean> packetPlace;
     public final Setting<Boolean> packetBreak;
     public final Setting<Arm> swingArm;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> tick;
     public final Setting<Boolean> render;
     public final Setting<Color> pistonColor;
     public Entity PAcrystal;
     public List<BlockPos> pistonPos;
     public Timer crystalTimerPA;
     public int oldCrystal;
     public int limitCounterPA;
     public boolean needBlockCEV;
     public Timer crystalTimerCEV;
     public int limitCounterCEV;
     public int stage;
     public Timer timerCiv;
     public List<BlockPos> detectedPosCiv;
     public Timer placeTimer;
     private int oldslot;
     private EnumHand oldhand;
     public Blocker() {
         super("Blocker", Categories.COMBAT, false, false);
         this.piston = register(new Setting("Piston", Boolean.TRUE));
         this.crystalSync = register(new Setting("CrystalSyncPA", Boolean.FALSE, v -> this.piston.getValue()));
         this.breakCrystalPA = register(new Setting("BreakCrystalPA", Boolean.FALSE, v -> this.piston.getValue()));
         this.teleportPA = register(new Setting("FlightBreakPA", Boolean.TRUE, v -> (this.piston.getValue() && this.breakCrystalPA.getValue())));
         this.limitPA = register(new Setting("LimitPA", 3, 10, 1, v -> (this.piston.getValue() && this.breakCrystalPA.getValue() && this.teleportPA.getValue())));
         this.crystalDelayPA = register(new Setting("CrystalDelayPA", 3.0F, 25.0F, 0.0F, v -> (this.piston.getValue() && this.breakCrystalPA.getValue())));
         this.range = register(new Setting("Range", 7.0F, 13.0F, 0.0F, v -> this.piston.getValue()));
         this.maxY = register(new Setting("MaxY", 4, 6, 2, v -> this.piston.getValue()));
         this.cev = register(new Setting("CevBreaker", Boolean.TRUE));
         this.crystalDelayCEV = register(new Setting("CrystalDelayCEV", 3.0F, 25.0F, 0.0F, v -> this.cev.getValue()));
         this.teleportCEV = register(new Setting("FlightBreakCEV", Boolean.TRUE, v -> this.cev.getValue()));
         this.limitCEV = register(new Setting("LimitCEV", 3, 10, 1, v -> (this.piston.getValue() && this.teleportCEV.getValue())));
         this.civ = register(new Setting("CivBreaker", Boolean.TRUE));
         this.crystalDelayCIV = register(new Setting("CrystalDelayCIV", 3.0F, 25.0F, 0.0F, v -> this.cev.getValue()));
         this.placeDelay = register(new Setting("PlaceDelay", 3.0F, 25.0F, 0.0F));
         this.packetPlace = register(new Setting("PacketPlace", Boolean.FALSE));
         this.packetBreak = register(new Setting("PacketBreak", Boolean.TRUE));
         this.swingArm = register(new Setting("SwingArm", Arm.None));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.FALSE));
         this.tick = register(new Setting("Tick", Boolean.TRUE));
         this.render = register(new Setting("Render", Boolean.TRUE));
         this.pistonColor = register(new Setting("PistonColor", new Color(230, 10, 10, 50), v -> this.piston.getValue()));
         this.PAcrystal = null;
         this.pistonPos = new ArrayList<>();
         this.crystalTimerPA = new Timer();
         this.oldCrystal = -1;
         this.limitCounterPA = 0;
         this.needBlockCEV = false;
         this.crystalTimerCEV = new Timer();
         this.limitCounterCEV = 0;
         this.stage = 0;
         this.timerCiv = new Timer();
         this.detectedPosCiv = new ArrayList<>();
         this.placeTimer = new Timer();
         this.oldslot = -1;
         this.oldhand = null;
     }
     public void onEnable() {
         this.PAcrystal = null;
         this.pistonPos = new ArrayList<>();
         this.crystalTimerPA = new Timer();
         this.needBlockCEV = false;
         this.crystalTimerCEV = new Timer();
         this.limitCounterCEV = 0;
         this.stage = 0;
         this.timerCiv = new Timer();
         this.detectedPosCiv = new ArrayList<>();
         this.placeTimer = new Timer();
     }
     public void onTick() {
         if (this.tick.getValue()) {
             doBlock();
         }
     }
     public void onUpdate() {
         if (!this.tick.getValue()) {
             doBlock();
         }
     }
     public void doBlock() {
         if (nullCheck()) {
             return;
         }
         int obby = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
         if (obby == -1) {
             return;
         }
         Module pa = CandeePlusRewrite.m_module.getModuleWithClass(PistonAura.class);
         if (this.piston.getValue() && !pa.isEnable) {
             execute(() -> {
                         detectPA();
                         blockPA(obby);
                     });
         }
         if (this.cev.getValue()) {
             execute(() -> blockCEV(obby));
         }
         Module civ = CandeePlusRewrite.m_module.getModuleWithClass(CivBreaker.class);
         if (this.civ.getValue() && !civ.isEnable && !pa.isEnable) {
             execute(() -> blockCIV(obby));
         }
         restoreItem();
     }
     public void execute(Runnable action) {
         try {
             action.run();
         }
         catch (Exception ignored) {}
     }
     public void onRender3D() {
         try {
             if (this.pistonPos != null && this.piston.getValue() && this.render.getValue()) {
                 for (BlockPos piston : this.pistonPos) {
                     RenderUtil3D.drawBox(piston, 1.0D, this.pistonColor.getValue(), 63);
                 }
             }
         }
         catch (Exception ignored) {}
     }
     public void blockCIV(int obby) {
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos[] array = { new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1), new BlockPos(1, 1, 1), new BlockPos(1, 1, -1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, -1) };
                 for (BlockPos offset : array) {
             BlockPos base = mypos.add(offset);
             List<Entity> crystals = mc.world.loadedEntityList.stream().filter(e -> e instanceof net.minecraft.entity.item.EntityEnderCrystal).collect(Collectors.toList());
             if (BlockUtil.getBlock(base) == Blocks.OBSIDIAN) {
                 for (Entity crystal : crystals) {
                     BlockPos crystalPos = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
                     if (base.equals(crystalPos.add(0, -1, 0))) {
                         mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                         this.detectedPosCiv.add(crystalPos);
                     }
                 }
             }
         }
         if (this.timerCiv.passedX(this.crystalDelayCIV.getValue())) {
             this.timerCiv.reset();
             Iterator<BlockPos> poses = this.detectedPosCiv.iterator();
             while (poses.hasNext()) {
                 BlockPos pos = poses.next();
                 if (BlockUtil.getBlock(pos) == Blocks.AIR) {
                     setItem(obby);
                     if (BlockUtil.getBlock(pos.add(0, -1, 0)) == Blocks.AIR) {
                         BlockUtil.placeBlock(pos.add(0, -1, 0), this.packetPlace.getValue());
                     }
                     BlockUtil.placeBlock(pos, this.packetPlace.getValue());
                     poses.remove();
                 }
             }
         }
     }
     public void blockCEV(int obby) {
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos ceilPos = mypos.add(0, 2, 0);
         if (this.placeTimer.passedX(this.crystalDelayCEV.getValue())) {
             if (this.stage == 1) {
                 this.crystalTimerCEV.reset();
                 setItem(obby);
                 BlockUtil.placeBlock(ceilPos.add(0, 1, 0), this.packetPlace.getValue());
                 this.stage = 0;
             }
             if (BlockUtil.getBlock(ceilPos) == Blocks.OBSIDIAN && CrystalUtil.hasCrystal(ceilPos) && this.teleportCEV.getValue() && this.limitCounterCEV < this.limitCEV.getValue()) {
                 this.crystalTimerCEV.reset();
                 this.limitCounterCEV++;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mypos.getY() + 0.2D, mc.player.posZ, false));
                 breakCrystal(CrystalUtil.getCrystal(ceilPos));
                 swingArm();
                 this.stage = 1;
             } else {
                 this.limitCounterCEV = 0;
             }
         }
     }
     public void blockPA(int obby) {
         if (this.crystalTimerPA.passedX(this.crystalDelayPA.getValue()) && this.piston.getValue() && this.breakCrystalPA.getValue() && this.PAcrystal != null) {
             this.crystalTimerPA.reset();
             if (this.PAcrystal.getEntityId() == this.oldCrystal) {
                 this.limitCounterPA++;
             } else {
                 this.limitCounterPA = 0;
             }
             BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
             BlockPos crystalPos = new BlockPos(this.PAcrystal.posX, this.PAcrystal.posY, this.PAcrystal.posZ);
             if (mypos.getY() + 2 == crystalPos.getY() && BlockUtil.getBlock(mypos.add(0, 2, 0)) == Blocks.OBSIDIAN && this.teleportPA.getValue() && this.limitCounterPA <= this.limitPA.getValue()) {
                 double offsetx = (mypos.getX() - crystalPos.getX()) * 0.4D;
                 double offsetz = (mypos.getZ() - crystalPos.getZ()) * 0.4D;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mypos.getX() + 0.5D + offsetx, mypos.getY() + 0.2D, mypos.getZ() + 0.5D + offsetz, false));
             }
             breakCrystal(this.PAcrystal);
             swingArm();
             this.oldCrystal = this.PAcrystal.getEntityId();
             this.PAcrystal = null;
         }
         if (this.placeTimer.passedX(this.placeDelay.getValue()) && this.piston.getValue()) {
             this.placeTimer.reset();
             Iterator<BlockPos> pistons = this.pistonPos.iterator();
             while (pistons.hasNext()) {
                 BlockPos pos = pistons.next();
                 if (BlockUtil.getBlock(pos) == Blocks.AIR) {
                     setItem(obby);
                     if (BlockUtil.hasNeighbour(pos)) {
                         BlockUtil.placeBlock(pos, this.packetPlace.getValue());
                     } else {
                         BlockUtil.placeBlock(pos.add(0, -1, 0), this.packetPlace.getValue());
                         BlockUtil.rightClickBlock(pos.add(0, -1, 0), EnumFacing.UP, this.packetPlace.getValue());
                     }
                     pistons.remove();
                 }
             }
         }
     }
     public void detectPA() {
         List<BlockPos> tmp = new ArrayList<>();
         Iterator<BlockPos> iterator = this.pistonPos.iterator();
         while (iterator.hasNext()) {
             BlockPos pos = iterator.next();
             if (tmp.contains(pos) || PlayerUtil.getDistance(pos) > this.range.getValue()) {
                 iterator.remove();
             }
             tmp.add(pos);
         }
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         for (int y = 0; y <= this.maxY.getValue(); y++) {
             for (BlockPos offset : offsets) {
                 BlockPos crystalPos = mypos.add(offset.getX(), y, offset.getZ());
                 if (CrystalUtil.hasCrystal(crystalPos) || !this.crystalSync.getValue()) {
                     List<BlockPos> pistonPos = new ArrayList<>();
                     BlockPos noOldCandy = crystalPos.add(offset);
                     BlockPos sidePos0 = crystalPos.add(offset.getZ(), 0, offset.getX());
                     BlockPos sidePos2 = crystalPos.add(offset.getZ() * -1, 0, offset.getX() * -1);
                     BlockPos noSushi0 = noOldCandy.add(offset);
                     BlockPos noSushi2 = noOldCandy.add(offset.getZ(), 0, offset.getX());
                     BlockPos noSushi3 = noOldCandy.add(offset.getZ() * -1, 0, offset.getX() * -1);
                     BlockPos noSushi4 = noSushi2.add(offset);
                     BlockPos noSushi5 = noSushi3.add(offset);
                     add(pistonPos, noOldCandy);
                     add(pistonPos, sidePos0);
                     add(pistonPos, sidePos2);
                     add(pistonPos, noSushi0);
                     add(pistonPos, noSushi2);
                     add(pistonPos, noSushi3);
                     add(pistonPos, noSushi4);
                     add(pistonPos, noSushi5);
                     List<BlockPos> imNoob = new ArrayList<>();
                     pistonPos.forEach(b -> imNoob.add(b.add(0, 1, 0)));
                     imNoob.forEach(pistonPos::add);
                     for (BlockPos piston : pistonPos) {
                         if (isPiston(piston)) {
                             pistonPos.add(piston);
                             if (!CrystalUtil.hasCrystal(crystalPos)) {
                                 continue;
                             }
                             this.PAcrystal = CrystalUtil.getCrystal(crystalPos);
                         }
                     }
                 }
             }
         }
     }
     public void add(List<BlockPos> target, BlockPos base) {
         target.add(base.add(0, 1, 0));
     }
     public boolean isPiston(BlockPos pos) {
         return (BlockUtil.getBlock(pos) == Blocks.PISTON || BlockUtil.getBlock(pos) == Blocks.STICKY_PISTON);
     }
     public void swingArm() {
         EnumHand arm = (this.swingArm.getValue() == Arm.Offhand) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
         if (this.swingArm.getValue() != Arm.None) {
             mc.player.swingArm(arm);
         }
     }
     public void breakCrystal(Entity crystal) {
         if (!(crystal instanceof net.minecraft.entity.item.EntityEnderCrystal)) {
             return;
         }
         if (this.packetBreak.getValue()) {
             mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
         } else {
             mc.playerController.attackEntity(mc.player, crystal);
         }
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
     public enum Arm
     {
         Mainhand,
         Offhand,
         None
             }
 }