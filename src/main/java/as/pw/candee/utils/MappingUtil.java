 package as.pw.candee.utils;
 import net.minecraft.client.Minecraft;
 public class MappingUtil
 {
     public static boolean isObfuscated() {
         try {
             return (Minecraft.class.getDeclaredField("instance") == null);
         }
         catch (Exception var1) {
             return true;
         }
     }
     public static final String tickLength = "tickLength";
     public static final String timer = "timer";
     public static final String placedBlockDirection = "placedBlockDirection";
     public static final String playerPosLookYaw = "yaw";
     public static final String playerPosLookPitch = "pitch";
     public static final String isInWeb = "isInWeb";
     public static final String cPacketPlayerYaw = "yaw";
     public static final String cPacketPlayerPitch = "pitch";
     public static final String renderManagerRenderPosX = "renderPosX";
     public static final String renderManagerRenderPosY = "renderPosY";
     public static final String renderManagerRenderPosZ = "renderPosZ";
     public static final String rightClickDelayTimer = "rightClickDelayTimer";
     public static final String sPacketEntityVelocityMotionX = "motionX";
     public static final String sPacketEntityVelocityMotionY = "motionY";
     public static final String sPacketEntityVelocityMotionZ = "motionZ";
 }