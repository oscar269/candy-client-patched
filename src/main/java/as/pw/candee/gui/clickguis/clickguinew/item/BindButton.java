 package as.pw.candee.gui.clickguis.clickguinew.item;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.module.Module;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 import org.lwjgl.input.Keyboard;
 public class BindButton
     extends Component {
     public final Module module;
     public boolean keyWaiting;
     public BindButton(Module module, float x) {
         this.keyWaiting = false;
         this.module = module;
         this.x = x;
         this.width = 100.0F;
         this.height = 16.0F;
     }
     public float doRender(int mouseX, int mouseY, float x, float y) {
         RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
         if (this.keyWaiting) {
             RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
         }
         if (isMouseHovering(mouseX, mouseY)) {
             RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
         }
         float bindy = getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString("Bind : " + (this.keyWaiting ? "..." : ((this.module.key.key == -1) ? "NONE" : Keyboard.getKeyName(this.module.key.key))), x + 6.0F, bindy, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         return 16.0F;
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && isMouseHovering(mouseX, mouseY)) {
             this.keyWaiting = !this.keyWaiting;
         }
     }
     public void onKeyTyped(char typedChar, int keyCode) {
         if (this.keyWaiting) {
             if (keyCode == 14 || keyCode == 1) {
                 this.module.setKey(-1);
             } else {
                 this.module.setKey(keyCode);
             }
             this.keyWaiting = false;
         }
     }
 }