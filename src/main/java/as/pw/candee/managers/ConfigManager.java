 package as.pw.candee.managers;
 import java.io.File;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 public class ConfigManager
 {
     public static void saveConfigs() {
         String folder = "candeeplusrewrite/";
         File dir = new File("candeeplusrewrite/");
         if (!dir.exists()) {
             dir.mkdirs();
         }
         for (Module.Categories category : Module.Categories.values()) {
             File categoryDir = new File("candeeplusrewrite/" + category.name().toLowerCase());
             if (!categoryDir.exists()) {
                 categoryDir.mkdirs();
             }
         }
         List<Module> modules = CandeePlusRewrite.m_module.modules;
         for (Module module : modules) {
             try {
                 module.saveConfig();
             }
             catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
     public static void loadConfigs() {
         List<Module> modules = CandeePlusRewrite.m_module.modules;
         for (Module module : modules) {
             try {
                 module.loadConfig();
             }
             catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
 }