package as.pw.candee.mixin.mixins;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(NetworkRegistry.class)
public class MixinNetworkRegistry {
    @Redirect(method = "newChannel(Lnet/minecraftforge/fml/common/ModContainer;Ljava/lang/String;[Lio/netty/channel/ChannelHandler;)Ljava/util/EnumMap;", remap = false, at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    public <K, V> V nullChannelFix(Map<K, V> instance, K k, V v) {
        if (instance != null) {
            instance.put(k, v);
        } else {
            LogManager.getLogger("NullPointerFix").warn("newChannel java.lang.NullPointerException Fixed");
        }
        return v;
    }
}