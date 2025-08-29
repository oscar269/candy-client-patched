 package as.pw.candee.module.combat;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;

 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.exploit.InstantMine;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.InventoryUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import as.pw.candee.utils.Timer;
 import net.minecraft.block.Block;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.EnumHand;
 import net.minecraft.util.math.BlockPos;
 public class AutoCityRewrite
     extends Module
 {
     public final Setting<Target> targetType;
     public final Setting<Float> targetRange;
     public final Setting<Float> range;
     public final Setting<Boolean> instantBreak;
     public final Setting<Boolean> noSwing;
     public final Setting<Boolean> switcH;
     public final Setting<Boolean> noSuicide;
     public final Setting<Boolean> burrow;
     public final Timer timer = new Timer(); public final Setting<Boolean> multi; public final Setting<Integer> delay; public final Setting<Boolean> tick; public final Setting<Color> renderColor; public final Setting<Boolean> outline; public final Setting<Float> width; public EntityPlayer target; public BlockPos breakPos;
     public AutoCityRewrite() {
         super("AutoCityRewrite", Categories.COMBAT, false, false);
         this.targetType = register(new Setting("Target", Target.Nearest));
         this.targetRange = register(new Setting("Target Range", 10.0F, 20.0F, 0.0F));
         this.range = register(new Setting("Range", 6.0F, 12.0F, 1.0F));
         this.instantBreak = register(new Setting("InstantBreak", Boolean.TRUE));
         this.noSwing = register(new Setting("NoSwing", Boolean.FALSE));
         this.switcH = register(new Setting("Switch", Boolean.FALSE));
         this.noSuicide = register(new Setting("NoSuicide", Boolean.TRUE));
         this.burrow = register(new Setting("MineBurrow", Boolean.TRUE));
         this.multi = register(new Setting("Multi", Boolean.FALSE));
         this.delay = register(new Setting("Delay", 200, 0, 1000));
         this.tick = register(new Setting("Tick", Boolean.TRUE));
         this.renderColor = register(new Setting("Color", new Color(230, 50, 50, 100)));
         this.outline = register(new Setting("Outline", Boolean.FALSE));
         this.width = register(new Setting("Width", 3.0F, 6.0F, 0.1F, v -> this.outline.getValue()));
     }
     public void onEnable() {
         reset();
     }
     public void onDisable() {
         reset();
     }
     public void onTick() {
         if (nullCheck()) {
             return;
         }
         if (this.tick.getValue()) run();
     }
     public void onUpdate() {
         if (nullCheck()) {
             return;
         }
         if (!this.tick.getValue()) run();
     }
     private void run() {
         if (nullCheck() || !this.timer.passedMs(this.delay.getValue()))
             return;    this.target = findTarget();
         if (this.target == null) {
             disable();
             return;
         }
         BlockPos base = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
         if (this.burrow.getValue() && isBurrowed(this.target)) {
             mineBlock(base);
             this.breakPos = base;
             this.timer.reset();
             return;
         }
         List<BlockPos> cityList = findCityBlocks(this.target);
         if (cityList.isEmpty()) {
             sendMessage("Cannot find cityable block! disabling...");
             disable();
             return;
         }
         if (this.multi.getValue()) {
             for (BlockPos pos : cityList) {
                 mineBlock(pos);
                 this.breakPos = pos;
             }
         } else {
             mineBlock(cityList.get(0));
             this.breakPos = cityList.get(0);
         }
         this.timer.reset();
         if (!this.tick.getValue()) disable();
     }
     private boolean isBurrowed(EntityPlayer player) {
         BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
         Block block = mc.world.getBlockState(pos).getBlock();
         return (block != Blocks.AIR && block != Blocks.BEDROCK && block != Blocks.WATER && block != Blocks.LAVA);
     }
     private List<BlockPos> findCityBlocks(EntityPlayer target) {
         List<BlockPos> result = new ArrayList<>();
         BlockPos base = new BlockPos(target.posX, target.posY, target.posZ);
         BlockPos[] offsets = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
         for (BlockPos offset : offsets) {
             BlockPos breakPos = base.add(offset);
             Block block = mc.world.getBlockState(breakPos).getBlock();
             if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST) {
                 if (this.noSuicide.getValue()) {
                     BlockPos mypos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                     if (mypos.equals(breakPos))
                         continue;
                 }    BlockPos up1 = breakPos.up();
                 BlockPos up2 = up1.up();
                 if (mc.world.getBlockState(up1).getBlock() == Blocks.AIR && mc.world.getBlockState(up2).getBlock() == Blocks.AIR)
                     result.add(breakPos);
             }
                         }
         return result;
     }
     private void mineBlock(BlockPos pos) {
         if (this.switcH.getValue()) {
             int pickel = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
             if (pickel != -1) {
                 mc.player.inventory.currentItem = pickel;
                 mc.playerController.updateController();
             }
         }
         if (!this.instantBreak.getValue()) {
             if (!this.noSwing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
             mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
         } else {
             if (!this.noSwing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
             CandeePlusRewrite.m_module.getModuleWithClass(InstantMine.class).enable();
             InstantMine.startBreak(pos, EnumFacing.DOWN);
         }
     }
     private EntityPlayer findTarget() {
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         players.removeIf(p -> (p == mc.player || FriendManager.isFriend(p.getName()) || p.isDead || p.getHealth() <= 0.0F));
         if (players.isEmpty()) return null;
         if (this.targetType.getValue() == Target.Nearest) {
             return players.stream().min(Comparator.comparing(p -> mc.player.getDistance(p))).orElse(null);
         }
         if (this.targetType.getValue() == Target.Looking) {
             return PlayerUtil.getLookingPlayer(this.targetRange.getValue());
         }
         if (this.targetType.getValue() == Target.Best) {
             return players.stream().max(Comparator.comparing(this::scoreCity)).orElse(null);
         }
         return null;
     }
     private int scoreCity(EntityPlayer p) {
         List<BlockPos> list = findCityBlocks(p);
         return list.size();
     }
     private void reset() {
         this.target = null;
         this.breakPos = null;
         this.timer.reset();
     }
     public void onRender3D() {
         if (this.breakPos != null) {
             RenderUtil3D.drawBox(this.breakPos, 1.0D, this.renderColor.getValue(), 63);
             if (this.outline.getValue())
                 RenderUtil3D.drawBoundingBox(this.breakPos, 1.0D, this.width.getValue(), this.renderColor.getValue());
         }
     }
     public enum Target
     {
         Nearest, Looking, Best
             }
 }