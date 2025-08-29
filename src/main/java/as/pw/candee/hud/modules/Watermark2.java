 package as.pw.candee.hud.modules;
 import java.awt.Color;
 import java.awt.image.BufferedImage;
 import java.io.IOException;
 import java.io.InputStream;
 import javax.imageio.ImageIO;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.CandyDynamicTexture;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.RenderHelper;
 import org.lwjgl.opengl.GL11;
 public class Watermark2 extends Hud {
     public final Setting<Float> scale;
     public final Setting<Boolean> rainbow;
     public final Setting<Integer> saturation;
     public final Setting<Integer> brightness;
     public final Setting<Integer> speed;
     public CandyDynamicTexture watermark;
     public Watermark2() {
         super("Watermark2", 10.0F, 10.0F);
         this.scale = register(new Setting("Scale", 0.6F, 1.0F, 0.1F));
         this.rainbow = register(new Setting("Rainbow", Boolean.FALSE));
         this.saturation = register(new Setting("Saturation", 50, 100, 0, v -> this.rainbow.getValue()));
         this.brightness = register(new Setting("Brightness", 100, 100, 0, v -> this.rainbow.getValue()));
         this.speed = register(new Setting("Speed", 40, 100, 1, v -> this.rainbow.getValue()));
         loadLogo();
     }
     public void onRender() {
         if (this.watermark == null) {
             loadLogo();
             return;
         }
         float width = this.watermark.GetWidth() * this.scale.getValue();
         float height = this.watermark.GetHeight() * this.scale.getValue();
         GlStateManager.enableTexture2D();
         GlStateManager.disableAlpha();
         RenderHelper.enableGUIStandardItemLighting();
         mc.renderEngine.bindTexture(this.watermark.GetResourceLocation());
         GlStateManager.glTexParameteri(3553, 10240, 9729);
         GlStateManager.glTexParameteri(3553, 10241, 9729);
         if (this.rainbow.getValue()) {
             Color color = new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0F, this.brightness.getValue() / 100.0F));
             GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
         } else {
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }
         GlStateManager.pushMatrix();
         RenderUtil.drawTexture(this.x.getValue(), this.y.getValue(), width, height);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.popMatrix();
         this.width = width;
         this.height = height;
     }
     public void loadLogo() {
         try (InputStream stream = Watermark2.class.getResourceAsStream("/assets/candeeplusrewrite/textures/watermark2.png")) {
             BufferedImage image = ImageIO.read(stream);
             int height = image.getHeight();
             int width = image.getWidth();
             this.watermark = new CandyDynamicTexture(image, height, width);
             this.watermark.SetResourceLocation(Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("candeeplusrewrite/textures", this.watermark));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }