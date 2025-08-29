 package as.pw.candee.module.misc;
 import com.mojang.authlib.GameProfile;
 import java.util.UUID;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 import net.minecraft.client.entity.EntityOtherPlayerMP;
 import net.minecraft.client.multiplayer.WorldClient;
 public class FakePlayer extends Module {
     public final Setting<Boolean> copyInv;
     private EntityOtherPlayerMP _fakePlayer;
     public FakePlayer() {
         super("FakePlayer", Categories.MISC, false, false);
         this.copyInv = register(new Setting("CopyInv", Boolean.TRUE));
     }
     public void onEnable() {
         if (mc.player == null) {
             disable();
             return;
         }
         this._fakePlayer = null;
         if (mc.player != null) {
             WorldClient world = mc.world;
             UUID fromString = UUID.fromString("53f654cb-e42a-4a67-8072-4bb70ed76230");
             getClass();
             (this._fakePlayer = new EntityOtherPlayerMP(world, new GameProfile(fromString, "8x0f"))).copyLocationAndAnglesFrom(mc.player);
             this._fakePlayer.rotationYawHead = mc.player.rotationYawHead;
             if (this.copyInv.getValue()) {
                 this._fakePlayer.inventory.copyInventory(mc.player.inventory);
             }
             mc.world.addEntityToWorld(-100, this._fakePlayer);
         }
     }
     public void onDisable() {
         if (mc.world != null && mc.player != null) {
             super.onDisable();
             try {
                 mc.world.removeEntity(this._fakePlayer);
             }
             catch (Exception ignored) {}
         }
     }
 }