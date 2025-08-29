 package as.pw.candee.managers;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.combat.CevBreaker;
 import as.pw.candee.module.combat.CivBreaker;
 import as.pw.candee.module.combat.PistonAura;
 import club.minnced.discord.rpc.DiscordEventHandlers;
 import club.minnced.discord.rpc.DiscordRPC;
 import club.minnced.discord.rpc.DiscordRichPresence;

 public class RpcManager
     extends Manager
 {
     private Thread _thread = null;
     public void enable(as.pw.candee.module.misc.DiscordRPC module) {
         DiscordRPC lib = DiscordRPC.INSTANCE;
         String applicationId = "1401898002305650799";
         String steamId = "";
         DiscordEventHandlers handlers = new DiscordEventHandlers();
         lib.Discord_Initialize("1401898002305650799", handlers, true, "");
         DiscordRichPresence presence = new DiscordRichPresence();
         presence.startTimestamp = System.currentTimeMillis() / 1000L;
         lib.Discord_UpdatePresence(presence);
         presence.largeImageText = "";
         (this._thread = new Thread(() -> {
                     while (!Thread.currentThread().isInterrupted()) {
                         lib.Discord_RunCallbacks();
                         if (module.girl.getValue()) {
                             presence.largeImageKey = "icon";
                         } else {
                             presence.largeImageKey = "icon";
                         }
                         presence.details = "Playing Candy+ Rewrite";
                         presence.state = getState();
                         lib.Discord_UpdatePresence(presence);
                         try {
                             Thread.sleep(3000L);
                         } catch (InterruptedException ignored) {}
                     }
                 }, "RPC-Callback-Handler")).start();
     }
     public void disable() {
         DiscordRPC.INSTANCE.Discord_Shutdown();
         this._thread = null;
     }
     public String getState() {
         if (mc.player == null) {
             return "Main Menu";
         }
         String state = "HP : " + Math.round(mc.player.getHealth() + mc.player.getAbsorptionAmount()) + " / " + Math.round(mc.player.getMaxHealth() + mc.player.getAbsorptionAmount());
         Module piston = CandeePlusRewrite.m_module.getModuleWithClass(PistonAura.class);
         Module cev = CandeePlusRewrite.m_module.getModuleWithClass(CevBreaker.class);
         Module civ = CandeePlusRewrite.m_module.getModuleWithClass(CivBreaker.class);
         if (piston == null || cev == null || civ == null) {
             return state;
         }
         if (piston.isEnable) {
             state = "Pushing crystal";
         }
         if (cev.isEnable) {
             state = "Breaking ceil";
         }
         if (civ.isEnable) {
             state = "Attacking side";
         }
         return state;
     }
 }