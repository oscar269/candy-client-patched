 package as.pw.candee.utils;
 import net.minecraft.util.math.MathHelper;
 public class TpsUtil
 {
     private static final double MAX_TPS = 40.0D;
     private static final double MIN_TPS = 0.1D;
     private static double tps;
     public static double getTps() {
         return tps;
     }
     public static void setTps(double tps) {
         TpsUtil.tps = MathHelper.clamp(tps, 0.1D, 40.0D);
     }
 }