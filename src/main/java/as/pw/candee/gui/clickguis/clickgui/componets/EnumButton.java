package as.pw.candee.gui.clickguis.clickgui.componets;

import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.RenderUtil;

public class EnumButton extends Component {
    private final Setting<Enum> setting;

    public EnumButton(Module m, Setting<Enum> setting, int x, int width, int height) {
        this.setting = setting;
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public void onRender2D(int y) {
        this.visible = true;
        this.y = y;
        float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
        RenderUtil.drawRect(this.x, y, this.width, this.height, this.enabledColor);
        RenderUtil.drawString(this.setting.name + " : " + this.setting.getValue().name(), (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
        drawOutLine();
    }

    public void mouseClicked(int x, int y, int button) {
        if (isMouseHovering(x, y) && button == 0)
            this.setting.increaseEnum();
    }
}