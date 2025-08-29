 package as.pw.candee.utils;
 import net.minecraft.block.Block;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.math.BlockPos;
 public class HoleUtil
     implements Util
 {
     public static boolean isObbyHole(BlockPos pos) {
         for (BlockPos offset : surroundOffsets) {
             if (BlockUtil.getBlock(pos.add(offset)) != Blocks.OBSIDIAN) {
                 return false;
             }
         }
         return true;
     }
     public static boolean isBedrockHole(BlockPos pos) {
         for (BlockPos offset : surroundOffsets) {
             if (BlockUtil.getBlock(pos.add(offset)) != Blocks.BEDROCK) {
                 return false;
             }
         }
         return true;
     }
     public static boolean isSafeHole(BlockPos pos) {
         for (BlockPos offset : surroundOffsets) {
             Block block = BlockUtil.getBlock(pos.add(offset));
             if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) {
                 return false;
             }
         }
         return true;
     }
     private static final BlockPos[] surroundOffsets = BlockUtil.toBlockPos(PlayerUtil.getOffsets(0, true));
 }