 package as.pw.candee.module.movement;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.MovementInput;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 import net.minecraftforge.client.event.InputUpdateEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 public class BlockMove
     extends Module
 {
     public final Setting<Boolean> middle;
     public final Setting<Integer> delay;
     public final Setting<Boolean> only;
     public final Setting<Boolean> avoid;
     public final Setting<Float> verticalSpeed;
     public final Setting<Boolean> voidSafe;
     public final Setting<Float> voidY;
     private final Timer timer;
     private final Vec3d[] sides;
     public BlockMove() {
         super("BlockMove", Categories.MOVEMENT, false, false);
         this.middle = register(new Setting("Middle", Boolean.TRUE));
         this.delay = register(new Setting("Delay", 250, 2000, 0));
         this.only = register(new Setting("Only In Block", Boolean.TRUE));
         this.avoid = register(new Setting("Avoid Out", Boolean.TRUE, v -> !this.only.getValue()));
         this.verticalSpeed = register(new Setting("VerticalSpeed", 0.3F, 1.0F, 0.05F));
         this.voidSafe = register(new Setting("VoidSafe", Boolean.TRUE));
         this.voidY = register(new Setting("VoidY", 60.0F, 256.0F, 0.0F, v -> this.voidSafe.getValue()));
         this.timer = new Timer();
         this.sides = new Vec3d[] { new Vec3d(0.24D, 0.0D, 0.24D), new Vec3d(-0.24D, 0.0D, 0.24D), new Vec3d(0.24D, 0.0D, -0.24D), new Vec3d(-0.24D, 0.0D, -0.24D) };
     }
     public void onEnable() {
         super.onEnable();
         this.timer.reset();
     }
     @SubscribeEvent
     public void onInputUpdate(InputUpdateEvent event) {
         if (mc.player == null || mc.world == null)
             return;
         double vy = this.voidY.getValue();
         if (this.voidSafe.getValue() && mc.player.posY <= vy) {
             mc.player.setPosition(mc.player.posX, vy + 1.0D, mc.player.posZ);
             cancelInput(event);
             this.timer.reset();
             return;
         }
         Vec3d posVec = mc.player.getPositionVector();
         boolean air = true;
         AxisAlignedBB playerBox = mc.player.getEntityBoundingBox();
         for (Vec3d side : this.sides) {
             if (!air)
                 break;    for (int i = 0; i < 2; i++) {
                 BlockPos pos = new BlockPos(posVec.add(side).add(0.0D, i, 0.0D));
                 if (!BlockUtil.isAir(pos)) {
                     AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
                     if (box != null && playerBox.intersects(box)) {
                         air = false;
                         break;
                     }
                 }
             }
         }
         if (this.only.getValue() && air)
             return;
         MovementInput input = event.getMovementInput();
         if (this.timer.passedMs(this.delay.getValue())) {
             if (mc.gameSettings.keyBindJump.isKeyDown()) {
                 double nextY = mc.player.posY + this.verticalSpeed.getValue();
                 mc.player.setPosition(mc.player.posX, nextY, mc.player.posZ);
                 this.timer.reset();
                 cancelInput(event); return;
             }
             if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                 double nextY = mc.player.posY - this.verticalSpeed.getValue();
                 if (this.voidSafe.getValue() && nextY <= vy) nextY = vy + 1.0D;
                 mc.player.setPosition(mc.player.posX, nextY, mc.player.posZ);
                 this.timer.reset();
                 cancelInput(event);
                 return;
             }
             BlockPos playerPos = this.middle.getValue() ? PlayerUtil.getPlayerPos() : new BlockPos(Math.round(posVec.x), posVec.y, Math.round(posVec.z));
             EnumFacing facing = mc.player.getHorizontalFacing();
             int dx = playerPos.offset(facing).getX() - playerPos.getX();
             int dz = playerPos.offset(facing).getZ() - playerPos.getZ();
             boolean addX = (dx != 0);
             Vec3d target = posVec;
             if (input.forwardKeyDown) {
                 target = moveTo(playerPos, addX, addX ? ((dx < 0)) : ((dz < 0)));
             } else if (input.backKeyDown) {
                 target = moveTo(playerPos, addX, addX ? ((dx > 0)) : ((dz > 0)));
             } else if (input.leftKeyDown) {
                 target = moveTo(playerPos, !addX, addX ? ((dx > 0)) : ((dz < 0)));
             } else if (input.rightKeyDown) {
                 target = moveTo(playerPos, !addX, addX ? ((dx < 0)) : ((dz > 0)));
             }
             if (target != null) {
                 mc.player.setPosition(target.x, target.y, target.z);
                 this.timer.reset();
                 cancelInput(event);
             }
         }
     }
     private Vec3d moveTo(BlockPos base, boolean xDir, boolean negative) {
         return negative ?
             toVec3d(base.add(xDir ? -1 : 0, 0, xDir ? 0 : -1)) :
             toVec3d(base.add(xDir ? 1 : 0, 0, xDir ? 0 : 1));
     }
     private Vec3d toVec3d(BlockPos pos) {
         if (this.middle.getValue()) {
             return new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
         }
         Vec3d last = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
         boolean any = (!mc.world.isAirBlock(pos) || !mc.world.isAirBlock(pos.up()));
         Vec3d v = new Vec3d(pos.getX() - 1.0E-8D, pos.getY(), pos.getZ());
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
             last = v;
         } else {
             any = true;
         }
         v = new Vec3d(pos.getX(), pos.getY(), pos.getZ() - 1.0E-8D);
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
             last = v;
         } else {
             any = true;
         }
         v = new Vec3d(pos.getX() - 1.0E-8D, pos.getY(), pos.getZ() - 1.0E-8D);
         if (mc.world.isAirBlock(new BlockPos(v)) && mc.world.isAirBlock((new BlockPos(v)).up())) {
             last = v;
         } else {
             any = true;
         }
         if (!this.only.getValue() && !any && this.avoid.getValue()) {
             return null;
         }
         return last;
     }
     private void cancelInput(InputUpdateEvent event) {
         (event.getMovementInput()).forwardKeyDown = false;
         (event.getMovementInput()).backKeyDown = false;
         (event.getMovementInput()).leftKeyDown = false;
         (event.getMovementInput()).rightKeyDown = false;
         (event.getMovementInput()).moveForward = 0.0F;
         (event.getMovementInput()).moveStrafe = 0.0F;
     }
 }