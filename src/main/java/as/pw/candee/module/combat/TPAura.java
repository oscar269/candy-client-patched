 package as.pw.candee.module.combat;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.managers.FriendManager;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.Timer;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.network.play.client.CPacketPlayer;
 import net.minecraft.util.EnumHand;
 import org.lwjgl.input.Keyboard;
 public class TPAura
     extends Module
 {
     private final Setting<Integer> maxRange;
     private final Setting<Boolean> onlyPlayer;
     private final Setting<Boolean> strafe;
     private final Setting<Boolean> look;
     private final Setting<Boolean> motionTp;
     private final Setting<Integer> attackDelay;
     private final Setting<Boolean> swing;
     private final Setting<Boolean> onGround;
     private final Setting<Integer> strafeKey;
     private final Timer timer = new Timer();
     private final List<Entity> targets = new ArrayList<>();
     public TPAura() {
         super("TPAura", Categories.COMBAT, false, false);
         this.maxRange = register(new Setting("MaxRange", 10, 50, 2));
         this.onlyPlayer = register(new Setting("OnlyPlayer", Boolean.TRUE));
         this.strafe = register(new Setting("Strafe", Boolean.FALSE));
         this.look = register(new Setting("LookRotate", Boolean.TRUE));
         this.motionTp = register(new Setting("MotionTp", Boolean.FALSE));
         this.attackDelay = register(new Setting("Delay", 500, 2000, 0));
         this.swing = register(new Setting("Swing", Boolean.TRUE));
         this.onGround = register(new Setting("OnGround", Boolean.FALSE));
         this.strafeKey = register(new Setting("StrafeKey", 0, 0, 256));
     }
     public void onTick() {
         if (mc.player == null || mc.world == null)
             return;
         this.targets.clear();
         for (Entity e : mc.world.loadedEntityList) {
                if (e == mc.player || !e.isEntityAlive() || (this.onlyPlayer.getValue() && !(e instanceof EntityPlayer)) || (
                 e instanceof EntityPlayer && FriendManager.isFriend((EntityPlayer)e)) ||
                 mc.player.getDistance(e) > this.maxRange.getValue())
                 continue;    this.targets.add(e);
         }
         if (this.targets.isEmpty())
             return;    Entity target = this.targets.get(0);
         if (target == null || !target.isEntityAlive())
             return;    if (!this.timer.passedMs(this.attackDelay.getValue()))
             return;
         if (this.look.getValue()) rotateTo(target);
         double px = mc.player.posX, py = mc.player.posY, pz = mc.player.posZ;
         tpTo(target.posX, target.posY, target.posZ);
         mc.playerController.attackEntity(mc.player, target);
         if (this.swing.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
         tpTo(px, py, pz);
         if (this.strafe.getValue() && Keyboard.isKeyDown(this.strafeKey.getValue())) {
             strafeAround(target);
         }
         this.timer.reset();
     }
     private void tpTo(double x, double y, double z) {
         EntityPlayerSP p = mc.player;
         if (p == null)
             return;
         double dx = x - p.posX;
         double dy = y - p.posY;
         double dz = z - p.posZ;
         double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
         if (dist < 1.0E-4D)
             return;
         int steps = this.motionTp.getValue() ? Math.max(1, (int)Math.ceil(dist / 0.35D)) : Math.max(1, (int)Math.ceil(dist / 1.2D));
         double sx = dx / steps, sy = dy / steps, sz = dz / steps;
         for (int i = 0; i < steps; i++) {
             double nx = p.posX + sx;
             double ny = p.posY + sy;
             double nz = p.posZ + sz;
             p.setPosition(nx, ny, nz);
             p.connection.sendPacket(new CPacketPlayer.Position(nx, ny, nz, this.onGround.getValue()));
         }
     }
     private void rotateTo(Entity t) {
         double dx = t.posX - mc.player.posX;
         double dz = t.posZ - mc.player.posZ;
         double dy = t.posY + t.getEyeHeight() / 2.0D - mc.player.posY + mc.player.getEyeHeight();
         double distxz = Math.sqrt(dx * dx + dz * dz);
         float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
         float pitch = (float)-(Math.atan2(dy, distxz) * 180.0D / Math.PI);
         mc.player.rotationYaw = yaw;
         mc.player.rotationPitch = pitch;
     }
     private void strafeAround(Entity t) {
         double angle = (mc.player.ticksExisted % 360) * Math.PI / 18.0D;
         double radius = 8.0D;
         double tx = t.posX + Math.cos(angle) * radius;
         double tz = t.posZ + Math.sin(angle) * radius;
         double dx = tx - mc.player.posX;
         double dz = tz - mc.player.posZ;
         double dy = t.posY - mc.player.posY;
         double max = 1.0D;
         mc.player.motionX = clamp(dx, -max, max);
         mc.player.motionZ = clamp(dz, -max, max);
         mc.player.motionY = clamp(dy, -1.0D, 1.0D);
     }
     private double clamp(double v, double min, double max) {
         return (v < min) ? min : (Math.min(v, max));
     }
 }