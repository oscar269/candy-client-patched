 package as.pw.candee.module.misc;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.item.ItemStack;
 public class Refill
     extends Module {
     public final Setting<Float> delay;
     public Timer timer;
     public Refill() {
         super("Refill", Categories.MISC, false, false);
         this.delay = register(new Setting("Delay", 1.0F, 10.0F, 0.0F));
         this.timer = new Timer();
     }
     public void onUpdate() {
         if (nullCheck()) {
             return;
         }
         if (this.timer == null) {
             this.timer = new Timer();
         }
         if (this.timer.passedDms(this.delay.getValue())) {
             this.timer.reset();
             for (int i = 0; i < 9; i++) {
                 ItemStack itemstack = mc.player.inventory.mainInventory.get(i);
                 if (!itemstack.isEmpty() &&
                     itemstack.isStackable() &&
                     itemstack.getCount() < itemstack.getMaxStackSize() &&
                     doRefill(itemstack)) {
                     break;
                 }
             }
         }
     }
     public boolean doRefill(ItemStack stack) {
         for (int i = 9; i < 36; i++) {
             ItemStack item = mc.player.inventory.getStackInSlot(i);
             if (CanItemBeMergedWith(item, stack)) {
                 InventoryUtil.moveItem(i);
                 return true;
             }
         }
         return false;
     }
     private boolean CanItemBeMergedWith(ItemStack p_Source, ItemStack p_Target) {
         return (p_Source.getItem() == p_Target.getItem() && p_Source.getDisplayName().equals(p_Target.getDisplayName()));
     }
 }