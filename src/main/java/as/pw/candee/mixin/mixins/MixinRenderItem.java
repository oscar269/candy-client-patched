 package as.pw.candee.mixin.mixins;
 import as.pw.candee.module.render.EnchantmentColor;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.Util;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.RenderItem;
 import net.minecraft.client.renderer.block.model.IBakedModel;
 import net.minecraft.client.renderer.texture.TextureManager;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.util.ResourceLocation;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Shadow;
 import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 @Mixin({RenderItem.class})
 public abstract class MixinRenderItem
     implements Util
 {
     @Shadow
     protected abstract void renderModel(IBakedModel paramIBakedModel, int paramInt);
     @Unique
private void candyPlusRewrite_OpenSRC$renderEffect(IBakedModel model, CallbackInfo info) {
         int color = ColorUtil.toRGBA(EnchantmentColor.INSTANCE.color.getValue());
         TextureManager textureManager = mc.getTextureManager();
         GlStateManager.depthMask(false);
         GlStateManager.depthFunc(514);
         GlStateManager.disableLighting();
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
         textureManager.bindTexture(RES_ITEM_GLINT);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(8.0F, 8.0F, 8.0F);
         float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
         GlStateManager.translate(f, 0.0F, 0.0F);
         GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
         renderModel(model, color);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(8.0F, 8.0F, 8.0F);
         float f2 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
         GlStateManager.translate(-f2, 0.0F, 0.0F);
         GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
         renderModel(model, color);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         GlStateManager.enableLighting();
         GlStateManager.depthFunc(515);
         GlStateManager.depthMask(true);
         textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         GlStateManager.popMatrix();
     }
     @Unique
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
 }