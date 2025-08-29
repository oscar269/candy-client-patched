 package as.pw.candee.gui.clickguis.vapegui.components;
 import as.pw.candee.gui.clickguis.vapegui.Component;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class BooleanButton
     extends Component {
     public final Setting<Boolean> setting;
     public float _x;
     public BooleanButton(Setting<Boolean> setting, float x) {
         this._x = 0.0F;
         this.setting = setting;
         this.x = x;
         this.width = 110.0F;
         this.height = 18.0F;
         this._x = getX();
     }
     public float doRender(float y, int mouseX, int mouseY) {
         this.y = y;
         RenderUtil.drawRect(this.x, y, this.width, this.height, this.baseColor);
         float namey = getCenter(y, this.height, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(this.setting.name, this.x + 8.0F, namey, this.setting.getValue() ? this.white : this.gray, false, 1.0F);
         float linex = this.x + this.width - 20.0F;
         RenderUtil.drawRect(linex, namey - 3.0F, 15.0F, 8.0F, this.setting.getValue() ? this.mainColor : ColorUtil.toRGBA(60, 60, 60));
         RenderUtil.drawRect(this._x, namey - 2.0F, 4.0F, 6.0F, this.baseColor);
         if (isMouseHovering(mouseX, mouseY, this._x, namey - 2.0F, 4.0F, 6.0F)) {
             RenderUtil.drawRect(this._x, namey - 2.0F, 4.0F, 6.0F, ColorUtil.toRGBA(255, 255, 255, 50));
         }
         this._x += (float)((getX() - this._x) * 0.5D);
         return this.height;
     }
     public void onMouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
         float namey = getCenter(this.y, this.height, RenderUtil.getStringHeight(1.0F));
         if (isMouseHovering(mouseX, mouseY, this._x, namey - 2.0F, 4.0F, 6.0F) && 0 == clickedMouseButton) {
             this.setting.setValue(!this.setting.getValue());
         }
     }
     public float getX() {
         return this.setting.getValue() ? (this.x + this.width - 13.0F) : (this.x + this.width - 20.0F);
     }
 }