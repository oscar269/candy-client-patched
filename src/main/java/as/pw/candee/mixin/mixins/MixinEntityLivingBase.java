 package as.pw.candee.mixin.mixins;
 import as.pw.candee.event.events.render.SwingAnimationEvent;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraftforge.common.MinecraftForge;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
 @Mixin({EntityLivingBase.class})
 public abstract class MixinEntityLivingBase
 {
     @Inject(method = {"getArmSwingAnimationEnd()I"}, at = {@At("HEAD")}, cancellable = true)
     private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> cir) {
         SwingAnimationEvent armSwingAnimation = new SwingAnimationEvent(EntityLivingBase.class.cast(this), 6);
         MinecraftForge.EVENT_BUS.post(armSwingAnimation);
         cir.setReturnValue(armSwingAnimation.getSpeed());
     }
 }