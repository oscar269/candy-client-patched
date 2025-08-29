 package as.pw.candee.module.misc;
 import java.util.Map;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityItemFrame;
 import net.minecraft.item.ItemMap;
 import net.minecraft.item.ItemStack;
 import net.minecraft.world.storage.MapData;
 import net.minecraft.world.storage.MapDecoration;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class AntiMapBan
     extends Module
 {
     public AntiMapBan() {
         super("AntiMapBan", Categories.MISC, false, false);
     }
     public void onTick() {
         if (nullCheck())
             return;
         ItemStack currentItem = mc.player.inventory.getCurrentItem();
         if (!currentItem.isEmpty() && currentItem.getItem() instanceof ItemMap) {
             MapData mapData = ((ItemMap)currentItem.getItem()).getMapData(currentItem, mc.world);
             if (mapData != null) {
                 getMapDecorations(mapData).clear();
             }
         }
         for (Entity entity : mc.world.loadedEntityList) {
             if (entity instanceof EntityItemFrame) {
                 EntityItemFrame frame = (EntityItemFrame)entity;
                 ItemStack frameItem = frame.getDisplayedItem();
                 if (!frameItem.isEmpty() && frameItem.getItem() instanceof ItemMap) {
                     MapData mapData = ((ItemMap)frameItem.getItem()).getMapData(frameItem, frame.world);
                     if (mapData != null) {
                         getMapDecorations(mapData).clear();
                     }
                 }
             }
         }
     }
     @SubscribeEvent
     public void onPacketReceive(PacketEvent.Receive event) {
         if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketMaps) {
             event.cancel();
         }
     }
     public Map<String, MapDecoration> getMapDecorations(MapData mapData) {
         return mapData.mapDecorations;
     }
 }