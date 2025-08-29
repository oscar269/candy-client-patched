 package as.pw.candee.mixin.mixins;
 import as.pw.candee.event.events.player.UpdateWalkingPlayerEvent;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.AbstractClientPlayer;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.network.NetHandlerPlayClient;
 import net.minecraft.stats.RecipeBook;
 import net.minecraft.stats.StatisticsManager;
 import net.minecraft.world.World;
 import net.minecraftforge.common.MinecraftForge;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 @Mixin(value = {EntityPlayerSP.class}, priority = 9998)
 public class MixinEntityPlayerSP extends AbstractClientPlayer {
     public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
         super(p_i47378_2_, p_i47378_3_.getGameProfile());
     }
     @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("HEAD")})
     private void preMotion(CallbackInfo info) {
         UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
         MinecraftForge.EVENT_BUS.post(event);
     }
     @Inject(method = {"onUpdateWalkingPlayer"}, at = {@At("RETURN")})
     private void MotionPost(CallbackInfo info) {
         UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
         MinecraftForge.EVENT_BUS.post(event);
     }
 }