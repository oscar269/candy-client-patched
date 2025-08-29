 package as.pw.candee.module.misc;
 import java.util.HashMap;
 import java.util.Map;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.client.CPacketChatMessage;
 public class PopAnnouncer extends Module {
     private final Map<String, Integer> popCount = new HashMap<>(); public final Setting<Float> range;
     public PopAnnouncer() {
         super("PopAnnouncer", Categories.MISC, false, false);
         this.range = register(new Setting("Range", 10.0F, 20.0F, 1.0F));
     }
     public void onTotemPop(EntityPlayer player) {
         if (player == null || nullCheck())
             return;    if (mc.player.getDistance(player) > this.range.getValue())
             return;    String name = player.getName();
         if (player == mc.player)
             return;    if (FriendManager.isFriend(name))
             return;
         int count = this.popCount.getOrDefault(name, 0) + 1;
         this.popCount.put(name, count);
         String msg = "ez pop " + name + " [" + count + "]";
         sendChat(msg);
     }
     public void onPlayerDeath(PlayerDeathEvent event) {
         if (event.player != null) this.popCount.remove(event.player.getName());
     }
     public void onPlayerLogout(EntityPlayer player) {
         if (player != null) this.popCount.remove(player.getName());
     }
     public void sendChat(String str) {
         mc.player.connection.sendPacket(new CPacketChatMessage(str));
     }
 }