 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.event.events.player.UpdateWalkingPlayerEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderPlayer;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import org.lwjgl.opengl.GL11;
 public class Afterglow
     extends Module
 {
     public final Setting<Float> delay;
     public final Setting<Color> color;
     public final Setting<Float> fadeSpeed;
     public final Setting<Float> thickness;
     public Afterglow() {
         super("Afterglow", Categories.RENDER, false, false);
         this.delay = register(new Setting("Delay", 10.0F, 20.0F, 1.0F));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 200)));
         this.fadeSpeed = register(new Setting("Fadeout Speed", 10.0F, 20.0F, 1.0F));
         this.thickness = register(new Setting("Thickness", 3.0F, 10.0F, 1.0F));
         this.glows = new ArrayList<>();
         this.timer = new Timer();
     }
     @SubscribeEvent
     public void onUpdateWalkingEvent(UpdateWalkingPlayerEvent event) {
         if (this.timer == null) {
             this.timer = new Timer();
         }
         if (this.timer.passedDms(this.delay.getValue())) {
             if (mc.player.motionX == 0.0D && mc.player.motionZ == 0.0D) {
                 return;
             }
             double[] forward = PlayerUtil.forward(-0.5D);
             this.glows.add(new AfterGlow(forward[0] + mc.player.posX, mc.player.posY, forward[1] + mc.player.posZ, mc.player.rotationYaw, new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), 150)));
         }
     }
     public void onRender3D(float ticks) {
         if (render == null) {
             render = new RenderPlayer(mc.getRenderManager());
         }
         GlStateManager.pushMatrix();
         GlStateManager.disableTexture2D();
         GlStateManager.enableBlend();
         GlStateManager.enableDepth();
         float posx = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * ticks);
         float posy = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * ticks);
         float posz = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * ticks);
         GlStateManager.translate(posx * -1.0F, posy * -1.0F, posz * -1.0F);
         for (AfterGlow glow : this.glows) {
             GL11.glPushMatrix();
             GL11.glDepthRange(0.0D, 0.01D);
             GL11.glDisable(2896);
             GL11.glDisable(3553);
             GL11.glPolygonMode(1032, 6913);
             GL11.glEnable(3008);
             GL11.glEnable(3042);
             GL11.glLineWidth(this.thickness.getValue());
             GL11.glEnable(2848);
             GL11.glHint(3154, 4354);
             GL11.glColor4f(glow.r / 255.0F, glow.g / 255.0F, glow.b / 255.0F, glow.a / 255.0F);
             mc.getRenderManager().renderEntityStatic(mc.player, 0.0F, false);
             GL11.glHint(3154, 4352);
             GL11.glPolygonMode(1032, 6914);
             GL11.glEnable(2896);
             GL11.glDepthRange(0.0D, 1.0D);
             GL11.glEnable(3553);
             GL11.glPopMatrix();
             glow.includeAlpha(this.fadeSpeed.getValue());
         }
         GlStateManager.disableDepth();
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GlStateManager.popMatrix();
         this.glows.removeIf(g -> (g.a >= 255));
     }
     public static RenderPlayer render = null;
     public final List<AfterGlow> glows;
     public Timer timer;
     public static class AfterGlow {
         public final double x;
         public final double y;
         public final double z;
         public final float yaw;
         public final int r;
         public final int g;
         public final int b;
         public int a;
         public AfterGlow(double x, double y, double z, float yaw, Color color) {
             this.x = x;
             this.y = y;
             this.z = z;
             this.yaw = yaw;
             this.r = color.getRed();
             this.g = color.getGreen();
             this.b = color.getBlue();
             this.a = color.getAlpha();
         }
         public void includeAlpha(Float speed) {
             this.a += speed.intValue();
             if (this.a > 255)
                 this.a = 255;
         }
     }
 }