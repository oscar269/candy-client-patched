 package as.pw.candee.utils;
 import net.minecraft.util.math.BlockPos;
 public class StringUtil
     implements Util {
     public static String getPositionString(BlockPos pos) {
         return "X:" + pos.getX() + " Y:" + pos.getY() + " Z:" + pos.getZ();
     }
     public static String getName(String full) {
         StringBuilder r = new StringBuilder();
         boolean a = false;
         for (char c : full.toCharArray()) {
             if (!a) {
                 r.append(String.valueOf(c).toUpperCase());
             } else {
                 r.append(String.valueOf(c).toLowerCase());
             }
             a = true;
         }
         return r.toString();
     }
 }