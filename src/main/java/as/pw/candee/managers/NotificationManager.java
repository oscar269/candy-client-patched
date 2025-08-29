 package as.pw.candee.managers;
 import as.pw.candee.CandeePlusRewrite;
 import com.mojang.realmsclient.gui.ChatFormatting;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.stream.Collectors;

 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.module.render.Notification;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.client.Minecraft;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.server.SPacketEntityStatus;
 import net.minecraft.util.math.MathHelper;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class NotificationManager
     extends Manager
 {
     public List<Notif> notifs = new ArrayList<>();
     public List<EntityPlayer> players = new ArrayList<>();
     private final Map<String, Integer> popCounter = new HashMap<>();
     private int scaledWidth;
     private int scaledHeight;
     public void showNotification(String msg) {
         if (mc.player == null) {
             return;
         }
         if (!(CandeePlusRewrite.m_module.getModuleWithClass(Notification.class)).isEnable) {
             return;
         }
         Notif notif = new Notif(msg);
         for (Notif notif2 : this.notifs) {
                             notif2.y -= (CandeePlusRewrite.m_font.getHeight() + 40);
         }
         updateResolution();
         notif.y = (this.scaledHeight - 50);
         notif._y = (this.scaledHeight - 50);
         this.notifs.add(notif);
     }
     public void onUpdate() {
         if (mc.world == null) {
             return;
         }
         for (EntityPlayer player : mc.world.playerEntities) {
             if (player != mc.player &&
                 !this.players.contains(player)) {
                 showNotification(player.getName() + " is coming towards you!");
             }
         }
         this.players = new ArrayList<>(mc.world.playerEntities);
     }
     @SubscribeEvent
     public void onPacketReceive(PacketEvent.Receive event) {
         if (event.getPacket() instanceof SPacketEntityStatus) {
             SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
             if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                 EntityPlayer player = (EntityPlayer)packet.getEntity(mc.world);
                 Notification notification = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
                 if (notification.pop.getValue()) {
                     int pop = countPop(player.getName());
                     if (pop == 1) {
                         showNotification(player.getName() + " popped a totem!");
                     } else {
                         showNotification(player.getName() + " popped " + pop + " totems!");
                     }
                 }
             }
         }
     }
     @SubscribeEvent
     public void onPlayerDeath(PlayerDeathEvent event) {
         Notification notification = (Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class);
         if (notification.death.getValue()) {
             EntityPlayer player = event.player;
             if (player == null) {
                 return;
             }
             int pop = getPop(player.getName());
             if (pop == 0) {
                 showNotification(ChatFormatting.RED + player.getName() + " dead!");
             } else {
                 showNotification(ChatFormatting.RED + player.getName() + " dead after " + pop + " pop!");
             }
             this.popCounter.remove(player.getName());
         }
     }
     public int countPop(String name) {
         if (!this.popCounter.containsKey(name)) {
             this.popCounter.put(name, 1);
             return 1;
         }
         this.popCounter.replace(name, this.popCounter.get(name) + 1);
         return this.popCounter.get(name);
     }
     public int getPop(String name) {
         if (!this.popCounter.containsKey(name)) {
             return 0;
         }
         return this.popCounter.get(name);
     }
     public void onRender2D() {
         try {
             if (mc.player == null) {
                 return;
             }
             for (Notif notification : this.notifs) {
                 updateResolution();
                 String msg = notification.msg;
                 int width = CandeePlusRewrite.m_font.getWidth(msg);
                 RenderUtil.drawRect((this.scaledWidth - width - 26) + notification.offsetX, notification._y - 21.0F, (width + 27), (CandeePlusRewrite.m_font.getHeight() + 12), ColorUtil.toRGBA(new Color(35, 35, 35, 255)));
                 RenderUtil.drawRect((this.scaledWidth - width - 25) + notification.offsetX, notification._y - 20.0F, (width + 25), (CandeePlusRewrite.m_font.getHeight() + 10), ColorUtil.toRGBA(new Color(45, 45, 45, 255)));
                 RenderUtil.drawRect((this.scaledWidth - width - 26) + notification.offsetX, notification._y - 20.0F + CandeePlusRewrite.m_font.getHeight() + 10.0F, (width + 26) * (notification.max - notification.ticks) / notification.max, 1.0F, ColorUtil.toRGBA(new Color(170, 170, 170, 255)));
                 RenderUtil.drawString(msg, (this.scaledWidth - width - 20) + notification.offsetX, notification._y - 10.0F - 3.0F, ColorUtil.toRGBA(255, 255, 255), false, 1.0F);
                 if (notification.ticks <= 0.0F) {
                                                     notification.offsetX += (500.0F - notification.offsetX) / 10.0F;
                     continue;
                 }
                                         notification.ticks--;
                                         notification.offsetX += (0.0F - notification.offsetX) / 4.0F;
                                         notification._y += (notification.y - notification._y) / 4.0F;
             }
             this.notifs = this.notifs.stream().filter(n -> ((n.offsetX < 450.0F || n.ticks != 0.0F) && n._y >= -100.0F)).collect(Collectors.toList());
         }
         catch (Exception ignored) {}
     }
     public void updateResolution() {
         this.scaledWidth = mc.displayWidth;
         this.scaledHeight = mc.displayHeight;
         int scaleFactor = 1;
         boolean flag = mc.isUnicode();
         int i = mc.gameSettings.guiScale;
         if (i == 0) {
             i = 1000;
         }
         while (scaleFactor < i && this.scaledWidth / (scaleFactor + 1) >= 320 && this.scaledHeight / (scaleFactor + 1) >= 240) {
             scaleFactor++;
         }
         if (flag && scaleFactor % 2 != 0 && scaleFactor != 1) {
             scaleFactor--;
         }
         double scaledWidthD = (double) this.scaledWidth / scaleFactor;
         double scaledHeightD = (double) this.scaledHeight / scaleFactor;
         this.scaledWidth = MathHelper.ceil(scaledWidthD);
         this.scaledHeight = MathHelper.ceil(scaledHeightD);
     }
     public static class Notif
     {
         public final String msg;
         public float offsetX;
         public float y;
         public float _y;
         public float ticks;
         public float max;
         public Notif(String msg) {
             this.offsetX = 300.0F;
             this.y = 0.0F;
             this._y = 0.0F;
             this.ticks = 0.0F;
             this.max = 0.0F;
             this.msg = msg;
             int fps = Minecraft.getDebugFPS();
             if (fps == 0) {
                 fps = 60;
             }
             int seconds = ((Notification) CandeePlusRewrite.m_module.getModuleWithClass(Notification.class)).time.getValue();
             this.ticks = (seconds * fps);
             this.max = (seconds * fps);
         }
     }
 }