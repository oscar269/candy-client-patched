 package as.pw.candee.gui.clickguis.clickguinew;
 import java.awt.Color;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.render.ClickGUI;
 import as.pw.candee.utils.ColorUtil;
 public class Component
 {
     public Module module;
     public float x;
     public float y;
     public float width;
     public float height;
     public final Color color = ((ClickGUI) CandeePlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue();
     public final int color0 = ColorUtil.toRGBA(30, 35, 30);
     public final int hovering = ColorUtil.toRGBA(170, 170, 170, 100);
     public float doRender(int mouseX, int mouseY, float x, float y) {
         return 0.0F;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {}
     public void onMouseReleased(int mouseX, int mouseY, int state) {}
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {}
     public void onKeyTyped(char typedChar, int keyCode) {}
     public float getCenter(float a, float b, float c) {
         return a + (b - c) / 2.0F;
     }
     public Boolean isMouseHovering(int mouseX, int mouseY) {
         return this.x < mouseX && this.x + this.width > mouseX && this.y < mouseY && this.y + this.height > mouseY;
     }
     public Boolean isMouseHovering(float mouseX, float mouseY, float cx, float cy, float cw, float ch) {
         return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
     }
 }