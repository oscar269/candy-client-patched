 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.managers.HoleManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RotationUtil;
 import as.pw.candee.utils.Timer;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Vec3d;
 import net.minecraftforge.client.event.InputUpdateEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 public class AutoChase
     extends Module
 {
     public final Setting<Integer> targetRange;
     public final Setting<Integer> fixedRange;
     public final Setting<Integer> cancelRange;
     public final Setting<Integer> downRange;
     public final Setting<Integer> upRange;
     public final Setting<Float> hRange;
     public final Setting<Float> timer;
     public final Setting<Float> speed;
     public final Setting<Boolean> step;
     public final Setting<ModeType> mode;
     public final Setting<Float> height;
     public final Setting<Float> vHeight;
     public final Setting<Boolean> abnormal;
     public final Setting<Integer> centerSpeed;
     public final Setting<Boolean> only;
     public final Setting<Boolean> single;
     public final Setting<Boolean> twoBlocks;
     public final Setting<Boolean> custom;
     public final Setting<Boolean> four;
     public final Setting<Boolean> near;
     public final Setting<Boolean> disable;
     private int stuckTicks;
     private BlockPos originPos;
     private boolean isActive;
     private boolean wasInHole;
     private double playerSpeed;
     private EntityPlayer target;
     private final Timer gameTimer;
     private final HoleManager holeManager;
     private final double[] pointFiveToOne = new double[] { 0.41999998688698D };
     private final double[] one = new double[] { 0.41999998688698D, 0.7531999805212D };
     private final double[] oneFive = new double[] { 0.42D, 0.753D, 1.001D, 1.084D, 1.006D };
     private final double[] oneSixTwoFive = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D };
     private final double[] oneEightSevenFive = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D };
     private final double[] two = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D };
     private final double[] twoFive = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
     private final double[] threeStep = new double[] { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D };
     private final double[] fourStep = new double[] { 0.42D, 0.75D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D, 1.78D, 1.63D, 1.51D, 1.9D, 2.21D, 2.45D, 2.43D, 2.78D, 2.63D, 2.51D, 2.9D, 3.21D, 3.45D, 3.43D };
     private final double[] betaShared = new double[] { 0.419999986887D, 0.7531999805212D, 1.0013359791121D, 1.1661092609382D, 1.249187078744682D, 1.176759275064238D };
     private final double[] betaTwo = new double[] { 1.596759261951216D, 1.929959255585439D };
     private final double[] betaTwoFive = new double[] { 1.596759261951216D, 1.929959255585439D, 2.178095254176385D, 2.3428685360024515D, 2.425946353808919D };
     public AutoChase() {
         super("AutoChase", Categories.COMBAT, false, false);
         this.targetRange = register(new Setting("Target Range", 16, 256, 0));
         this.fixedRange = register(new Setting("Fixed Target Range", 16, 256, 0));
         this.cancelRange = register(new Setting("Cancel Range", 6, 16, 0));
         this.downRange = register(new Setting("Down Range", 5, 8, 0));
         this.upRange = register(new Setting("Up Range", 1, 8, 0));
         this.hRange = register(new Setting("H Range", 4.0F, 8.0F, 1.0F));
         this.timer = register(new Setting("Timer", 2.0F, 50.0F, 1.0F));
         this.speed = register(new Setting("Speed", 2.0F, 10.0F, 0.0F));
         this.step = register(new Setting("Step", Boolean.TRUE));
         this.mode = register(new Setting("Mode", ModeType.NCP));
         this.height = register(new Setting("NCP Height", 2.5F, 4.0F, 1.0F, v ->
                     (this.mode.getValue() == ModeType.NCP && this.step.getValue())));
         this.vHeight = register(new Setting("Vanilla Height", 2.5F, 4.0F, 1.0F, v ->
                     (this.mode.getValue() == ModeType.VANILLA && this.step.getValue())));
         this.abnormal = register(new Setting("Abnormal", Boolean.FALSE, v ->
                     (this.mode.getValue() != ModeType.VANILLA && this.step.getValue())));
         this.centerSpeed = register(new Setting("Center Speed", 2, 10, 1));
         this.only = register(new Setting("Only 1x1", Boolean.TRUE));
         this.single = register(new Setting("Single Hole", Boolean.TRUE, v -> !this.only.getValue()));
         this.twoBlocks = register(new Setting("Double Hole", Boolean.TRUE, v -> !this.only.getValue()));
         this.custom = register(new Setting("Custom Hole", Boolean.TRUE, v -> !this.only.getValue()));
         this.four = register(new Setting("Four Blocks", Boolean.TRUE, v -> !this.only.getValue()));
         this.near = register(new Setting("Near Target", Boolean.TRUE));
         this.disable = register(new Setting("Disable", Boolean.TRUE));
         this.gameTimer = new Timer();
         this.holeManager = new HoleManager();
     }
     public void onEnable() {
         super.onEnable();
         sendMessage("This Module is Beta Module");
         this.wasInHole = false;
         BlockPos playerPos = PlayerUtil.getPlayerPos();
         this.originPos = playerPos;
         this.stuckTicks = 0;
         this.isActive = false;
         this.gameTimer.reset();
     }
     public void onDisable() {
         super.onDisable();
         this.isActive = false;
         this.stuckTicks = 0;
         resetTimer();
         if (mc.player != null) {
             if (mc.player.getRidingEntity() != null) {
                 (mc.player.getRidingEntity()).stepHeight = 1.0F;
             }
             mc.player.stepHeight = 0.6F;
         }
     }
     @SubscribeEvent
     public void onInputUpdate(InputUpdateEvent event) {
         if (this.isActive && event.getMovementInput() != null) {
             (event.getMovementInput()).jump = false;
             (event.getMovementInput()).sneak = false;
             (event.getMovementInput()).forwardKeyDown = false;
             (event.getMovementInput()).backKeyDown = false;
             (event.getMovementInput()).leftKeyDown = false;
             (event.getMovementInput()).rightKeyDown = false;
             (event.getMovementInput()).moveForward = 0.0F;
             (event.getMovementInput()).moveStrafe = 0.0F;
         }
     }
     @SubscribeEvent
     public void onPlayerTick(TickEvent.PlayerTickEvent event) {
         if (nullCheck()) {
             return;
         }
         if (event.phase != TickEvent.Phase.START || mc.player == null || mc.world == null)
             return;
         this.isActive = false;
         resetTimer();
         if (!mc.player.isEntityAlive() || mc.player.isElytraFlying() || mc.player.capabilities.isFlying) {
             return;
         }
         double currentSpeed = Math.hypot(mc.player.motionX, mc.player.motionZ);
         if (currentSpeed <= 0.05D) {
             this.originPos = PlayerUtil.getPlayerPos();
         }
         this.target = getNearestPlayer(this.target);
         if (this.target == null) {
             return;
         }
         double range = mc.player.getDistance(this.target);
         boolean inRange = (range <= this.cancelRange.getValue());
         if (shouldDisable(currentSpeed, inRange)) {
             if (this.disable.getValue()) {
                 disable();
             }
             return;
         }
         BlockPos hole = findHoles(this.target, inRange);
         if (hole != null) {
             double x = hole.getX() + 0.5D;
             double y = hole.getY();
             double z = hole.getZ() + 0.5D;
             if (checkYRange((int)mc.player.posY, hole.getY())) {
                 Vec3d playerPos = mc.player.getPositionVector();
                 double yawRad = Math.toRadians((RotationUtil.getRotationTo(playerPos, new Vec3d(x, y, z))).x);
                 double dist = Math.hypot(x - playerPos.x, z - playerPos.z);
                 if (mc.player.onGround) {
                     this.playerSpeed = getBaseMoveSpeed() * ((isCollidingWithLiquid() && !isInLiquid()) ? 0.91D : this.speed.getValue());
                     boolean slowDown = true;
                 }
                 double moveSpeed = Math.min(dist, this.playerSpeed);
                 mc.player.motionX = -Math.sin(yawRad) * moveSpeed;
                 mc.player.motionZ = Math.cos(yawRad) * moveSpeed;
                 if (moveSpeed != 0.0D && (-Math.sin(yawRad) != 0.0D || Math.cos(yawRad) != 0.0D)) {
                     setTimer((float)(50.0D / this.timer.getValue()));
                     this.isActive = true;
                 }
             }
         }
         if (mc.player.collidedHorizontally && hole == null) {
             this.stuckTicks++;
         } else {
             this.stuckTicks = 0;
         }
         if (canStep()) {
             mc.player.stepHeight = getHeight();
         } else {
             if (mc.player.getRidingEntity() != null) {
                 (mc.player.getRidingEntity()).stepHeight = 1.0F;
             }
             mc.player.stepHeight = 0.6F;
         }
         if (this.target == null) {
             this.isActive = false;
         }
     }
     private EntityPlayer getNearestPlayer(EntityPlayer currentTarget) {
         if (currentTarget != null && mc.player
             .getDistance(currentTarget) <= this.fixedRange.getValue() &&
             isInvalidTarget(currentTarget)) {
             return currentTarget;
         }
         return mc.world.playerEntities.stream()
             .filter(p -> (mc.player.getDistance(p) <= this.targetRange.getValue()))
             .filter(p -> (mc.player.getEntityId() != p.getEntityId()))
             .filter(this::isInvalidTarget)
             .min(Comparator.comparing(p -> mc.player.getDistance(p)))
             .orElse(null);
     }
     private boolean isInvalidTarget(EntityPlayer player) {
         return (!player.isDead && !(player
.getHealth() <= 0.0F) &&
                                     !FriendManager.isFriend(player.getName()));
     }
     private BlockPos findHoles(EntityPlayer target, boolean inRange) {
         if (inRange && this.wasInHole) {
             return null;
         }
         this.wasInHole = false;
         List<BlockPos> holes = new ArrayList<>();
         List<BlockPos> blockPosList = getSphere(getPlayerPos(target), this.hRange.getValue(), 8.0f, false, true, 0);
         for (BlockPos pos : blockPosList) {
             if (!checkYRange((int)mc.player.posY, pos.getY()) || (
                 !mc.world.isAirBlock(PlayerUtil.getPlayerPos().up(2)) && (int)mc.player.posY < pos.getY()))
                 continue;
             if (this.holeManager.isSafe(pos) &&
                 mc.world.isAirBlock(pos) && mc.world.isAirBlock(pos.up()) && mc.world.isAirBlock(pos.up(2))) {
                 boolean valid = true;
                 for (int high = 0; high < mc.player.posY - pos.getY(); high++) {
                     if (high != 0) {
                         if (mc.player.posY > pos.getY() &&
                             !mc.world.isAirBlock(new BlockPos(pos.getX(), pos.getY() + high, pos.getZ()))) {
                             valid = false;
                             break;
                         }
                         if (mc.player.posY < pos.getY()) {
                             BlockPos newPos = new BlockPos(pos.getX(), pos.getY() + high, pos.getZ());
                             if (mc.world.isAirBlock(newPos) && (mc.world.isAirBlock(newPos.down()) || mc.world.isAirBlock(newPos.up()))) {
                                 valid = false;
                                 break;
                             }
                         }
                     }
                 }
                 if (valid) {
                     holes.add(pos);
                 }
             }
         }
         return holes.stream()
             .min(Comparator.comparing(p -> this.near.getValue() ? target.getDistance(p.getX() + 0.5D, p.getY(), p.getZ() + 0.5D) : mc.player.getDistance(p.getX() + 0.5D, p.getY(), p.getZ() + 0.5D)))
             .orElse(null);
     }
     private boolean shouldDisable(Double currentSpeed, boolean inRange) {
         if (this.isActive) return false;
         if (!mc.player.onGround) return false;
         if (this.stuckTicks > 5 && currentSpeed < 0.05D) return true;
         if (this.holeManager.isSafe(new BlockPos(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY() + 0.5D, PlayerUtil.getPlayerPos().getZ())) && inRange) {
             BlockPos holePos = new BlockPos(PlayerUtil.getPlayerPos().getX(), PlayerUtil.getPlayerPos().getY(), PlayerUtil.getPlayerPos().getZ());
             Vec3d center = new Vec3d(holePos.getX() + 0.5D, holePos.getY(), holePos.getZ() + 0.5D);
             double xDiff = Math.abs(center.x - mc.player.posX);
             double zDiff = Math.abs(center.z - mc.player.posZ);
             if ((xDiff > 0.3D || zDiff > 0.3D) && !this.wasInHole) {
                 double motionX = center.x - mc.player.posX;
                 double motionZ = center.z - mc.player.posZ;
                 mc.player.motionX = motionX / this.centerSpeed.getValue();
                 mc.player.motionZ = motionZ / this.centerSpeed.getValue();
             }
             return this.wasInHole = true;
         }
         return false;
     }
     private boolean checkYRange(int playerY, int holeY) {
         if (playerY >= holeY) {
             return (playerY - holeY <= this.downRange.getValue());
         }
         return (holeY - playerY <= this.upRange.getValue());
     }
     public float getHeight() {
         return (this.mode.getValue() == ModeType.VANILLA) ? this.vHeight.getValue() : this.height.getValue();
     }
     private boolean canStep() {
         return (!mc.player.isInWater() && mc.player.onGround && !mc.player.isOnLadder() && !mc.player.movementInput.jump && mc.player.collidedVertically && mc.player.fallDistance < 0.1D && this.step
.getValue() && this.isActive);
     }
     private void sendOffsets(double[] offsets) {
         for (double offset : offsets) {
             mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
         }
     }
     private double getBaseMoveSpeed() {
         return 0.2873D;
     }
     private boolean isCollidingWithLiquid() {
         return false;
     }
     private boolean isInLiquid() {
         return (mc.player.isInWater() || mc.player.isInLava());
     }
     private void setTimer(float tickLength) {}
     private void resetTimer() {
         setTimer(50.0F);
     }
     private List<BlockPos> getSphere(BlockPos center, float r, float h, boolean hollow, boolean sphere, int plusY) {
         ArrayList<BlockPos> circleblocks = new ArrayList<>();
         int cx = center.getX();
         int cy = center.getY();
         int cz = center.getZ();
         int x = cx - (int) r;
         while ((float) x <= (float) cx + r) {
             int z = cz - (int) r;
             while ((float) z <= (float) cz + r) {
                 int y = sphere ? cy - (int) r : cy;
                 while (true) {
                     float f = y;
                     float f2 = sphere ? (float) cy + r : (float) (cy + h);
                     if (!(f < f2)) break;
                     double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                     if (!(!(dist < (double) (r * r)) || hollow && dist < (double) ((r - 1.0f) * (r - 1.0f)))) {
                         BlockPos l = new BlockPos(x, y + plusY, z);
                         circleblocks.add(l);
                     }
                     ++y;
                 }
                 ++z;
             }
             ++x;
         }
         return circleblocks;
     }
     private BlockPos getPlayerPos(EntityPlayer player) {
         return new BlockPos(player.posX, player.posY, player.posZ);
     }
     public enum ModeType {
         NCP,
         VANILLA
             }
 }