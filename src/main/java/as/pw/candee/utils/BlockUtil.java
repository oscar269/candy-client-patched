 package as.pw.candee.utils;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Iterator;
 import java.util.List;
 import net.minecraft.block.Block;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.entity.Entity;
 import net.minecraft.init.Blocks;
 import net.minecraft.network.play.client.CPacketAnimation;
 import net.minecraft.network.play.client.CPacketEntityAction;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.util.math.Vec3i;
 public class BlockUtil
     implements Util
 {
     public static AxisAlignedBB getBoundingBox(BlockPos pos) {
         if (pos == null) {
             return null;
         }
         AxisAlignedBB box = getState(pos).getCollisionBoundingBox(mc.world, pos);
         return (box == null) ? null : new AxisAlignedBB(pos.getX() + box.minX, pos.getY() + box.minY, pos.getZ() + box.minZ, pos.getX() + box.maxX, pos.getY() + box.maxY, pos.getZ() + box.maxZ);
     }
     public static boolean isAir(Block block) {
         return (block == Blocks.AIR || block instanceof net.minecraft.block.BlockAir);
     }
     public static boolean isAir(BlockPos pos) {
         return isAir(mc.world.getBlockState(pos).getBlock());
     }
     public static boolean isAirBlock(Block block) {
         return (block == Blocks.AIR || block instanceof net.minecraft.block.BlockAir);
     }
     public static boolean isAirBlock(BlockPos pos) {
         return isAirBlock(mc.world.getBlockState(pos).getBlock());
     }
     public static boolean placeBlock(BlockPos pos, boolean packet) {
         Block block = mc.world.getBlockState(pos).getBlock();
         if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid)) {
             return false;
         }
         EnumFacing side = getPlaceableSide(pos);
         if (side == null) {
             return false;
         }
         BlockPos neighbour = pos.offset(side);
         EnumFacing opposite = side.getOpposite();
         if (!canBeClicked(neighbour)) {
             return false;
         }
         Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
         Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
         if (packet) {
             rightClickBlock(neighbour, hitVec, EnumHand.MAIN_HAND, opposite);
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
             mc.player.swingArm(EnumHand.MAIN_HAND);
         }
         return true;
     }
     public static void faceplace(BlockPos pos, EnumHand hand, EnumFacing facing, boolean packet) {
         if (packet) {
             mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.5F, 0.5F, 0.5F));
         }
         else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, new Vec3d(0.5D, 0.5D, 0.5D), hand);
         }
     }
     public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet) {
         EnumFacing side = getPlaceableSide(pos);
         if (side == null) return;
         BlockPos neighbour = pos.offset(side);
         EnumFacing opposite = side.getOpposite();
         Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
         Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
         boolean sneaking = false;
         if (rightclickableBlocks.contains(neighbourBlock) && !mc.player.isSneaking()) {
             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
             sneaking = true;
         }
         if (rotate) {
             faceVector(hitVec, true);
         }
         if (packet) {
             rightClickBlock(neighbour, hitVec, hand, opposite, true);
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, hand);
             mc.player.swingArm(hand);
         }
         if (sneaking) {
             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
         }
             }
     public static void rightClickBlock(BlockPos pos, EnumFacing facing, boolean packet) {
         Vec3d hitVec = (new Vec3d(pos)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));
         if (packet) {
             rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
             mc.player.swingArm(EnumHand.MAIN_HAND);
         }
     }
     public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet) {
         Vec3d hitVec = (new Vec3d(pos)).add(hVec).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));
         if (packet) {
             rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
             mc.player.swingArm(EnumHand.MAIN_HAND);
         }
     }
     public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
         float f = (float)(vec.x - pos.getX());
         float f2 = (float)(vec.y - pos.getY());
         float f3 = (float)(vec.z - pos.getZ());
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
     }
     public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
         if (packet) {
             float f = (float)(vec.x - pos.getX());
             float f2 = (float)(vec.y - pos.getY());
             float f3 = (float)(vec.z - pos.getZ());
             mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
         } else {
             mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
         }
         mc.player.swingArm(EnumHand.MAIN_HAND);
     }
     public static boolean canRightClickForPlace(BlockPos pos) {
         return !rightclickableBlocks.contains(getBlock(pos));
     }
     public static boolean canBeClicked(BlockPos pos) {
         return getBlock(pos).canCollideCheck(getState(pos), false);
     }
     public static Block getBlock(BlockPos pos) {
         return getState(pos).getBlock();
     }
     public static Block getBlock(double x, double y, double z) {
         return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
     }
     private static IBlockState getState(BlockPos pos) {
         return mc.world.getBlockState(pos);
     }
     public static boolean checkForNeighbours(BlockPos blockPos) {
         if (!hasNeighbour(blockPos)) {
             for (EnumFacing side : EnumFacing.values()) {
                 BlockPos neighbour = blockPos.offset(side);
                 if (hasNeighbour(neighbour)) {
                     return true;
                 }
             }
             return false;
         }
         return true;
     }
     public static EnumFacing getPlaceableSide(BlockPos pos) {
         for (EnumFacing side : EnumFacing.values()) {
             BlockPos neighbour = pos.offset(side);
             if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                 IBlockState blockState = mc.world.getBlockState(neighbour);
                 if (!blockState.getMaterial().isReplaceable() && !blackList.contains(getBlock(neighbour))) {
                     return side;
                 }
             }
         }
         return null;
     }
     public static boolean hasNeighbour(BlockPos blockPos) {
         for (EnumFacing side : EnumFacing.values()) {
             BlockPos neighbour = blockPos.offset(side);
             if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable() && !blackList.contains(getBlock(neighbour))) {
                 return true;
             }
         }
         return false;
     }
     public static boolean canBeNeighbour(BlockPos pos, BlockPos a) {
         for (EnumFacing side : EnumFacing.values()) {
             BlockPos neighbour = pos.offset(side);
             if (a.equals(neighbour)) {
                 return true;
             }
         }
         return false;
     }
     public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
         ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
         int cx = pos.getX();
         int cy = pos.getY();
         int cz = pos.getZ();
         int x = cx - (int) r;
         while ((float) x <= (float) cx + r) {
             int z = cz - (int) r;
             while ((float) z <= (float) cz + r) {
                 int y = sphere ? cy - (int) r : cy;
                 while (true) {
                     float f = y;
                     float f2 = sphere ? (float) cy + r : (float) (cy + h);
                     if (!(f < f2)) break;
                     double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                     if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0f) * (r - 1.0f)))) {
                         BlockPos l = new BlockPos(x, y + plus_y, z);
                         circleblocks.add(l);
                     }
                     ++y;
                 }
                 ++z;
             }
             ++x;
         }
         return circleblocks;
     }
     public static boolean canPlaceBlock(BlockPos pos) {
         return ((getBlock(pos) == Blocks.AIR || getBlock(pos) instanceof net.minecraft.block.BlockLiquid) && hasNeighbour(pos) && !blackList.contains(getBlock(pos)));
     }
     public static boolean canPlaceBlockFuture(BlockPos pos) {
         return ((getBlock(pos) == Blocks.AIR || getBlock(pos) instanceof net.minecraft.block.BlockLiquid) && !blackList.contains(getBlock(pos)));
     }
     public static boolean isBlockSolid(BlockPos pos) {
         return !isBlockUnSolid(pos);
     }
     public static boolean isBlockUnSolid(BlockPos pos) {
         return isBlockUnSolid(mc.world.getBlockState(pos).getBlock());
     }
     public static boolean isBlockUnSolid(Block block) {
         return unSolidBlocks.contains(block);
     }
     public static boolean isBlockUnSafe(Block block) {
         return unSafeBlocks.contains(block);
     }
     public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
         BlockPos[] list = new BlockPos[vec3ds.length];
         for (int i = 0; i < vec3ds.length; i++) {
             list[i] = new BlockPos(vec3ds[i]);
         }
         return list;
     }
     public static double getDistance(double blockposx, double blockposy, double blockposz, double blockposx1, double blockposy1, double blockposz1) {
         double deltaX = blockposx1 - blockposx;
         double deltaY = blockposy1 - blockposy;
         double deltaZ = blockposz1 - blockposz;
         return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
     }
     public static boolean canBreak(BlockPos pos) {
         IBlockState blockState = mc.world.getBlockState(pos);
         Block block = blockState.getBlock();
         return (block.getBlockHardness(blockState, mc.world, pos) != -1.0F);
     }
     public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing) {
         RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5D, pos.getY() - 0.5D, pos.getZ() + 0.5D));
         EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
         mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
         if (swing) {
             mc.player.connection.sendPacket(new CPacketAnimation(hand));
         }
     }
     public static boolean placeBlock(BlockPos pos, int slot, boolean rotate, boolean rotateBack, EnumHand hand) {
         if (isBlockEmpty(pos)) {
             int old_slot = -1;
             if (slot != mc.player.inventory.currentItem) {
                 old_slot = mc.player.inventory.currentItem;
                 mc.player.inventory.currentItem = slot;
             }
             EnumFacing[] values = EnumFacing.values();
                             for (EnumFacing f : values) {
                 Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();
                 Vec3d vec = new Vec3d(pos.getX() + 0.5D + f.getXOffset() * 0.5D, pos.getY() + 0.5D + f.getYOffset() * 0.5D, pos.getZ() + 0.5D + f.getZOffset() * 0.5D);
                 if (!emptyBlocks.contains(neighborBlock) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25D) {
                     float[] rot = { mc.player.rotationYaw, mc.player.rotationPitch };
                     if (rotate) {
                         rotatePacket(vec.x, vec.y, vec.z);
                     }
                     if (rightclickableBlocks.contains(neighborBlock)) {
                         mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                     }
                     mc.playerController.processRightClickBlock(mc.player, mc.world, pos.offset(f), f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                     if (rightclickableBlocks.contains(neighborBlock)) {
                         mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                     }
                     if (rotateBack) {
                         mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rot[0], rot[1], mc.player.onGround));
                     }
                     mc.player.swingArm(EnumHand.MAIN_HAND);
                     return true;
                 }
             }
         }
         return false;
     }
     public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
         boolean sneaking = false;
         EnumFacing side = getFirstFacing(pos);
         if (side == null) {
             return;
         }
         BlockPos neighbour = pos.offset(side);
         EnumFacing opposite = side.getOpposite();
         Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
         Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
         if (!mc.player.isSneaking()) {
             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
             mc.player.setSneaking(true);
             sneaking = true;
         }
         if (rotate) {
             faceVector(hitVec, true);
         }
         rightClickBlock(neighbour, hitVec, hand, opposite, packet);
         mc.player.swingArm(EnumHand.MAIN_HAND);
             }
     public static void faceVector(Vec3d vec, boolean normalizeAngle) {
         float[] rotations = getLegitRotations(vec);
         mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1], mc.player.onGround));
     }
     public static float[] getLegitRotations(Vec3d vec) {
         Vec3d eyesPos = getEyesPos();
         double diffX = vec.x - eyesPos.x;
         double diffY = vec.y - eyesPos.y;
         double diffZ = vec.z - eyesPos.z;
         double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
         float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
         float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
         return new float[] { mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
     }
     public static EnumFacing getFirstFacing(BlockPos pos) {
         Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
         if (iterator.hasNext()) {
                             return iterator.next();
         }
         return null;
     }
     public static Vec3d getEyesPos() {
         return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
     }
     public static List<EnumFacing> getPossibleSides(BlockPos pos) {
         List<EnumFacing> facings = new ArrayList<>();
         for (EnumFacing side : EnumFacing.values()) {
             BlockPos neighbour = pos.offset(side);
             if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                 IBlockState blockState = mc.world.getBlockState(neighbour);
                 if (!blockState.getMaterial().isReplaceable()) {
                     facings.add(side);
                 }
             }
         }
         return facings;
     }
     public static void rotatePacket(double x, double y, double z) {
         double diffX = x - mc.player.posX;
         double diffY = y - mc.player.posY + mc.player.getEyeHeight();
         double diffZ = z - mc.player.posZ;
         double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
         float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
         float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
         mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
     }
     public static boolean isBlockEmpty(BlockPos pos) {
         try {
             if (emptyBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                 AxisAlignedBB box = new AxisAlignedBB(pos);
                 for (Entity e : mc.world.loadedEntityList) {
                     if (e instanceof net.minecraft.entity.EntityLivingBase && box.intersects(e.getEntityBoundingBox())) {
                         return false;
                     }
                 }
                 return true;
             }
         } catch (Exception ignored) {}
         return false;
     }
     public static EnumFacing getBackwardFacing(EnumFacing facing) {
         Vec3i vec = facing.getDirectionVec();
         return EnumFacing.getFacingFromVector((vec.getX() * -1), (vec.getY() * -1), (vec.getZ() * -1));
     }
     public static final List<Block> emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
     public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
     public static final List<Block> unSolidBlocks = Arrays.asList(Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.AIR, Blocks.PORTAL, Blocks.END_PORTAL, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.SAPLING, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.VINE, Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH);
     public static final List<Block> unSafeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
     public static final List<Block> rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
     public static float[] calcAngle(Vec3d from, Vec3d to) {
         double difX = to.x - from.x;
         double difY = (to.y - from.y) * -1.0D;
         double difZ = to.z - from.z;
         double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
         return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
     }
     public static CPacketPlayer.Rotation getFaceVectorPacket(Vec3d vec, Boolean roundAngles) {
         float[] rotations = getNeededRotations2(vec);
         CPacketPlayer.Rotation e = new CPacketPlayer.Rotation(rotations[0], roundAngles ? MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1], mc.player.onGround);
         mc.player.connection.sendPacket(e);
         return e;
     }
     public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
         double difX = to.x - from.x;
         double difZ = to.z - from.z;
         return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D) };
     }
     private static float[] getNeededRotations2(Vec3d vec) {
         Vec3d eyesPos = getEyesPos();
         double diffX = vec.x - eyesPos.x;
         double diffY = vec.y - eyesPos.y;
         double diffZ = vec.z - eyesPos.z;
         double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
         float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
         float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
         return new float[] { mc.player.rotationYaw +
                 MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch +
                 MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
     }
     public static double getMaxHeight(AxisAlignedBB box) {
         if (mc.player == null || mc.world == null) return Double.NaN;
         List<AxisAlignedBB> collisions = mc.world.getCollisionBoxes(mc.player, box.offset(0.0D, -1.0D, 0.0D));
         double maxY = 0.0D;
         boolean updated = false;
         for (AxisAlignedBB collision : collisions) {
             if (collision.maxY > maxY) {
                 updated = true;
                 maxY = collision.maxY;
             }
         }
         return updated ? maxY : Double.NaN;
     }
 }