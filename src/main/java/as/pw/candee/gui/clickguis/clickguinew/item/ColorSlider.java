 package as.pw.candee.gui.clickguis.clickguinew.item;
 import java.awt.Color;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class ColorSlider
     extends Component {
     private final Setting<Color> setting;
     private boolean isOpening;
     private boolean redChanging;
     private boolean greenChanging;
     private boolean blueChanging;
     private boolean alphaChanging;
     private float rLx;
     private float gLx;
     private float bLx;
     private float aLx;
     public ColorSlider(Setting<Color> setting, float x) {
         this.isOpening = false;
         this.alphaChanging = false;
         this.aLx = 0.0F;
         this.setting = setting;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
         this.rLx = setting.getValue().getRed() / 255.0F;
         this.gLx = setting.getValue().getGreen() / 255.0F;
         this.bLx = setting.getValue().getBlue() / 255.0F;
         this.aLx = setting.getValue().getAlpha() / 255.0F;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         if (!this.setting.visible()) {
             return 0.0F;
         }
         RenderUtil.drawRect(x, this.y = y, this.width, this.height, ColorUtil.toRGBA(this.color));
         float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
         float rcy1 = y + (this.height - 11.0F) / 2.0F;
         float rcy2 = y + (this.height - 9.0F) / 2.0F;
         RenderUtil.drawRect(x + this.width - 21.0F, rcy1, 12.0F, 12.0F, this.color0);
         RenderUtil.drawRect(x + this.width - 20.0F, rcy2, 10.0F, 10.0F, ColorUtil.toRGBA(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha())));
         RenderUtil.drawString(this.setting.name, x + 5.0F, centeredY, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         if (this.isOpening) {
             float colorFieldHeight = 93.0F;
             float colorFieldY = y + this.height;
             float colorFieldHeightY = colorFieldY + 93.0F;
             RenderUtil.drawRect(x, colorFieldY, this.width, 93.0F, this.color0);
             int gray = ColorUtil.toRGBA(50, 50, 50);
             RenderUtil.drawString("Red", x + 5.0F, colorFieldY + 5.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             RenderUtil.drawRect(x + 10.0F, colorFieldY + 15.0F, this.width - 20.0F, 5.0F, gray);
             RenderUtil.drawRect(x + 10.0F + (this.width - 20.0F) * this.rLx, colorFieldY + 13.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
             RenderUtil.drawString("Green", x + 5.0F, colorFieldY + 27.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             RenderUtil.drawRect(x + 10.0F, colorFieldY + 37.0F, this.width - 20.0F, 5.0F, gray);
             RenderUtil.drawRect(x + 10.0F + (this.width - 20.0F) * this.gLx, colorFieldY + 35.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
             RenderUtil.drawString("Blue", x + 5.0F, colorFieldY + 49.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             RenderUtil.drawRect(x + 10.0F, colorFieldY + 59.0F, this.width - 20.0F, 5.0F, gray);
             RenderUtil.drawRect(x + 10.0F + (this.width - 20.0F) * this.bLx, colorFieldY + 57.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
             RenderUtil.drawString("Alpha", x + 5.0F, colorFieldY + 71.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             RenderUtil.drawRect(x + 10.0F, colorFieldY + 81.0F, this.width - 20.0F, 5.0F, gray);
             RenderUtil.drawRect(x + 10.0F + (this.width - 20.0F) * this.aLx, colorFieldY + 79.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
             return 93.0F + this.height;
         }
         return this.height;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         float colorFieldHeight = 70.0F;
         float colorFieldY = this.y + this.height;
         float colorFieldHeightY = colorFieldY + 70.0F;
         if (isMouseHovering(mouseX, mouseY) && mouseButton == 1) {
             this.isOpening = !this.isOpening;
         }
         if (!this.isOpening) {
             return;
         }
         if (isMouseHovering(mouseX, mouseY, this.x + 8.0F, (int) (colorFieldY + 7.0F), this.width - 12.0F, 15.0F)) {
             this.redChanging = true;
         }
         if (isMouseHovering(mouseX, mouseY, this.x + 8.0F, (int) (colorFieldY + 29.0F), this.width - 12.0F, 15.0F)) {
             this.greenChanging = true;
         }
         if (isMouseHovering(mouseX, mouseY, this.x + 8.0F, (int) (colorFieldY + 51.0F), this.width - 12.0F, 15.0F)) {
             this.blueChanging = true;
         }
         if (isMouseHovering(mouseX, mouseY, this.x + 8.0F, (int) (colorFieldY + 73.0F), this.width - 12.0F, 15.0F)) {
             this.alphaChanging = true;
         }
     }
     public void onMouseReleased(int mouseX, int mouseY, int state) {
         this.redChanging = false;
         this.greenChanging = false;
         this.blueChanging = false;
         this.alphaChanging = false;
     }
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
         if (!this.isOpening) {
             return;
         }
         float colorFieldHeight = 70.0F;
         float colorFieldY = this.y + this.height;
         float colorFieldHeightY = colorFieldY + 70.0F;
         if (this.x + 8.0F < mouseX && this.x + this.width - 12.0F > mouseX && clickedMouseButton == 0 && this.redChanging) {
             this.rLx = (mouseX - this.x + 10.0F) / (this.width - 20.0F);
             if (this.rLx > 1.0F) {
                 this.rLx = 1.0F;
             }
             if (this.rLx < 0.0F) {
                 this.rLx = 0.0F;
             }
             this.setting.setValue(new Color((int)(Math.round(255.0F * this.rLx * 10.0F) / 10.0F), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha()));
         }
         if (this.x + 8.0F < mouseX && this.x + this.width - 12.0F > mouseX && clickedMouseButton == 0 && this.greenChanging) {
             this.gLx = (mouseX - this.x + 10.0F) / (this.width - 20.0F);
             if (this.gLx > 1.0F) {
                 this.gLx = 1.0F;
             }
             if (this.gLx < 0.0F) {
                 this.gLx = 0.0F;
             }
             this.setting.setValue(new Color(this.setting.getValue().getRed(), (int)(Math.round(255.0F * this.gLx * 10.0F) / 10.0F), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha()));
         }
         if (this.x + 8.0F < mouseX && this.x + this.width - 12.0F > mouseX && clickedMouseButton == 0 && this.blueChanging) {
             this.bLx = (mouseX - this.x + 10.0F) / (this.width - 20.0F);
             if (this.bLx > 1.0F) {
                 this.bLx = 1.0F;
             }
             if (this.bLx < 0.0F) {
                 this.bLx = 0.0F;
             }
             this.setting.setValue(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), (int)(Math.round(255.0F * this.bLx * 10.0F) / 10.0F), this.setting.getValue().getAlpha()));
         }
         if (this.x + 8.0F < mouseX && this.x + this.width - 12.0F > mouseX && clickedMouseButton == 0 && this.alphaChanging) {
             this.aLx = (mouseX - this.x + 10.0F) / (this.width - 20.0F);
             if (this.aLx > 1.0F) {
                 this.aLx = 1.0F;
             }
             if (this.aLx < 0.0F) {
                 this.aLx = 0.0F;
             }
             this.setting.setValue(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), (int)(Math.round(255.0F * this.aLx * 10.0F) / 10.0F)));
         }
     }
 }