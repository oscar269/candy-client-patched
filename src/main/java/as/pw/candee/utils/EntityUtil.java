 package as.pw.candee.utils;
 import java.util.ArrayList;
 import java.util.List;
 import net.minecraft.block.Block;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.enchantment.Enchantment;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.MoverType;
 import net.minecraft.entity.monster.EntityIronGolem;
 import net.minecraft.entity.passive.EntityWolf;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 import net.minecraft.nbt.NBTTagList;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketUseEntity;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3d;
 public class EntityUtil
     implements Util
 {
     public static BlockPos getFlooredPos(Entity e) {
         return new BlockPos(Math.floor(e.posX), Math.floor(e.posY), Math.floor(e.posZ));
     }
     public static Entity getPredictedPosition(Entity entity, double x) {
         if (x == 0.0D) {
             return entity;
         }
         EntityPlayer e = null;
         double motionX = entity.posX - entity.lastTickPosX;
         double motionY = entity.posY - entity.lastTickPosY;
         double motionZ = entity.posZ - entity.lastTickPosZ;
         boolean shouldPredict = false;
         boolean shouldStrafe = false;
         double motion = Math.sqrt(Math.pow(motionX, 2.0D) + Math.pow(motionZ, 2.0D) + Math.pow(motionY, 2.0D));
         if (motion > 0.1D) {
             shouldPredict = true;
         }
         if (!shouldPredict) {
             return entity;
         }
         if (motion > 0.31D) {
             shouldStrafe = true;
         }
         for (int i = 0; i < x; i++) {
             if (e == null) {
                 if (isOnGround(0.0D, 0.0D, 0.0D, entity)) {
                     motionY = shouldStrafe ? 0.4D : -0.07840015258789D;
                 } else {
                     motionY -= 0.08D;
                     motionY *= 0.9800000190734863D;
                 }
                 e = placeValue(motionX, motionY, motionZ, (EntityPlayer)entity);
             } else {
                 if (isOnGround(0.0D, 0.0D, 0.0D, e)) {
                     motionY = shouldStrafe ? 0.4D : -0.07840015258789D;
                 } else {
                     motionY -= 0.08D;
                     motionY *= 0.9800000190734863D;
                 }
                 e = placeValue(motionX, motionY, motionZ, e);
             }
         }
         return e;
     }
     public static boolean isOnGround(double x, double y, double z, Entity entity) {
         double d3 = y;
         List<AxisAlignedBB> list1 = (Minecraft.getMinecraft()).world.getCollisionBoxes(entity, entity.getEntityBoundingBox().offset(x, y, z));
         if (y != 0.0D) {
             for (int l = list1.size(), k = 0; k < l; k++) {
                 y = list1.get(k).calculateYOffset(entity.getEntityBoundingBox(), y);
             }
         }
         return (d3 != y && d3 < 0.0D);
     }
     public static EntityPlayer placeValue(double x, double y, double z, EntityPlayer entity) {
         List<AxisAlignedBB> list1 = (Minecraft.getMinecraft()).world.getCollisionBoxes(entity, entity.getEntityBoundingBox().offset(x, y, z));
         if (y != 0.0D) {
             for (int l = list1.size(), k = 0; k < l; k++) {
                 y = list1.get(k).calculateYOffset(entity.getEntityBoundingBox(), y);
             }
             if (y != 0.0D) {
                 entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0D, y, 0.0D));
             }
         }
         if (x != 0.0D) {
             for (int l2 = list1.size(), j5 = 0; j5 < l2; j5++) {
                 x = calculateXOffset(entity.getEntityBoundingBox(), x, list1.get(j5));
             }
             if (x != 0.0D) {
                 entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
             }
         }
         if (z != 0.0D) {
             for (int i6 = list1.size(), k2 = 0; k2 < i6; k2++) {
                 z = calculateZOffset(entity.getEntityBoundingBox(), z, list1.get(k2));
             }
             if (z != 0.0D) {
                 entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0D, 0.0D, z));
             }
         }
         return entity;
     }
     public static boolean basicChecksEntity(EntityPlayer pl) {
         return (pl == null || pl.getName().equals(mc.player.getName()) || pl.isDead || pl.getHealth() + pl.getAbsorptionAmount() <= 0.0F || pl.isCreative());
     }
     public static void motion(double x, double y, double z) {
         EntityPlayerSP player = mc.player;
         double dist = 5.0D;
         for (int i = 0; i < 20 && dist > 1.0D; i++) {
             double dx = x - player.posX;
             double dy = y - player.posY;
             double dz = z - player.posZ;
             double hdist = Math.sqrt(dx * dx + dz * dz);
             double rx = Math.atan2(dx, dz);
             double ry = Math.atan2(dy, hdist);
             dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
             double o = Math.min(dist, 1.0D);
             Vec3d vec = new Vec3d(Math.sin(rx) * Math.cos(ry) * o, o * Math.sin(ry), Math.cos(rx) * Math.cos(ry) * o);
             mc.player.move(MoverType.SELF, vec.x, vec.y, vec.z);
             mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
         }
     }
     public static BlockPos getPosition(Entity pl) {
         return new BlockPos(Math.floor(pl.posX), Math.floor(pl.posY + 0.5D), Math.floor(pl.posZ));
     }
     public static List<BlockPos> getBlocksIn(Entity pl) {
         List<BlockPos> blocks = new ArrayList<>();
         AxisAlignedBB bb = pl.getEntityBoundingBox();
         for (double x = Math.floor(bb.minX); x < Math.ceil(bb.maxX); x++) {
             double y; for (y = Math.floor(bb.minY); y < Math.ceil(bb.maxY); y++) {
                 double z; for (z = Math.floor(bb.minZ); z < Math.ceil(bb.maxZ); z++) {
                     blocks.add(new BlockPos(x, y, z));
                 }
             }
         }
         return blocks;
     }
     public static double calculateXOffset(AxisAlignedBB other, double offsetX, AxisAlignedBB this1) {
         if (other.maxY > this1.minY && other.minY < this1.maxY && other.maxZ > this1.minZ && other.minZ < this1.maxZ) {
             if (offsetX > 0.0D && other.maxX <= this1.minX) {
                 double d1 = this1.minX - other.maxX;
                 if (d1 < offsetX) {
                     offsetX = d1;
                 }
             } else {
                 double d2;
                 if (offsetX < 0.0D && other.minX >= this1.maxX && (d2 = this1.maxX - other.minX) > offsetX) {
                     offsetX = d2;
                 }
             }
         }
         return offsetX;
     }
     public static double calculateZOffset(AxisAlignedBB other, double offsetZ, AxisAlignedBB this1) {
         if (other.maxX > this1.minX && other.minX < this1.maxX && other.maxY > this1.minY && other.minY < this1.maxY) {
             if (offsetZ > 0.0D && other.maxZ <= this1.minZ) {
                 double d1 = this1.minZ - other.maxZ;
                 if (d1 < offsetZ) {
                     offsetZ = d1;
                 }
             } else {
                 double d2;
                 if (offsetZ < 0.0D && other.minZ >= this1.maxZ && (d2 = this1.maxZ - other.minZ) > offsetZ) {
                     offsetZ = d2;
                 }
             }
         }
         return offsetZ;
     }
     public static boolean isLiving(Entity e) {
         return e instanceof EntityLivingBase;
     }
     public static boolean isPassive(Entity e) {
         return ((!(e instanceof EntityWolf) || !((EntityWolf)e).isAngry()) && (e instanceof net.minecraft.entity.passive.EntityAmbientCreature || e instanceof net.minecraft.entity.EntityAgeable || e instanceof net.minecraft.entity.passive.EntityTameable || e instanceof net.minecraft.entity.passive.EntitySquid || (e instanceof EntityIronGolem && ((EntityIronGolem) e).getAttackTarget() == null)));
     }
     public static void attackEntity(Entity entity, boolean packet) {
         if (packet) {
             mc.player.connection.sendPacket(new CPacketUseEntity(entity));
         } else {
             mc.playerController.attackEntity(mc.player, entity);
         }
     }
     public static float getHealth(Entity entity) {
         if (entity.isEntityAlive()) {
             EntityLivingBase livingBase = (EntityLivingBase)entity;
             return livingBase.getHealth() + livingBase.getAbsorptionAmount();
         }
         return 0.0F;
     }
     public static Block isColliding(double posX, double posY, double posZ) {
         Block block = null;
         if (mc.player != null) {
             AxisAlignedBB bb = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0D, 0.0D, 0.0D).offset(posX, posY, posZ) : mc.player.getEntityBoundingBox().contract(0.0D, 0.0D, 0.0D).offset(posX, posY, posZ);
             int y = (int)bb.minY;
             for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
                 for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
                     block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                 }
             }
         }
         return block;
     }
     public static boolean isInLiquid() {
         if (mc.player == null) {
             return false;
         }
         if (mc.player.fallDistance >= 3.0F) {
             return false;
         }
         boolean inLiquid = false;
         AxisAlignedBB bb = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().getEntityBoundingBox() : mc.player.getEntityBoundingBox();
         int y = (int)bb.minY;
         for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
             for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
                 Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                 if (!(block instanceof net.minecraft.block.BlockAir)) {
                     if (!(block instanceof net.minecraft.block.BlockLiquid)) {
                         return false;
                     }
                     inLiquid = true;
                 }
             }
         }
         return inLiquid;
     }

     public static boolean isInWeb() {
            if (mc.player == null) {
                return false;
            }
            boolean inWeb = false;
            AxisAlignedBB bb = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().getEntityBoundingBox() : mc.player.getEntityBoundingBox();
            int y = (int)bb.minY;
            for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
                for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
                    Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (!(block instanceof net.minecraft.block.BlockAir)) {
                        if (!(block instanceof net.minecraft.block.BlockWeb)) {
                            return false;
                        }
                        inWeb = true;
                    }
                }
            }
            return inWeb;
     }

     public static boolean isFullArmor(EntityPlayer entity) {
         boolean fullArmor = true;
         int diamondItems = 0;
         boolean hasBlast = false;
         for (ItemStack is : entity.getArmorInventoryList()) {
             if (is.isEmpty()) {
                 fullArmor = false;
                 break;
             }
             if (is.getItem() == Items.DIAMOND_HELMET) {
                 diamondItems++;
             }
             if (is.getItem() == Items.DIAMOND_CHESTPLATE) {
                 diamondItems++;
             }
             if (is.getItem() == Items.DIAMOND_LEGGINGS) {
                 diamondItems++;
             }
             if (is.getItem() == Items.DIAMOND_BOOTS) {
                 diamondItems++;
             }
             NBTTagList enchants = is.getEnchantmentTagList();
             List<String> enchantments = new ArrayList<>();
             if (enchants != null) {
                 for (int index = 0; index < enchants.tagCount(); index++) {
                     short id = enchants.getCompoundTagAt(index).getShort("id");
                     short level = enchants.getCompoundTagAt(index).getShort("lvl");
                     Enchantment enc = Enchantment.getEnchantmentByID(id);
                     if (enc != null) {
                         enchantments.add(enc.getTranslatedName(level));
                     }
                 }
             }
             if (!enchantments.contains("Blast Protection IV")) {
                 continue;
             }
             hasBlast = true;
         }
         return (fullArmor && diamondItems == 4 && hasBlast);
     }
     public static boolean isInHole(Entity entity) {
         return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
     }
     public static boolean isBlockValid(BlockPos blockPos) {
         return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
     }
     public static boolean isObbyHole(BlockPos blockPos) {
         BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
                 for (BlockPos pos : array) {
             IBlockState touchingState = mc.world.getBlockState(pos);
             if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                 return false;
             }
         }
         return true;
     }
     public static boolean isBedrockHole(BlockPos blockPos) {
         BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
                 for (BlockPos pos : array) {
             IBlockState touchingState = mc.world.getBlockState(pos);
             if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                 return false;
             }
         }
         return true;
     }
     public static boolean isBothHole(BlockPos blockPos) {
         BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
                 for (BlockPos pos : array) {
             IBlockState touchingState = mc.world.getBlockState(pos);
             if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)) {
                 return false;
             }
         }
         return true;
     }
 }