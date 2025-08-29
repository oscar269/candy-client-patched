 package as.pw.candee.gui.clickguis.vapegui;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.gui.clickguis.CGui;
 import as.pw.candee.gui.clickguis.vapegui.components.Panel;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.render.HUDEditor;
 import net.minecraft.client.Minecraft;
 import net.minecraft.util.ResourceLocation;
 import org.lwjgl.input.Mouse;
 public class VapeGui
     extends CGui
 {
     public void initGui() {
         if ((Minecraft.getMinecraft()).entityRenderer.getShaderGroup() != null) {
             (Minecraft.getMinecraft()).entityRenderer.getShaderGroup().deleteShaderGroup();
         }
         (Minecraft.getMinecraft()).entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
         if (!panelList.isEmpty()) {
             return;
         }
         int x = 50;
         for (Module.Categories category : Module.Categories.values()) {
             if (category == Module.Categories.HUB) {
                 this.hudPanel = new Panel(category, 100.0F, 10.0F);
             } else {
                 panelList.add(new Panel(category, x, 10.0F));
                 x += 120;
             }
         }
     }
     public void drawScreen(int mouseX, int mouseY, float partialTicks) {
         scroll();
         if (HUDEditor.instance.isEnable) {
             this.hudPanel.onRender(mouseX, mouseY);
         } else {
             panelList.forEach(p -> p.onRender(mouseX, mouseY));
         }
     }
     public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (HUDEditor.instance.isEnable) {
             this.hudPanel.onMouseClicked(mouseX, mouseY, mouseButton);
             CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseClicked(mouseX, mouseY, mouseButton));
         } else {
             panelList.forEach(p -> p.onMouseClicked(mouseX, mouseY, mouseButton));
         }
     }
     public void scroll() {
         int dWheel = Mouse.getDWheel();
         if (dWheel < 0) {
             if (HUDEditor.instance.isEnable) {
                 Panel hudPanel = this.hudPanel;
                 hudPanel.y -= 15.0F;
             } else {
                 panelList.forEach(p -> p.y -= 15.0F);
             }
         } else if (dWheel > 0) {
             if (HUDEditor.instance.isEnable) {
                 Panel hudPanel2 = this.hudPanel;
                 hudPanel2.y += 15.0F;
             } else {
                 panelList.forEach(p -> p.y += 15.0F);
             }
         }
     }
     public static final List<Panel> panelList = new ArrayList<>();
     public Panel hudPanel;
 }