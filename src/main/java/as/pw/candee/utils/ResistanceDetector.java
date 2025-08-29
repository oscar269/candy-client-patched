 package as.pw.candee.utils;
 import java.util.ArrayList;
 import java.util.ConcurrentModificationException;
 import java.util.HashMap;
 import java.util.concurrent.Executors;
 import java.util.concurrent.ScheduledExecutorService;
 import java.util.concurrent.TimeUnit;
 import net.minecraft.entity.player.EntityPlayer;
 public class ResistanceDetector
     implements Util
 {
     public static void init() {
         ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
         scheduler.scheduleAtFixedRate(() -> {
             try {
                 HashMap<String, Integer> a = new HashMap<>();
                 ArrayList<String> i = new ArrayList<>();
                 resistanceList.forEach((k, v) -> {
                     if (v > 0) {
                         a.put(k, v - 1);
                     } else {
                         i.add(k);
                     }
                 });
                 a.forEach((k, v) -> {
                     if (resistanceList.containsKey(k)) {
                         resistanceList.replace(k, v);
                     }
                 });
                 a.clear();
                 i.forEach(w -> {
                     resistanceList.remove(i);
                 });
             }
             catch (ConcurrentModificationException concurrentModificationException) {
                 // empty catch block
             }
         }, 0L, 1L, TimeUnit.SECONDS);
             }
     public static void onUpdate() {
         if (mc.world != null && mc.player != null) {
             for (EntityPlayer uwu : mc.world.playerEntities) {
                 if (uwu.getAbsorptionAmount() < 9.0F) {
                     continue;
                 }
                 resistanceList.remove(uwu.getName());
                 resistanceList.put(uwu.getName(), 180);
             }
         }
     }
     public static final HashMap<String, Integer> resistanceList = new HashMap<>();
 }