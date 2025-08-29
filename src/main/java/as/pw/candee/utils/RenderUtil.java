 package as.pw.candee.utils;
 import java.awt.Color;
 import java.awt.Rectangle;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.gui.font.CFontRenderer;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.ScaledResolution;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.RenderHelper;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.culling.Frustum;
 import net.minecraft.client.renderer.culling.ICamera;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.item.ItemStack;
 import org.lwjgl.opengl.GL11;
 public class RenderUtil
     implements Util
 {
     public static void drawRect(float x, float y, float w, float h, int color) {
         float alpha = (color >> 24 & 0xFF) / 255.0F;
         float red = (color >> 16 & 0xFF) / 255.0F;
         float green = (color >> 8 & 0xFF) / 255.0F;
         float blue = (color & 0xFF) / 255.0F;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
         bufferbuilder.pos(x, (y + h), 0.0D).color(red, green, blue, alpha).endVertex();
         bufferbuilder.pos((x + w), (y + h), 0.0D).color(red, green, blue, alpha).endVertex();
         bufferbuilder.pos((x + w), y, 0.0D).color(red, green, blue, alpha).endVertex();
         bufferbuilder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
         tessellator.draw();
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
     }
     public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
         float f = (startColor >> 24 & 0xFF) / 255.0F;
         float f2 = (startColor >> 16 & 0xFF) / 255.0F;
         float f3 = (startColor >> 8 & 0xFF) / 255.0F;
         float f4 = (startColor & 0xFF) / 255.0F;
         float f5 = (endColor >> 24 & 0xFF) / 255.0F;
         float f6 = (endColor >> 16 & 0xFF) / 255.0F;
         float f7 = (endColor >> 8 & 0xFF) / 255.0F;
         float f8 = (endColor & 0xFF) / 255.0F;
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.disableAlpha();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         GlStateManager.shadeModel(7425);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
         bufferbuilder.pos(left, top, 0.0D).color(f2, f3, f4, f).endVertex();
         bufferbuilder.pos(left, bottom, 0.0D).color(f2, f3, f4, f).endVertex();
         bufferbuilder.pos(right, bottom, 0.0D).color(f6, f7, f8, f5).endVertex();
         bufferbuilder.pos(right, top, 0.0D).color(f6, f7, f8, f5).endVertex();
         tessellator.draw();
         GlStateManager.shadeModel(7424);
         GlStateManager.disableBlend();
         GlStateManager.enableAlpha();
         GlStateManager.enableTexture2D();
     }
     public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
         float red = (hex >> 16 & 0xFF) / 255.0F;
         float green = (hex >> 8 & 0xFF) / 255.0F;
         float blue = (hex & 0xFF) / 255.0F;
         float alpha = (hex >> 24 & 0xFF) / 255.0F;
         GlStateManager.pushMatrix();
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.disableAlpha();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.shadeModel(7425);
         GL11.glLineWidth(thickness);
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
         bufferbuilder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
         bufferbuilder.pos(x1, y1, 0.0D).color(red, green, blue, alpha).endVertex();
         tessellator.draw();
         GlStateManager.shadeModel(7424);
         GL11.glDisable(2848);
         GlStateManager.disableBlend();
         GlStateManager.enableAlpha();
         GlStateManager.enableTexture2D();
         GlStateManager.popMatrix();
     }
     public static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
         float f = 0.00390625F;
         float f2 = 0.00390625F;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos((xCoord + 0.0F), (yCoord + maxV), 0.0D).tex((minU * 0.00390625F), ((minV + maxV) * 0.00390625F)).endVertex();
         bufferbuilder.pos((xCoord + maxU), (yCoord + maxV), 0.0D).tex(((minU + maxU) * 0.00390625F), ((minV + maxV) * 0.00390625F)).endVertex();
         bufferbuilder.pos((xCoord + maxU), (yCoord + 0.0F), 0.0D).tex(((minU + maxU) * 0.00390625F), (minV * 0.00390625F)).endVertex();
         bufferbuilder.pos((xCoord + 0.0F), (yCoord + 0.0F), 0.0D).tex((minU * 0.00390625F), (minV * 0.00390625F)).endVertex();
         tessellator.draw();
     }
     public static void drawTexture(float x, float y, float textureX, float textureY, float width, float height) {
         GL11.glPushAttrib(524288);
         GL11.glDisable(3089);
         GlStateManager.clear(256);
         GL11.glPopAttrib();
         float f = 0.00390625F;
         float f2 = 0.00390625F;
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.color(0, 0, 0, 150);
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos(x, (y + height), 0.0D).tex((textureX * 0.00390625F), ((textureY + height) * 0.00390625F)).endVertex();
         bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((textureX + width) * 0.00390625F), ((textureY + height) * 0.00390625F)).endVertex();
         bufferbuilder.pos((x + width), y, 0.0D).tex(((textureX + width) * 0.00390625F), (textureY * 0.00390625F)).endVertex();
         bufferbuilder.pos(x, y, 0.0D).tex((textureX * 0.00390625F), (textureY * 0.00390625F)).endVertex();
         tessellator.draw();
     }
     public static void drawTexture(float x, float y, float width, float height, float u, float v, float t, float s) {
         GL11.glPushAttrib(524288);
         GL11.glDisable(3089);
         GlStateManager.clear(256);
         GL11.glPopAttrib();
         GlStateManager.depthMask(false);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(4, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos((x + width), y, 0.0D).tex(t, v).endVertex();
         bufferbuilder.pos(x, y, 0.0D).tex(u, v).endVertex();
         bufferbuilder.pos(x, (y + height), 0.0D).tex(u, s).endVertex();
         bufferbuilder.pos(x, (y + height), 0.0D).tex(u, s).endVertex();
         bufferbuilder.pos((x + width), (y + height), 0.0D).tex(t, s).endVertex();
         bufferbuilder.pos((x + width), y, 0.0D).tex(t, v).endVertex();
         tessellator.draw();
     }
     public static void drawTexture(float posX, float posY, float width, float height) {
         GL11.glPushMatrix();
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableAlpha();
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         GL11.glTranslatef(posX, posY, 0.0F);
         GL11.glBegin(7);
         GL11.glTexCoord2f(0.0F, 0.0F);
         GL11.glVertex3f(0.0F, 0.0F, 0.0F);
         GL11.glTexCoord2f(0.0F, 1.0F);
         GL11.glVertex3f(0.0F, height, 0.0F);
         GL11.glTexCoord2f(1.0F, 1.0F);
         GL11.glVertex3f(width, height, 0.0F);
         GL11.glTexCoord2f(1.0F, 0.0F);
         GL11.glVertex3f(width, 0.0F, 0.0F);
         GL11.glEnd();
         GlStateManager.disableAlpha();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableLighting();
         GL11.glPopMatrix();
     }
     public static float drawString(String str, float x, float y, int color, boolean shadow, float s) {
         CFontRenderer font = CandeePlusRewrite.m_font.fontRenderer;
         if (shadow) {
             font.drawString(str, x + 0.5D * s, y + 0.5D * s, ColorUtil.toRGBA(new Color(0, 0, 0, 255)), true, 1.0F);
         }
         return font.drawString(str, x, y, color, false, s);
     }
     public static float getStringWidth(String str, float s) {
         CFontRenderer font = CandeePlusRewrite.m_font.fontRenderer;
         return font.getStringWidth(str) * s;
     }
     public static float getStringHeight(float s) {
         CFontRenderer font = CandeePlusRewrite.m_font.fontRenderer;
         return (font.getHeight() - 1) * s;
     }
     public static int getRainbow(int speed, int offset, float s, float b) {
         float hue = (float)((System.currentTimeMillis() + offset) % speed);
         return Color.getHSBColor(hue /= speed, s, b).getRGB();
     }
     public static void renderItem(ItemStack item, float x, float y) {
         GlStateManager.enableTexture2D();
         GlStateManager.depthMask(true);
         GL11.glPushAttrib(524288);
         GL11.glDisable(3089);
         GlStateManager.clear(256);
         GL11.glPopAttrib();
         GlStateManager.enableDepth();
         GlStateManager.disableAlpha();
         GlStateManager.pushMatrix();
         (Minecraft.getMinecraft().getRenderItem()).zLevel = -150.0F;
         RenderHelper.enableGUIStandardItemLighting();
         Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, (int)x, (int)y);
         Minecraft.getMinecraft().getRenderItem().renderItemOverlays((Minecraft.getMinecraft()).fontRenderer, item, (int)x, (int)y);
         RenderHelper.disableStandardItemLighting();
         (Minecraft.getMinecraft().getRenderItem()).zLevel = 0.0F;
         GlStateManager.popMatrix();
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
     }
     public static void renderItem(ItemStack item, float x, float y, boolean itemOverlay) {
         GlStateManager.enableTexture2D();
         GlStateManager.depthMask(true);
         GL11.glPushAttrib(524288);
         GL11.glDisable(3089);
         GlStateManager.clear(256);
         GL11.glPopAttrib();
         GlStateManager.enableDepth();
         GlStateManager.disableAlpha();
         GlStateManager.pushMatrix();
         (Minecraft.getMinecraft().getRenderItem()).zLevel = -150.0F;
         RenderHelper.enableGUIStandardItemLighting();
         Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, (int)x, (int)y);
         if (itemOverlay) {
             Minecraft.getMinecraft().getRenderItem().renderItemOverlays((Minecraft.getMinecraft()).fontRenderer, item, (int)x, (int)y);
         }
         RenderHelper.disableStandardItemLighting();
         (Minecraft.getMinecraft().getRenderItem()).zLevel = 0.0F;
         GlStateManager.popMatrix();
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
     }
     public static void renderEntity(EntityLivingBase entity, float x, float y, float scale) {
         GlStateManager.pushMatrix();
         GL11.glPushAttrib(1048575);
         try {
             GlStateManager.enableTexture2D();
             GlStateManager.enableBlend();
             GlStateManager.depthMask(true);
             GlStateManager.enableDepth();
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
             RenderHelper.enableGUIStandardItemLighting();
             drawEntityOnScreen((int)x, (int)y, (int)scale, entity);
             RenderHelper.disableStandardItemLighting();
         } finally {
             GL11.glPopAttrib();
             GlStateManager.popMatrix();
         }
     }
     private static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent) {
         GlStateManager.enableColorMaterial();
         GlStateManager.pushMatrix();
         GlStateManager.translate(posX, posY, 50.0F);
         GlStateManager.scale(-scale, scale, scale);
         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         float f = ent.renderYawOffset;
         float f2 = ent.rotationYaw;
         float f3 = ent.rotationPitch;
         float f4 = ent.prevRotationYawHead;
         float f5 = ent.rotationYawHead;
         GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
         RenderHelper.enableStandardItemLighting();
         GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
         ent.rotationYawHead = ent.rotationYaw;
         ent.prevRotationYawHead = ent.rotationYaw;
         GlStateManager.translate(0.0F, 0.0F, 0.0F);
         RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
         rendermanager.setPlayerViewY(180.0F);
         rendermanager.setRenderShadow(false);
         rendermanager.getEntityRenderObject(ent).doRender(ent, 0.0D, 0.0D, 0.0D, ent.rotationYaw, 1.0F);
         rendermanager.setRenderShadow(true);
         ent.renderYawOffset = f;
         ent.rotationYaw = f2;
         ent.rotationPitch = f3;
         ent.prevRotationYawHead = f4;
         ent.rotationYawHead = f5;
         GlStateManager.popMatrix();
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.disableTexture2D();
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
     }
     public static float guiToScreen(float shit) {
         ScaledResolution scaledresolution = new ScaledResolution(mc);
         double scaleFactor = scaledresolution.getScaleFactor();
         return (float)(shit * scaleFactor);
     }
     public static void Scissor(float x, float y, float width, float height) {
         _lasted = new Rectangle((int)x, (int)y, (int)width, (int)height);
         GL11.glScissor((int)guiToScreen(x), (int)(mc.displayHeight - guiToScreen(y) + guiToScreen(height)), (int)guiToScreen(width), (int)guiToScreen(height));
     }
     public static void pushScissor() {
         lasted = _lasted;
     }
     public static void popScissor() {
         GL11.glScissor((int)guiToScreen(lasted.x), (int)(mc.displayHeight - guiToScreen(lasted.y) + guiToScreen(lasted.height)), (int)guiToScreen(lasted.width), (int)guiToScreen(lasted.height));
     }
     private static final ICamera camera = new Frustum();
     private static Rectangle _lasted;
     private static Rectangle lasted;
 }