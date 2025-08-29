 package as.pw.candee.module.combat;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketHeldItemChange;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
 import net.minecraft.util.EnumHand;
 public class AutoXP
     extends Module {
     public final Setting<Boolean> packetThrow;
     public final Setting<Boolean> silentSwitch;
     public final Setting<Float> delay;
     public AutoXP() {
         super("AutoXP", Categories.COMBAT, false, false);
         this.packetThrow = register(new Setting("PacketThrow", Boolean.TRUE));
         this.silentSwitch = register(new Setting("SilentSwitch", Boolean.TRUE));
         this.delay = register(new Setting("Delay", 0.0F, 25.0F, 0.0F));
         this.oldSlot = -1;
         this.oldHand = null;
         this.timer = new Timer();
     }
     public int oldSlot; public EnumHand oldHand; public Timer timer;
     public void onEnable() {
         this.timer = new Timer();
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         int xp = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
         if (xp == -1) {
             restoreItem();
             return;
         }
         if (this.timer.passedX(this.delay.getValue())) {
             this.timer.reset();
             setItem(xp);
             mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, 90.0F, true));
             if (this.packetThrow.getValue()) {
                 mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
             } else {
                 mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
             }
         }
         restoreItem();
     }
     public void setItem(int slot) {
         if (this.silentSwitch.getValue()) {
             this.oldHand = null;
             if (mc.player.isHandActive()) {
                 this.oldHand = mc.player.getActiveHand();
             }
             this.oldSlot = mc.player.inventory.currentItem;
             mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
         } else {
             mc.player.inventory.currentItem = slot;
             mc.playerController.updateController();
         }
     }
     public void restoreItem() {
         if (this.oldSlot != -1 && this.silentSwitch.getValue()) {
             if (this.oldHand != null) {
                 mc.player.setActiveHand(this.oldHand);
             }
             mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
             this.oldSlot = -1;
             this.oldHand = null;
         }
     }
 }