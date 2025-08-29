 package as.pw.candee.managers;
 import java.util.*;

 import as.pw.candee.event.events.network.PacketEvent;
 import as.pw.candee.event.events.player.PlayerDeathEvent;
 import as.pw.candee.hud.modules.*;
 import as.pw.candee.module.Module;
 import as.pw.candee.module.combat.*;
 import as.pw.candee.module.exploit.*;
 import as.pw.candee.module.misc.*;
 import as.pw.candee.module.movement.*;
 import as.pw.candee.module.render.*;
 import net.minecraft.entity.player.EntityPlayer;

 public class ModuleManager extends Manager {
     public final List<Module> modules = new ArrayList<>();
     public void load() {
         register(new AntiBurrow());
         register(new Aura());
         register(new AntiRegear());
         register(new AnvilAura());
         register(new AutoChase());
         register(new AutoCity());
         register(new AutoCityRewrite());
         register(new AutoMend());
         register(new AutoPush());
         register(new AutoSelfFill());
         register(new AutoTotem());
         register(new BedAura());
         register(new Blocker());
         register(new BowSpam());
         register(new CevBreaker());
         register(new CivBreaker());
         register(new CrystalAura());
         register(new HoleFill());
         register(new PistonAura());
         register(new Quiver());
         register(new SelfAnvil());
         register(new TPAura());
         register(new Velocity());
         register(new Burrow());
         register(new CornerClip());
         register(new InstantMine());
         register(new NoMiningTrace());
         register(new PacketCanceller());
         register(new PingBypass());
         register(new SilentPickel());
         register(new TrapPhase());
         register(new XCarry());
         register(new AntiMapBan());
         register(new AntiUnicode());
         register(new AutoDrop());
         register(new AutoEZ());
         register(new ArmorAlert());
         register(new BaseFinder());
         register(new BuildRandom());
         register(new ChatLag());
         register(new ChatSuffix());
         register(new DiscordRPC());
         register(new DonkeyNotifier());
         register(new FakePlayer());
         register(new HoleBreakAlert());
         register(new InstantWither());
         register(new PopAnnouncer());
         register(new Refill());
         register(new Spammer());
         register(new AntiHunger());
         register(new Blink());
         register(new BlockMove());
         register(new HoleTP());
         register(new FastStop());
         register(new Follow());
         register(new NoSlowdown());
         register(new PhaseWalk());
         register(new PhaseFly());
         register(new ReverseStep());
         register(new Step());
         register(new Flight());
         register(new Speed());
         register(new TPCart());
         register(new Afterglow());
         register(new Animation());
         register(new BreadCrumbs());
         register(new BurrowESP());
         register(new CandyCrystal());
         register(new CityESP());
         register(new ClickGUI());
         register(new CrystalSpawns());
         register(new CustomFov());
         register(new EnchantmentColor());
         register(new Freecam());
         register(new HoleESP());
         register(new HUDEditor());
         register(new ItemViewModel());
         register(new NoOverlay());
         register(new Notification());
         register(new SmallShield());
         register(new Watermark());
         register(new Watermark2());
         register(new PvPResources());
         register(new PlayerList());
         register(new CombatInfo());
         register(new PlayerModel());
         register(new TargetHUD());
         register(new ModuleList());
         register(new InventoryViewer());
         register(new Welcomer());
     }
     public void register(Module module) {
         this.modules.add(module);
     }
     public void onUpdate() {
         this.modules.stream().filter(m -> m.isEnable).forEach(Module::onUpdate);
     }
     public void onTick() {
         this.modules.stream().filter(m -> m.isEnable).forEach(Module::onTick);
     }
     public void onConnect() {
         this.modules.stream().filter(m -> m.isEnable).forEach(Module::onConnect);
     }
     public void onRender2D() {
         this.modules.stream().filter(m -> m.isEnable).forEach(Module::onRender2D);
     }
     public void onRender3D() {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> {
                     mc.profiler.startSection(m.name);
                     m.onRender3D();
                     mc.profiler.endSection();
                 });
     }
     public void onRender3D(float ticks) {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> {
                     mc.profiler.startSection(m.name);
                     m.onRender3D(ticks);
                     mc.profiler.endSection();
                 });
     }
     public void onPacketSend(PacketEvent.Send event) {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> m.onPacketSend(event));
     }
     public void onPacketReceive(PacketEvent.Receive event) {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> m.onPacketReceive(event));
     }
     public void onTotemPop(EntityPlayer player) {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> m.onTotemPop(player));
     }
     public void onPlayerDeath(PlayerDeathEvent event) {
         this.modules.stream().filter(m -> m.isEnable).forEach(m -> m.onPlayerDeath(event));
     }
     public void onKeyInput(int key) {
         this.modules.stream().filter(m -> (m.key.getKey() == key)).forEach(Module::toggle);
     }
     public List<Module> getModulesWithCategories(Module.Categories c) {
         List<Module> moduleList = new ArrayList<>();
         for (Module m : this.modules) {
             if (m.category == c) {
                 moduleList.add(m);
             }
         }
         return moduleList;
     }
     public Module getModuleWithName(String name) {
         Module r = null;
         for (Module m : this.modules) {
             if (m.name.equalsIgnoreCase(name)) {
                 r = m;
             }
         }
         return r;
     }
     public Module getModuleWithClass(Class<? extends Module> clazz) {
         Module r = null;
         for (Module m : this.modules) {
             if (m.getClass() == clazz) {
                 r = m;
             }
         }
         return r;
     }
     public static Collection<Module> getModules() {
         return modulesClassMap.values();
     }
     public static ArrayList<Module> getModulesInCategory(Module.Categories category) {
         ArrayList<Module> list = new ArrayList<>();
         for (Module module : modulesClassMap.values()) {
             if (module.category != category)
                 continue;    list.add(module);
         }
         return list;
     }
     public static <T extends Module> T getModule(Class<T> clazz) {
         return (T)modulesClassMap.get(clazz);
     }
     public static Module getModule(String name) {
         if (name == null) return null;
         return modulesNameMap.get(name.toLowerCase(Locale.ROOT));
     }
     public static boolean isModuleEnabled(Class<? extends Module> clazz) {
         Module module = getModule((Class)clazz);
         return (module != null && module.isEnable());
     }
     public static boolean isModuleEnabled(String name) {
         Module module = getModule(name);
         return (module != null && module.isEnable());
     }
     public static final LinkedHashMap<Class<? extends Module>, Module> modulesClassMap = new LinkedHashMap<>();
     public static final LinkedHashMap<String, Module> modulesNameMap = new LinkedHashMap<>();
 }