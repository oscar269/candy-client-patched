 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.List;
 import java.util.stream.Collectors;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.PlayerUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.math.BlockPos;
 public class CityESP
     extends Module {
     public final Setting<Float> range;
     public final Setting<Color> color;
     public final Setting<Boolean> outline;
     public final Setting<Float> width;
     public CityESP() {
         super("CityESP", Categories.RENDER, false, false);
         this.range = register(new Setting("Range", 10.0F, 12.0F, 1.0F));
         this.color = register(new Setting("Color", new Color(230, 50, 50, 100)));
         this.outline = register(new Setting("Outline", Boolean.FALSE));
         this.width = register(new Setting("Width", 3.0F, 6.0F, 0.1F, v -> this.outline.getValue()));
     }
     public void onRender3D() {
         List<EntityPlayer> players = mc.world.playerEntities.stream().filter(e -> (e.getEntityId() != mc.player.getEntityId())).collect(Collectors.toList());
         for (EntityPlayer player : players) {
             BlockPos[] array = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
                             for (BlockPos offset : array) {
                 BlockPos position = (new BlockPos(player.posX, player.posY, player.posZ)).add(offset);
                 if (PlayerUtil.getDistance(position) <= this.range.getValue() &&
                     BlockUtil.getBlock(position) == Blocks.OBSIDIAN) {
                     RenderUtil3D.drawBox(position, 1.0D, this.color.getValue(), 63);
                     if (this.outline.getValue())
                         RenderUtil3D.drawBoundingBox(position, 1.0D, this.width.getValue(), this.color.getValue());
                 }
             }
         }
     }
 }