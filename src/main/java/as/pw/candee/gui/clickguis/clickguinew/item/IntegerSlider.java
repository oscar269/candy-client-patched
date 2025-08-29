 package as.pw.candee.gui.clickguis.clickguinew.item;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class IntegerSlider
     extends Component {
     public final Setting<Integer> setting;
     public float ratio;
     public boolean changing;
     public IntegerSlider(Setting<Integer> setting, float x) {
         this.setting = setting;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
         this.ratio = getRatio(setting.getValue(), setting.maxValue, setting.minValue);
         this.changing = false;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         if (this.setting.visible()) {
             RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
             float width = this.width * this.ratio;
             RenderUtil.drawRect(x, y, width, 16.0F, ColorUtil.toRGBA(this.color));
             float fonty = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
             RenderUtil.drawString(this.setting.name, x + 6.0F, fonty, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             RenderUtil.drawString(this.setting.getValue().toString(), x + width - RenderUtil.getStringWidth(this.setting.getValue().toString(), 1.0F) - 6.0F, fonty, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             return 16.0F;
         }
         return 0.0F;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
             setValue(mouseX);
             this.changing = true;
         }
     }
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
         if (this.changing && clickedMouseButton == 0) {
             setValue(mouseX);
         }
     }
     public void onMouseReleased(int mouseX, int mouseY, int state) {
         this.changing = false;
     }
     public void setValue(float mouseX) {
         float v = (mouseX - this.x) / this.width;
         if (v > 1.0F) {
             v = 1.0F;
         }
         if (v < 0.0F) {
             v = 0.0F;
         }
         this.ratio = v;
         float newValue = (this.setting.maxValue - this.setting.minValue) * this.ratio + this.setting.minValue;
         this.setting.setValue(Math.round(newValue));
     }
     public float getRatio(float value, float maxValue, float minValue) {
         return (value - minValue) / maxValue;
     }
 }