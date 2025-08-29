 package as.pw.candee.hud.modules;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class ModuleList
     extends Hud
 {
     public final Setting<Boolean> shadow;
     public final Setting<Color> color;
     public final Setting<Boolean> background;
     public final Setting<Color> backcolor;
     public final Setting<Boolean> edge;
     public final Setting<Color> edgeColor;
     public ModuleList() {
         super("ModuleList", 0.0F, 110.0F);
         this.shadow = register(new Setting("Shadow", Boolean.FALSE));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
         this.background = register(new Setting("Background", Boolean.TRUE));
         this.backcolor = register(new Setting("BGColor", new Color(40, 40, 40, 70), v -> this.background.getValue()));
         this.edge = register(new Setting("Edge", Boolean.TRUE));
         this.edgeColor = register(new Setting("EGColor", new Color(255, 255, 255, 150), v -> this.edge.getValue()));
     }
     public void onRender() {
         float y = this.y.getValue();
         float size = 1.0F;
         float _width = 0.0F;
         float height = RenderUtil.getStringHeight(1.0F);
         List<Module> sortedModuleList = new ArrayList<>(CandeePlusRewrite.m_module.modules);
         sortedModuleList.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
         for (Module module : sortedModuleList) {
             if (!module.isEnable) {
                 continue;
             }
             String name = module.name;
             float width = RenderUtil.getStringWidth(name, 1.0F);
             if (width > _width) {
                 _width = width;
             }
             if (this.background.getValue()) {
                 RenderUtil.drawRect(this.x.getValue(), y, width + 20.0F, height + 10.0F, ColorUtil.toRGBA(this.backcolor.getValue()));
                 if (this.edge.getValue()) {
                     RenderUtil.drawRect(this.x.getValue(), y, 2.0F, height + 10.0F, ColorUtil.toRGBA(this.edgeColor.getValue()));
                 }
             }
             RenderUtil.drawString(name, this.x.getValue() + 10.0F, y + 5.0F, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), 1.0F);
             y += height + 10.0F;
         }
         y -= height + 11.0F;
         this.width = _width + 20.0F;
         this.height = y - this.y.getValue();
     }
 }