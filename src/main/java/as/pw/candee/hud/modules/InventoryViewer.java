package as.pw.candee.hud.modules;

import as.pw.candee.hud.Hud;
import as.pw.candee.utils.ColorUtil;
import as.pw.candee.utils.RenderUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryViewer extends Hud {
    public InventoryViewer() {
        super("InventoryViewer", 150.0F, 100.0F);
    }
    public void onRender() {
        if (nullCheck()) {
            return;
        }
        RenderUtil.drawRect(this.x.getValue() - 6.0F, this.y.getValue() - 6.0F, 180.0F, 72.0F, ColorUtil.toRGBA(0, 0, 0));
        RenderUtil.drawRect(this.x.getValue() - 5.0F, this.y.getValue() - 5.0F, 180.0F, 69.0F, ColorUtil.toRGBA(40, 40, 40));
        float _x = 0.0F;
        float _y = 0.0F;
        int c = 0;
        int scale = 19;
        InventoryPlayer inv = mc.player.inventory;
        for (int i = 9; i < 36; i++) {
            ItemStack item = inv.getStackInSlot(i);
            RenderUtil.renderItem(item, this.x.getValue() + _x + 3.0F, this.y.getValue() + _y + 3.0F);
            _x += 19.0F;
            if (++c == 9) {
                _x = 0.0F;
                _y += 19.0F;
                c = 0;
            }
        }
        this.width = 168.0F;
        this.height = 60.0F;
    }
}