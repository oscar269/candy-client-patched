 package as.pw.candee.mixin.mixins;
 import com.google.common.base.Predicate;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.event.events.render.RenderGetEntitiesInAABBExcludingEvent;
 import net.minecraft.client.multiplayer.WorldClient;
 import net.minecraft.client.renderer.EntityRenderer;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraftforge.common.MinecraftForge;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Redirect;
 @Mixin({EntityRenderer.class})
 public class MixinEntityRenderer
 {
     @Redirect(method = {"getMouseOver"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
     public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
         RenderGetEntitiesInAABBExcludingEvent event = new RenderGetEntitiesInAABBExcludingEvent(worldClient, entityIn, boundingBox, predicate);
         MinecraftForge.EVENT_BUS.post(event);
         if (event.isCancelled()) {
             return new ArrayList<>();
         }
         return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
     }
 }