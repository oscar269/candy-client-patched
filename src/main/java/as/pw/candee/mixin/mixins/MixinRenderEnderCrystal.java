 package as.pw.candee.mixin.mixins;
 import java.awt.Color;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.event.events.render.RenderEntityModelEvent;
 import as.pw.candee.module.render.CandyCrystal;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.client.model.ModelBase;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderEnderCrystal;
 import net.minecraft.entity.Entity;
 import net.minecraftforge.common.MinecraftForge;
 import org.lwjgl.opengl.GL11;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Redirect;
 @Mixin({RenderEnderCrystal.class})
 public class MixinRenderEnderCrystal {
     @Redirect(method = {"doRender*"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
     public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
         CandyCrystal candycrystal = (CandyCrystal) CandeePlusRewrite.m_module.getModuleWithClass(CandyCrystal.class);
         if (candycrystal == null) {
             return;
         }
         GlStateManager.scale(candycrystal.scale.getValue(), candycrystal.scale.getValue(), candycrystal.scale.getValue());
         if (candycrystal.isEnable && candycrystal.wireframe.getValue()) {
             RenderEntityModelEvent event = new RenderEntityModelEvent(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
             MinecraftForge.EVENT_BUS.post(event);
         }
         if (candycrystal.isEnable && candycrystal.chams.getValue()) {
             GL11.glPushAttrib(1048575);
             GL11.glDisable(3008);
             GL11.glDisable(3553);
             GL11.glDisable(2896);
             GL11.glEnable(3042);
             GL11.glBlendFunc(770, 771);
             GL11.glLineWidth(1.5F);
             GL11.glEnable(2960);
             if (candycrystal.rainbow.getValue()) {
                 Color rainbowColor1 = candycrystal.colorSync.getValue() ? candycrystal.getCurrentColor() : new Color(RenderUtil.getRainbow(candycrystal.speed.getValue() * 100, 0, candycrystal.saturation.getValue() / 100.0F, candycrystal.brightness.getValue() / 100.0F));
                 Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                 if (candycrystal.throughWalls.getValue()) {
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                 }
                 GL11.glEnable(10754);
                 GL11.glColor4f(rainbowColor2.getRed() / 255.0F, rainbowColor2.getGreen() / 255.0F, rainbowColor2.getBlue() / 255.0F, candycrystal.alpha.getValue() / 255.0F);
                 model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                 if (candycrystal.throughWalls.getValue()) {
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                 }
             } else if (candycrystal.xqz.getValue() && candycrystal.throughWalls.getValue()) {
                 Color hiddenColor = candycrystal.hiddenColor.getValue();
                 Color color = candycrystal.color.getValue();
                 Color visibleColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                 if (candycrystal.throughWalls.getValue()) {
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                 }
                 GL11.glEnable(10754);
                 GL11.glColor4f(hiddenColor.getRed() / 255.0F, hiddenColor.getGreen() / 255.0F, hiddenColor.getBlue() / 255.0F, candycrystal.alpha.getValue() / 255.0F);
                 model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                 if (candycrystal.throughWalls.getValue()) {
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                 }
                 GL11.glColor4f(visibleColor.getRed() / 255.0F, visibleColor.getGreen() / 255.0F, visibleColor.getBlue() / 255.0F, candycrystal.alpha.getValue() / 255.0F);
                 model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
             }
             else {
                 Color color = candycrystal.colorSync.getValue() ? candycrystal.getCurrentColor() : candycrystal.color.getValue();
                                         if (candycrystal.throughWalls.getValue()) {
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                 }
                 GL11.glEnable(10754);
                 GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, candycrystal.alpha.getValue() / 255.0F);
                 model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                 if (candycrystal.throughWalls.getValue()) {
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                 }
             }
             GL11.glEnable(3042);
             GL11.glEnable(2896);
             GL11.glEnable(3553);
             GL11.glEnable(3008);
             GL11.glPopAttrib();
             if (candycrystal.glint.getValue()) {
                 GL11.glDisable(2929);
                 GL11.glDepthMask(false);
                 GlStateManager.enableAlpha();
                 GlStateManager.color(1.0F, 0.0F, 0.0F, 0.13F);
                 model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                 GlStateManager.disableAlpha();
                 GL11.glEnable(2929);
                 GL11.glDepthMask(true);
             }
         } else {
             model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
         }
         if (candycrystal.isEnable())
             GlStateManager.scale(1.0F / candycrystal.scale.getValue(), 1.0F / candycrystal.scale.getValue(), 1.0F / candycrystal.scale.getValue());
     }
 }