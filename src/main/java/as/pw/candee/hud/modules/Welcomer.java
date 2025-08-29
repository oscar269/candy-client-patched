 package as.pw.candee.hud.modules;
 import java.awt.Color;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 public class Welcomer
     extends Hud {
     public final Setting<Boolean> scroll;
     public final Setting<Float> speed;
     public final Setting<Float> size;
     public final Setting<Boolean> shadow;
     public final Setting<Color> color;
     public final Setting<Boolean> background;
     public final Setting<Color> backcolor;
     private float offsetx;
     public Welcomer() {
         super("Welcomer", 5.0F, 5.0F);
         this.scroll = register(new Setting("Scroll", Boolean.FALSE));
         this.speed = register(new Setting("Speed", 4.0F, 10.0F, 0.1F));
         this.size = new Setting("Size", 1.0F, 5.0F, 0.5F);
         this.shadow = register(new Setting("Shadow", Boolean.FALSE));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
         this.background = register(new Setting("Background", Boolean.FALSE));
         this.backcolor = register(new Setting("BGColor", new Color(40, 40, 40, 60), v -> this.background.getValue()));
         this.offsetx = 0.0F;
     }
     public void onRender() {
         String message = "Welcome " + getPlayerName();
         float size = this.size.getValue();
         float width = RenderUtil.getStringWidth(message, size);
         float height = RenderUtil.getStringHeight(size);
         if (this.background.getValue()) {
             RenderUtil.drawRect(this.x.getValue() + this.offsetx, this.y.getValue(), width + 20.0F * size, height + 10.0F * size, ColorUtil.toRGBA(this.backcolor.getValue()));
         }
         RenderUtil.drawString(message, this.x.getValue() + 10.0F + this.offsetx, this.y.getValue() + 5.0F, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), size);
         if (this.scroll.getValue()) {
             this.offsetx += this.speed.getValue();
         } else {
             this.offsetx = 0.0F;
         }
         if (this.scaledWidth + RenderUtil.getStringWidth(message, size) + 10.0F < this.offsetx) {
             this.offsetx = RenderUtil.getStringWidth(message, size) * -1.0F - 10.0F;
         }
         this.width = width + 20.0F * size;
         this.height = height + 10.0F * size;
     }
     public String getPlayerName() {
         return mc.player.getName();
     }
 }