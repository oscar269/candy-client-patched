package as.pw.candee.gui.clickguis.clickgui.componets;

import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.RenderUtil;

public class SliderButton extends Component {
    private float value;
    private final Setting<Integer> setting;
    private boolean changing;

    public SliderButton(Module module, Setting<Integer> setting, int x, int width, int height) {
        this.setting = setting;
        this.x = x;
        this.width = width;
        this.height = height;
        this.changing = false;
        int diff = setting.maxValue - setting.minValue;
        this.value = getValue();
    }

    public void onRender2D(int y) {
        this.visible = this.setting.visible();
        if (this.visible) {
            this.y = y;
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.buttonColor);
            RenderUtil.drawRect(this.x, y, this.width * this.value, this.height, this.enabledColor);
            float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
            RenderUtil.drawString(this.setting.name, (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
            RenderUtil.drawString(String.valueOf(this.setting.getValue()), (this.x + this.width) - RenderUtil.getStringWidth(String.valueOf(this.setting.getValue()), 1.0F) - 3.0F, centeredY, this.whiteColor, false, 1.0F);
            drawOutLine();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
            this.changing = true;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.changing = false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        if (this.x - 10 < mouseX && this.x + this.width + 10 > mouseX && clickedMouseButton == 0 && this.changing) {
            this.value = (float) (mouseX - this.x) / this.width;
            if (this.value > 1.0F) {
                this.value = 1.0F;
            }
            if (this.value < 0.0F) {
                this.value = 0.0F;
            }
            this.setting.setValue((int) ((this.setting.maxValue - this.setting.minValue) * this.value + this.setting.minValue));
        }
    }

    private float getValue() {
        return (float) this.setting.getValue() / (this.setting.maxValue - this.setting.minValue);
    }
}