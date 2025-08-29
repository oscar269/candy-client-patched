 package as.pw.candee.module.misc;
 import java.util.Random;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.item.ItemStack;
 import net.minecraft.util.math.BlockPos;
 public class BuildRandom
     extends Module
 {
     public final Setting<Integer> range;
     public final Setting<Integer> delay;
     private final Random random = new Random();
     private final Timer timer = new Timer();
     public BuildRandom() {
         super("BuildRandom", Categories.MISC, false, false);
         this.range = register(new Setting("Range", 4, 10, 1));
         this.delay = register(new Setting("Delay", 80, 500, 10));
     }
     public void onTick() {
         if (nullCheck())
             return;    if (!checkHeldItem())
             return;    if (!this.timer.passedMs(this.delay.getValue()))
             return;
         int bound = this.range.getValue() * 2 + 1;
         int attempts = 0;
         try {
             BlockPos pos;
             do {
                 pos = (new BlockPos(mc.player.getPosition())).add(this.random.nextInt(bound) - this.range.getValue(), this.random.nextInt(bound) - this.range.getValue(), this.random.nextInt(bound) - this.range.getValue());
             } while (++attempts < 128 && !tryToPlaceBlock(pos));
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
     private boolean tryToPlaceBlock(BlockPos pos) {
         if (pos == null || !mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
             return false;
         }
         double maxReach = this.range.getValue() + 1.5D;
         if (mc.player.getDistanceSq(pos) > maxReach * maxReach) {
             return false;
         }
         boolean result = BlockUtil.placeBlock(pos, false);
         if (result) this.timer.reset();
         return result;
     }
     private boolean checkHeldItem() {
         ItemStack stack = mc.player.inventory.getCurrentItem();
         return (!stack.isEmpty() && (stack.getItem() instanceof net.minecraft.item.ItemBlock || stack.getItem() instanceof net.minecraft.item.ItemSkull));
     }
 }