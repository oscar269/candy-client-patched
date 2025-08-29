package as.pw.candee.gui.clickguis.clickgui;

import java.util.ArrayList;
import java.util.List;

import as.pw.candee.CandeePlusRewrite;
import as.pw.candee.gui.clickguis.CGui;
import as.pw.candee.hud.Hud;
import as.pw.candee.module.Module;
import as.pw.candee.module.render.HUDEditor;
import org.lwjgl.input.Mouse;

public class CandyGUI extends CGui {
    public void initGui() {
        if (panels.isEmpty()) {
            int x = 30;
            for (Module.Categories c : Module.Categories.values()) {
                if (c == Module.Categories.HUB) {
                    hubPanel = new Panel(c, 30, 30, true);
                } else {
                    panels.add(new Panel(c, x, 20, true));
                    CandeePlusRewrite.Info("Loaded module panel : " + c.name());
                    x += 120;
                }
            }
        }
    }

    public void onGuiClosed() {
        if (HUDEditor.instance.isEnable) {
            HUDEditor.instance.disable();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        scroll();
        if (HUDEditor.instance.isEnable) {
            hubPanel.onRender(mouseX, mouseY, partialTicks);
        } else {
            panels.forEach(p -> p.onRender(mouseX, mouseY, partialTicks));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (HUDEditor.instance.isEnable) {
            hubPanel.mouseClicked(mouseX, mouseY, mouseButton);
            CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseClicked(mouseX, mouseY, mouseButton));
        } else {
            panels.forEach(p -> p.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (HUDEditor.instance.isEnable) {
            hubPanel.mouseReleased(mouseX, mouseY, state);
            CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseReleased(mouseX, mouseY, state));
        } else {
            panels.forEach(p -> p.mouseReleased(mouseX, mouseY, state));
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (HUDEditor.instance.isEnable) {
            hubPanel.mouseClickMove(mouseX, mouseY, clickedMouseButton);
            CandeePlusRewrite.m_module.modules.stream().filter(m -> m instanceof Hud).forEach(m -> ((Hud)m).mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
        } else {
            panels.forEach(p -> p.mouseClickMove(mouseX, mouseY, clickedMouseButton));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (HUDEditor.instance.isEnable) {
            hubPanel.keyTyped(typedChar, keyCode);
        } else {
            panels.forEach(p -> p.keyTyped(typedChar, keyCode));
        }
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (Exception ignored) {}
    }

    public void scroll() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            if (HUDEditor.instance.isEnable) {
                Panel hubPanel = CandyGUI.hubPanel;
                hubPanel.y -= 15;
            } else {
                panels.forEach(p -> p.y -= 15);
            }
        } else if (dWheel > 0) {
            if (HUDEditor.instance.isEnable) {
                Panel hubPanel2 = CandyGUI.hubPanel;
                hubPanel2.y += 15;
            } else {
                panels.forEach(p -> p.y += 15);
            }
        }
    }

    private static final List<Panel> panels = new ArrayList<>();
    private static Panel hubPanel = null;
    public static boolean isHovering = false;
}