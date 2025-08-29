 package as.pw.candee.module.combat;
 import java.awt.Color;
 import java.util.Comparator;
 import java.util.List;
 import java.util.stream.Collectors;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.CrystalUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityEnderCrystal;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketAnimation;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
 import net.minecraft.network.play.client.CPacketUseEntity;
 import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
 import net.minecraft.network.play.server.SPacketSpawnMob;
 import net.minecraft.network.play.server.SPacketSpawnObject;
 import net.minecraft.network.play.server.SPacketSpawnPainting;
 import net.minecraft.network.play.server.SPacketSpawnPlayer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.fml.common.eventhandler.EventPriority;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class CrystalAura extends Module {
     public final Setting<Boolean> place;
     public final Setting<Float> placeDelay;
     public final Setting<Float> placeRange;
     public final Setting<Float> wallRangePlace;
     public final Setting<Boolean> placeSwing;
     public final Setting<Boolean> autoSwitch;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Boolean> opPlace;
     public final Setting<Boolean> explode;
     public final Setting<Boolean> predict;
     public final Setting<Float> explodeDelay;
     public final Setting<Float> breakRange;
     public final Setting<Float> wallRangeBreak;
     public final Setting<Boolean> packetBreak;
     public final Setting<swingArm> swing;
     public final Setting<Boolean> packetSwing;
     public final Setting<Boolean> predictHit;
     public final Setting<Integer> amount;
     public final Setting<Integer> amountOffset;
     public final Setting<Boolean> checkOtherEntity;
     public final Setting<Boolean> ignoreSelfDmg;
     public final Setting<Float> maxSelf;
     public final Setting<Float> minDmg;
     public final Setting<Boolean> smartMode;
     public final Setting<Float> dmgError;
     public final Setting<Boolean> antiSuicide;
     public final Setting<Float> pauseHealth;
     public final Setting<Boolean> betterFps;
     public final Setting<Color> color;
     public final Setting<Boolean> avoidFriends;
     public EntityPlayer target;
     public int lastEntityID;
     public final Timer placeTimer;
     public final Timer breakTimer;
     private EnumHand oldhand;
     private int oldslot;
     public BlockPos renderPos = null;
     public CrystalAura() {
         super("CrystalAura", Categories.COMBAT, false, false);
         this.place = register(new Setting("Place", Boolean.TRUE));
         this.placeDelay = register(new Setting("PlaceDelay", 6.0F, 16.0F, 0.0F, v -> this.place.getValue()));
         this.placeRange = register(new Setting("PlaceRange", 7.0F, 16.0F, 1.0F, v -> this.place.getValue()));
         this.wallRangePlace = register(new Setting("WallRangePlace", 4.0F, 16.0F, 1.0F, v -> this.place.getValue()));
         this.placeSwing = register(new Setting("Swing", Boolean.FALSE));
         this.autoSwitch = register(new Setting("Switch", Boolean.TRUE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.FALSE, v -> this.autoSwitch.getValue()));
         this.opPlace = register(new Setting("1.13", Boolean.FALSE, v -> this.place.getValue()));
         this.explode = register(new Setting("Explode", Boolean.TRUE));
         this.predict = register(new Setting("Predict", Boolean.TRUE));
         this.explodeDelay = register(new Setting("ExplodeDelay", 6.0F, 16.0F, 0.0F, v -> this.explode.getValue()));
         this.breakRange = register(new Setting("ExplodeRange", 6.0F, 16.0F, 1.0F, v -> this.explode.getValue()));
         this.wallRangeBreak = register(new Setting("WallRangeBreak", 3.0F, 16.0F, 1.0F, v -> this.explode.getValue()));
         this.packetBreak = register(new Setting("PacketBreak", Boolean.TRUE));
         this.swing = register(new Setting("SwingArm", swingArm.Mainhand));
         this.packetSwing = register(new Setting("PacketSwing", Boolean.TRUE, v -> (this.swing.getValue() != swingArm.None)));
         this.predictHit = register(new Setting("PredictHit", Boolean.FALSE));
         this.amount = register(new Setting("Amount", 1, 15, 1, v -> this.predictHit.getValue()));
         this.amountOffset = register(new Setting("Offset", 1, 10, 0, v -> this.predictHit.getValue()));
         this.checkOtherEntity = register(new Setting("OtherEntity", Boolean.FALSE, v -> this.predictHit.getValue()));
         this.ignoreSelfDmg = register(new Setting("IgnoreSelfDamage", Boolean.FALSE));
         this.maxSelf = register(new Setting("MaxSelfDamage", 5.0F, 36.0F, 0.0F, v -> !this.ignoreSelfDmg.getValue()));
         this.minDmg = register(new Setting("MinDamage", 3.0F, 36.0F, 0.0F));
         this.smartMode = register(new Setting("SmartMode", Boolean.TRUE));
         this.dmgError = register(new Setting("DamageError", 3.0F, 15.0F, 1.0F, v -> this.smartMode.getValue()));
         this.antiSuicide = register(new Setting("AntiSuicide", Boolean.TRUE));
         this.pauseHealth = register(new Setting("RequireHealth", 3.0F, 36.0F, 0.0F));
         this.betterFps = register(new Setting("BetterFps", Boolean.TRUE));
         this.color = register(new Setting("Color", new Color(230, 50, 50, 100)));
         this.avoidFriends = register(new Setting("AvoidFriends", Boolean.TRUE));
         this.lastEntityID = -1;
         this.placeTimer = new Timer();
         this.breakTimer = new Timer();
         this.oldhand = null;
         this.oldslot = -1;
     }
     public void onTick() {
         doCrystalAura();
     }
     public void doCrystalAura() {
         try {
             if (nullCheck())
                 return;    this.target = getNearestEnemy(35.0D);
             if (this.target == null) {
                 this.renderPos = null;
                 return;
             }
             if (this.pauseHealth.getValue() > mc.player.getHealth()) {
                 this.renderPos = null;
                 return;
             }
             if (this.place.getValue()) doPlace();
             if (this.explode.getValue()) doBreak();
         } catch (Exception ignored) {}
     }
     public void doPlace() {
         EnumHand hand;
         if (!this.placeTimer.passedDms(this.placeDelay.getValue()))
             return;    this.placeTimer.reset();
         if (this.autoSwitch.getValue()) {
             if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                 hand = EnumHand.OFF_HAND;
             } else {
                 int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
                 if (crystalSlot == -1) {
                     this.renderPos = null;
                     return;
                 }
                 setItem(crystalSlot);
                 hand = EnumHand.MAIN_HAND;
             }
         } else if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
             hand = EnumHand.MAIN_HAND;
         } else {
             if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                 this.renderPos = null;
                 return;
             }
             hand = EnumHand.OFF_HAND;
         }
         double maxDmg = 0.0D;
         BlockPos bestPos = null;
         for (BlockPos pos : CrystalUtil.possiblePlacePositions(this.placeRange.getValue(), true, this.opPlace.getValue())) {
             if (CrystalUtil.canSeePos(pos) && PlayerUtil.getDistance(pos) > this.wallRangePlace.getValue())
                 continue;    if (this.avoidFriends.getValue()) {
                 EntityPlayer near = getClosestEnemy(pos, 4.0D);
                 if (near == null)
                     continue;
             }    double selfDamage = CrystalUtil.calculateDamage(pos.getX() + 0.5D, (pos.getY() + 1), pos.getZ() + 0.5D, mc.player);
             if (selfDamage > this.maxSelf.getValue() && !this.ignoreSelfDmg.getValue())
                 continue;    double enemyDamage = CrystalUtil.calculateDamage(pos.getX() + 0.5D, (pos.getY() + 1), pos.getZ() + 0.5D, this.target);
             if (enemyDamage >= this.minDmg.getValue() && (
                 selfDamage <= enemyDamage || Math.abs(selfDamage - enemyDamage) < this.dmgError.getValue() || !this.smartMode.getValue()) &&
                 enemyDamage > maxDmg) {
                 maxDmg = enemyDamage;
                 bestPos = pos;
             }
         }
         if (bestPos == null) {
             this.renderPos = null;
             restoreItem();
             return;
         }
         this.renderPos = bestPos;
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bestPos, EnumFacing.UP, hand, 0.5F, 0.5F, 0.5F));
         if (this.placeSwing.getValue()) {
             mc.player.connection.sendPacket(new CPacketAnimation(hand));
         }
         if (this.predictHit.getValue() && this.packetBreak.getValue() && this.lastEntityID != -1) {
             for (int i = this.amountOffset.getValue(); i < this.amount.getValue(); i++) {
                 Entity e = mc.world.getEntityByID(this.lastEntityID + i);
                 if (e instanceof EntityEnderCrystal) {
                     mc.player.connection.sendPacket(new CPacketUseEntity(e));
                     EnumHand h = (this.swing.getValue() == swingArm.Mainhand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                     if (this.swing.getValue() != swingArm.None) {
                         if (this.packetSwing.getValue()) { mc.player.connection.sendPacket(new CPacketAnimation(h)); }
                         else { mc.player.swingArm(h); }
                     }
                 }
             }
         }
         restoreItem();
     }
     public void doBreak() {
         if (!this.breakTimer.passedDms(this.explodeDelay.getValue()))
             return;    this.breakTimer.reset();
         AxisAlignedBB box = new AxisAlignedBB(mc.player.posX - this.breakRange.getValue(), mc.player.posY - this.breakRange.getValue(), mc.player.posZ - this.breakRange.getValue(), mc.player.posX + this.breakRange.getValue(), mc.player.posY + this.breakRange.getValue(), mc.player.posZ + this.breakRange.getValue());
         List<EntityEnderCrystal> crystals = mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, box);
         EntityEnderCrystal bestCrystal = crystals.stream().filter(c -> isValidCrystal(c.posX, c.posY, c.posZ)).min(Comparator.comparingDouble(PlayerUtil::getDistance)).orElse(null);
         if (bestCrystal == null)
             return;
         if (this.packetBreak.getValue()) {
             mc.player.connection.sendPacket(new CPacketUseEntity(bestCrystal));
         } else {
             mc.playerController.attackEntity(mc.player, bestCrystal);
         }
         EnumHand hand = (this.swing.getValue() == swingArm.Mainhand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
         if (this.swing.getValue() != swingArm.None) {
             if (this.packetSwing.getValue()) { mc.player.connection.sendPacket(new CPacketAnimation(hand)); }
             else { mc.player.swingArm(hand); }
         }
     }
     @SubscribeEvent(priority = EventPriority.HIGH)
     public void onPacketReceive(PacketEvent.Receive event) {
         if (event.getPacket() instanceof SPacketSpawnObject) {
             SPacketSpawnObject p = (SPacketSpawnObject)event.getPacket();
             if (p.getType() == 51 && this.explode.getValue() && this.predict.getValue() && this.packetBreak.getValue() && this.target != null) {
                 this.lastEntityID = p.getEntityID();
                 if (!isValidCrystal(p.getX(), p.getY(), p.getZ()))
                     return;    for (Entity entity : mc.world.loadedEntityList) {
                     if (entity instanceof EntityEnderCrystal) {
                         double dx = entity.posX - p.getX();
                         double dy = entity.posY - p.getY();
                         double dz = entity.posZ - p.getZ();
                         if (dx * dx + dy * dy + dz * dz < 1.0D) {
                             mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                             EnumHand hand = (this.swing.getValue() == swingArm.Mainhand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                             if (this.swing.getValue() != swingArm.None) {
                                 if (this.packetSwing.getValue()) { mc.player.connection.sendPacket(new CPacketAnimation(hand)); break; }
                                    mc.player.swingArm(hand);
                             }
                             break;
                         }
                     }
                 }
             }
         }
         if (this.checkOtherEntity.getValue()) {
             if (event.getPacket() instanceof SPacketSpawnExperienceOrb) this.lastEntityID = ((SPacketSpawnExperienceOrb)event.getPacket()).getEntityID();
             if (event.getPacket() instanceof SPacketSpawnMob) this.lastEntityID = ((SPacketSpawnMob)event.getPacket()).getEntityID();
             if (event.getPacket() instanceof SPacketSpawnPainting) this.lastEntityID = ((SPacketSpawnPainting)event.getPacket()).getEntityID();
             if (event.getPacket() instanceof SPacketSpawnPlayer) this.lastEntityID = ((SPacketSpawnPlayer)event.getPacket()).getEntityID();
         }
     }
     public boolean isValidCrystal(double posX, double posY, double posZ) {
         if (this.target == null) return false;
         if (this.avoidFriends.getValue() && FriendManager.isFriend(this.target.getName())) return false;
         BlockPos pos = new BlockPos(posX, posY, posZ);
         if (PlayerUtil.getDistance(pos) > this.breakRange.getValue()) return false;
         if (CrystalUtil.canSeePos(pos) && PlayerUtil.getDistance(pos) > this.wallRangeBreak.getValue()) return false;
         double selfDamage = CrystalUtil.calculateDamage(posX, posY, posZ, mc.player);
         if (selfDamage > this.maxSelf.getValue() && !this.ignoreSelfDmg.getValue()) return false;
         if (mc.player.getHealth() - selfDamage <= 0.0D && this.antiSuicide.getValue()) return false;
         double enemyDamage = CrystalUtil.calculateDamage(posX, posY, posZ, this.target);
         return (enemyDamage >= this.minDmg.getValue() && (selfDamage <= enemyDamage || Math.abs(selfDamage - enemyDamage) < this.dmgError.getValue() || !this.smartMode.getValue()));
     }
     public void setItem(int slot) {
         if (!this.autoSwitch.getValue())
             return;    if (slot < 0 || slot > 8)
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
     public void onRender3D() {
         if (this.renderPos != null) {
             RenderUtil3D.drawBox(this.renderPos, 1.0D, this.color.getValue(), 63);
         }
     }
     private EntityPlayer getNearestEnemy(double maxRange) {
         List<EntityPlayer> list = mc.world.playerEntities.stream().filter(p -> (p != null && p != mc.player && !p.isDead && !FriendManager.isFriend(p.getName()))).filter(p -> (PlayerUtil.getDistance(p) <= maxRange)).sorted(Comparator.comparingDouble(PlayerUtil::getDistance)).collect(Collectors.toList());
         return list.isEmpty() ? null : list.get(0);
     }
     private EntityPlayer getClosestEnemy(BlockPos pos, double radius) {
         double r2 = radius * radius;
         EntityPlayer closest = null;
         double best = Double.MAX_VALUE;
         for (EntityPlayer p : mc.world.playerEntities) {
             if (p == null || p == mc.player || p.isDead ||
                 FriendManager.isFriend(p.getName()))
                 continue;    double d = p.getDistanceSq(pos);
             if (d <= r2 && d < best) {
                 best = d;
                 closest = p;
             }
         }
         return closest;
     }
     public enum swingArm {
         Mainhand,
         Offhand,
         None
}
     public static class CrystalPos {
         public final BlockPos pos;
         public final double dmg;
         public CrystalPos(BlockPos pos, double dmg) {
             this.pos = pos;
             this.dmg = dmg;
         }
     }
 }