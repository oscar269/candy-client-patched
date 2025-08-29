package as.pw.candee.module.combat;
import java.util.ArrayList;
import java.util.List;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
public class Quiver extends Module {
        private final Setting<Integer> tickDelay;
        private int lastSlot;
        public Quiver() { super("Quiver", Categories.COMBAT, false, false);
                this.lastSlot = -1; this.tickDelay = register(new Setting("TickDelay", 3, 0, 8)); }
        public void onUpdate() { if (mc.player == null) return;    if (mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= this.tickDelay.getValue()) { mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, -90.0F, mc.player.onGround)); mc.playerController.onStoppedUsingItem(mc.player); }    List<Integer> arrowSlots = getItemInventory(Items.TIPPED_ARROW); if (arrowSlots.size() == 1 && arrowSlots.get(0) == -1) return;    int speedSlot = -1, strengthSlot = -1;
                for (int slot : arrowSlots) {
                        ResourceLocation loc = PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(slot)).getRegistryName();
                        if (loc == null) continue;
                        String path = loc.getPath();
                        if (path.contains("swiftness")) {
                                speedSlot = slot;
                                continue;
                        }
                        if (path.contains("strength"))
                                strengthSlot = slot;
                }
                if (speedSlot != -1) { silentSwitchTo(speedSlot); mc.playerController.onStoppedUsingItem(mc.player); restoreSwitch(); }    } private void silentSwitchTo(int slot) { if (slot < 0)
                        return;    this.lastSlot = mc.player.inventory.currentItem;
                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot)); }
        private List<Integer> getItemInventory(Item item) { List<Integer> slots = new ArrayList<>(); for (int i = 9; i < 36; i++) { if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                                slots.add(i);    }
                if (slots.isEmpty())
                        slots.add(-1);    return slots; } private void restoreSwitch() { if (this.lastSlot != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.lastSlot));
                        this.lastSlot = -1;
                }    }
}