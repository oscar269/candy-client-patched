 package as.pw.candee.hud.modules;
 import java.awt.Color;
 import as.pw.candee.hud.Hud;
 import as.pw.candee.setting.Setting;
 import as.pw.candee.utils.ColorUtil;
 import as.pw.candee.utils.RenderUtil;
 import net.minecraft.block.Block;
 import net.minecraft.init.Blocks;
 import net.minecraft.init.Items;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemBlock;
 import net.minecraft.item.ItemStack;
 public class PvPResources
     extends Hud {
     public final Setting<Boolean> crystal;
     public final Setting<Boolean> xp;
     public final Setting<Boolean> gap;
     public final Setting<Boolean> totem;
     public final Setting<Boolean> obby;
     public final Setting<Boolean> piston;
     public final Setting<Boolean> redstone;
     public final Setting<Boolean> torch;
     public final Setting<Boolean> block;
     public final Setting<Boolean> shadow;
     public final Setting<Color> color;
     public final Setting<Boolean> background;
     public final Setting<Color> backcolor;
     public PvPResources() {
         super("PvPResources", 300.0F, 100.0F);
         this.crystal = register(new Setting("Crystal", Boolean.TRUE));
         this.xp = register(new Setting("Xp", Boolean.TRUE));
         this.gap = register(new Setting("Gap", Boolean.TRUE));
         this.totem = register(new Setting("Totem", Boolean.TRUE));
         this.obby = register(new Setting("Obsidian", Boolean.TRUE));
         this.piston = register(new Setting("Piston", Boolean.TRUE));
         this.redstone = register(new Setting("RedStone", Boolean.TRUE));
         this.torch = register(new Setting("Torch", Boolean.TRUE, v -> this.redstone.getValue()));
         this.block = register(new Setting("Block", Boolean.TRUE, v -> this.redstone.getValue()));
         this.shadow = register(new Setting("Shadow", Boolean.FALSE));
         this.color = register(new Setting("Color", new Color(255, 255, 255, 255)));
         this.background = register(new Setting("Background", Boolean.FALSE));
         this.backcolor = register(new Setting("BGColor", new Color(40, 40, 40, 60), v -> this.background.getValue()));
     }
     public void onRender() {
         float x = this.x.getValue();
         float y = this.y.getValue();
         if (this.crystal.getValue()) {
             renderItem(Items.END_CRYSTAL, getItemCount(Items.END_CRYSTAL), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.xp.getValue()) {
             renderItem(Items.EXPERIENCE_BOTTLE, getItemCount(Items.EXPERIENCE_BOTTLE), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.gap.getValue()) {
             renderItem(Items.GOLDEN_APPLE, getItemCount(Items.GOLDEN_APPLE), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.totem.getValue()) {
             renderItem(Items.TOTEM_OF_UNDYING, getItemCount(Items.TOTEM_OF_UNDYING), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.obby.getValue()) {
             renderBlock(Blocks.OBSIDIAN, getBlockCount(Blocks.OBSIDIAN), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.piston.getValue()) {
             renderBlock(Blocks.PISTON, getBlockCount(Blocks.PISTON) + getBlockCount(Blocks.STICKY_PISTON), x, y);
             y += RenderUtil.getStringHeight(1.0F) + 13.0F;
         }
         if (this.redstone.getValue()) {
             if (this.block.getValue()) {
                 renderBlock(Blocks.REDSTONE_BLOCK, getBlockCount(Blocks.REDSTONE_BLOCK), x, y);
                 y += RenderUtil.getStringHeight(1.0F) + 13.0F;
             }
             if (this.torch.getValue()) {
                 renderBlock(Blocks.REDSTONE_TORCH, getBlockCount(Blocks.REDSTONE_TORCH), x, y);
                 y += RenderUtil.getStringHeight(1.0F) + 13.0F;
             }
         }
         y -= RenderUtil.getStringHeight(1.0F) + 13.0F;
         this.width = x + 20.0F + RenderUtil.getStringWidth(" : 64", 1.0F) - this.x.getValue();
         this.height = y - this.y.getValue();
     }
     public void renderItem(Item item, int count, float x, float y) {
         RenderUtil.renderItem(new ItemStack(item), x, y - 8.0F, false);
         RenderUtil.drawString(" : " + count, x + 20.0F, y, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), 1.0F);
     }
     public void renderBlock(Block block, int count, float x, float y) {
         RenderUtil.renderItem(new ItemStack(block), x, y - 10.0F, false);
         RenderUtil.drawString(" : " + count, x + 20.0F, y, ColorUtil.toRGBA(this.color.getValue()), this.shadow.getValue(), 1.0F);
     }
     public int getItemCount(Item item) {
         int count = 0;
         for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
             ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
             if (itemStack.getItem() == item) {
                 count += itemStack.getCount();
             }
         }
         return count;
     }
     public int getBlockCount(Block block) {
         int count = 0;
         for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
             ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
             if (itemStack.getItem() instanceof ItemBlock && ((ItemBlock)itemStack.getItem()).getBlock() == block) {
                 count += itemStack.getCount();
             }
         }
         return count;
     }
 }