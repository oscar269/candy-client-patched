 package as.pw.candee.module.combat;
 import as.pw.candee.module.Module;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
 import net.minecraft.util.math.BlockPos;
 public class BowSpam
     extends Module {
     public BowSpam() {
         super("BowSpam", Categories.COMBAT, false, false);
     }
     public void onTick() {
         try {
             if (nullCheck()) {
                 return;
             }
             if (mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemBow) {
                 mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                 mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                 mc.player.stopActiveHand();
             }
         } catch (Exception ignored) {}
     }
 }