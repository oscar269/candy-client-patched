 package as.pw.candee.module.render;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraftforge.client.event.EntityViewRenderEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class ItemViewModel
     extends Module {
     public final Setting<Float> fov;
     public ItemViewModel() {
         super("ItemViewModel", Categories.RENDER, false, false);
         this.fov = register(new Setting("Fov", 120.0F, 300.0F, 0.0F));
     }
     @SubscribeEvent
     public void onEntityViewRender(EntityViewRenderEvent.FOVModifier event) {
         event.setFOV(this.fov.getValue());
     }
 }