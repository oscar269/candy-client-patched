 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.exploit.InstantMine;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 public class AutoCity
     extends Module
 {
     public final Setting<Target> targetType;
     public final Setting<Float> targetRange;
     public final Setting<Float> range;
     public final Setting<Boolean> instantBreak;
     public final Setting<Boolean> noSwing;
     public final Setting<Boolean> switcH;
     public final Setting<Boolean> noSuicide;
     public EntityPlayer target;
     public BlockPos breakPos;
     public AutoCity() {
         super("AutoCity", Categories.COMBAT, false, false);
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.range = register(new Setting("Range", 10.0F, 20.0F, 0.0F));
         this.instantBreak = register(new Setting("InstantBreak", Boolean.TRUE));
         this.noSwing = register(new Setting("NoSwing", Boolean.FALSE));
         this.switcH = register(new Setting("Switch", Boolean.FALSE));
         this.noSuicide = register(new Setting("NoSuicide", Boolean.TRUE));
         this.target = null;
         this.breakPos = null;
     }
     public void onEnable() {
         if (nullCheck()) {
             return;
         }
         this.target = findTarget();
         if (this.target == null) {
             sendMessage("Cannot find target! disabling...");
             disable();
             return;
         }
         if (findSpace(this.target) == -1) {
             sendMessage("Cannot find space! disabling...");
             disable();
             return;
         }
         sendMessage("Breaking...");
         if (!this.instantBreak.getValue()) {
             if (!this.noSwing.getValue()) {
                 mc.player.swingArm(EnumHand.MAIN_HAND);
             }
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
         } else {
             if (!this.noSwing.getValue()) {
                 mc.player.swingArm(EnumHand.MAIN_HAND);
             }
             CandeePlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
             InstantMine.startBreak(this.breakPos, EnumFacing.DOWN);
         }
         if (this.switcH.getValue()) {
             int pickel = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
             if (pickel == -1) {
                 return;
             }
             mc.player.inventory.currentItem = pickel;
             mc.playerController.updateController();
         }
         disable();
     }
     public int findSpace(EntityPlayer target) {
         BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
         BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         List<CitySpace> spaces = new ArrayList<>();
         BlockPos s = null;
         for (BlockPos offset : offsets) {
             CitySpace pos = new CitySpace();
             BlockPos breakPos = base.add(offset);
             if (BlockUtil.getBlock(breakPos) == Blocks.OBSIDIAN || BlockUtil.getBlock(breakPos) == Blocks.ENDER_CHEST) {
                 if (this.noSuicide.getValue()) {
                     boolean shouldSkip = false;
                                                     for (int length2 = offsets.length, j = 0; j < length2; j++) {
                         s = offsets[j];
                         BlockPos spos = mypos.add(s);
                         if (spos.equals(breakPos)) {
                             shouldSkip = true;
                         }
                     }
                     if (shouldSkip) {
                         continue;
                     }
                 }
                 pos.setPos(breakPos);
                 BlockPos levelPos = breakPos.add(offset);
                 if (BlockUtil.getBlock(levelPos) != Blocks.AIR) {
                     pos.setLevel(0);
                 }
                 else if (BlockUtil.getBlock(levelPos.add(0, 1, 0)) != Blocks.AIR) {
                     pos.setLevel(1);
                 } else {
                     pos.setLevel(2);
                 }
                 spaces.add(pos);
             }
                         }
         CitySpace space = spaces.stream().filter(s2 -> (PlayerUtil.getDistance(s2.pos) <= this.range.getValue())).max(Comparator.comparing(s2 -> s2.level + this.range.getValue() - PlayerUtil.getDistance(s2.pos))).orElse(null);
         if (space == null) {
             return -1;
         }
         this.breakPos = space.pos;
         return space.level;
     }
     public EntityPlayer findTarget() {
         EntityPlayer target = null;
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         players.removeIf(p -> (p == mc.player || FriendManager.isFriend(p.getName())));
         if (this.targetType.getValue() == Target.Nearest) {
             target = PlayerUtil.getNearestPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Looking) {
             target = PlayerUtil.getLookingPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Best) {
             target = players.stream().max(Comparator.comparing(this::findSpace)).orElse(null);
         }
         return target;
     }
     public enum Target
     {
         Nearest,
         Looking,
         Best
             }
     public static class CitySpace
     {
         public BlockPos pos;
         public int level = -1;
         public void setPos(BlockPos pos) {
             this.pos = pos;
         }
         public void setLevel(int level) {
             this.level = level;
         }
     }
 }