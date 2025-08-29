 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.UUID;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.entity.Entity;
 import org.lwjgl.opengl.GL11;
 public class CrystalSpawns
     extends Module
 {
     private final Setting<Boolean> rainbow = register(new Setting("Rainbow", Boolean.FALSE));
     private final Setting<Integer> red = register(new Setting("Red", 0, 255, 0));
     private final Setting<Integer> green = register(new Setting("Green", 0, 255, 0));
     private final Setting<Integer> blue = register(new Setting("Blue", 0, 255, 0));
     private static CrystalSpawns INSTANCE;
     public static final HashMap<UUID, Thingering> thingers = new HashMap<>();
     public CrystalSpawns() {
         super("CrystalSpawns", Categories.RENDER, false, false);
         setInstance();
     }
     public static CrystalSpawns getInstance() {
         if (INSTANCE == null) {
             INSTANCE = new CrystalSpawns();
         }
         return INSTANCE;
     }
     private void setInstance() {
         INSTANCE = this;
     }
     public void onTick() {
         if (mc.world == null)
             return;    for (Entity entity : mc.world.loadedEntityList) {
             if (!(entity instanceof net.minecraft.entity.item.EntityEnderCrystal) || thingers.containsKey(entity.getUniqueID()))
                 continue;    thingers.put(entity.getUniqueID(), new Thingering(entity));
             thingers.get(entity.getUniqueID()).starTime = System.currentTimeMillis();
         }
     }
     public void onRender3D() {
         if (mc.player == null || mc.world == null)
             return;    for (Map.Entry<UUID, Thingering> entry : thingers.entrySet()) {
             long time = System.currentTimeMillis();
             long duration = time - entry.getValue().starTime;
             if (duration >= 1500L)
                 continue;
             float cycle = (float)(duration % 1500L) / 1500.0F;
             float moveY = (float)Math.sin(Math.PI * cycle) * 1.5F;
             float scale = 0.6F + 0.2F * (float)Math.sin(Math.PI * cycle);
             float opacity = 1.0F - (float)duration / 1500.0F;
             drawCircle(entry.getValue().entity, mc.getRenderPartialTicks(), scale, moveY, opacity);
         }
     }
     public void drawCircle(Entity entity, float partialTicks, double rad, float moveY, float alpha) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         startSmooth();
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         GL11.glLineWidth(2.5F);
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - (mc.getRenderManager()).viewerPosX;
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - (mc.getRenderManager()).viewerPosY;
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - (mc.getRenderManager()).viewerPosZ;
         GL11.glBegin(3);
         for (int i = 0; i <= 90; i++) {
             Color color = this.rainbow.getValue() ? rainbowColor(i * 4) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
             GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, alpha);
             double theta = (i * 2) * Math.PI / 90.0D;
             GL11.glVertex3d(x + rad * Math.cos(theta), y + moveY, z + rad * Math.sin(theta));
         }
         GL11.glEnd();
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         endSmooth();
         GL11.glEnable(3553);
         GL11.glPopMatrix();
     }
     private Color rainbowColor(int delay) {
         float hue = (float)(System.currentTimeMillis() % 11520L + delay) / 11520.0F;
         return Color.getHSBColor(hue, 0.8F, 1.0F);
     }
     public static void startSmooth() {
         GL11.glEnable(2848);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glHint(3154, 4354);
     }
     public static void endSmooth() {
         GL11.glDisable(2848);
         GL11.glDisable(3042);
     }
     public static class Thingering {
         public final Entity entity;
         public long starTime;
         public Thingering(Entity entity) {
             this.entity = entity;
             this.starTime = 0L;
         }
     }
 }