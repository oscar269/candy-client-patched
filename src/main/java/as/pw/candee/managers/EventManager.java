 package as.pw.candee.managers;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.command.CommandManager;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.utils.RenderUtil3D;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.gui.ScaledResolution;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.server.SPacketEntityStatus;
 import net.minecraft.util.text.TextComponentString;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraftforge.client.event.ClientChatEvent;
 import net.minecraftforge.client.event.RenderGameOverlayEvent;
 import net.minecraftforge.client.event.RenderWorldLastEvent;
 import net.minecraftforge.event.entity.living.LivingEvent;
 import net.minecraftforge.fml.common.eventhandler.EventPriority;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.InputEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 import net.minecraftforge.fml.common.network.FMLNetworkEvent;
 import org.lwjgl.input.Keyboard;
 public class EventManager extends Manager {
     @SubscribeEvent
     public void onUpdate(LivingEvent.LivingUpdateEvent event) {
         if (nullCheck()) {
             CandeePlusRewrite.m_notif.onUpdate();
             CandeePlusRewrite.m_rotate.updateRotations();
             CandeePlusRewrite.m_hole.update();
             CandeePlusRewrite.m_module.onUpdate();
         }
     }
     @SubscribeEvent
     public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
         CandeePlusRewrite.m_module.onConnect();
     }
     @SubscribeEvent
     public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
         CandeePlusRewrite.Info("Saving configs...");
         ConfigManager.saveConfigs();
         CandeePlusRewrite.Info("Successfully save configs!");
     }
     @SubscribeEvent
     public void onTick(TickEvent.ClientTickEvent event) {
         CandeePlusRewrite.m_module.onTick();
     }
     @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
     public void onKeyInput(InputEvent.KeyInputEvent event) {
         if (Keyboard.getEventKeyState()) {
             CandeePlusRewrite.m_module.onKeyInput(Keyboard.getEventKey());
         }
     }
     @SubscribeEvent(priority = EventPriority.HIGHEST)
     public void onChatSent(ClientChatEvent event) {
         String msg = event.getMessage();
         String prefix = CommandManager.getCommandPrefix();
         if (msg.startsWith(prefix)) {
             event.setCanceled(true);
             Minecraft mc = Minecraft.getMinecraft();
             mc.ingameGUI.getChatGUI().addToSentMessages(msg);
             try {
                 CommandManager.callCommand(msg.substring(prefix.length()), false);
             } catch (Exception e) {
                 e.printStackTrace();
                 if (mc.player != null) {
                     mc.player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Error: " + e
                                 .getMessage()));
                 }
             }
         }
     }
     @SubscribeEvent(priority = EventPriority.LOW)
     public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
         if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
             ScaledResolution resolution = new ScaledResolution(mc);
             CandeePlusRewrite.m_module.onRender2D();
             CandeePlusRewrite.m_notif.onRender2D();
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         }
     }
     @SubscribeEvent
     public void onWorldRender(RenderWorldLastEvent event) {
         if (event.isCanceled()) {
             return;
         }
         if (mc.player == null || mc.world == null) {
             return;
         }
         mc.profiler.startSection("candy");
         mc.profiler.startSection("setup");
         RenderUtil3D.prepare();
         mc.profiler.endSection();
         CandeePlusRewrite.m_module.onRender3D();
         CandeePlusRewrite.m_module.onRender3D(event.getPartialTicks());
         mc.profiler.startSection("release");
         RenderUtil3D.release();
         mc.profiler.endSection();
         mc.profiler.endSection();
     }
     @SubscribeEvent
     public void onPacketSend(PacketEvent.Send event) {
         CandeePlusRewrite.m_module.onPacketSend(event);
     }
     @SubscribeEvent
     public void onPacketReceive(PacketEvent.Receive event) {
         CandeePlusRewrite.m_module.onPacketReceive(event);
         if (event.getPacket() instanceof SPacketEntityStatus) {
             SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
             if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                 EntityPlayer player = (EntityPlayer)packet.getEntity(mc.world);
                 CandeePlusRewrite.m_module.onTotemPop(player);
             }
         }
     }
     @SubscribeEvent
     public void onPlayerDeath(PlayerDeathEvent event) {
         CandeePlusRewrite.m_module.onPlayerDeath(event);
     }
 }