 package as.pw.candee.module.misc;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import net.minecraft.block.material.Material;
 import net.minecraft.init.Blocks;
 import net.minecraft.item.Item;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraftforge.event.entity.player.PlayerInteractEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class InstantWither
     extends Module {
     public final Setting<Integer> delay;
     private int tickDelay;
     private int lastSlot;
     public InstantWither() {
         super("InstantWither", Categories.MISC, false, false);
         this.delay = register(new Setting("Delay", 2, 10, 0));
         this.tickDelay = 0;
         this.lastSlot = 0;
     }
     public void onDisable() {
         this.tickDelay = 0;
         this.lastSlot = 0;
     }
     @SubscribeEvent
     public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
         if (nullCheck() || this.tickDelay > 0)
             return;
         if (mc.player.getHeldItem(event.getHand()).getItem() != Item.getItemFromBlock(Blocks.SOUL_SAND)) {
             return;
         }
         BlockPos base = event.getPos().offset(event.getFace());
         if (mc.world.getBlockState(base).getMaterial() == Material.AIR)
             return;
         EnumFacing front = mc.player.getHorizontalFacing();
         EnumFacing left = front.rotateYCCW();
         int sandSlot = InventoryUtil.findHotbarBlock(Blocks.SOUL_SAND);
         int skullSlot = InventoryUtil.findHotbarItem(Item.getItemById(397));
         if (sandSlot != -1) {
             int[][] offset = { { 0, 0, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { -1, 1, 0 } };
             for (int[] pos : offset) {
                 placeBlocks(base.up(pos[1]).offset(front, pos[2]).offset(left, pos[0]), sandSlot);
             }
         }
         if (skullSlot != -1) {
             int[][] offset = { { 1, 2, 0 }, { 0, 2, 0 }, { -1, 2, 0 } };
             for (int[] pos : offset) {
                 placeBlocks(base.up(pos[1]).offset(front, pos[2]).offset(left, pos[0]), skullSlot);
             }
         }
     }
     public void onTick() {
         if (this.tickDelay > 0) this.tickDelay--;
     }
     private void placeBlocks(BlockPos pos, int slot) {
         if (mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
             if (mc.player.inventory.currentItem != slot) {
                 this.lastSlot = mc.player.inventory.currentItem;
                 InventoryUtil.switchToHotbarSlot(slot, true);
             }
             BlockUtil.placeBlock(pos, false);
             InventoryUtil.switchToHotbarSlot(this.lastSlot, true);
             this.tickDelay = this.delay.getValue();
         }
     }
 }