 package as.pw.candee.module.movement;
 import java.util.List;
 import java.util.Random;
 import java.util.stream.Collectors;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.EnumHand;
 public class TPCart
     extends Module
 {
     public final Setting<Float> range;
     public final Setting<Float> delay;
     public Timer timer;
     public TPCart() {
         super("TPCart", Categories.MOVEMENT, false, false);
         this.range = register(new Setting("Range", 10.0F, 20.0F, 2.0F));
         this.delay = register(new Setting("Delay", 10.0F, 30.0F, 1.0F));
         this.timer = new Timer();
     }
     public void onTick() {
         try {
             if (nullCheck()) {
                 return;
             }
             if (this.timer == null) {
                 this.timer = new Timer();
             }
             if (this.timer.passedX(this.delay.getValue())) {
                 List<Entity> carts = mc.world.loadedEntityList.stream().filter(e -> e instanceof net.minecraft.entity.item.EntityMinecart).filter(e -> !e.equals(mc.player.getRidingEntity())).filter(e -> (PlayerUtil.getDistance(e) <= this.range.getValue())).collect(Collectors.toList());
                 Entity minecart = carts.get((new Random()).nextInt(carts.size()));
                 if (minecart == null) {
                     return;
                 }
                 mc.playerController.interactWithEntity(mc.player, minecart, EnumHand.MAIN_HAND);
             }
         } catch (Exception ignored) {}
     }
 }