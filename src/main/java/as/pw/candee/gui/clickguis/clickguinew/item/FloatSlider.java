 package as.pw.candee.gui.clickguis.clickguinew.item;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class FloatSlider extends Component {
     public final Setting<? extends Number> setting;
     public float ratio;
     public boolean changing;
     public FloatSlider(Setting<? extends Number> setting, float x) {
         this.setting = setting;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
         Number value = setting.getValue();
         Number max = setting.maxValue;
         Number min = setting.minValue;
         this.ratio = getRatio(value.floatValue(), max.floatValue(), min.floatValue());
         this.changing = false;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         if (!this.setting.visible()) return 0.0F;
         this.x = x;
         this.y = y;
         RenderUtil.drawRect(this.x, this.y, this.width, this.height, this.color0);
         float fill = this.width * this.ratio;
         RenderUtil.drawRect(this.x, this.y, fill, this.height, ColorUtil.toRGBA(this.color));
         float fontY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
         RenderUtil.drawString(this.setting.name, this.x + 6.0F, fontY, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         String valStr = this.setting.getValue().toString();
         RenderUtil.drawString(valStr, this.x + fill -
                 RenderUtil.getStringWidth(valStr, 1.0F) - 6.0F, fontY,
                 ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         return this.height;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
             setValue(mouseX);
             this.changing = true;
         }
     }
     public void onMouseClickMove(int mouseX, int mouseY, int button) {
         if (this.changing && button == 0) {
             setValue(mouseX);
         }
     }
     public void onMouseReleased(int mouseX, int mouseY, int state) {
         this.changing = false;
     }
     private void setValue(float mouseX) {
         float v = (mouseX - this.x) / this.width;
         v = Math.max(0.0F, Math.min(1.0F, v));
         this.ratio = v;
         Number max = this.setting.maxValue;
         Number min = this.setting.minValue;
         float newVal = (max.floatValue() - min.floatValue()) * this.ratio + min.floatValue();
         float rounded = Math.round(newVal * 10.0F) / 10.0F;
         if (this.setting.getValue() instanceof Double) {
             this.setting.setValue((double) rounded);
         } else {
             this.setting.setValue(rounded);
         }
     }
     private float getRatio(float value, float maxValue, float minValue) {
         return (value - minValue) / (maxValue - minValue);
     }
 }