 package as.pw.candee.gui.clickguis.clickguinew.item;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class BooleanButton
     extends Component {
     public final Setting<Boolean> setting;
     public BooleanButton(Setting<Boolean> setting, float x) {
         this.setting = setting;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         if (this.setting.visible()) {
             RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
             if (this.setting.getValue()) {
                 RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
             }
             if (isMouseHovering(mouseX, mouseY)) {
                 RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
             }
             String name = this.setting.name;
             float namey = getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
             RenderUtil.drawString(name, x + 6.0F, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
             return 16.0F;
         }
         return 0.0F;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && isMouseHovering(mouseX, mouseY))
             this.setting.setValue(!this.setting.getValue());
     }
 }