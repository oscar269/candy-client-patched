 package as.pw.candee.module.misc;
 import as.pw.candee.command.CommandManager;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import net.minecraft.network.play.client.CPacketChatMessage;
 public class ChatSuffix
     extends Module {
     public ChatSuffix() {
         super("ChatSuffix", Categories.MISC, false, false);
     }
     public void onPacketSend(PacketEvent.Send event) {
         if (event.packet instanceof CPacketChatMessage) {
             CPacketChatMessage p = (CPacketChatMessage)event.packet;
             String msg = p.getMessage();
             if (msg.startsWith("/") || msg.startsWith(CommandManager.getCommandPrefix()))
                 return;
             String suffixCheck = "Candy+ Rewrite".toLowerCase();
             if (msg.contains(" || " + toUnicode(suffixCheck)))
                 return;
             String suffix = toUnicode(" || " + suffixCheck);
             String newMsg = msg + suffix;
             if (newMsg.length() > 255)
                 return;
             event.cancel();
             mc.player.connection.sendPacket(new CPacketChatMessage(newMsg));
         }
     }
     public String toUnicode(String s) {
         return s.toLowerCase().replace("a", "ᴀ").replace("b", "ʙ").replace("c", "ᴄ").replace("d", "ᴅ").replace("e", "ᴇ").replace("f", "ꜰ").replace("g", "ɢ").replace("h", "ʜ").replace("i", "ɪ").replace("j", "ᴊ").replace("k", "ᴋ").replace("l", "ʟ").replace("m", "ᴍ").replace("n", "ɴ").replace("o", "ᴏ").replace("p", "ᴘ").replace("q", "ǫ").replace("r", "ʀ").replace("s", "ꜱ").replace("t", "ᴛ").replace("u", "ᴜ").replace("v", "ᴠ").replace("w", "ᴡ").replace("x", "ˣ").replace("y", "ʏ").replace("z", "ᴢ");
     }
 }