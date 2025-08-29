 package as.pw.candee.module.render;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 public class SmallShield
     extends Module {
     public final Setting<Boolean> normalOffset;
     public final Setting<Float> offset;
     public final Setting<Float> offX;
     public final Setting<Float> offY;
     public final Setting<Float> mainX;
     public final Setting<Float> mainY;
     public SmallShield() {
         super("SmallShield", Categories.RENDER, false, false);
         this.normalOffset = register(new Setting("OffNormal", Boolean.FALSE));
         this.offset = register(new Setting("Offset", 0.7F, 1.0F, 0.0F, v -> this.normalOffset.getValue()));
         this.offX = register(new Setting("OffX", 0.0F, 1.0F, -1.0F, v -> !this.normalOffset.getValue()));
         this.offY = register(new Setting("OffY", 0.0F, 1.0F, -1.0F, v -> !this.normalOffset.getValue()));
         this.mainX = register(new Setting("MainX", 0.0F, 1.0F, -1.0F));
         this.mainY = register(new Setting("MainY", 0.0F, 1.0F, -1.0F));
     }
     public static SmallShield getINSTANCE() {
         return (SmallShield) CandeePlusRewrite.m_module.getModuleWithClass(SmallShield.class);
     }
     public void onUpdate() {
         if (nullCheck()) {
             return;
         }
         if (this.normalOffset.getValue());
     }
 }