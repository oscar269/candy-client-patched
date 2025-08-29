 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.event.events.player.UpdateWalkingPlayerEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import org.lwjgl.opengl.GL11;
 public class BreadCrumbs
     extends Module {
     public final Setting<Color> color;
     public final Setting<Float> fadeSpeed;
     public final Setting<Float> thickness;
     public final Setting<Float> offset;
     public final Setting<Boolean> other;
     public final List<Trace> traces;
     public BreadCrumbs() {
         super("BreadCrumbs", Categories.RENDER, false, false);
         this.color = register(new Setting("Color", new Color(130, 10, 220, 200)));
         this.fadeSpeed = register(new Setting("Fadeout Speed", 10.0F, 20.0F, 1.0F));
         this.thickness = register(new Setting("Thickness", 3.0F, 10.0F, 1.0F));
         this.offset = register(new Setting("OffsetY", 5.0F, 10.0F, 0.0F));
         this.other = register(new Setting("Other", Boolean.FALSE));
         this.traces = new ArrayList<>();
     }
     public void onRender3D(float ticks) {
         try {
             doRender(ticks);
         }
         catch (Exception ignored) {}
     }
     @SubscribeEvent
     public void onUpdateWalkingEvent(UpdateWalkingPlayerEvent event) {
         this.traces.add(new Trace(mc.player.posX, mc.player.posY + this.offset.getValue() - 5.0D, mc.player.posZ, this.color.getValue()));
     }
     public void doRender(float ticks) {
         if (this.traces.isEmpty())
             return;
         GlStateManager.pushMatrix();
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.disableDepth();
         float posx = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * ticks);
         float posy = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * ticks);
         float posz = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * ticks);
         GlStateManager.translate(posx * -1.0F, posy * -1.0F, posz * -1.0F);
         Tessellator tessellator = new Tessellator(2097152);
         BufferBuilder worldRenderer = tessellator.getBuffer();
         float thickness = this.thickness.getValue();
         GL11.glEnable(2848);
         GL11.glLineWidth(thickness);
         try {
             worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
                for (Trace trace : this.traces) {
                        int r = trace.r;
                        int g = trace.g;
                        int b = trace.b;
                        int a = trace.a;
                        worldRenderer.pos(trace.x, trace.y, trace.z).color(r, g, b, a).endVertex();
                        trace.includeAlpha(this.fadeSpeed.getValue());
                }
             tessellator.draw();
         } catch (IllegalStateException e) {
             try {
                 worldRenderer.finishDrawing();
             } catch (Exception ignored) {}
         }
         GL11.glLineWidth(1.0F);
         GL11.glDisable(2848);
         GlStateManager.enableDepth();
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GlStateManager.popMatrix();
         this.traces.removeIf(t -> (t.a <= 0));
     }
     public static class Trace
     {
         public final double x;
         public final double y;
         public final double z;
         public final int r;
         public final int g;
         public final int b;
         public int a;
         public Trace(double x, double y, double z, Color color) {
             this.x = x;
             this.y = y;
             this.z = z;
             this.r = color.getRed();
             this.g = color.getGreen();
             this.b = color.getBlue();
             this.a = color.getAlpha();
         }
         public void includeAlpha(Float speed) {
             this.a -= speed.intValue();
             if (this.a < 0)
                 this.a = 0;
         }
     }
 }