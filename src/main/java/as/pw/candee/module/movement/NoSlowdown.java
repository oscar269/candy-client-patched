 package as.pw.candee.module.movement;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.item.Item;
 import net.minecraft.network.play.client.CPacketEntityAction;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.event.entity.player.PlayerInteractEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class NoSlowdown
     extends Module
 {
     public final Setting<Float> webHorizontalFactor;
     public final Setting<Float> webVerticalFactor;
     public final Setting<Boolean> noSlow;
     public final Setting<Boolean> strict;
     public final Setting<Boolean> sneakPacket;
     public final Setting<Boolean> webs;
     private boolean sneaking;
     public NoSlowdown() {
         super("NoSlowdown", Categories.MOVEMENT, false, false);
         this.webHorizontalFactor = register(new Setting("WebHSpeed", 2.0F, 50.0F, 0.0F));
         this.webVerticalFactor = register(new Setting("WebVSpeed", 2.0F, 50.0F, 0.0F));
         this.noSlow = register(new Setting("NoSlow", Boolean.TRUE));
         this.strict = register(new Setting("Strict", Boolean.FALSE));
         this.sneakPacket = register(new Setting("SneakPacket", Boolean.FALSE));
         this.webs = register(new Setting("Webs", Boolean.FALSE));
         this.sneaking = false;
     }
     public void onUpdate() {
         Item item = mc.player.getActiveItemStack().getItem();
         if (this.sneaking && !mc.player.isHandActive() && this.sneakPacket.getValue()) {
             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
             this.sneaking = false;
         }
     }
     @SubscribeEvent
     public void onUseItem(PlayerInteractEvent.RightClickItem event) {
         Item item = mc.player.getHeldItem(event.getHand()).getItem();
         if ((item instanceof net.minecraft.item.ItemFood || item instanceof net.minecraft.item.ItemBow || (item instanceof net.minecraft.item.ItemPotion && this.sneakPacket.getValue())) && !this.sneaking) {
             mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
             this.sneaking = true;
         }
     }
     @SubscribeEvent
     public void onPacket(PacketEvent.Send event) {
         if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer && this.strict.getValue() && this.noSlow.getValue() && mc.player.isHandActive() && !mc.player.isRiding())
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), EnumFacing.DOWN));
     }
 }