package as.pw.candee.gui.clickguis.clickgui.componets;

import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.module.Module;
import as.pw.candee.utils.RenderUtil;
import org.lwjgl.input.Keyboard;
import as.pw.candee.module.render.ClickGUI;

public class KeybindButton extends Component {
    private final Module module;
    private boolean isWaiting;

    public KeybindButton(Module m, int x, int width, int height) {
        this.isWaiting = false;
        this.module = m;
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public void onRender2D(int y) {
        this.visible = true;
        this.y = y;
        float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
        if (this.isWaiting) {
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.enabledColor);
            RenderUtil.drawString("Key : ...", (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
        } else {
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.buttonColor);
            RenderUtil.drawString("Key : " + ((this.module.key.getKey() != -1) ? Keyboard.getKeyName(this.module.key.getKey()) : "NONE"), (this.x + 5), centeredY, this.whiteColor, false, 1.0F);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
            this.isWaiting = !this.isWaiting;
        }
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isWaiting) {
            if (keyCode == 14 || keyCode == 1) {
                if (!(this.module instanceof ClickGUI)) {
                    this.module.setKey(-1);
                }
            } else {
                this.module.setKey(keyCode);
            }
            this.isWaiting = false;
        }
    }
}