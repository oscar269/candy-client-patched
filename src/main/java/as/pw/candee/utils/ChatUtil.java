 package as.pw.candee.utils;
 import net.minecraft.client.Minecraft;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextFormatting;
 public class ChatUtil
 {
     public static void sendMessage(String str) {
         (Minecraft.getMinecraft()).player.sendMessage(new TextComponentString(TextFormatting.GRAY + "[" + TextFormatting.LIGHT_PURPLE + "Candy+R" + TextFormatting.GRAY + "] " + str));
     }
     public static void sendServerMessage(String str) {
         (Minecraft.getMinecraft()).player.sendChatMessage(str);
     }
 }