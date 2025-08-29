 package as.pw.candee.gui.clickguis.vapegui;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.render.ClickGUI;
 import as.pw.candee.utils.ColorUtil;
 public class Component {
     public float x;
     public float y;
     public float width;
     public float height;
     public final int white;
     public final int gray;
     public final int panelColor;
     public final int baseColor;
     public int mainColor;
     public Component() {
         this.white = ColorUtil.toRGBA(255, 255, 255);
         this.gray = ColorUtil.toRGBA(200, 200, 200);
         this.panelColor = ColorUtil.toRGBA(19, 19, 19);
         this.baseColor = ColorUtil.toRGBA(25, 25, 25);
         updateColor();
     }
     public void onRender(int mouseX, int mouseY) {
         updateColor();
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {}
     public void onMouseReleased(int mouseX, int mouseY, int state) {}
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {}
     public float doRender(float y, int mouseX, int mouseY) {
         return 0.0F;
     }
     public float getCenter(float a, float b, float c) {
         return a + (b - c) / 2.0F;
     }
     public Boolean isMouseHovering(int mouseX, int mouseY) {
         if (this.x < mouseX && this.x + this.width > mouseX) {
             return this.y < mouseY && this.y + this.height > mouseY;
         }
         return Boolean.FALSE;
     }
     public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
         if (cx < mouseX && cx + cw > mouseX) {
             return cy < mouseY && cy + ch > mouseY;
         }
         return Boolean.FALSE;
     }
     public void updateColor() {
         this.mainColor = ColorUtil.toRGBA(((ClickGUI) CandeePlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue());
     }
 }