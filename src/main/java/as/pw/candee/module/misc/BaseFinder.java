 package as.pw.candee.module.misc;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ChatUtil;
 import as.pw.candee.utils.StringUtil;
 import net.minecraft.tileentity.TileEntity;
 public class BaseFinder
     extends Module
 {
     private final Setting<Boolean> chest;
     private final Setting<Boolean> hopper;
     private final Setting<Boolean> furnace;
     private final Setting<Boolean> shulker;
     private List<TileEntity> notified;
     public BaseFinder() {
         super("BaseFinder", Categories.MISC, false, false);
         this.chest = register(new Setting("Chest", Boolean.TRUE));
         this.hopper = register(new Setting("Hopper", Boolean.TRUE));
         this.furnace = register(new Setting("Furnace", Boolean.TRUE));
         this.shulker = register(new Setting("Shulker", Boolean.TRUE));
         this.notified = new ArrayList<>();
     }
     public void onDisable() {
         this.notified = new ArrayList<>();
     }
     public void onUpdate() {
         if (nullCheck())
             return;    List<TileEntity> tiles = new ArrayList<>(mc.world.loadedTileEntityList);
         tiles.removeIf(te -> ((!(te instanceof net.minecraft.tileentity.TileEntityChest) || !this.chest.getValue()) && (!(te instanceof net.minecraft.tileentity.TileEntityHopper) || !this.hopper.getValue()) && (!(te instanceof net.minecraft.tileentity.TileEntityFurnace) || !this.furnace.getValue()) && (!(te instanceof net.minecraft.tileentity.TileEntityShulkerBox) || !this.shulker.getValue())));
         for (TileEntity te : tiles) {
             String name; if (this.notified.contains(te))
                 continue;    this.notified.add(te);
             if (te instanceof net.minecraft.tileentity.TileEntityChest) { name = "Chest"; }
             else if (te instanceof net.minecraft.tileentity.TileEntityHopper) { name = "Hopper"; }
             else if (te instanceof net.minecraft.tileentity.TileEntityFurnace) { name = "Furnace"; }
             else { name = "ShulkerBox"; }
                sendMessage(name + ": " + StringUtil.getPositionString(te.getPos()));
             ChatUtil.sendMessage(name + ": " + StringUtil.getPositionString(te.getPos()));
         }
     }
 }