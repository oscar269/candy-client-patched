 package as.pw.candee.module.render;
 import as.pw.candee.event.events.render.FlagGetEvent;
 import as.pw.candee.event.events.render.SwingAnimationEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.util.EnumHand;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class Animation
     extends Module
 {
     public static Animation INSTANCE;
     public final Setting<Boolean> swingSpeed;
     public final Setting<Integer> speed;
     public final Setting<Boolean> offhand;
     public final Setting<Boolean> sneak;
     public Animation() {
         super("Animation", Categories.RENDER, false, false);
         this.swingSpeed = register(new Setting("SwingSpeed", Boolean.FALSE));
         this.speed = register(new Setting("Speed", 12, 6, 32, v -> this.swingSpeed.getValue()));
         this.offhand = register(new Setting("Offhand", Boolean.FALSE));
         this.sneak = register(new Setting("Sneak", Boolean.FALSE));
         INSTANCE = this;
     }
     @SubscribeEvent
     public void onArmSwingAnim(SwingAnimationEvent event) {
         if (this.swingSpeed.getValue() && event.getEntity() == mc.player) {
             event.setSpeed(this.speed.getValue());
         }
     }
     public void onTick() {
         if (this.offhand.getValue() && mc.player != null && mc.player.isSwingInProgress) {
             mc.player.swingingHand = EnumHand.OFF_HAND;
         }
         if (this.swingSpeed.getValue()) {
             this.speed.getValue().toString();
         }
     }
     @SubscribeEvent
     public void onFlagGet(FlagGetEvent event) {
         if (event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer && event
             .getEntity() != mc.player && event
             .getFlag() == 1 && this.sneak
.getValue())
             event.setReturnValue(true);
     }
 }