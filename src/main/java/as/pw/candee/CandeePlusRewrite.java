 package as.pw.candee;
 import as.pw.candee.command.CommandManager;
 import as.pw.candee.mainmenu.MainMenu;
 import as.pw.candee.managers.ConfigManager;
 import as.pw.candee.managers.EventManager;
 import as.pw.candee.managers.FontManager;
 import as.pw.candee.managers.HoleManager;
 import as.pw.candee.managers.ModuleManager;
 import as.pw.candee.managers.NotificationManager;
 import as.pw.candee.managers.RotateManager;
 import as.pw.candee.managers.RpcManager;

 import net.minecraft.client.Minecraft;
 import net.minecraftforge.client.event.GuiOpenEvent;
 import net.minecraftforge.common.MinecraftForge;
 import net.minecraftforge.fml.common.Mod;
 import net.minecraftforge.fml.common.Mod.EventHandler;
 import net.minecraftforge.fml.common.event.FMLInitializationEvent;
 import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import org.apache.logging.log4j.Level;
 import org.apache.logging.log4j.Logger;
 import org.lwjgl.opengl.Display;
 @Mod(modid = "candeeplusrewrite", name = "Candee+ Rewrite", version = "0.3.4")
 public class CandeePlusRewrite
 {
     public static final String MODID = "candeeplusrewrite";
     public static final String NAME = "Candee+ Rewrite";
     public static final String NAME2 = "Candee+R";
     public static final String VERSION = "0.3.4";
     public static final String NV = "Candee+ Rewrite v0.3.4";
     @EventHandler
     public void preInit(FMLPreInitializationEvent event) {
         logger = event.getModLog();
     }
     @EventHandler
     public void init(FMLInitializationEvent event) {
         Info("loading Candee+ Rewrite ...");
         Display.setTitle("Candee+ Rewrite v0.3.4");
         m_event.load();
         m_module.load();
         m_font.load();
         m_hole.load();
         m_rpc.load();
         m_rotate.load();
         m_notif.load();
         CommandManager.init();
         Info("Loading Configs...");
         ConfigManager.loadConfigs();
         Info("Successfully Load CandeePlusRewrite!");
         if (event.getSide().isClient()) {
             Minecraft.getMinecraft().displayGuiScreen(new MainMenu());
             MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
         }
     }
     public static void unload() {
         if (!savedConfig) {
             Info("Saving Configs...");
             ConfigManager.saveConfigs();
             Info("Successfully Save Configs!");
             savedConfig = true;
         }
     }
     public static void Info(String msg) {
         if (logger == null) {
             return;
         }
         logger.info(msg);
     }
     public static void Log(Level level, String msg) {
         logger.log(level, msg);
     }
     public static final ModuleManager m_module = new ModuleManager();
     public static final EventManager m_event = new EventManager();
     public static final FontManager m_font = new FontManager();
     public static final HoleManager m_hole = new HoleManager();
     public static final RpcManager m_rpc = new RpcManager();
     public static final RotateManager m_rotate = new RotateManager(); private static Logger logger;
     public static final NotificationManager m_notif = new NotificationManager();
     private static boolean savedConfig = false;
     public static class GuiEventHandler {
         @SubscribeEvent
         public void onGuiOpen(GuiOpenEvent event) {
             if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu)
                 event.setGui(new MainMenu());
         }
     }
 }