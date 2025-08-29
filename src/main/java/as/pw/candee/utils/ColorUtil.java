 package as.pw.candee.utils;
 import java.awt.Color;
 public class ColorUtil
     implements Util {
     public static int toARGB(int r, int g, int b, int a) {
         return (new Color(r, g, b, a)).getRGB();
     }
     public static int toRGBA(int r, int g, int b) {
         return toRGBA(r, g, b, 255);
     }
     public static int toRGBA(int r, int g, int b, int a) {
         return (r << 16) + (g << 8) + b + (a << 24);
     }
     public static int toRGBA(Color c) {
         return (c.getRed() << 16) + (c.getGreen() << 8) + c.getBlue() + (c.getAlpha() << 24);
     }
     public static int toRGBA(float r, float g, float b, float a) {
         return toRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
     }
     public static Color getColor(int hex) {
         return new Color(hex);
     }
     public static int getRed(int hex) {
         return hex >> 16 & 0xFF;
     }
     public static int getGreen(int hex) {
         return hex >> 8 & 0xFF;
     }
     public static int getBlue(int hex) {
         return hex & 0xFF;
     }
     public static int getHoovered(int color, boolean isHoovered) {
         return isHoovered ? ((color & 0x7F7F7F) << 1) : color;
     }
 }