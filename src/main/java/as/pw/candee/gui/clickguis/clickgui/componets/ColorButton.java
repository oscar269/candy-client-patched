package as.pw.candee.gui.clickguis.clickgui.componets;

import java.awt.Color;
import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.gui.clickguis.clickgui.Panel;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;

public class ColorButton extends Component {
    private final Setting<Color> setting;
    private boolean isOpening;
    private boolean redChanging;
    private boolean greenChanging;
    private boolean blueChanging;
    private boolean alphaChanging;
    private float rLx;
    private float gLx;
    private float bLx;
    private float aLx;

    public ColorButton(Module module, Setting<Color> setting, int x, int width, int height) {
        this.isOpening = false;
        this.alphaChanging = false;
        this.aLx = 0.0F;
        this.setting = setting;
        this.x = x;
        this.width = width;
        this.height = height;
        this.rLx = setting.getValue().getRed() / 255.0F;
        this.gLx = setting.getValue().getGreen() / 255.0F;
        this.bLx = setting.getValue().getBlue() / 255.0F;
        this.aLx = setting.getValue().getAlpha() / 255.0F;
    }

    public void onRender2D(int y) {
        this.visible = this.setting.visible();
        if (this.visible) {
            this.y = y;
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.enabledColor);
            float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
            float rcy1 = y + (this.height - 11) / 2.0F;
            float rcy2 = y + (this.height - 9) / 2.0F;
            RenderUtil.drawRect((this.x + this.width - 21), rcy1, 12.0F, 12.0F, this.outlineColor);
            RenderUtil.drawRect((this.x + this.width - 20), rcy2, 10.0F, 10.0F, ColorUtil.toRGBA(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha())));
            RenderUtil.drawString(this.setting.name, (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
            if (this.isOpening) {
                Panel.Cy += 93;
                float colorFieldY = (y + this.height);
                float colorFieldHeightY = colorFieldY + 93.0F;
                RenderUtil.drawRect(this.x, colorFieldY, this.width, 93.0F, this.defaultColor);
                int gray = ColorUtil.toRGBA(50, 50, 50);
                RenderUtil.drawString("Red", (this.x + 5), colorFieldY + 5.0F, this.whiteColor, false, 1.0F);
                RenderUtil.drawRect((this.x + 10), colorFieldY + 15.0F, (this.width - 20), 5.0F, gray);
                RenderUtil.drawRect((this.x + 10) + (this.width - 20) * this.rLx, colorFieldY + 13.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawString("Green", (this.x + 5), colorFieldY + 27.0F, this.whiteColor, false, 1.0F);
                RenderUtil.drawRect((this.x + 10), colorFieldY + 37.0F, (this.width - 20), 5.0F, gray);
                RenderUtil.drawRect((this.x + 10) + (this.width - 20) * this.gLx, colorFieldY + 35.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawString("Blue", (this.x + 5), colorFieldY + 49.0F, this.whiteColor, false, 1.0F);
                RenderUtil.drawRect((this.x + 10), colorFieldY + 59.0F, (this.width - 20), 5.0F, gray);
                RenderUtil.drawRect((this.x + 10) + (this.width - 20) * this.bLx, colorFieldY + 57.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawString("Alpha", (this.x + 5), colorFieldY + 71.0F, this.whiteColor, false, 1.0F);
                RenderUtil.drawRect((this.x + 10), colorFieldY + 81.0F, (this.width - 20), 5.0F, gray);
                RenderUtil.drawRect((this.x + 10) + (this.width - 20) * this.aLx, colorFieldY + 79.0F, 3.0F, 9.0F, ColorUtil.toRGBA(255, 255, 255));
                RenderUtil.drawLine(this.x, y, (this.x + this.width), y, 2.0F, this.outlineColor);
                RenderUtil.drawLine(this.x, colorFieldY, (this.x + this.width), colorFieldY, 2.0F, this.outlineColor);
                RenderUtil.drawLine(this.x, y, this.x, colorFieldHeightY, 2.0F, this.outlineColor);
                RenderUtil.drawLine((this.x + this.width), y, (this.x + this.width), colorFieldHeightY, 2.0F, this.outlineColor);
            } else {
                drawOutLine();
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float colorFieldY = (this.y + this.height);
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 1) {
            this.isOpening = !this.isOpening;
        }
        if (!this.isOpening) {
            return;
        }
        if (isMouseHovering(mouseX, mouseY, this.x + 8, (int) (colorFieldY + 7.0F), this.width - 12, 15)) {
            this.redChanging = true;
        }
        if (isMouseHovering(mouseX, mouseY, this.x + 8, (int) (colorFieldY + 29.0F), this.width - 12, 15)) {
            this.greenChanging = true;
        }
        if (isMouseHovering(mouseX, mouseY, this.x + 8, (int) (colorFieldY + 51.0F), this.width - 12, 15)) {
            this.blueChanging = true;
        }
        if (isMouseHovering(mouseX, mouseY, this.x + 8, (int) (colorFieldY + 73.0F), this.width - 12, 15)) {
            this.alphaChanging = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.redChanging = false;
        this.greenChanging = false;
        this.blueChanging = false;
        this.alphaChanging = false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        if (!this.isOpening) {
            return;
        }
        float colorFieldY = (this.y + this.height);

        if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.redChanging) {
            this.rLx = (mouseX - (this.x + 10)) / (this.width - 20.0F);
            if (this.rLx > 1.0F) {
                this.rLx = 1.0F;
            }
            if (this.rLx < 0.0F) {
                this.rLx = 0.0F;
            }
            this.setting.setValue(new Color((int)(Math.round(255.0F * this.rLx * 10.0F) / 10.0F), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha()));
        }
        if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.greenChanging) {
            this.gLx = (mouseX - (this.x + 10)) / (this.width - 20.0F);
            if (this.gLx > 1.0F) {
                this.gLx = 1.0F;
            }
            if (this.gLx < 0.0F) {
                this.gLx = 0.0F;
            }
            this.setting.setValue(new Color(this.setting.getValue().getRed(), (int)(Math.round(255.0F * this.gLx * 10.0F) / 10.0F), this.setting.getValue().getBlue(), this.setting.getValue().getAlpha()));
        }
        if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.blueChanging) {
            this.bLx = (mouseX - (this.x + 10)) / (this.width - 20.0F);
            if (this.bLx > 1.0F) {
                this.bLx = 1.0F;
            }
            if (this.bLx < 0.0F) {
                this.bLx = 0.0F;
            }
            this.setting.setValue(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), (int)(Math.round(255.0F * this.bLx * 10.0F) / 10.0F), this.setting.getValue().getAlpha()));
        }
        if (this.x + 8 < mouseX && this.x + this.width - 12 > mouseX && clickedMouseButton == 0 && this.alphaChanging) {
            this.aLx = (mouseX - (this.x + 10)) / (this.width - 20.0F);
            if (this.aLx > 1.0F) {
                this.aLx = 1.0F;
            }
            if (this.aLx < 0.0F) {
                this.aLx = 0.0F;
            }
            this.setting.setValue(new Color(this.setting.getValue().getRed(), this.setting.getValue().getGreen(), this.setting.getValue().getBlue(), (int)(Math.round(255.0F * this.aLx * 10.0F) / 10.0F)));
        }
    }
}