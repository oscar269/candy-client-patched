 package as.pw.candee.module.combat;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 public class AutoTotem
     extends Module {
     public final Setting<Boolean> packet;
     public final Setting<Boolean> doublE;
     public AutoTotem() {
         super("AutoTotem", Categories.COMBAT, false, false);
         this.packet = register(new Setting("Packet", Boolean.TRUE));
         this.doublE = register(new Setting("Double", Boolean.TRUE));
     }
     public void onTotemPop(EntityPlayer player) {
         if (player.getEntityId() != mc.player.getEntityId()) {
             return;
         }
         if (this.packet.getValue()) {
             doTotem();
         }
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         if (shouldTotem()) {
             doTotem();
         }
     }
     public void onUpdate() {
         if (nullCheck()) {
             return;
         }
         if (shouldTotem() && this.doublE.getValue()) {
             doTotem();
         }
     }
     public void doTotem() {
         int totem = findTotemSlot();
         if (totem == -1) {
             return;
         }
         InventoryUtil.moveItemTo(totem, InventoryUtil.offhandSlot);
     }
     public boolean shouldTotem() {
         return (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING);
     }
     public int findTotemSlot() {
         for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); i++) {
             if (i != InventoryUtil.offhandSlot) {
                 ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
                 if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                     return i;
                 }
             }
         }
         return -1;
     }
 }