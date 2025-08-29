 package as.pw.candee.module.misc;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.StringUtil;
 import net.minecraft.entity.Entity;
 public class DonkeyNotifier
     extends Module
 {
     private final Setting<Boolean> donkey;
     private final Setting<Boolean> llama;
     private List<Entity> entities;
     public DonkeyNotifier() {
         super("DonkeyNotifer", Categories.MISC, false, false);
         this.donkey = register(new Setting("Donkey", Boolean.TRUE));
         this.llama = register(new Setting("Llama", Boolean.TRUE));
         this.entities = new ArrayList<>();
     }
     public void onDisable() {
         this.entities = new ArrayList<>();
     }
     public void onUpdate() {
         if (nullCheck()) {
             return;
         }
         List<Entity> donkeys = new ArrayList<>(mc.world.loadedEntityList);
         donkeys.removeIf(e2 -> ((!this.donkey.getValue() || !(e2 instanceof net.minecraft.entity.passive.EntityDonkey)) && (!this.llama.value || !(e2 instanceof net.minecraft.entity.passive.EntityLlama))));
        for (Entity e : donkeys) {
                if (!this.entities.contains(e)) {
                        this.entities.add(e);
                        sendMessage("Found a " + ((e instanceof net.minecraft.entity.passive.EntityDonkey) ? "Donkey" : "Llama") + " at " + StringUtil.getPositionString(e.getPosition()));
                }
        }
     }
 }