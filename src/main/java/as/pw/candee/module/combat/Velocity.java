 package as.pw.candee.module.combat;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 public class Velocity
     extends Module
 {
     final Setting<Integer> horizontal_vel;
     final Setting<Integer> vertical_vel;
     final Setting<Boolean> explosions;
     final Setting<Boolean> bobbers;
     public Velocity() {
         super("Velocity", Categories.COMBAT, false, false);
         this.horizontal_vel = register(new Setting("Horizontal", 0, 100, 0));
         this.vertical_vel = register(new Setting("Vertical", 0, 100, 0));
         this.explosions = register(new Setting("Explosions", Boolean.TRUE));
         this.bobbers = register(new Setting("Bobbers", Boolean.TRUE));
     }
 }