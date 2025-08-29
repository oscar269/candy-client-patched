 package as.pw.candee.module;
 import com.google.gson.Gson;
 import com.mojang.realmsclient.gui.ChatFormatting;
 import java.awt.Color;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Objects;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.managers.Manager;
 import as.pw.candee.module.misc.DiscordRPC;
import as.pw.candee.module.render.Notification;
 import as.pw.candee.setting.Bind;
 import as.pw.candee.setting.EnumConverter;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ChatUtil;
 import as.pw.candee.utils.Util;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.text.ITextComponent;
 import net.minecraft.util.text.TextComponentBase;
 import net.minecraftforge.common.MinecraftForge;
 public class Module
     implements Util
 {
     public String name;
     public Categories category;
     public boolean hide;
     public boolean isEnable;
     public final Bind key;
     public List<Setting> settings;
     public Module(String name, Categories category, int defaultKey, boolean hide, boolean defaultStatus) {
         this.name = "";
         this.category = Categories.MISC;
         this.hide = false;
         this.isEnable = false;
         this.key = new Bind();
         this.settings = new ArrayList<>();
         this.name = name;
         this.category = category;
         setKey(defaultKey);
         this.hide = hide;
         this.isEnable = defaultStatus;
     }
     public Module(String name, Categories category, boolean hide, boolean defaultStatus) {
         this.name = "";
         this.category = Categories.MISC;
         this.hide = false;
         this.isEnable = false;
         this.key = new Bind();
         this.settings = new ArrayList<>();
         this.name = name;
         this.category = category;
         setKey(-1);
         this.hide = hide;
         this.isEnable = defaultStatus;
     }
     public void onUpdate() {}
     public void onTick() {}
     public void onConnect() {}
     public void onRender2D() {}
     public void onRender3D() {}
     public void onRender3D(float ticks) {}
     public void onPacketSend(PacketEvent.Send event) {}
     public void onEnable() {
         MinecraftForge.EVENT_BUS.register(this);
     }
     public void onDisable() {
         MinecraftForge.EVENT_BUS.unregister(this);
     }
     public void onPlayerDeath(PlayerDeathEvent event) {}
     public void onPlayerLogout(EntityPlayer player) {}
     public void onPacketReceive(PacketEvent.Receive event) {}
     public void onTotemPop(EntityPlayer entity) {}
     public boolean isEnable() {
         return this.isEnable;
     }
     public String getName() {
         return this.name;
     }
     public void toggle() {
         if (!this.isEnable) {
             enable();
         } else {
             disable();
         }
             }
     public void enable() {
         if (!this.isEnable) {
             SendMessage(this.name + " : " + ChatFormatting.GREEN + "Enabled");
             Notification notification = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
             if (notification != null && notification.chat.getValue()) {
                 ChatUtil.sendMessage(this.name + " : " + ChatFormatting.GREEN + "Enabled");
             }
             this.isEnable = true;
             onEnable();
         }
     }
     public void disable() {
         if (this.isEnable) {
             SendMessage(this.name + " : " + ChatFormatting.RED + "Disabled");
             Notification notification = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
             if (notification != null && notification.chat.getValue()) {
                 ChatUtil.sendMessage(this.name + " : " + ChatFormatting.RED + "Disabled");
             }
             this.isEnable = false;
             onDisable();
         }
     }
     public Setting register(Setting setting) {
         this.settings.add(setting);
         return setting;
     }
     public boolean nullCheck() {
         return (mc.player == null || mc.world == null);
     }
     public void setKey(int Nkey) {
         this.key.setKey(Nkey);
     }
     public void saveConfig() throws IOException {
         Gson gson = new Gson();
         Map<String, Object> mappedSettings = new HashMap<>();
         for (Setting s : this.settings) {
             if (s.value instanceof Enum) {
                 mappedSettings.put(s.name, EnumConverter.currentEnum((Enum)s.value) + "N"); continue;
             }
             if (s.value instanceof Color) {
                 Color color = (Color)s.value;
                 mappedSettings.put(s.name, color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha() + "C"); continue;
             }
             if (s.value instanceof Integer) {
                 mappedSettings.put(s.name, s.value + "I");
                 continue;
             }
             mappedSettings.put(s.name, s.value);
         }
         mappedSettings.put("ismoduleenabled", this.isEnable);
         mappedSettings.put("keybindcode", this.key.getKey());
         String json = gson.toJson(mappedSettings);
         File config = new File("candeeplusrewrite/" + this.category.name().toLowerCase() + "/" + this.name.toLowerCase() + ".json");
         FileWriter writer = new FileWriter(config);
         writer.write(json);
         writer.close();
     }
     public void loadConfig() throws Exception {
         Path path = Paths.get("candeeplusrewrite/" + this.category.name().toLowerCase() + "/" + this.name.toLowerCase() + ".json");
         if (!Files.exists(path)) {
             return;
         }
         String context = readAll(path);
         Gson gson = new Gson();
         Map<String, Object> mappedSettings = (Map<String, Object>)gson.fromJson(context, Map.class);
         mappedSettings.forEach(this::setConfig);
     }
     public void setConfig(String name, Object value) {
         if (Objects.equals(name, "ismoduleenabled")) {
             this.isEnable = (Boolean) value;
             if (this.isEnable) {
                 MinecraftForge.EVENT_BUS.register(this);
                 if (this instanceof DiscordRPC) {
                     onEnable();
                 }
             }
             return;
         }
         if (Objects.equals(name, "keybindcode")) {
             this.key.setKey(((Double)value).intValue());
             return;
         }
         List<Setting> settings = new ArrayList<>(this.settings);
        for (Setting setting : settings) {
                String n = setting.name;
                if (Objects.equals(n, name)) {
                        char c = value.toString().charAt(value.toString().length() - 1);
                        if (c == 'N') {
                                String enumValue = value.toString().replace("N", "");
                                setting.setEnum(Integer.parseInt(enumValue));
                        }
                        else if (c == 'C') {
                                String[] color = value.toString().replace("C", "").split(",");
                                setting.setValue(new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]), Integer.parseInt(color[3])));
                        }
                        else if (c == 'I') {
                                String intValue = value.toString().replace("I", "");
                                setting.setValue(Double.valueOf(intValue).intValue());
                        }
                        else if (value instanceof Double) {
                                setting.setValue(((Double) value).floatValue());
                        } else {
                                setting.setValue(value);
                        }
                }
        }
         this.settings = new ArrayList<>(settings);
     }
     public static String readAll(Path path) throws IOException {
         return Files.lines(path).reduce("", (prev, line) -> prev + line + System.lineSeparator());
     }
     public void sendMessage(String str) {
         Notification notif = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
         if (notif.isEnable && notif.message.getValue()) {
             CandeePlusRewrite.m_notif.showNotification(str);
         } else {
             ChatUtil.sendMessage(str);
         }
     }
     private void SendMessage(String str) {
         if (GUICheck()) {
             Notification notif = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
             if (notif.isEnable && notif.togglE.getValue()) {
                 CandeePlusRewrite.m_notif.showNotification(str);
             } else {
                 ChatUtil.sendMessage(str);
             }
         }
     }
     private boolean GUICheck() {
         return !this.name.equalsIgnoreCase("clickgui");
     }
     public enum Categories
     {
         COMBAT,
         EXPLOIT,
         MISC,
         MOVEMENT,
         RENDER,
         HUB
             }
     public static class ChatMessage
         extends TextComponentBase {
         private final String text;
         public ChatMessage(String text) {
             Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
             Matcher matcher = pattern.matcher(text);
             StringBuffer stringBuffer = new StringBuffer();
             while (matcher.find()) {
                 String replacement = matcher.group().substring(1);
                 matcher.appendReplacement(stringBuffer, replacement);
             }
             matcher.appendTail(stringBuffer);
             this.text = stringBuffer.toString();
         }
         public String getUnformattedComponentText() {
             return this.text;
         }
         public ITextComponent createCopy() {
             return null;
         }
         public ITextComponent shallowCopy() {
             return new Manager.ChatMessage(this.text);
         }
     }
 }