 package as.pw.candee.mixin.mixins;
 import as.pw.candee.event.events.world.BlockEvent;
 import net.minecraft.client.multiplayer.PlayerControllerMP;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.common.MinecraftForge;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

 @Mixin({PlayerControllerMP.class})
 public class MixinPlayerControllerMP {
     @Inject(method = {"clickBlock"}, at = {@At("HEAD")}, cancellable = true)
     private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
         BlockEvent event = new BlockEvent(3, pos, face);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCancelled()) {
             info.cancel();
         }
     }
     @Inject(method = {"onPlayerDamageBlock"}, at = {@At("HEAD")}, cancellable = true)
     private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
         BlockEvent event = new BlockEvent(4, pos, face);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCancelled())
             info.cancel();
     }
 }