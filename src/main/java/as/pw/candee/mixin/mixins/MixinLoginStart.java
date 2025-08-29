package as.pw.candee.mixin.mixins;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.CPacketLoginStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin({CPacketLoginStart.class})
public class MixinLoginStart {
    @Inject(method = {"writePacketData"}, at = {@At("HEAD")})
    public void writePacketData(PacketBuffer buf, CallbackInfo ci) {}
}