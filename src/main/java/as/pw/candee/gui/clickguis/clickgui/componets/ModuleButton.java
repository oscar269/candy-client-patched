package as.pw.candee.gui.clickguis.clickgui.componets;

import java.util.ArrayList;
import java.util.List;
import as.pw.candee.gui.clickguis.clickgui.Component;
import as.pw.candee.gui.clickguis.clickgui.Panel;
import as.pw.candee.module.Module;
import as.pw.candee.module.render.HUDEditor;
import as.pw.candee.setting.Setting;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;

public class ModuleButton extends Component {
    public final List<Component> components;
    public final Module module;
    private boolean isOpening;

    public ModuleButton(Module module, int x, int width, int height) {
        this.components = new ArrayList<>();
        this.isOpening = false;
        this.module = module;
        this.x = x;
        this.width = width;
        this.height = height;
        module.settings.forEach(this::addButtonBySetting);
        if (module.getClass() != HUDEditor.class) {
            this.components.add(new KeybindButton(module, x, width, height));
        }
    }

    public void onRender(int y) {
        this.y = y;
        RenderUtil.drawRect(this.x, y, this.width, this.height, this.defaultColor);
        if (this.module.isEnable) {
            RenderUtil.drawRect(this.x, y, this.width, this.height, this.enabledColor);
        }
        float centeredY = y + (this.height - RenderUtil.getStringHeight(1.0F)) / 2.0F;
        RenderUtil.drawString(this.module.name, (this.x + 3), centeredY, this.module.isEnable ? ColorUtil.toRGBA(255, 255, 255, 255) : ColorUtil.toARGB(255, 255, 255, 255), false, 1.0F);
        if (this.isOpening) {
            this.components.forEach(c -> {
                c.onRender2D(Panel.Cy += this.height);
                if (!c.visible) {
                    Panel.Cy -= this.height;
                }
            });
        }
    }

    public void addButtonBySetting(Setting<?> s) {
        if (s.getValue() instanceof Boolean) {
            this.components.add(new BooleanButton(this.module, (Setting<Boolean>) s, this.x, this.width, this.height));
        }
        else if (s.getValue() instanceof Integer) {
            this.components.add(new SliderButton(this.module, (Setting<Integer>) s, this.x, this.width, this.height));
        }
        else if (s.getValue() instanceof Float) {
            this.components.add(new FloatSliderButton(this.module, (Setting<? extends Number>) s, this.x, this.width, this.height));
        }
        else if (s.getValue() instanceof java.awt.Color) {
            this.components.add(new ColorButton(this.module, (Setting)s, this.x, this.width, this.height));
        } else {
            this.components.add(new EnumButton(this.module, (Setting)s, this.x, this.width, this.height));
        }
    }

    public void changeX(int x) {
        this.x = x;
        this.components.forEach(c -> c.x = x);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isOpening) {
            this.components.forEach(c -> {
                if (c.visible) {
                    c.mouseClicked(mouseX, mouseY, mouseButton);
                }
            });
        }
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 0) {
            this.module.toggle();
        }
        if (isMouseHovering(mouseX, mouseY) && mouseButton == 1) {
            this.isOpening = !this.isOpening;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.isOpening) {
            this.components.forEach(c -> c.mouseReleased(mouseX, mouseY, state));
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton) {
        if (this.isOpening) {
            this.components.forEach(c -> c.mouseClickMove(mouseX, mouseY, clickedMouseButton));
        }
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isOpening)
            this.components.forEach(c -> c.onKeyTyped(typedChar, keyCode));
    }
}