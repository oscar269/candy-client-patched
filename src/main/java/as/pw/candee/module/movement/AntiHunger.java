 package as.pw.candee.module.movement;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import net.minecraft.network.Packet;
 import net.minecraft.network.play.client.CPacketEntityAction;
 public class AntiHunger
     extends Module
 {
     public AntiHunger() {
         super("AntiHunger", Categories.MOVEMENT, false, false);
     }
     public void onEnable() {
         if (mc.player != null && mc.getConnection() != null) {
             mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
         }
         super.onEnable();
     }
     public void onDisable() {
         if (mc.player != null && mc.getConnection() != null && mc.player.isSprinting()) {
             mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
         }
         super.onDisable();
     }
     public void onPacketSend(PacketEvent.Send event) {
         Packet<?> packet = event.getPacket();
         if (packet instanceof CPacketEntityAction) {
             CPacketEntityAction action = (CPacketEntityAction)packet;
             if (action.getAction() == CPacketEntityAction.Action.START_SPRINTING || action
                 .getAction() == CPacketEntityAction.Action.STOP_SPRINTING)
                 event.cancel();
         }
     }
 }