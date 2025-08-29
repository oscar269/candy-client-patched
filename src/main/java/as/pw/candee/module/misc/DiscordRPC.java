 package as.pw.candee.module.misc;
 import as.pw.candee.CandeePlusRewrite;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 public class DiscordRPC
     extends Module {
     public final Setting<Boolean> girl;
     public DiscordRPC() {
         super("DiscordRPC", Categories.MISC, false, false);
         this.girl = register(new Setting("Girl", Boolean.FALSE));
     }
     public void onEnable() {
         CandeePlusRewrite.m_rpc.enable(this);
     }
     public void onDisable() {
         CandeePlusRewrite.m_rpc.disable();
     }
 }