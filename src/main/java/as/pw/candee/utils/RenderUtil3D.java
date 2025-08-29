 package as.pw.candee.utils;
 import java.awt.Color;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import org.lwjgl.opengl.GL11;
 import org.lwjgl.util.glu.Sphere;
 public class RenderUtil3D
     implements Util
 {
     public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, Color color) {
         drawLine(posx, posy, posz, posx2, posy2, posz2, color, 1.0F);
     }
     public static void drawLine(double posx, double posy, double posz, double posx2, double posy2, double posz2, Color color, float width) {
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         GlStateManager.glLineWidth(width);
         glColor(color);
         bufferbuilder.begin(1, DefaultVertexFormats.POSITION);
         vertex(posx, posy, posz, bufferbuilder);
         vertex(posx2, posy2, posz2, bufferbuilder);
         tessellator.draw();
     }
     public static void drawBox(BlockPos blockPos, double height, Color color, int sides) {
         drawBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D, height, 1.0D, color, color.getAlpha(), sides);
     }
     public static void drawBox(AxisAlignedBB bb, boolean check, double height, Color color, int sides) {
         drawBox(bb, check, height, color, color.getAlpha(), sides);
     }
     public static void drawBox(AxisAlignedBB bb, boolean check, double height, Color color, int alpha, int sides) {
         if (check) {
             drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ, color, alpha, sides);
         } else {
             drawBox(bb.minX, bb.minY, bb.minZ, bb.maxX - bb.minX, height, bb.maxZ - bb.minZ, color, alpha, sides);
         }
     }
     public static void drawBox(double x, double y, double z, double w, double h, double d, Color color, int alpha, int sides) {
         GlStateManager.disableAlpha();
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         glColor(color);
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
         doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides, false);
         tessellator.draw();
         GlStateManager.enableAlpha();
     }
     public static void drawRect(double x, double y, double z, double w, double h, Color color, int alpha, int sides) {
         GlStateManager.disableAlpha();
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         glColor(color);
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
         doVerticies(new AxisAlignedBB(x, y, z, x + w, y + h, z), color, alpha, bufferbuilder, sides, false);
         tessellator.draw();
         GlStateManager.enableAlpha();
     }
     public static void drawBoundingBox(BlockPos bp, double height, float width, Color color) {
         drawBoundingBox(getBoundingBox(bp, 1.0D, height, 1.0D), width, color, color.getAlpha());
     }
     public static void drawBoundingBox(AxisAlignedBB bb, double width, Color color) {
         drawBoundingBox(bb, width, color, color.getAlpha());
     }
     public static void drawBoundingBox(AxisAlignedBB bb, double width, Color color, int alpha) {
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         GlStateManager.glLineWidth((float)width);
         glColor(color);
         bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
         colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
         colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
         colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
         colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
         colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
         colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
         colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
         colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
         colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
         tessellator.draw();
     }
     public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Color color, int sides) {
         drawBoundingBoxWithSides(getBoundingBox(blockPos, 1.0D, 1.0D, 1.0D), width, color, color.getAlpha(), sides);
     }
     public static void drawBoundingBoxWithSides(BlockPos blockPos, int width, Color color, int alpha, int sides) {
         drawBoundingBoxWithSides(getBoundingBox(blockPos, 1.0D, 1.0D, 1.0D), width, color, alpha, sides);
     }
     public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Color color, int sides) {
         drawBoundingBoxWithSides(axisAlignedBB, width, color, color.getAlpha(), sides);
     }
     public static void drawBoundingBoxWithSides(AxisAlignedBB axisAlignedBB, int width, Color color, int alpha, int sides) {
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         GlStateManager.glLineWidth(width);
         bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
         doVerticies(axisAlignedBB, color, alpha, bufferbuilder, sides, true);
         tessellator.draw();
     }
     public static void drawBoxWithDirection(AxisAlignedBB bb, Color color, float rotation, float width, int mode) {
         double xCenter = bb.minX + (bb.maxX - bb.minX) / 2.0D;
         double zCenter = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
         Points square = new Points(bb.minY, bb.maxY, xCenter, zCenter, rotation);
         if (mode == 0) {
             square.addPoints(bb.minX, bb.minZ);
             square.addPoints(bb.minX, bb.maxZ);
             square.addPoints(bb.maxX, bb.maxZ);
             square.addPoints(bb.maxX, bb.minZ);
         }
        if (mode == 0) {
                drawDirection(square, color, width);
        }
     }
     public static void drawDirection(Points square, Color color, float width) {
         int i;
         for (i = 0; i < 4; i++) {
             drawLine(square.getPoint(i)[0], square.yMin, square.getPoint(i)[1], square.getPoint((i + 1) % 4)[0], square.yMin, square.getPoint((i + 1) % 4)[1], color, width);
         }
         for (i = 0; i < 4; i++) {
             drawLine(square.getPoint(i)[0], square.yMax, square.getPoint(i)[1], square.getPoint((i + 1) % 4)[0], square.yMax, square.getPoint((i + 1) % 4)[1], color, width);
         }
         for (i = 0; i < 4; i++) {
             drawLine(square.getPoint(i)[0], square.yMin, square.getPoint(i)[1], square.getPoint(i)[0], square.yMax, square.getPoint(i)[1], color, width);
         }
     }
     public static void drawSphere(double x, double y, double z, float size, int slices, int stacks, float lineWidth, Color color) {
         Sphere sphere = new Sphere();
         GlStateManager.glLineWidth(lineWidth);
         glColor(color);
         sphere.setDrawStyle(100013);
         GlStateManager.pushMatrix();
         GlStateManager.translate(x - (mc.getRenderManager()).viewerPosX, y - (mc.getRenderManager()).viewerPosY, z - (mc.getRenderManager()).viewerPosZ);
         sphere.draw(size, slices, stacks);
         GlStateManager.popMatrix();
     }
     private static void vertex(double x, double y, double z, BufferBuilder bufferbuilder) {
         bufferbuilder.pos(x - (mc.getRenderManager()).viewerPosX, y - (mc.getRenderManager()).viewerPosY, z - (mc.getRenderManager()).viewerPosZ).endVertex();
     }
     private static void colorVertex(double x, double y, double z, Color color, int alpha, BufferBuilder bufferbuilder) {
         bufferbuilder.pos(x - (mc.getRenderManager()).viewerPosX, y - (mc.getRenderManager()).viewerPosY, z - (mc.getRenderManager()).viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).endVertex();
     }
     private static AxisAlignedBB getBoundingBox(BlockPos bp, double width, double height, double depth) {
         double x = bp.getX();
         double y = bp.getY();
         double z = bp.getZ();
         return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
     }
     private static void doVerticies(AxisAlignedBB axisAlignedBB, Color color, int alpha, BufferBuilder bufferbuilder, int sides, boolean five) {
         if ((sides & 0x20) != 0) {
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             }
         }
         if ((sides & 0x10) != 0) {
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             }
         }
         if ((sides & 0x4) != 0) {
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             }
         }
         if ((sides & 0x8) != 0) {
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             }
         }
         if ((sides & 0x2) != 0) {
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, alpha, bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, alpha, bufferbuilder);
             }
         }
         if ((sides & 0x1) != 0) {
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferbuilder);
             colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             if (five) {
                 colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferbuilder);
             }
         }
     }
     public static void prepare() {
         GL11.glHint(3154, 4354);
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         GlStateManager.shadeModel(7425);
         GlStateManager.depthMask(false);
         GlStateManager.enableBlend();
         GlStateManager.disableDepth();
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         GlStateManager.disableCull();
         GlStateManager.enableAlpha();
         GL11.glEnable(2848);
         GL11.glEnable(34383);
     }
     public static void release() {
         GL11.glDisable(34383);
         GL11.glDisable(2848);
         GlStateManager.enableAlpha();
         GlStateManager.enableCull();
         GlStateManager.enableTexture2D();
         GlStateManager.enableDepth();
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
         GlStateManager.glLineWidth(1.0F);
         GlStateManager.shadeModel(7424);
         GL11.glHint(3154, 4352);
     }
     private static void glColor(Color color) {
         GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
     }
     private static final Minecraft mc = Minecraft.getMinecraft();
     public static class Points
     {
         final double[][] point;
         private int count;
         private final double xCenter;
         private final double zCenter;
         public final double yMin;
         public final double yMax;
         private final float rotation;
         public Points(double yMin, double yMax, double xCenter, double zCenter, float rotation) {
             this.point = new double[10][2];
             this.count = 0;
             this.yMin = yMin;
             this.yMax = yMax;
             this.xCenter = xCenter;
             this.zCenter = zCenter;
             this.rotation = rotation;
         }
         public void addPoints(double x, double z) {
             x -= this.xCenter;
             z -= this.zCenter;
             double rotateX = x * Math.cos(this.rotation) - z * Math.sin(this.rotation);
             double rotateZ = x * Math.sin(this.rotation) + z * Math.cos(this.rotation);
             rotateX += this.xCenter;
             rotateZ += this.zCenter;
             (new double[2])[0] = rotateX; (new double[2])[1] = rotateZ; this.point[this.count++] = new double[2];
         }
         public double[] getPoint(int index) {
             return this.point[index];
         }
     }
 }