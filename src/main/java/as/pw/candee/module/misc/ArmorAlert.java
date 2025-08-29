 package as.pw.candee.module.misc;
 import java.util.HashMap;
 import java.util.Map;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.inventory.EntityEquipmentSlot;
 import net.minecraft.item.ItemStack;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 public class ArmorAlert
     extends Module
 {
     public final Setting<Float> alertPercent;
     public final Setting<Float> alertDelay;
     private final Map<EntityEquipmentSlot, Long> lastAlertTimes = new HashMap<>();
     public ArmorAlert() {
         super("ArmorAlert", Categories.MISC, false, false);
         this.alertPercent = register(new Setting("AlertPercent", 20.0F, 100.0F, 1.0F));
         this.alertDelay = register(new Setting("AlertDelay", 2000.0F, 1000.0F, 60000.0F));
     }
     @SubscribeEvent
     public void onClientTick(TickEvent.ClientTickEvent event) {
         if (mc.player == null)
             return;
         long currentTime = System.currentTimeMillis();
         int alertPercentInt = this.alertPercent.getValue().intValue();
         int alertDelayInt = this.alertDelay.getValue().intValue();
         for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
             if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                 ItemStack armor = mc.player.getItemStackFromSlot(slot);
                 if (!armor.isEmpty()) {
                     int maxDurability = armor.getMaxDamage();
                     int currentDamage = armor.getItemDamage();
                     int durabilityLeft = maxDurability - currentDamage;
                     int percent = durabilityLeft * 100 / maxDurability;
                     long lastAlertTime = this.lastAlertTimes.getOrDefault(slot, 0L);
                     if (percent <= alertPercentInt && currentTime - lastAlertTime >= alertDelayInt) {
                         sendMessage(slot.name() + " armor durability is low: " + percent + "%");
                         this.lastAlertTimes.put(slot, currentTime);
                     }
                 }
             }
         }
     }
 }