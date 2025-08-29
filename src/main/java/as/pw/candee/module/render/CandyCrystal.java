 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.event.events.render.RenderEntityModelEvent;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.item.EntityEnderCrystal;
 import net.minecraft.network.play.server.SPacketDestroyEntities;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import org.lwjgl.opengl.GL11;
 public class CandyCrystal
     extends Module {
     public final Setting<Boolean> chams;
     public final Setting<Boolean> throughWalls;
     public final Setting<Boolean> wireframeThroughWalls;
     public final Setting<Boolean> glint;
     public final Setting<Boolean> wireframe;
     public final Setting<Float> scale;
     public final Setting<Float> lineWidth;
     public final Setting<Boolean> colorSync;
     public final Setting<Boolean> rainbow;
     public final Setting<Integer> saturation;
     public final Setting<Integer> brightness;
     public final Setting<Integer> speed;
     public final Setting<Boolean> xqz;
     public final Setting<Color> color;
     public final Setting<Color> hiddenColor;
     public final Setting<Integer> alpha;
     public final Map<EntityEnderCrystal, Float> scaleMap;
     public float hue;
     public final Map<Integer, Integer> colorHeightMap;
     public CandyCrystal() {
         super("CandyCrystal", Categories.RENDER, false, false);
         this.chams = register(new Setting("Chams", Boolean.FALSE));
         this.throughWalls = register(new Setting("ThroughWalls", Boolean.TRUE));
         this.wireframeThroughWalls = register(new Setting("WireThroughWalls", Boolean.TRUE));
         this.glint = register(new Setting("Glint", Boolean.FALSE, v -> this.chams.getValue()));
         this.wireframe = register(new Setting("Wireframe", Boolean.FALSE));
         this.scale = register(new Setting("Scale", 1.0F, 10.0F, 0.1F));
         this.lineWidth = register(new Setting("LineWidth", 1.0F, 3.0F, 0.1F));
         this.colorSync = register(new Setting("Sync", Boolean.FALSE));
         this.rainbow = register(new Setting("Rainbow", Boolean.FALSE));
         this.saturation = register(new Setting("Saturation", 50, 100, 0, v -> this.rainbow.getValue()));
         this.brightness = register(new Setting("Brightness", 100, 100, 0, v -> this.rainbow.getValue()));
         this.speed = register(new Setting("Speed", 40, 100, 1, v -> this.rainbow.getValue()));
         this.xqz = register(new Setting("XQZ", Boolean.FALSE, v -> (!this.rainbow.getValue() && this.throughWalls.getValue())));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 100), v -> !this.rainbow.getValue()));
         this.hiddenColor = register(new Setting("Hidden Color", new Color(255, 255, 255, 100), v -> this.xqz.getValue()));
         this.alpha = register(new Setting("Alpha", 50, 255, 0, v -> !this.rainbow.getValue()));
         this.scaleMap = new ConcurrentHashMap<>();
         this.colorHeightMap = new HashMap<>();
     }
     public void onUpdate() {
         try {
             for (Entity crystal : mc.world.loadedEntityList) {
                 if (!(crystal instanceof EntityEnderCrystal)) {
                     continue;
                 }
                 if (!this.scaleMap.containsKey(crystal)) {
                     this.scaleMap.put((EntityEnderCrystal)crystal, 3.125E-4F);
                 } else {
                     this.scaleMap.put((EntityEnderCrystal)crystal, this.scaleMap.get(crystal) + 3.125E-4F);
                 }
                 if (this.scaleMap.get(crystal) < 0.0625F * this.scale.getValue()) {
                     continue;
                 }
                 this.scaleMap.remove(crystal);
             }
         } catch (Exception ignored) {}
     }
     @SubscribeEvent
     public void onReceivePacket(PacketEvent.Receive event) {
         if (event.getPacket() instanceof SPacketDestroyEntities) {
             SPacketDestroyEntities packet = (SPacketDestroyEntities)event.getPacket();
             for (int id : packet.getEntityIDs()) {
                 Entity entity = mc.world.getEntityByID(id);
                 if (entity instanceof EntityEnderCrystal) {
                     this.scaleMap.remove(entity);
                 }
             }
         }
     }
     @SubscribeEvent
     public void onRenderModel(RenderEntityModelEvent event) {
         if (!(event.entity instanceof EntityEnderCrystal) || !this.wireframe.getValue()) {
             return;
         }
         Color color = this.colorSync.getValue() ? getCurrentColor() : this.color.getValue();
         boolean fancyGraphics = mc.gameSettings.fancyGraphics;
         mc.gameSettings.fancyGraphics = false;
         float gamma = mc.gameSettings.gammaSetting;
         mc.gameSettings.gammaSetting = 10000.0F;
         GL11.glPushMatrix();
         GL11.glPushAttrib(1048575);
         GL11.glPolygonMode(1032, 6913);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         if (this.wireframeThroughWalls.getValue()) {
             GL11.glDisable(2929);
         }
         GL11.glEnable(2848);
         GL11.glEnable(3042);
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
         GlStateManager.glLineWidth(this.lineWidth.getValue());
         event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
         GL11.glPopAttrib();
         GL11.glPopMatrix();
     }
     public void onTick() {
         int colorSpeed = 101 - this.speed.getValue();
         float hue = (float)(System.currentTimeMillis() % (360L * colorSpeed)) / 360.0F * colorSpeed;
         this.hue = hue;
         float tempHue = hue;
         for (int i = 0; i <= 510; i++) {
             this.colorHeightMap.put(i, Color.HSBtoRGB(tempHue, this.saturation.getValue() / 255.0F, this.brightness.getValue() / 255.0F));
             tempHue += 0.0013071896F;
         }
     }
     public Color getCurrentColor() {
         return Color.getHSBColor(this.hue, this.saturation.getValue() / 255.0F, this.brightness.getValue() / 255.0F);
     }
 }