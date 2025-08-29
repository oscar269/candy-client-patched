 package as.pw.candee.utils;
 import java.util.ArrayList;
 import java.util.Random;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3d;
 public class MathUtil
     implements Util
 {
     public static int getRandom(int min, int max) {
         return rnd.nextInt(max - min + 1) + min;
     }
     public static ArrayList moveItemToFirst(ArrayList<?> list, int index) {
         ArrayList newlist = new ArrayList();
         newlist.add(list.get(index));
         for (int i = 0; i < list.size(); i++) {
             if (i != index) {
                 newlist.add(list.get(index));
             }
         }
         return new ArrayList(list);
     }
     public static float[] calcAngle(Vec3d from, Vec3d to) {
         double difX = to.x - from.x;
         double difY = (to.y - from.y) * -1.0D;
         double difZ = to.z - from.z;
         double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
         return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
     }
     public static float square(float v1) {
         return v1 * v1;
     }
     public static final Random rnd = new Random();
     public static double[] directionSpeed(double speed) {
         float forward = mc.player.movementInput.moveForward;
         float side = mc.player.movementInput.moveStrafe;
         float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
         if (forward != 0.0F) {
             if (side > 0.0F) {
                 yaw += ((forward > 0.0F) ? -45 : 45);
             } else if (side < 0.0F) {
                 yaw += ((forward > 0.0F) ? 45 : -45);
             }
             side = 0.0F;
             if (forward > 0.0F) {
                 forward = 1.0F;
             } else if (forward < 0.0F) {
                 forward = -1.0F;
             }
         }
         double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
         double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
         double posX = forward * speed * cos + side * speed * sin;
         double posZ = forward * speed * sin - side * speed * cos;
         return new double[] { posX, posZ };
     }
 }