 package as.pw.candee.gui.clickguis.clickguinew;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.gui.clickguis.CGui;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.render.HUDEditor;
 import net.minecraft.client.Minecraft;
 import net.minecraft.util.ResourceLocation;
 import org.lwjgl.input.Mouse;
 public class CandyGUI2
     extends CGui
 {
     public void initGui() {
         if ((Minecraft.getMinecraft()).entityRenderer.getShaderGroup() != null) {
             (Minecraft.getMinecraft()).entityRenderer.getShaderGroup().deleteShaderGroup();
         }
         (Minecraft.getMinecraft()).entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
         if (panelList.isEmpty()) {
             int x = 50;
             for (Module.Categories category : Module.Categories.values()) {
                 if (category == Module.Categories.HUB) {
                     hubPanel = new Panel(200.0F, 20.0F, category);
                 } else {
                     panelList.add(new Panel(x, 20.0F, category));
                     x += 120;
                 }
             }
         }
     }
     public void onGuiClosed() {
         if ((Minecraft.getMinecraft()).entityRenderer.getShaderGroup() != null) {
             (Minecraft.getMinecraft()).entityRenderer.getShaderGroup().deleteShaderGroup();
         }
     }
     public void drawScreen(int mouseX, int mouseY, float partialTicks) {
         scroll();
         if (HUDEditor.instance.isEnable) {
             hubPanel.onRender(mouseX, mouseY);
         } else {
             panelList.forEach(p -> p.onRender(mouseX, mouseY));
         }
     }
     public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
         if (HUDEditor.instance.isEnable) {
             hubPanel.onMouseClicked(mouseX, mouseY, mouseButton);
             CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseClicked(mouseX, mouseY, mouseButton));
         } else {
             panelList.forEach(p -> p.onMouseClicked(mouseX, mouseY, mouseButton));
         }
     }
     public void mouseReleased(int mouseX, int mouseY, int state) {
         if (HUDEditor.instance.isEnable) {
             hubPanel.onMouseReleased(mouseX, mouseY, state);
             CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseReleased(mouseX, mouseY, state));
         } else {
             panelList.forEach(p -> p.onMouseReleased(mouseX, mouseY, state));
         }
     }
     public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
         if (HUDEditor.instance.isEnable) {
             hubPanel.onMouseClickMove(mouseX, mouseY, clickedMouseButton);
             CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
         } else {
             panelList.forEach(p -> p.onMouseClickMove(mouseX, mouseY, clickedMouseButton));
         }
     }
     public void keyTyped(char typedChar, int keyCode) {
         if (1 != keyCode) {
             if (HUDEditor.instance.isEnable) {
                 hubPanel.keyTyped(typedChar, keyCode);
             } else {
                 panelList.forEach(p -> p.keyTyped(typedChar, keyCode));
             }
             return;
         }
         if (HUDEditor.instance.isEnable) {
             HUDEditor.instance.disable();
             return;
         }
         this.mc.displayGuiScreen(null);
     }
     public void scroll() {
         int dWheel = Mouse.getDWheel();
         if (dWheel < 0) {
             if (HUDEditor.instance.isEnable) {
                 Panel hubPanel = CandyGUI2.hubPanel;
                 hubPanel.y -= 15.0F;
             } else {
                 panelList.forEach(p -> p.y -= 15.0F);
             }
         } else if (dWheel > 0) {
             if (HUDEditor.instance.isEnable) {
                 Panel hubPanel2 = CandyGUI2.hubPanel;
                 hubPanel2.y += 15.0F;
             } else {
                 panelList.forEach(p -> p.y += 15.0F);
             }
         }
     }
     public static final List<Panel> panelList = new ArrayList<>();
     public static Panel hubPanel;
 }