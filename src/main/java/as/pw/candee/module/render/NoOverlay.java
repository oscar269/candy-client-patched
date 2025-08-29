 package as.pw.candee.module.render;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import net.minecraft.init.MobEffects;
 import net.minecraft.network.Packet;
 public class NoOverlay
     extends Module
 {
     public NoOverlay() {
         super("NoOverlay", Categories.RENDER, true, true);
     }
     public void onRender3D() {
         if (mc.player == null) {
             return;
         }
         mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
         mc.player.removeActivePotionEffect(MobEffects.NAUSEA);
     }
     public void onPacketReceive(PacketEvent.Receive event) {
         Packet<?> packet = event.packet;
         if (packet instanceof net.minecraft.network.play.server.SPacketSpawnExperienceOrb || packet instanceof net.minecraft.network.play.server.SPacketExplosion)
             event.cancel();
     }
 }