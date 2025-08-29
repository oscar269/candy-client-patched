 package as.pw.candee.module.render;
 import java.awt.Color;
 import as.pw.candee.gui.clickguis.CGui;
import as.pw.candee.gui.clickguis.clickgui.CandyGUI;
 import as.pw.candee.gui.clickguis.clickguinew.CandyGUI2;
 import as.pw.candee.module.Module;
 import as.pw.candee.setting.Setting;
 public class ClickGUI
     extends Module {
     private final Setting<type> guiType;
     public final Setting<Color> color;
     public final Setting<Boolean> outline;
     public ClickGUI() {
         super("ClickGUI", Categories.RENDER, 21, false, false);
         this.guiType = register(new Setting("Type", type.New));
         this.color = register(new Setting("Color", new Color(210, 0, 130, 255), v -> (this.guiType.getValue() != type.Old)));
         this.outline = register(new Setting("Outline", Boolean.FALSE, v -> (this.guiType.getValue() == type.New)));
     }
     public void onEnable() {
         if (nullCheck()) {
             return;
         }
         if (!(mc.currentScreen instanceof CGui)) {
             if (this.guiType.getValue() == type.New) {
                 mc.displayGuiScreen(new CandyGUI2());
             } else {
                 mc.displayGuiScreen(new CandyGUI());
             }
         }
     }
     public void onUpdate() {
         if (!(mc.currentScreen instanceof CGui) && !HUDEditor.instance.isEnable) {
             disable();
         }
     }
     public enum type
     {
         Old,
         New
             }
 }