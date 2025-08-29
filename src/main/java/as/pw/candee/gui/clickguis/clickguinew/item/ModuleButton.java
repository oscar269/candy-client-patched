 package as.pw.candee.gui.clickguis.clickguinew.item;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.atomic.AtomicReference;
 import as.pw.candee.gui.clickguis.clickguinew.Component;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class ModuleButton
     extends Component
 {
     public final List<Component> componentList;
     public boolean open;
     public ModuleButton(Module module, float x) {
         this.componentList = new ArrayList<>();
         this.module = module;
         this.x = x;
         this.width = 100.0F;
         this.open = false;
         this.height = 16.0F;
         module.settings.forEach(this::addSetting);
         add(new BindButton(module, x));
     }
     public float onRender(int mouseX, int mouseY, float x, float y) {
         RenderUtil.drawRect(this.x = x, this.y = y, 100.0F, 16.0F, this.color0);
         if (this.module.isEnable) {
             RenderUtil.drawRect(x, y, 100.0F, 16.0F, ColorUtil.toRGBA(this.color));
         }
         if (isMouseHovering(mouseX, mouseY)) {
             RenderUtil.drawRect(x, y, 100.0F, 16.0F, this.hovering);
         }
         String name = this.module.name;
         float namey = getCenter(y, 16.0F, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(name, x + 3.0F, namey, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         RenderUtil.drawString("...", x + 100.0F - RenderUtil.getStringWidth("...", 1.0F) - 3.0F, namey - 1.0F, ColorUtil.toRGBA(250, 250, 250), false, 1.0F);
         AtomicReference<Float> height = new AtomicReference<>(16.0F);
         if (this.open) {
             this.componentList.forEach(c -> {
                         float h = c.doRender(mouseX, mouseY, x, y + height.get());
                         RenderUtil.drawRect(x, y + height.get(), 2.0F, h, ColorUtil.toRGBA(this.color));
                         height.updateAndGet((v -> v + h));
                     });
         }
         return height.get();
     }
     public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (mouseButton == 0 && isMouseHovering(mouseX, mouseY)) {
             this.module.toggle();
         }
         if (mouseButton == 1 && isMouseHovering(mouseX, mouseY)) {
             this.open = !this.open;
         }
         if (this.open) {
             this.componentList.forEach(c -> c.onMouseClicked(mouseX, mouseY, mouseButton));
         }
     }
     public void onMouseReleased(int mouseX, int mouseY, int state) {
         if (this.open) {
             this.componentList.forEach(c -> c.onMouseReleased(mouseX, mouseY, state));
         }
     }
     public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
         if (this.open) {
             this.componentList.forEach(c -> c.onMouseClickMove(mouseX, mouseY, clickedMouseButton));
         }
     }
     public void onKeyTyped(char typedChar, int keyCode) {
         if (this.open) {
             this.componentList.forEach(c -> c.onKeyTyped(typedChar, keyCode));
         }
     }
     public void addSetting(Setting<Boolean> setting) {
         Object value = setting.value;
         if (value instanceof Boolean) {
             add(new BooleanButton(setting, this.x));
         }
         else if (value instanceof Integer) {
             add(new IntegerSlider((Setting)setting, this.x));
         }
         else if (value instanceof Float) {
             add(new FloatSlider((Setting)setting, this.x));
         }
         else if (value instanceof java.awt.Color) {
             add(new ColorSlider((Setting)setting, this.x));
         } else {
             add(new EnumButton((Setting)setting, this.x));
         }
     }
     private void add(Component component) {
         this.componentList.add(component);
     }
 }