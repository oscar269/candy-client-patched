 package as.pw.candee.gui.clickguis.clickguinew.item;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class EnumButton
     extends Component {
     public final Setting<Enum> setting;
     public EnumButton(Setting<Enum> setting, float x) {
         this.setting = setting;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
         if (isMouseHovering(mouseX, mouseY)) {
             RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
         }
         float fonty = getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(this.setting.name + " : " + this.setting.getValue().name(), x + 6.0F, fonty, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         return 16.0F;
     }
     public void onMouseClicked(int x, int y, int button) {
         if (isMouseHovering(x, y) && button == 0)
             this.setting.increaseEnum();
     }
 }