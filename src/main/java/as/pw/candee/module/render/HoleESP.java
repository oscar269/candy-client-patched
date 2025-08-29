 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.List;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.HoleUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import net.minecraft.util.math.BlockPos;
 public class HoleESP
     extends Module {
     public final Setting<Float> range;
     public final Setting<Color> obby;
     public final Setting<Color> bedrock;
     public final Setting<type> renderType;
     public final Setting<Boolean> outline;
     public final Setting<Float> width;
     public HoleESP() {
         super("HoleESP", Categories.RENDER, false, false);
         this.range = register(new Setting("Range", 10.0F, 12.0F, 1.0F));
         this.obby = register(new Setting("ObbyColor", new Color(230, 50, 50, 100)));
         this.bedrock = register(new Setting("BedrockColor", new Color(230, 150, 50, 100)));
         this.renderType = register(new Setting("RenderType", type.Down));
         this.outline = register(new Setting("Outline", Boolean.FALSE));
         this.width = register(new Setting("Width", 3.0F, 6.0F, 0.1F, v -> this.outline.getValue()));
     }
     public void onRender3D() {
         try {
             List<BlockPos> holes = CandeePlusRewrite.m_hole.getHoles();
             for (BlockPos hole : holes) {
                 if (PlayerUtil.getDistance(hole) > this.range.getValue()) {
                     continue;
                 }
                 Color color = this.obby.getValue();
                 if (HoleUtil.isBedrockHole(hole)) {
                     color = this.bedrock.getValue();
                 }
                 if (this.renderType.getValue() == type.Full) {
                     RenderUtil3D.drawBox(hole, 1.0D, color, 63);
                 } else {
                     RenderUtil3D.drawBox(hole, 1.0D, color, 1);
                 }
                 if (!this.outline.getValue()) {
                     continue;
                 }
                 RenderUtil3D.drawBoundingBox(hole, 1.0D, this.width.getValue(), color);
             }
         } catch (Exception ignored) {}
     }
     public enum type
     {
         Full,
         Down
             }
 }