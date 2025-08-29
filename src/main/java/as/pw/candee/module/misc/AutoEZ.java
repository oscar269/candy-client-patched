 package as.pw.candee.module.misc;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.client.CPacketChatMessage;
 public class AutoEZ extends Module {
     public AutoEZ() {
         super("AutoEZ", Categories.MISC, false, false);
         this.range = register(new Setting("Range", 10.0F, 50.0F, 1.0F));
     }
     public void onPlayerDeath(PlayerDeathEvent event) {
         if (nullCheck())
             return;    EntityPlayer player = event.player;
         if (player.getHealth() > 0.0F)
             return;    if (mc.player.getDistance(player) > this.range.getValue())
             return;
         if (player == mc.player)
             return;    if (FriendManager.isFriend(player.getName()))
             return;
         EZ();
     }
     public final Setting<Float> range;
     public void EZ() {
         sendChat("you just got ez'd by candy+ rewrite");
     }
     public void sendChat(String str) {
         mc.player.connection.sendPacket(new CPacketChatMessage(str));
     }
 }