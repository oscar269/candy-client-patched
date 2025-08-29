 package as.pw.candee.module.misc;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import net.minecraft.network.play.server.SPacketChat;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraftforge.fml.common.eventhandler.EventPriority;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

 public class AntiUnicode
     extends Module
 {
     public final String[] list = new String[] { "☻", "⑥", "♻", "┥", "릢", "쎄", "縺", "ⰷ", "蒹", "ꈥ", "蒹", "㫃", "ㅾ", "ⰶ", "㫃", "㑾", "♳", "獀", "瑥", "∠", "릢", "쎄", "眽", "敫", "㑙", "潍", "剢", "䨰", "煖", "慳", "㝏", "牗", "䐹", "䥔", "穂", "䌶", "商", "摋", "杚", "楰", "㍇", "ㅑ", "堸", "畬", "橮", "㉨", "䐹", "䕹", "偶", "䱴", "砵", "䡀", "卭", "≦", "┊", "릢", "쎄", "縺", "㤵", "㔵", "ā", "ȁ", "́", "Ё", "ԁ", "؁", "܁", "ࠁ", "ँ", "ਁ", "ଁ", "ก", "༁", "ခ", "ᄁ", "ሁ", "ጁ", "ᐁ", "ᔁ", "ᘁ", "ᜁ", "᠁", "ᤁ", "ᨁ", "ᬁ", "ᰁ", "ᴁ", "ḁ", "瀁", "焁", "爁", "猁", "琁", "甁", "瘁", "省", "码", "礁", "稁", "笁", "簁", "紁", "縁", "缁", "老", "脁", "舁", "茁", "萁", "蔁", "蘁", "蜁", "蠁", "褁", "訁", "謁", "谁", "贁", "踁", "輁", "送", "鄁", "鈁", "錁", "鐁", "锁", "阁", "霁", "頁", "餁", "騁", "鬁", "鰁", "鴁", "鸁", "鼁", "ꀁ", "ꄁ", "ꈁ", "ꌁ", "ꐁ", "ꔁ", "ꘁ", "꜁", "ꠁ", "꤁", "ꨁ", "각", "괁", "긁", "꼁", "뀁", "넁", "눁", "댁", "됁", "딁", "똁", "뜁", "렁", "뤁", "먁", "묁", "밁", "봁", "攀", "猀", "琀", "爀", "甀" };
     public AntiUnicode() {
         super("AntiUnicode", Categories.MISC, false, false);
     }
     @SubscribeEvent(priority = EventPriority.HIGHEST)
     public void onPacketReceive(PacketEvent.Receive event) {
         if (event.getPacket() instanceof SPacketChat) {
             String message = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
             for (String unicode : this.list) {
                 if (message.contains(unicode)) {
                     ((SPacketChat) event.getPacket()).chatComponent = new TextComponentString("(lag message)");
                     event.setCanceled(true);
                     return;
                 }
             }
         }
     }
 }