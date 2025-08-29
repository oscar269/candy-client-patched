 package as.pw.candee.hud.modules;
 import com.mojang.realmsclient.gui.ChatFormatting;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.entity.player.EntityPlayer;
 public class PlayerList
     extends Hud
 {
     public final Setting<Integer> maxPlayers;
     public final Setting<Boolean> health;
     public final Setting<Boolean> distance;
     public final Setting<Boolean> shadow;
     public final Setting<Color> color;
     public PlayerList() {
         super("PlayerList", 50.0F, 50.0F);
         this.maxPlayers = register(new Setting("MaxPlayers", 5, 10, 3));
         this.health = register(new Setting("Health", Boolean.TRUE));
         this.distance = register(new Setting("Distance", Boolean.TRUE));
         this.shadow = register(new Setting("Shadow", Boolean.FALSE));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
     }
     public void onRender() {
         try {
             List<EntityPlayer> players = getPlayerList();
             float w = 0.0F;
             float h = 0.0F;
             float textSize = 1.0F;
             float lineHeight = RenderUtil.getStringHeight(textSize) + 4.0F;
             for (EntityPlayer p : players) {
                 int hp = PlayerUtil.getHealth(p);
                 double dist = PlayerUtil.getDistance(p);
                 String str = p.getName();
                 if (this.health.getValue()) str = str + " " + getHealthColor(hp) + hp;
                 if (this.distance.getValue()) str = str + " " + getDistanceColor(dist) + (int)dist;
                 float textW = RenderUtil.getStringWidth(str, textSize);
                 if (textW > w) w = textW;
                 RenderUtil.drawString(str, this.x.getValue(), this.y.getValue() + h,
                         ColorUtil.toRGBA(this.color.getValue()), this.shadow
.getValue(), textSize);
                 h += lineHeight;
             }
             this.width = w;
             this.height = h;
         } catch (Exception ignored) {}
     }
     private ChatFormatting getDistanceColor(double d) {
         if (d > 20.0D)
             return ChatFormatting.GREEN;
         if (d > 6.0D)
             return ChatFormatting.YELLOW;
         return ChatFormatting.RED;
     }
     private ChatFormatting getHealthColor(int hp) {
         if (hp > 23)
             return ChatFormatting.GREEN;
         if (hp > 7)
             return ChatFormatting.YELLOW;
         return ChatFormatting.RED;
     }
     private List<EntityPlayer> getPlayerList() {
         List<EntityPlayer> list = new ArrayList<>(mc.world.playerEntities);
         list.removeIf(p -> (p == mc.player));
         list.sort(Comparator.comparingDouble(PlayerUtil::getDistance));
         if (list.size() > this.maxPlayers.getValue()) {
             return new ArrayList<>(list.subList(0, this.maxPlayers.getValue()));
         }
         return list;
     }
 }