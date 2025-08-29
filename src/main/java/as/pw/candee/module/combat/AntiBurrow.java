 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.exploit.InstantMine;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 public class AntiBurrow
     extends Module
 {
     public final Setting<Target> targetType;
     public final Setting<Float> targetRange;
     public final Setting<Float> range;
     public final Setting<Boolean> instantBreak;
     public final Setting<Boolean> noSwing;
     public final Setting<Boolean> switcH;
     public final Setting<Boolean> obby;
     public final Setting<Boolean> echest;
     public EntityPlayer target;
     public BlockPos breakPos;
     public AntiBurrow() {
         super("AntiBurrow", Categories.COMBAT, false, false);
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.range = register(new Setting("Range", 10.0F, 20.0F, 0.0F));
         this.instantBreak = register(new Setting("InstantBreak", Boolean.TRUE));
         this.noSwing = register(new Setting("NoSwing", Boolean.FALSE));
         this.switcH = register(new Setting("Switch", Boolean.FALSE));
         this.obby = register(new Setting("Obby", Boolean.FALSE));
         this.echest = register(new Setting("EChest", Boolean.FALSE));
         this.target = null;
         this.breakPos = null;
     }
     public void onEnable() {
         if (nullCheck()) {
             return;
         }
         this.target = findTarget();
         if (this.target == null) {
             sendMessage("Cannot find target! disabling");
             disable();
             return;
         }
         if (!canBreak(getPos(this.target))) {
             sendMessage("Target is not in block! disabling");
             return;
         }
         this.breakPos = getPos(this.target);
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
     public EntityPlayer findTarget() {
         EntityPlayer target = null;
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         if (this.targetType.getValue() == Target.Nearest) {
             target = PlayerUtil.getNearestPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Looking) {
             target = PlayerUtil.getLookingPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Best) {
             target = players.stream().filter(e -> canBreak(getPos(e))).min(Comparator.comparing(PlayerUtil::getDistance)).orElse(null);
         }
         return target;
     }
     public BlockPos getPos(EntityPlayer player) {
         return new BlockPos(player.posX, player.posY, player.posZ);
     }
     public boolean canBreak(BlockPos pos) {
         Block block = BlockUtil.getBlock(pos);
         boolean can = (block == Blocks.ANVIL);
         if (block == Blocks.ENDER_CHEST && this.echest.getValue()) {
             can = true;
         }
         if (block == Blocks.OBSIDIAN && this.obby.getValue()) {
             can = true;
         }
         return can;
     }
     public enum Target
     {
         Nearest,
         Looking,
         Best
             }
 }