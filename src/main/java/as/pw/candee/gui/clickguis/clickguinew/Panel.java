 package as.pw.candee.gui.clickguis.clickguinew;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.atomic.AtomicReference;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.gui.clickguis.clickguinew.item.ModuleButton;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.render.ClickGUI;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 import as.pw.candee.utils.StringUtil;
 public class Panel
 {
     public float x;
     public float y;
     public final float width;
     public final float height;
     public boolean open;
     public final Color color;
     public final Module.Categories category;
     public final List<ModuleButton> modules;
     public boolean moving;
     public float diffx;
     public float diffy;
     public Panel(float x, float y, Module.Categories category) {
         this.modules = new ArrayList<>();
         this.moving = false;
         this.diffy = 0.0F;
         this.x = x;
         this.y = y;
         this.width = 100.0F;
         this.height = 15.0F;
         this.open = true;
         this.color = ((ClickGUI) CandeePlusRewrite.m_module.getModuleWithClass(ClickGUI.class)).color.getValue();
         this.category = category;
         List<Module> modules = new ArrayList<>(CandeePlusRewrite.m_module.getModulesWithCategories(category));
         modules.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
         modules.forEach(m -> this.modules.add(new ModuleButton(m, x)));
     }
     public void onRender(int mouseX, int mouseY) {
         AtomicReference<Float> _y = new AtomicReference<>(this.y + 15.0F);
         if (this.open) {
             this.modules.forEach(m -> _y.updateAndGet(v -> v + m.onRender(mouseX, mouseY , this.x , _y.get())));
         }
         String name = StringUtil.getName(this.category.name());
         RenderUtil.drawRect(this.x, this.y, 100.0F, 15.0F, ColorUtil.toRGBA(30, 30, 30));
         RenderUtil.drawLine(this.x, this.y + 15.0F, this.x + 100.0F, this.y + 15.0F, 2.0F, ColorUtil.toRGBA(this.color));
         float namex = getCenter(this.x, 100.0F, RenderUtil.getStringWidth(name, 1.0F));
         float namey = getCenter(this.y, 15.0F, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(name, namex, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         ClickGUI module = (ClickGUI) CandeePlusRewrite.m_module.getModuleWithClass(ClickGUI.class);
         if (module == null) {
             return;
         }
         if (module.outline.getValue()) {
             RenderUtil.drawLine(this.x, this.y, this.x + 100.0F, this.y, 1.0F, ColorUtil.toRGBA(this.color));
             RenderUtil.drawLine(this.x, _y.get(), this.x + 100.0F, _y.get(), 1.0F, ColorUtil.toRGBA(this.color));
             RenderUtil.drawLine(this.x, this.y, this.x, _y.get(), 1.0F, ColorUtil.toRGBA(this.color));
             RenderUtil.drawLine(this.x + 100.0F, this.y, this.x + 100.0F, _y.get(), 1.0F, ColorUtil.toRGBA(this.color));
         }
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && isMouseHovering(mouseX, mouseY)) {
             this.moving = true;
             this.diffx = this.x - mouseX;
             this.diffy = this.y - mouseY;
         }
         if (mouseButton == 1 && isMouseHovering(mouseX, mouseY)) {
             this.open = !this.open;
         }
         if (this.open) {
             this.modules.forEach(m -> m.onMouseClicked(mouseX, mouseY, mouseButton));
         }
     }
     public void onMouseReleased(int mouseX, int mouseY, int state) {
         this.moving = false;
         if (this.open) {
             this.modules.forEach(m -> m.onMouseReleased(mouseX, mouseY, state));
         }
     }
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
         if (clickedMouseButton == 0 && this.moving) {
             this.x = mouseX + this.diffx;
             this.y = mouseY + this.diffy;
         }
         if (this.open) {
             this.modules.forEach(m -> m.onMouseClickMove(mouseX, mouseY, clickedMouseButton));
         }
     }
     public void keyTyped(char typedChar, int keyCode) {
         if (this.open) {
             this.modules.forEach(m -> m.onKeyTyped(typedChar, keyCode));
         }
     }
     public Boolean isMouseHovering(int mouseX, int mouseY) {
         return this.x < mouseX && this.x + this.width > mouseX && this.y < mouseY && this.y + this.height > mouseY;
     }
     public float getCenter(float a, float b, float c) {
         return a + (b - c) / 2.0F;
     }
 }