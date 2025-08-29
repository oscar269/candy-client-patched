 package as.pw.candee.module.misc;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.combat.AutoMend;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemStack;
 public class AutoDrop
     extends Module
 {
     public final Setting<Boolean> sword;
     public final Setting<Boolean> bow;
     public final Setting<Boolean> pickel;
     public final Setting<Boolean> armor;
     public final Setting<Boolean> autoMend;
     public final Setting<Float> damege;
     public final Setting<Float> delay;
     public Timer timer;
     public AutoDrop() {
         super("AutoDrop", Categories.MISC, false, false);
         this.sword = register(new Setting("Sword", Boolean.TRUE));
         this.bow = register(new Setting("Bow", Boolean.TRUE));
         this.pickel = register(new Setting("Pickel", Boolean.TRUE));
         this.armor = register(new Setting("DamageArmor", Boolean.TRUE));
         this.autoMend = register(new Setting("PauseWhenAutoMend", Boolean.TRUE, v -> this.armor.getValue()));
         this.damege = register(new Setting("MinDamege", 50.0F, 100.0F, 0.0F, v -> this.armor.getValue()));
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
             for (int i = 9; i < 36; i++) {
                 ItemStack itemStack = mc.player.inventoryContainer.getInventory().get(i);
                 Item item = itemStack.getItem();
                 if (!itemStack.isEmpty() &&
                     itemStack.getItem() != Items.AIR) {
                     if (item instanceof net.minecraft.item.ItemSword && this.sword.getValue()) {
                         InventoryUtil.dropItem(i);
                         break;
                     }
                     if (item instanceof net.minecraft.item.ItemBow && this.bow.getValue()) {
                         InventoryUtil.dropItem(i);
                         break;
                     }
                     if (item instanceof net.minecraft.item.ItemPickaxe && this.pickel.getValue()) {
                         InventoryUtil.dropItem(i);
                         break;
                     }
                     if (item instanceof net.minecraft.item.ItemArmor && this.armor.getValue()) {
                         Module automend = CandeePlusRewrite.m_module.getModuleWithClass(AutoMend.class);
                         if ((!automend.isEnable || !this.autoMend.getValue()) &&
                             getDamage(itemStack) <= this.damege.getValue()) {
                             InventoryUtil.dropItem(i);
                             break;
                         }
                     }
                 }
             }
         }
     }
     public float getDamage(ItemStack itemStack) {
         return (float) (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage() * 1.0F * 100.0F;
     }
 }