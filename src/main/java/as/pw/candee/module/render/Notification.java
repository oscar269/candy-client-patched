package as.pw.candee.module.render;
import as.pw.candee.module.Module;
import as.pw.candee.setting.Setting;
public class Notification
        extends Module {
        public final Setting<Integer> time;
        public final Setting<Boolean> togglE;
        public final Setting<Boolean> chat;
        public final Setting<Boolean> message;
        public final Setting<Boolean> player;
        public final Setting<Boolean> pop;
        public final Setting<Boolean> death;
        public Notification() {
                super("Notification", Categories.RENDER, false, true);
                this.time = register(new Setting("Time", 2, 5, 1));
                this.togglE = register(new Setting("Toggle", Boolean.FALSE));
                this.chat = register(new Setting("ChatToggle", Boolean.FALSE));
                this.message = register(new Setting("Message", Boolean.TRUE));
                this.player = register(new Setting("Player", Boolean.TRUE));
                this.pop = register(new Setting("Totem", Boolean.TRUE));
                this.death = register(new Setting("Death", Boolean.TRUE));
        }
}