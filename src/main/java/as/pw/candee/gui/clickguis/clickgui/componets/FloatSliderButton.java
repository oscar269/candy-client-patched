package as.pw.candee.gui.clickguis.clickgui.componets;
import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.RenderUtil;
public class FloatSliderButton extends Component {
    private float value;
    private final Setting<? extends Number> setting;
    private boolean changing;
    private final float diff;
    public FloatSliderButton(Module module, Setting<? extends Number> setting, int x, int width, int height) {
        this.setting = setting;
        this.x = x;
        this.width = width;
        this.height = height;
        this.changing = false;
        this.diff = setting.maxValue.floatValue() - setting.minValue.floatValue();
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
            String str = this.setting.getValue().toString();RenderUtil.drawString(str, (this.x + this.width) - RenderUtil.getStringWidth(str, 1.0F) - 3.0F, centeredY, this.whiteColor, false, 1.0F);
            drawOutLine();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) this.changing = true;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.changing = false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        if (!this.changing || clickedMouseButton != 0)
            return;    if (mouseX < this.x - 10 || mouseX > this.x + this.width + 10) return;    float v = (float) (mouseX - this.x) / this.width;v = Math.max(0.0F, Math.min(1.0F, v));this.value = v;float newVal = (this.setting.maxValue.floatValue() - this.setting.minValue.floatValue()) * this.value + this.setting.minValue.floatValue();float rounded = Math.round(newVal * 10.0F) / 10.0F;Number out = (this.setting.getValue() instanceof Double) ?
                (double) rounded : rounded;this.setting.setValue(out);
    }

    private float getValue() {
        return (this.setting.getValue().floatValue() - this.setting.minValue.floatValue()) / this.diff;
    }
}