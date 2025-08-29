 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemSword;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
 public class Aura
     extends Module
 {
     public final Setting<Boolean> targetPlayer;
     public final Setting<Boolean> targetMob;
     public final Setting<Boolean> targetPassive;
     public final Setting<Boolean> targetNeutral;
     public final Setting<Boolean> targetHostile;
     public final Setting<Float> range;
     public final Setting<Float> wallRange;
     public final Setting<Boolean> preferAxe;
     public final Setting<Float> enemyHp;
     public final Setting<Float> minSelfHp;
     public final Setting<Integer> delayMs;
     public final Setting<Boolean> autoSwap;
     public final Setting<Boolean> swordOnly;
     public final Setting<Boolean> criticals;
     public final Setting<Integer> critPackets;
     private long lastAttackTime = 0L;
     public Aura() {
         super("Aura", Categories.COMBAT, false, false);
         this.targetPlayer = register(new Setting("Player", Boolean.TRUE));
         this.targetMob = register(new Setting("Mob", Boolean.FALSE));
         this.targetPassive = register(new Setting("Passive", Boolean.FALSE));
         this.targetNeutral = register(new Setting("Neutral", Boolean.FALSE));
         this.targetHostile = register(new Setting("Hostile", Boolean.FALSE));
         this.range = register(new Setting("Range", 4.5F, 10.0F, 1.0F));
         this.wallRange = register(new Setting("WallRange", 3.0F, 6.0F, 1.0F));
         this.preferAxe = register(new Setting("PreferAxe", Boolean.TRUE));
         this.enemyHp = register(new Setting("EnemyHP", 6.0F, 20.0F, 0.0F));
         this.minSelfHp = register(new Setting("MinSelfHP", 6.0F, 20.0F, 0.0F));
         this.delayMs = register(new Setting("DelayMs", 50, 1000, 0));
         this.autoSwap = register(new Setting("AutoSwap", Boolean.TRUE));
         this.swordOnly = register(new Setting("SwordOnly", Boolean.TRUE));
         this.criticals = register(new Setting("Criticals", Boolean.TRUE));
         this.critPackets = register(new Setting("CritPackets", 2, 1, 4));
     }
     public void onEnable() {
         if (this.autoSwap.getValue()) {
             int swordSlot = findWeaponSlot(ItemSword.class);
             if (swordSlot != -1) {
                 mc.player.inventory.currentItem = swordSlot;
                 mc.playerController.updateController();
             }
         }
     }
     public void onUpdate() {
         EntityLivingBase target;
         if (mc.player == null || mc.world == null)
             return;
         if (mc.player.getHealth() <= this.minSelfHp.getValue())
             return;    if (this.swordOnly.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword))
             return;
         List<EntityLivingBase> possibleTargets = new ArrayList<>();
         EntityLivingBase lowHpTarget = null;
         List<EntityLivingBase> entities = mc.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(mc.player.posX - this.range
.getValue(), mc.player.posY - this.range
.getValue(), mc.player.posZ - this.range
.getValue(), mc.player.posX + this.range
.getValue(), mc.player.posY + this.range
.getValue(), mc.player.posZ + this.range
.getValue()));
         for (EntityLivingBase entity : entities) {
             if (entity == mc.player ||
                 !isValidTarget(entity))
                 continue;
             double distSq = mc.player.getDistanceSq(entity);
             if (distSq > (this.range.getValue() * this.range.getValue()) || (
                 !canSeeEntity(entity) && distSq > (this.wallRange.getValue() * this.wallRange.getValue())))
                 continue;
             possibleTargets.add(entity);
             if (entity.getHealth() < this.enemyHp.getValue()) {
                 lowHpTarget = entity;
                 break;
             }
         }
         if (lowHpTarget != null) {
             target = lowHpTarget;
         } else if (!possibleTargets.isEmpty()) {
             possibleTargets.sort(Comparator.comparingDouble(mc.player::getDistanceSq));
             target = possibleTargets.get(0);
         } else {
             return;
         }
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.lastAttackTime < this.delayMs.getValue()) {
             return;
         }
         if (this.criticals.getValue() && mc.player.onGround &&
             !mc.player.isInWater() &&
             !mc.player.isInLava()) {
             sendCriticalPackets(this.critPackets.getValue());
         }
         mc.playerController.attackEntity(mc.player, target);
         mc.player.swingArm(EnumHand.MAIN_HAND);
         this.lastAttackTime = currentTime;
     }
     private void sendCriticalPackets(int mode) {
         switch (mode) {
             case 1:
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.10000000149011612D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 break;
             case 2:
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1E-5D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 break;
             case 3:
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625101D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0125D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 break;
             case 4:
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1625D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 4.0E-6D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0E-6D, mc.player.posZ, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                 break;
         }
     }
     private boolean isValidTarget(EntityLivingBase entity) {
         if (entity == null || entity.isDead || !entity.isEntityAlive()) return false;
         if (entity instanceof EntityPlayer) {
             if (!this.targetPlayer.getValue()) return false;
             if (entity == mc.player) return false;
             return !FriendManager.isFriend(entity.getName());
         }
         return this.targetMob.getValue();
     }
     private boolean canSeeEntity(EntityLivingBase entity) {
         Vec3d eyesPos = mc.player.getPositionEyes(1.0F);
         Vec3d entityPos = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
         RayTraceResult result = mc.world.rayTraceBlocks(eyesPos, entityPos, false, true, false);
         return (result == null || result.typeOfHit == RayTraceResult.Type.MISS);
     }
     private int findWeaponSlot(Class<?> weaponClass) {
         for (int i = 0; i < 9; i++) {
             if (weaponClass.isAssignableFrom(mc.player.inventory.getStackInSlot(i).getItem().getClass())) {
                 return i;
             }
         }
         return -1;
     }
 }