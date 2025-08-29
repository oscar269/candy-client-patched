 package as.pw.candee.hud;
 import java.awt.Color;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.render.HUDEditor;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.util.math.MathHelper;
 public class Hud
     extends Module {
     public final Setting<Float> x;
     public final Setting<Float> y;
     public float width;
     public float height;
     public boolean selecting;
     private float diffX;
     private float diffY;
     public float scaledWidth;
     public float scaledHeight;
     public float scaleFactor;
     public Hud(String name, float x, float y) {
         super(name, Categories.HUB, false, false);
         this.height = 0.0F;
         this.selecting = false;
         this.diffY = 0.0F;
         this.scaleFactor = 0.0F;
         this.x = register(new Setting("X", x, 2000.0F, 0.0F));
         this.y = register(new Setting("Y", x, 2000.0F, 0.0F));
     }
     public void onRender2D() {
         if ((CandeePlusRewrite.m_module.getModuleWithClass(HUDEditor.class)).isEnable) {
             Color color = this.selecting ? new Color(20, 20, 20, 110) : new Color(20, 20, 20, 80);
             RenderUtil.drawRect(this.x.getValue() - 10.0F, this.y.getValue() - 5.0F, this.width + 20.0F, this.height + 10.0F, ColorUtil.toRGBA(color));
         }
         onRender();
     }
     public void onRender() {}
     public void onUpdate() {
         updateResolution();
         this.x.maxValue = this.scaledWidth;
         this.y.maxValue = this.scaledHeight;
     }
     public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && isMouseHovering(mouseX, mouseY)) {
             this.diffX = this.x.getValue() - mouseX;
             this.diffY = this.y.getValue() - mouseY;
             this.selecting = true;
         }
     }
     public void mouseReleased(int mouseX, int mouseY, int state) {
         this.selecting = false;
     }
     public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
         if (this.selecting && clickedMouseButton == 0) {
             this.x.setValue(mouseX + this.diffX);
             this.y.setValue(mouseY + this.diffY);
         }
     }
     public Boolean isMouseHovering(int mouseX, int mouseY) {
         return this.x.getValue() - 10.0F < mouseX && this.x.getValue() + this.width + 10.0F > mouseX && this.y.getValue() - 10.0F < mouseY && this.y.getValue() + this.height + 10.0F > mouseY;
     }
     public void setWidth(float width) {
         this.width = width;
     }
     public void setHeight(float height) {
         this.height = height;
     }
     public void updateResolution() {
         this.scaledWidth = mc.displayWidth;
         this.scaledHeight = mc.displayHeight;
         this.scaleFactor = 1.0F;
         boolean flag = mc.isUnicode();
         int i = mc.gameSettings.guiScale;
         if (i == 0) {
             i = 1000;
         }
         while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1.0F) >= 320.0F && this.scaledHeight / (this.scaleFactor + 1.0F) >= 240.0F) {
             this.scaleFactor++;
         }
         if (flag && this.scaleFactor % 2.0F != 0.0F && this.scaleFactor != 1.0F) {
             this.scaleFactor--;
         }
         double scaledWidthD = this.scaledWidth / this.scaleFactor;
         double scaledHeightD = this.scaledHeight / this.scaleFactor;
         this.scaledWidth = MathHelper.ceil(scaledWidthD);
         this.scaledHeight = MathHelper.ceil(scaledHeightD);
     }
 }