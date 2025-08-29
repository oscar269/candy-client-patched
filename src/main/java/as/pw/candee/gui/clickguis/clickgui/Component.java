package as.pw.candee.gui.clickguis.clickgui;

import as.pw.candee.CandeePlusRewrite;
import as.pw.candee.gui.font.CFontRenderer;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;
import as.pw.candee.utils.Util;

public class Component implements Util {
    public final int enabledColor = ColorUtil.toRGBA(230, 90, 100, 255);
    public final int defaultColor = ColorUtil.toRGBA(25, 25, 25, 255);
    public final int whiteColor = ColorUtil.toRGBA(255, 255, 255, 255);
    public final int buttonColor = ColorUtil.toRGBA(35, 35, 35, 255);
    public final int outlineColor = ColorUtil.toRGBA(210, 70, 80, 255);
    public CFontRenderer fontRenderer = CandeePlusRewrite.m_font.fontRenderer;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean visible;

    public void onRender2D(int y) {}

    public Boolean isMouseHovering(int mouseX, int mouseY, int cx, int cy, int cw, int ch) {
        return cx < mouseX && cx + cw > mouseX && cy < mouseY && cy + ch > mouseY;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    public void mouseReleased(int mouseX, int mouseY, int state) {}

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {}

    public void onKeyTyped(char typedChar, int keyCode) {}

    public void drawOutLine() {
        RenderUtil.drawLine(this.x, this.y, (this.x + this.width), this.y, 2.0F, this.outlineColor);
        RenderUtil.drawLine(this.x, (this.y + this.height), (this.x + this.width), (this.y + this.height), 2.0F, this.outlineColor);
        RenderUtil.drawLine(this.x, this.y, this.x, (this.y + this.height), 2.0F, this.outlineColor);
        RenderUtil.drawLine((this.x + this.width), this.y, (this.x + this.width), (this.y + this.height), 2.0F, this.outlineColor);
    }

    public Boolean isMouseHovering(int mouseX, int mouseY) {
        return this.x < mouseX && this.x + this.width > mouseX && this.y < mouseY && this.y + this.height > mouseY;
    }
}