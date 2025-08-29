 package as.pw.candee.module.render;
 import java.awt.Color;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.BlockUtil;
 import as.pw.candee.utils.RenderUtil3D;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.math.BlockPos;
 public class BurrowESP
     extends Module {
     public final Setting<Boolean> obby;
     public final Setting<Color> color;
     public BurrowESP() {
         super("BurrowESP", Categories.RENDER, false, false);
         this.obby = register(new Setting("Only Obby", Boolean.FALSE));
         this.color = register(new Setting("Color", new Color(255, 64, 207, 210)));
     }
     public void onRender3D() {
         List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
         for (EntityPlayer player : players) {
             BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
             if (BlockUtil.getBlock(pos) != Blocks.AIR && (!this.obby.getValue() || BlockUtil.getBlock(pos) == Blocks.OBSIDIAN))
                 RenderUtil3D.drawBox(pos, 1.0D, this.color.getValue(), 63);
         }
     }
 }