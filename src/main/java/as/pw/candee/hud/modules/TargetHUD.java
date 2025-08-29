 package as.pw.candee.hud.modules;
 import java.awt.Color;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.InventoryPlayer;
 import net.minecraft.item.ItemStack;
 public class TargetHUD extends Hud {
     public static EntityPlayer target;
     public double health;
     public final Setting<Boolean> shadow;
     public final Setting<Color> color;
     public final Setting<Float> size;
     private final float baseWidth = 200.0F;
     private final float baseHeight = 70.0F;
     public TargetHUD() {
         super("TargetHud", 100.0F, 50.0F);
         this.health = 36.0D;
         this.shadow = register(new Setting("Shadow", Boolean.TRUE));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
         this.size = register(new Setting("Size", 1.0F, 2.0F, 0.5F));
     }
     public void onRender() {
         try {
             if (nullCheck())
                 return;
             float scale = this.size.getValue();
             float width = 200.0F * scale;
             float height = 70.0F * scale;
             this.width = width;
             this.height = height;
             target = PlayerUtil.getNearestPlayer(30.0D);
             if (target == null)
                 return;
             float startX = this.x.getValue();
             float startY = this.y.getValue();
             RenderUtil.drawRect(startX, startY, width, height, ColorUtil.toRGBA(40, 40, 40));
             RenderUtil.renderEntity(target, startX + 30.0F * scale, startY + height - 7.0F * scale, 30.0F * scale);
             float targetHealth = target.getHealth() + target.getAbsorptionAmount();
             this.health += (targetHealth - this.health) * 0.4D;
             double lineWidth = (width * targetHealth / 36.0F);
             RenderUtil.drawGradientRect(startX, startY + height - scale, startX + (float)lineWidth, startY + height,
                     ColorUtil.toRGBA(255, 0, 0),
                     ColorUtil.toRGBA(getHealthColor((int)targetHealth)));
             int white = ColorUtil.toRGBA(255, 255, 255);
             float fontSpacing = 12.0F * scale;
             RenderUtil.drawString(target.getName(), startX + 60.0F * scale, startY + 10.0F * scale, white, this.shadow.getValue(), scale);
             float itemY = startY + 20.0F * scale;
             renderItem(getArmorInv(3), (int)(60.0F * scale), (int)itemY);
             renderItem(getArmorInv(2), (int)(80.0F * scale), (int)itemY);
             renderItem(getArmorInv(1), (int)(100.0F * scale), (int)itemY);
             renderItem(getArmorInv(0), (int)(120.0F * scale), (int)itemY);
             renderItem(target.getHeldItemMainhand(), (int)(140.0F * scale), (int)itemY);
             renderItem(target.getHeldItemOffhand(), (int)(160.0F * scale), (int)itemY);
             RenderUtil.drawString("Health : " + (int)targetHealth, startX + 60.0F * scale, startY + 42.0F * scale, white, this.shadow.getValue(), scale);
             RenderUtil.drawString("Distance : " + (int)PlayerUtil.getDistance(target), startX + 60.0F * scale, startY + 42.0F * scale + fontSpacing, white, this.shadow.getValue(), scale);
         }
         catch (Exception ignored) {}
     }
     public void renderItem(ItemStack item, int offsetX, int offsetY) {
         if (item == null || item.isEmpty())
             return;    RenderUtil.renderItem(item, this.x.getValue() + offsetX, this.y.getValue() + offsetY - 4.0F);
     }
     public ItemStack getArmorInv(int slot) {
         InventoryPlayer inv = target.inventory;
         return inv.armorItemInSlot(slot);
     }
     public float getItemDmg(ItemStack is) {
         return (float) (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage() * 100.0F;
     }
     public int getItemDmgColor(ItemStack is) {
         int red, green;
         float maxDmg = is.getMaxDamage();
         float dmg = maxDmg - is.getItemDamage();
         double offset = (255.0F / maxDmg / 2.0F);
         if (dmg > maxDmg / 2.0F) {
             red = (int)((maxDmg - dmg) * offset);
             green = 255;
         } else {
             red = 255;
             green = (int)(255.0D - (maxDmg / 2.0D - dmg) * offset);
         }
         return ColorUtil.toRGBA(red, green, 0, 255);
     }
     private static Color getHealthColor(int health) {
         int red, green;
         health = Math.max(0, Math.min(health, 36));
         if (health > 18) {
             red = (int)((36 - health) * 14.1666667D);
             green = 255;
         } else {
             red = 255;
             green = (int)(255.0D - (18 - health) * 14.1666667D);
         }
         return new Color(red, green, 0, 255);
     }
 }