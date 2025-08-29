package as.pw.candee.module.combat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import as.pw.candee.managers.FriendManager;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.MathUtil;
import as.pw.candee.utils.Timer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
public class BedAura extends Module {
        public final Setting<Boolean> rotate = register(new Setting("Rotate", Boolean.FALSE));
        public final Setting<Boolean> swing = register(new Setting("Swing", Boolean.TRUE));
        public final Setting<Boolean> airPlace = register(new Setting("AirPlace", Boolean.FALSE));
        public final Setting<Float> breakRange = register(new Setting("BreakRange", 7.0F, 7.0F, 0.1F));
        public final Setting<Float> placeRange = register(new Setting("PlaceRange", 7.0F, 7.0F, 0.1F));
        public final Setting<Integer> breakSpeed = register(new Setting("BreakSpeed", 20, 20, 1));
        public final Setting<Integer> placeSpeed = register(new Setting("PlaceSpeed", 20, 20, 1));
        public final Setting<Boolean> autoSwitch = register(new Setting("AutoSwitch", Boolean.TRUE));
        public final Setting<Boolean> autoMove = register(new Setting("AutoMove", Boolean.TRUE));
        private final Timer hitTimer = new Timer();
        private final Timer placeTimer = new Timer();
        private BlockPos breakPos = null;
        private BlockPos finalPos = null;
        private EnumFacing finalFacing = null;
        private int priorSlot = -1;
        private static boolean isSpoofingAngles;
        private static double yaw;
        private static double pitch;
        public BedAura() {
                super("BedAura", Categories.COMBAT, false, false);
        }
        public void onTick() {
                if (mc.player == null || mc.world == null)
                        return;    this.breakPos = null;
                this.finalPos = null;
                if (mc.player.dimension == 0)
                        return;    if (this.hitTimer.passedMs((1000 - this.breakSpeed.getValue() * 50L))) {
                        this.breakPos = findBedTarget();
                }
                if (this.breakPos == null && this.placeTimer.passedMs((1000 - this.placeSpeed.getValue() * 50L))) {
                        if (mc.player.inventory.getCurrentItem().getItem() == Items.BED || isOffhand()) {
                                findPlaceTarget();
                        } else if (!getTargets().isEmpty() && this.autoSwitch.getValue() && !isOffhand()) {
                                int i; for (i = 0; i < 9; i++) {
                                        ItemStack stack = mc.player.inventory.mainInventory.get(i);
                                        if (stack.getItem() == Items.BED) {
                                                this.priorSlot = mc.player.inventory.currentItem;
                                                mc.player.inventory.currentItem = i;
                                                mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                                                findPlaceTarget();
                                                break;
                                        }
                                }
                                if (this.autoMove.getValue() && mc.player.inventory.getCurrentItem().getItem() != Items.BED) {
                                        for (i = 9; i <= 35; i++) {
                                                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                                                        mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
                                                        mc.playerController.windowClick(0, (mc.player.inventory.currentItem < 9) ? (mc.player.inventory.currentItem + 36) : mc.player.inventory.currentItem, 0, ClickType.PICKUP, mc.player);
                                                        mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
                                                }
                                        }
                                }
                        }
                } else if (this.breakPos != null) {
                        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(1.0F), (new Vec3d(this.breakPos)).add(0.5D, 0.5D, 0.5D));
                        yaw = angle[0];
                        pitch = angle[1];
                        isSpoofingAngles = true;
                }
                if (isSpoofingAngles) {
                        mc.player.rotationYaw = (float)yaw;
                        mc.player.rotationPitch = (float)pitch;
                }
        }
        public void onUpdate() {
                if (this.breakPos != null) {
                        breakBed(this.breakPos);
                } else if (this.finalPos != null) {
                        placeBed();
                }
                if (this.priorSlot != -1 && !isOffhand()) {
                        mc.player.inventory.currentItem = this.priorSlot;
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.priorSlot));
                        this.priorSlot = -1;
                }
        }
        public boolean isOffhand() {
                return mc.player.getHeldItemOffhand().getItem() instanceof net.minecraft.item.ItemBed;
        }
        private void breakBed(BlockPos bed) {
                if (bed == null)
                        return;    Vec3d hitVec = (new Vec3d(bed)).add(0.5D, 0.5D, 0.5D);
                EnumFacing facing = EnumFacing.UP;
                mc.playerController.processRightClickBlock(mc.player, mc.world, bed, facing, hitVec, EnumHand.MAIN_HAND);
                if (this.swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
                this.hitTimer.reset();
        }
        private void placeBed() {
                Vec3d hitVec = (new Vec3d(this.finalPos.down())).add(0.5D, 0.5D, 0.5D).add((new Vec3d(this.finalFacing.getOpposite().getDirectionVec())).scale(0.5D));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, this.finalPos.down(), EnumFacing.UP, hitVec, isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.placeTimer.reset();
                this.finalPos = null;
        }
        private BlockPos findBedTarget() {
                TileEntityBed bed = mc.world.loadedTileEntityList.stream()
                        .filter(e -> e instanceof TileEntityBed)
                        .map(e -> (TileEntityBed)e)
                        .filter(e -> e.isHeadPiece())
                        .filter(e -> (mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= this.breakRange.getValue()))
                        .filter(e -> suicideCheck(e.getPos())).min(Comparator.comparing(e -> mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ())))
                        .orElse(null);
                if (bed != null) {
                        return bed.getPos();
                }
                return null;
        }
        private void findPlaceTarget() {
                List<EntityPlayer> targets = getTargets();
                if (targets.isEmpty())
                        return;    checkTarget(new BlockPos(targets.get(0)), true);
        }
        private void checkTarget(BlockPos pos, boolean firstCheck) {
                if (mc.world.getBlockState(pos).getBlock() == Blocks.BED)
                        return;    float damage = calculateDamage(pos, mc.player);
                if (damage > mc.player.getHealth() + mc.player.getAbsorptionAmount() + 0.5D) {
                        if (firstCheck && this.airPlace.getValue()) {
                                checkTarget(pos.up(), false);
                        }
                        return;
                }
                if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
                        if (firstCheck && this.airPlace.getValue()) {
                                checkTarget(pos.up(), false);
                        }
                        return;
                }
                ArrayList<BlockPos> positions = new ArrayList<>();
                HashMap<BlockPos, EnumFacing> facings = new HashMap<>();
                for (EnumFacing facing : EnumFacing.values()) {
                        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
                                BlockPos position = pos.offset(facing);
                                if (mc.player.getDistanceSq(position) <= Math.pow(this.placeRange.getValue(), 2.0D) &&
                                        mc.world.getBlockState(position).getMaterial().isReplaceable() &&
                                        !mc.world.getBlockState(position.down()).getMaterial().isReplaceable())
                                { positions.add(position);
                                        facings.put(position, facing.getOpposite()); }
                        }
                }    if (positions.isEmpty()) {
                        if (firstCheck && this.airPlace.getValue()) {
                                checkTarget(pos.up(), false);
                        }
                        return;
                }
                positions.sort(Comparator.comparingDouble(pos2 -> mc.player.getDistanceSq(pos2)));
                this.finalPos = positions.get(0);
                this.finalFacing = facings.get(this.finalPos);
                float[] rotation = { mc.player.rotationYaw, mc.player.rotationPitch };
                if (this.rotate.getValue()) {
                        rotation = calculateAngle(mc.player.getPositionEyes(1.0F), new Vec3d(this.finalPos.down().getX() + 0.5D, (this.finalPos.down().getY() + 1), this.finalPos.down().getZ() + 0.5D));
                }
                yaw = rotation[0];
                pitch = rotation[1];
                isSpoofingAngles = true;
        }
        public List<EntityPlayer> getTargets() {
                return mc.world.playerEntities.stream()
                        .filter(e -> !e.isDead)
                        .filter(e -> !FriendManager.isFriend(e.getName()))
                        .filter(e -> (e != mc.player))
                        .filter(e -> (mc.player.getDistance(e) < this.placeRange.getValue() + 2.0F))
                        .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                        .collect(Collectors.toList());
        }
        private boolean suicideCheck(BlockPos pos) {
                return ((mc.player.getHealth() + mc.player.getAbsorptionAmount() - calculateDamage(pos.getX() + 0.5D, (pos.getY() + 1), pos.getZ() + 0.5D, mc.player)) > 0.5D);
        }
        public float calculateDamage(BlockPos bedPos, Entity entity) {
                return calculateDamage(bedPos.getX() + 0.5D, bedPos.getY() + 1.0D, bedPos.getZ() + 0.5D, entity);
        }
        public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
                float doubleExplosionSize = 12.0F;
                double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
                Vec3d vec3d = new Vec3d(posX, posY, posZ);
                double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                double v = (1.0D - distancedsize) * blockDensity;
                float damage = (int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D);
                double finald = 1.0D;
                if (entity instanceof EntityLivingBase) {
                        finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
                }
                return (float)finald;
        }
        public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
                if (entity instanceof EntityPlayer) {
                        EntityPlayer ep = (EntityPlayer)entity;
                        DamageSource ds = DamageSource.causeExplosionDamage(explosion);
                        damage = CombatRules.getDamageAfterAbsorb(damage, ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                        int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
                        float f = MathHelper.clamp(k, 0.0F, 20.0F);
                        damage *= 1.0F - f / 25.0F;
                        if (entity.isPotionActive(Potion.getPotionById(11))) {
                                damage -= damage / 4.0F;
                        }
                        return damage;
                }
                damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                return damage;
        }
        private static float getDamageMultiplied(float damage) {
                int diff = mc.world.getDifficulty().getId();
                return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
        }
        public static float[] calculateAngle(Vec3d from, Vec3d to) {
                double diffX = to.x - from.x;
                double diffY = to.y - from.y;
                double diffZ = to.z - from.z;
                double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
                float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
                float pitch = (float)-Math.toDegrees(Math.atan2(diffY, dist));
                return new float[] { yaw, pitch };
        }
}