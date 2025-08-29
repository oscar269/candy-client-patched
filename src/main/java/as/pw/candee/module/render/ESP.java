 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.stream.Collectors;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.RenderUtil3D;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.util.math.AxisAlignedBB;
 public class ESP
     extends Module
 {
     public final Setting<Float> width;
     public final Setting<Color> color;
     public ESP() {
         super("ESP", Categories.RENDER, false, false);
         this.width = register(new Setting("Width", 1.5F, 5.0F, 0.5F));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
     }
     public void onRender3D() {
         if (nullCheck()) {
             return;
         }
         for (EntityPlayer player : mc.world.playerEntities.stream().filter(e -> (e.getEntityId() != mc.player.getEntityId())).collect(Collectors.toList())) {
             drawESP(player);
         }
     }
     public void drawESP(EntityPlayer player) {
         GlStateManager.pushMatrix();
         AxisAlignedBB bb = player.getCollisionBoundingBox();
         double z = bb.minZ + (bb.maxZ - bb.minZ) / 2.0D;
         RenderUtil3D.drawLine(bb.minX, bb.maxY, z, bb.maxX, bb.maxY, z, this.color.getValue(), this.width.getValue());
         RenderUtil3D.drawLine(bb.minX, bb.minY, z, bb.maxX, bb.minY, z, this.color.getValue(), this.width.getValue());
         RenderUtil3D.drawLine(bb.minX, bb.minY, z, bb.minX, bb.maxY, z, this.color.getValue(), this.width.getValue());
         RenderUtil3D.drawLine(bb.maxX, bb.minY, z, bb.maxX, bb.maxY, z, this.color.getValue(), this.width.getValue());
         double x = bb.minX - 0.28D;
         double y = bb.minY;
         double width = 0.04D;
         double height = bb.maxY - bb.minY;
         RenderUtil3D.drawRect(x, y, z, 0.04D, height, new Color(0, 0, 0, 255), 255, 63);
         RenderUtil3D.drawRect(x, y, z, 0.04D, height * player.getHealth() / 36.0D, getHealthColor((int)player.getHealth()), 255, 63);
         GlStateManager.popMatrix();
     }
     private static Color getHealthColor(int health) {
         if (health > 36) {
             health = 36;
         }
         if (health < 0) {
             health = 0;
         }
         int red = 0;
         int green = 0;
         if (health > 18) {
             red = (int)((36 - health) * 14.1666666667D);
             green = 255;
         } else {
             red = 255;
             green = (int)(255.0D - (18 - health) * 14.1666666667D);
         }
         return new Color(red, green, 0, 255);
     }
 }