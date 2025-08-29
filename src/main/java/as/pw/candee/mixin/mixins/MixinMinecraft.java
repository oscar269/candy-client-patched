 package as.pw.candee.mixin.mixins;
 import as.pw.candee.CandeePlusRewrite;
 import net.minecraft.client.Minecraft;
 import net.minecraft.crash.CrashReport;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.Redirect;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 @Mixin({Minecraft.class})
 public class MixinMinecraft
 {
     @Inject(method = {"shutdownMinecraftApplet"}, at = {@At("HEAD")})
     private void stopClient(CallbackInfo callbackInfo) {
         CandeePlusRewrite.unload();
     }
     @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
     public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
         CandeePlusRewrite.unload();
     }
 }