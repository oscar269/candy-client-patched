 package as.pw.candee.module.movement;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.entity.EntityOtherPlayerMP;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.Packet;
 import net.minecraft.util.math.BlockPos;
 public class Blink
     extends Module
 {
     public final Setting<Boolean> noEntity;
     public final Setting<Boolean> limit;
     public final Setting<Integer> maxPackets;
     public final Setting<Boolean> all;
     public final Setting<Integer> skip;
     public EntityPlayer entity;
     public BlockPos startPos;
     public List<Packet<?>> packets;
     public Blink() {
         super("Blink", Categories.MOVEMENT, false, false);
         this.noEntity = register(new Setting("NoEntity", Boolean.FALSE));
         this.limit = register(new Setting("Limit", Boolean.FALSE));
         this.maxPackets = register(new Setting("MaxPackets", 20, 70, 10, s -> this.limit.getValue()));
         this.all = register(new Setting("Cancel All", Boolean.FALSE));
         this.skip = register(new Setting("Skip", 0, 3, 0));
         this.entity = null;
         this.startPos = null;
         this.packets = null;
     }
     public void onEnable() {
         if (nullCheck()) {
             disable();
             return;
         }
         this.packets = new ArrayList<>();
         if (!this.noEntity.getValue()) {
             (this.entity = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile())).copyLocationAndAnglesFrom(mc.player);
             this.entity.rotationYaw = mc.player.rotationYaw;
             this.entity.rotationYawHead = mc.player.rotationYawHead;
             this.entity.inventory.copyInventory(mc.player.inventory);
             mc.world.addEntityToWorld(6942069, this.entity);
             this.startPos = mc.player.getPosition();
         }
     }
     public void onDisable() {
         if (nullCheck() || this.packets == null) {
             return;
         }
         int counter = 0;
         for (Packet<?> packet : this.packets) {
             if (this.skip.getValue() <= counter) {
                 mc.player.connection.sendPacket(packet);
                 counter = 0;
             }
             counter++;
         }
         mc.world.removeEntityFromWorld(this.entity.getEntityId());
     }
     public void onUpdate() {
         if (nullCheck() || this.packets == null) {
             return;
         }
         if (this.limit.getValue() && this.packets.size() > this.maxPackets.getValue()) {
             sendMessage("Packets size has reached the limit! disabling...");
             this.packets = new ArrayList<>();
             disable();
         }
     }
     public void onPacketSend(PacketEvent.Send event) {
         if (nullCheck() || this.packets == null) {
             return;
         }
         Packet<?> packet = event.packet;
         if (!this.all.getValue()) {
             if (packet instanceof net.minecraft.network.play.client.CPacketChatMessage || packet instanceof net.minecraft.network.play.client.CPacketConfirmTeleport || packet instanceof net.minecraft.network.play.client.CPacketKeepAlive || packet instanceof net.minecraft.network.play.client.CPacketTabComplete || packet instanceof net.minecraft.network.play.client.CPacketClientStatus) {
                 return;
             }
             this.packets.add(packet);
             event.cancel();
         }
         else if (packet instanceof net.minecraft.network.play.client.CPacketPlayer) {
             this.packets.add(packet);
             event.cancel();
         }
     }
 }