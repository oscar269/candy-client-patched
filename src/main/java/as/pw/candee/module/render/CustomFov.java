 package as.pw.candee.module.render;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.settings.GameSettings;
 public class CustomFov
     extends Module {
     public final Setting<Float> cfov;
     public CustomFov() {
         super("CustomFov", Categories.RENDER, false, false);
         this.cfov = register(new Setting("Fov", 0.0F, 180.0F, 1.0F));
     }
     public void onUpdate() {
         mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.cfov.getValue());
     }
 }