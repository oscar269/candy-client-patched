 package as.pw.candee.hud.modules;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.RenderUtil;
 public class PlayerModel extends Hud {
     public final Setting<Float> scale;
     public PlayerModel() {
         super("PlayerModel", 100.0F, 150.0F);
         this.scale = register(new Setting("Scale", 50.0F, 100.0F, 30.0F));
     }
     public void onRender() {
         this.width = (mc.player.width + 0.5F) * this.scale.getValue() + 10.0F;
         this.height = (mc.player.height + 0.5F) * this.scale.getValue();
         RenderUtil.renderEntity(mc.player, this.x.getValue() + this.width - 30.0F, this.y.getValue() + this.height - 20.0F, this.scale.getValue());
     }
 }