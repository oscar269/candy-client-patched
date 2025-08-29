package as.pw.candee.gui.clickguis.clickgui.componets;
import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.RenderUtil;
public class BooleanButton extends Component {
    public final Module module;
    public final Setting<Boolean> setting;

    public BooleanButton(Module module, Setting<Boolean> setting, int x, int width, int height) {
        this.module = module;
        this.setting = setting;
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public void onRender2D(int y) {
        this.visible = this.setting.visible();
        if (this.visible) {
            this.y = y;
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.setting.getValue() ? this.enabledColor : this.buttonColor);
            float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
            RenderUtil.drawString(this.setting.name, (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
            if (this.setting.getValue()) {
                drawOutLine();
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 0)
            this.setting.setValue(!this.setting.getValue());
    }
}