 package as.pw.candee.module.render;
 import java.awt.Color;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 public class EnchantmentColor
     extends Module {
     public final Setting<Color> color;
     public static EnchantmentColor INSTANCE;
     public EnchantmentColor() {
         super("EnchantmentColor", Categories.RENDER, false, false);
         this.color = register(new Setting("Color", new Color(255, 255, 255, 50)));
         INSTANCE = this;
     }
 }