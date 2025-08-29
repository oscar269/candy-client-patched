 package as.pw.candee.utils;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import net.minecraft.block.Block;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.MoverType;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.potion.Potion;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3d;
 import org.apache.logging.log4j.Level;
 public class PlayerUtil
     implements Util
 {
     public static int getHealth(EntityPlayer player) {
         return (int)(player.getHealth() + player.getAbsorptionAmount());
     }
     public static BlockPos getPlayerPos() {
         return new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
     }
     public static BlockPos getPlayerPos(EntityPlayer player) {
         return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
     }
     public static EntityPlayer getNearestPlayer(double range) {
         return mc.world.playerEntities.stream().filter(p -> (mc.player.getDistance(p) <= range)).filter(p -> (mc.player.getEntityId() != p.getEntityId())).min(Comparator.comparing(p -> mc.player.getDistance(p))).orElse(null);
     }
     public static EntityPlayer getLookingPlayer(double range) {
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         for (int i = 0; i < players.size(); i++) {
             if (getDistance(players.get(i)) > range) {
                 players.remove(i);
             }
         }
         players.remove(mc.player);
         EntityPlayer target = null;
         Vec3d positionEyes = mc.player.getPositionEyes(mc.getRenderPartialTicks());
         Vec3d rotationEyes = mc.player.getLook(mc.getRenderPartialTicks());
         int precision = 2;
         for (int j = 0; j < (int)range; j++) {
             for (int k = 2; k > 0; k--) {
                 for (EntityPlayer targetTemp : players) {
                     AxisAlignedBB playerBox = targetTemp.getEntityBoundingBox();
                     double xArray = positionEyes.x + rotationEyes.x * j + rotationEyes.x / k;
                     double yArray = positionEyes.y + rotationEyes.y * j + rotationEyes.y / k;
                     double zArray = positionEyes.z + rotationEyes.z * j + rotationEyes.z / k;
                     if (playerBox.maxY >= yArray && playerBox.minY <= yArray && playerBox.maxX >= xArray && playerBox.minX <= xArray && playerBox.maxZ >= zArray && playerBox.minZ <= zArray) {
                         target = targetTemp;
                     }
                 }
             }
         }
         return target;
     }
     public static double getDistance(Entity entity) {
         if (entity == null || mc == null || mc.player == null) return Double.MAX_VALUE;
         return mc.player.getDistance(entity);
     }
     public static double getDistance(BlockPos pos) {
         if (pos == null || mc == null || mc.player == null) return Double.MAX_VALUE;
         return mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ());
     }
     public static double getDistanceI(BlockPos pos) {
         if (pos == null || mc == null || mc.player == null) return Double.MAX_VALUE;
         return getPlayerPos(mc.player).getDistance(pos.getX(), pos.getY(), pos.getZ());
     }
     public static BlockPos min(List<BlockPos> posList) {
         return posList.stream().min(Comparator.comparing(PlayerUtil::getDistance)).orElse(null);
     }
     public static Vec3d[] getOffsets(int y, boolean floor) {
         List<Vec3d> offsets = getOffsetList(y, floor);
         Vec3d[] array = new Vec3d[offsets.size()];
         return offsets.toArray(array);
     }
     public static List<Vec3d> getOffsetList(int y, boolean floor) {
         ArrayList<Vec3d> offsets = new ArrayList<>();
         offsets.add(new Vec3d(-1.0D, y, 0.0D));
         offsets.add(new Vec3d(1.0D, y, 0.0D));
         offsets.add(new Vec3d(0.0D, y, -1.0D));
         offsets.add(new Vec3d(0.0D, y, 1.0D));
         if (floor) {
             offsets.add(new Vec3d(0.0D, (y - 1), 0.0D));
         }
         return offsets;
     }
     public static boolean isMoving(EntityLivingBase entity) {
         return (entity.moveForward != 0.0F || entity.moveStrafing != 0.0F);
     }
     public static double getBaseMoveSpeed() {
         double baseSpeed = 0.2873D;
         if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
             int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
             baseSpeed *= 1.0D + 0.2D * (amplifier + 1);
         }
         return baseSpeed;
     }
     public static double[] forward(double speed) {
         float forward = mc.player.movementInput.moveForward;
         float side = mc.player.movementInput.moveStrafe;
         float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
         if (forward != 0.0F) {
             if (side > 0.0F) {
                 yaw += ((forward > 0.0F) ? -45 : 45);
             }
             else if (side < 0.0F) {
                 yaw += ((forward > 0.0F) ? 45 : -45);
             }
             side = 0.0F;
             if (forward > 0.0F) {
                 forward = 1.0F;
             }
             else if (forward < 0.0F) {
                 forward = -1.0F;
             }
         }
         double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
         double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
         double posX = forward * speed * cos + side * speed * sin;
         double posZ = forward * speed * sin - side * speed * cos;
         return new double[] { posX, posZ };
     }
     public static double[] forward(EntityPlayer player, double speed) {
         float forward = player.moveForward;
         float side = player.moveStrafing;
         float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * mc.getRenderPartialTicks();
         if (forward != 0.0F) {
             if (side > 0.0F) {
                 yaw += ((forward > 0.0F) ? -45 : 45);
             }
             else if (side < 0.0F) {
                 yaw += ((forward > 0.0F) ? 45 : -45);
             }
             side = 0.0F;
             if (forward > 0.0F) {
                 forward = 1.0F;
             }
             else if (forward < 0.0F) {
                 forward = -1.0F;
             }
         }
         double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
         double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
         double posX = forward * speed * cos + side * speed * sin;
         double posZ = forward * speed * sin - side * speed * cos;
         return new double[] { posX, posZ };
     }
     public static EnumFacing getLookingFacing() {
         switch (MathHelper.floor((mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 0x7) {
             case 0:
             case 1:
                 return EnumFacing.SOUTH;
             case 2:
             case 3:
                 return EnumFacing.WEST;
             case 4:
             case 5:
                 return EnumFacing.NORTH;
             case 6:
             case 7:
                 return EnumFacing.EAST;
         }
         CandeePlusRewrite.Log(Level.ERROR, "Invalid Rotation");
         return EnumFacing.EAST;
     }
     public static boolean isInsideBlock() {
         try {
             AxisAlignedBB playerBoundingBox = mc.player.getEntityBoundingBox();
             for (int x = MathHelper.floor(playerBoundingBox.minX); x < MathHelper.floor(playerBoundingBox.maxX) + 1; x++) {
                 for (int y = MathHelper.floor(playerBoundingBox.minY); y < MathHelper.floor(playerBoundingBox.maxY) + 1; y++) {
                     for (int z = MathHelper.floor(playerBoundingBox.minZ); z < MathHelper.floor(playerBoundingBox.maxZ) + 1; z++) {
                         Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                         if (!(block instanceof net.minecraft.block.BlockAir)) {
                             AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.world.getBlockState(new BlockPos(x, y, z)), mc.world, new BlockPos(x, y, z)).offset(x, y, z);
                             if (block instanceof net.minecraft.block.BlockHopper) {
                                 boundingBox = new AxisAlignedBB(x, y, z, (x + 1), (y + 1), (z + 1));
                             }
                             if (playerBoundingBox.intersects(boundingBox)) {
                                 return true;
                             }
                         }
                     }
                 }
             }
         } catch (Exception e) {
             return false;
         }
         return false;
     }
     public static void setPosition(double x, double y, double z) {
         mc.player.setPosition(x, y, z);
     }
     public static void setPosition(BlockPos pos) {
         mc.player.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
     }
     public static Vec3d getMotionVector() {
         return new Vec3d(mc.player.motionX, mc.player.motionY, mc.player.motionZ);
     }
     public static void vClip(double d) {
         mc.player.setPosition(mc.player.posX, mc.player.posY + d, mc.player.posZ);
     }
     public static void move(double x, double y, double z) {
         mc.player.move(MoverType.SELF, x, y, z);
     }
     public static void setMotionVector(Vec3d vec) {
         mc.player.motionX = vec.x;
         mc.player.motionY = vec.y;
         mc.player.motionZ = vec.z;
     }
 }