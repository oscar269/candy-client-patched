 package as.pw.candee.managers;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.PlayerUtil;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.math.BlockPos;
 public class HoleManager
     extends Manager
 {
     private final List<BlockPos> midSafety = new ArrayList<>();
     private List<BlockPos> holes = new ArrayList<>();
     public void update() {
         if (nullCheck()) {
             this.holes = calcHoles();
         }
     }
     public List<BlockPos> getHoles() {
         return this.holes;
     }
     public List<BlockPos> getMidSafety() {
         return this.midSafety;
     }
     public List<BlockPos> getSortedHoles() {
         this.holes.sort(Comparator.comparingDouble(hole -> mc.player.getDistanceSq(hole)));
         return getHoles();
     }
     public List<BlockPos> calcHoles() {
         ArrayList<BlockPos> safeSpots = new ArrayList<>();
         this.midSafety.clear();
         List<BlockPos> positions = BlockUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), 6.0F, 6, false, true, 0);
         for (BlockPos pos : positions) {
             if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) ||
                 !mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                 continue;
             }
             boolean isSafe = true;
             boolean midSafe = true;
             for (BlockPos offset : surroundOffset) {
                 Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
                 if (BlockUtil.isBlockUnSolid(block)) {
                     midSafe = false;
                 }
                 if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                     isSafe = false;
                 }
             }
             if (isSafe) {
                 safeSpots.add(pos);
             }
             if (!midSafe) {
                 continue;
             }
             this.midSafety.add(pos);
         }
         return safeSpots;
     }
     public List<BlockPos> calcHoles(float range) {
         ArrayList<BlockPos> safeSpots = new ArrayList<>();
         this.midSafety.clear();
         List<BlockPos> positions = BlockUtil.getSphere(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), range, 6, false, true, 0);
         for (BlockPos pos : positions) {
             if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) ||
                 !mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                 continue;
             }
             boolean isSafe = true;
             boolean midSafe = true;
             for (BlockPos offset : surroundOffset) {
                 Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
                 if (BlockUtil.isBlockUnSolid(block)) {
                     midSafe = false;
                 }
                 if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                     isSafe = false;
                 }
             }
             if (isSafe) {
                 safeSpots.add(pos);
             }
             if (!midSafe) {
                 continue;
             }
             this.midSafety.add(pos);
         }
         return safeSpots;
     }
     public boolean isSafe(BlockPos pos) {
         boolean isSafe = true;
         for (BlockPos offset : surroundOffset) {
             Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
             if (BlockUtil.isBlockUnSafe(block)) {
                 isSafe = false;
                 break;
             }
         }
         return isSafe;
     }
     public boolean inHole() {
         return (mc.player != null && isSafe(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)));
     }
     public boolean inHole(EntityPlayer player) {
         return (player != null && isSafe(new BlockPos(player.posX, player.posY, player.posZ)));
     }
     private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(PlayerUtil.getOffsets(0, true));
 }