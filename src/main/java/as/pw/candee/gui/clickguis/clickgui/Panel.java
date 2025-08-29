package as.pw.candee.gui.clickguis.clickgui;

import java.util.ArrayList;
import java.util.List;

import as.pw.candee.CandeePlusRewrite;
import as.pw.candee.gui.clickguis.clickgui.componets.ModuleButton;
import as.pw.candee.module.Module;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;
import as.pw.candee.utils.Util;

public class Panel implements Util {
    public final String name;
    public int x;
    public int y;
    public final int tmpy;
    public final int width;
    public final int height;
    public boolean opening;
    private boolean isMoving;
    private int diff_x;
    private int diff_y;

    public Panel(Module.Categories category, int x, int y, boolean isOpening) {
        this.tmpy = 0;
        this.name = category.name();
        this.x = x;
        this.y = y;
        this.width = 95;
        this.height = 17;
        this.opening = isOpening;
        this.isMoving = false;
        this.diff_x = 0;
        this.diff_y = 0;
        this.buttons = new ArrayList<>();
        List<Module> modules = new ArrayList<>(CandeePlusRewrite.m_module.getModulesWithCategories(category));
        modules.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
        for (Module m : modules) {
            if (m.hide) {
                continue;
            }
            this.buttons.add(new ModuleButton(m, x, this.width, 15));
        }
    }

    public void onRender(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.width, this.height, ColorUtil.toARGB(50, 50, 50, 255));
        float width = RenderUtil.getStringWidth(this.name, 1.0F);
        float centeredX = this.x + 0.0F;
        float centeredY = this.y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
        RenderUtil.drawString(this.name, centeredX, centeredY, ColorUtil.toARGB(255, 255, 255, 255), false, 1.0F);
        Cy = this.y + 2;
        if (this.opening) {
            Cy = this.y + 2;
            this.buttons.forEach(b -> b.onRender(Cy += 15));
        }
        int outlineColor = ColorUtil.toRGBA(210, 70, 80, 255);
        float y1 = (Cy - 2 + this.height);
        RenderUtil.drawLine(this.x, this.y, this.x, y1, 2.0F, outlineColor);
        RenderUtil.drawLine(this.x + width, this.y, this.x + width, y1, 2.0F, outlineColor);
        RenderUtil.drawLine(this.x, this.y, this.x + width, this.y, 2.0F, outlineColor);
        RenderUtil.drawLine(this.x, y1, this.x + width, y1, 2.0F, outlineColor);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.buttons.forEach(b -> b.mouseClicked(mouseX, mouseY, mouseButton));
        if (this.x < mouseX && this.x + this.width > mouseX && mouseButton == 0 && !CandyGUI.isHovering && this.y < mouseY && this.y + this.height > mouseY) {
            this.isMoving = true;
            CandyGUI.isHovering = true;
            this.diff_x = this.x - mouseX;
            this.diff_y = this.y - mouseY;
        }
        if (this.x < mouseX && this.x + this.width > mouseX && mouseButton == 1 && this.y < mouseY && this.y + this.height > mouseY) {
            this.opening = !this.opening;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.buttons.forEach(b -> b.mouseReleased(mouseX, mouseY, state));
        this.isMoving = false;
        CandyGUI.isHovering = false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        this.buttons.forEach(b -> b.mouseClickMove(mouseX, mouseY, clickedMouseButton));
        if (this.isMoving) {
            this.x = mouseX + this.diff_x;
            this.y = mouseY + this.diff_y;
            this.buttons.forEach(b -> b.changeX(this.x));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.buttons.forEach(b -> b.onKeyTyped(typedChar, keyCode));
    }

    public static int Cy = 0;
    public final List<ModuleButton> buttons;
}