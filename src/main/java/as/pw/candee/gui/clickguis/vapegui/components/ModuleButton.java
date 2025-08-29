 package as.pw.candee.gui.clickguis.vapegui.components;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.atomic.AtomicReference;
 import as.pw.candee.gui.clickguis.vapegui.Component;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class ModuleButton
     extends Component
 {
     public final List<Component> componentList;
     public final Module module;
     public boolean open;
     public ModuleButton(Module module, float x) {
         this.componentList = new ArrayList<>();
         this.module = module;
         this.x = x;
         this.width = 110.0F;
         this.height = 18.0F;
         this.open = false;
         module.settings.forEach(this::addSetting);
     }
     public float doRender(float y, int mouseX, int mouseY) {
         this.y = y;
         RenderUtil.drawRect(this.x, y, this.width, this.height, this.baseColor);
         boolean hovering = isMouseHovering(mouseX, mouseY);
         if (this.module.isEnable) {
             RenderUtil.drawRect(this.x, y, this.width, this.height - 0.2F, this.mainColor);
         }
         if (hovering) {
             RenderUtil.drawRect(this.x, y, this.width, this.height, ColorUtil.toRGBA(255, 255, 255, 50));
         }
         float namey = getCenter(y, this.height, RenderUtil.getStringHeight(1.0F));
         RenderUtil.drawString(this.module.name, this.x + 5.0F, namey, (this.module.isEnable || hovering) ? this.white : this.gray, false, 1.0F);
         float x = this.x + this.width - RenderUtil.getStringWidth("...", 1.0F) - 3.0F;
         RenderUtil.drawString("...", x, namey, (this.module.isEnable || hovering) ? this.white : this.gray, false, 1.0F);
         AtomicReference<Float> _height = new AtomicReference<>(this.height);
         if (this.open) {
             this.componentList.forEach(c -> {
                         float h = c.doRender(_height.get() + y, mouseX, mouseY);
                         RenderUtil.drawRect(x, y + _height.get(), 2.0F, h, this.mainColor);
                         _height.updateAndGet((v -> v + h));
                     });
         }
         return _height.get();
     }
     public void onMouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
         if (isMouseHovering(mouseX, mouseY) && clickedMouseButton == 1) {
             this.open = !this.open;
         }
         if (this.open) {
             this.componentList.forEach(c -> c.onMouseClicked(mouseX, mouseY, clickedMouseButton));
         }
     }
     public void addSetting(Setting<?> stg) {
         if (stg.getValue() instanceof Boolean) {
             this.componentList.add(new BooleanButton((Setting<Boolean>) stg, this.x));
         }
         if (stg.getValue() instanceof Enum)
             this.componentList.add(new EnumButton((Setting<Enum>) stg, this.x));
     }
 }