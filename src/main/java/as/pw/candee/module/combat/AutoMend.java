 package as.pw.candee.module.combat;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.concurrent.atomic.AtomicInteger;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.init.Items;
 import net.minecraft.item.ItemStack;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
 import net.minecraft.util.EnumHand;
 import org.lwjgl.input.Keyboard;
 public class AutoMend extends Module {
     public final Setting<Float> delay;
     public final Setting<Float> armor;
     public final Setting<Float> pct;
     public final Setting<Boolean> press;
     public final Setting<Boolean> silentSwitch;
     public boolean toggleoff;
     public Map<Integer, Integer> armors;
     public Timer xpTimer;
     public Timer armorTimer;
     private EnumHand oldhand;
     private int oldslot;
     public AutoMend() {
         super("AutoMend", Categories.COMBAT, false, false);
         this.delay = register(new Setting("Delay", 3.0F, 10.0F, 0.0F));
         this.armor = register(new Setting("ArmorDelay", 3.0F, 20.0F, 0.0F));
         this.pct = register(new Setting("Pct", 80.0F, 100.0F, 10.0F));
         this.press = register(new Setting("Press", Boolean.TRUE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.TRUE));
         this.toggleoff = false;
         this.armors = new HashMap<>();
         this.armorTimer = new Timer();
         this.oldhand = null;
         this.oldslot = -1;
         disable();
     }
     public void onEnable() {
         this.xpTimer = new Timer();
         this.armorTimer = new Timer();
     }
     public void onDisable() {
         if (!this.toggleoff) {
             this.toggleoff = true;
             this.armors = new HashMap<>();
             enable();
             return;
         }
         this.toggleoff = false;
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         if (!this.toggleoff) {
             if (this.armorTimer == null) {
                 this.armorTimer = new Timer();
             }
             this.armorTimer.reset();
             int xp = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
             if (xp == -1) {
                 sendMessage("Cannot find XP! disabling");
                 setDisable();
                 return;
             }
             if (isDone()) {
                 setDisable();
                 return;
             }
             if (this.xpTimer == null) {
                 this.xpTimer = new Timer();
             }
             if (this.xpTimer.passedX(this.delay.getValue())) {
                 this.xpTimer.reset();
                 setItem(xp);
                 mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, 90.0F, false));
                 mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                 restoreItem();
             }
             if (this.key.key != -1 && this.press.getValue() &&
                 !Keyboard.isKeyDown(this.key.key)) {
                 setDisable();
             }
         }
         else {
             if (this.armorTimer == null) {
                 this.armorTimer = new Timer();
             }
             if (this.armorTimer.passedDms(this.armor.getValue())) {
                 this.armorTimer.reset();
                 AtomicInteger c = new AtomicInteger();
                 AtomicInteger key = new AtomicInteger();
                 this.armors.forEach((k, v) -> {
                             if (c.get() == 0) {
                                 InventoryUtil.moveItemTo(k, v);
                                 key.set(k);
                             }
                             c.getAndIncrement();
                         });
                 if (c.get() == 0) {
                     disable();
                 } else {
                     this.armors.remove(key.get());
                 }
             }
         }
     }
     public void onPacketSend(PacketEvent.Send event) {
         if (event.getPacket() instanceof CPacketPlayer) {
             CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
         }
     }
     public void setDisable() {
         this.toggleoff = true;
     }
     public void moveArmorToSlot(int armor, int empty) {
         InventoryUtil.moveItemTo(armor, empty);
         this.armors.put(empty, armor);
     }
     public boolean isDone() {
         boolean done = true;
         for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); i++) {
             ItemStack itemStack = mc.player.inventoryContainer.getInventory().get(i);
             if (i > 4 &&
                 i < 9 &&
                 !itemStack.isEmpty()) {
                 if (getRate(itemStack) < this.pct.getValue()) {
                     done = false;
                 } else {
                     int slot = getFreeSlot();
                     if (slot != -1) {
                         moveArmorToSlot(i, slot);
                     }
                 }
             }
         }
         return done;
     }
     public int getFreeSlot() {
         for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); i++) {
             if (i != 0 && i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 7 &&
                 i != 8) {
                 ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
                 if (stack.isEmpty() || stack.getItem() == Items.AIR) {
                     return i;
                 }
             }
         }
         return -1;
     }
     public float getRate(ItemStack itemStack) {
         return (float) (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage() * 1.0F * 100.0F;
     }
     public void setItem(int slot) {
         if (this.silentSwitch.getValue()) {
             this.oldhand = null;
             if (mc.player.isHandActive()) {
                 this.oldhand = mc.player.getActiveHand();
             }
             this.oldslot = mc.player.inventory.currentItem;
             mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
             mc.player.inventory.currentItem = slot;
             mc.playerController.updateController();
         }
     }
     public void restoreItem() {
         if (this.oldslot != -1 && this.silentSwitch.getValue()) {
             if (this.oldhand != null) {
                 mc.player.setActiveHand(this.oldhand);
             }
             mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
             this.oldslot = -1;
             this.oldhand = null;
         }
     }
 }