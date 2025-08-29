 package as.pw.candee.module.render;
 import as.pw.candee.module.Module;
 public class HUDEditor
     extends Module
 {
     public HUDEditor() {
         super("HUDEditor", Categories.RENDER, false, false);
         instance = this;
     }
     public static HUDEditor instance = null;
 }