 package as.pw.candee.managers;
 import java.awt.Color;
 import java.awt.Font;
 import java.io.InputStream;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.Objects;
 import as.pw.candee.gui.font.CFontRenderer;
 public class FontManager
     extends Manager
 {
     public CFontRenderer iconFont;
     public Font fontRenderer_;
     public Font iconFont_;
     public CFontRenderer fontRenderer;
     private static Font getFont(Map<String, Font> locationMap, String location, int size) {
         Font font;
         try {
             if (locationMap.containsKey(location)) {
                 font = locationMap.get(location).deriveFont(0, size);
             } else {
                 InputStream is = FontManager.class.getResourceAsStream("/assets/candeeplusrewrite/fonts/" + location);
                 font = Font.createFont(0, Objects.requireNonNull(is));
                 locationMap.put(location, font);
                 font = font.deriveFont(0, size);
             }
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("Error loading font: " + location);
             font = new Font("default", 0, 18);
         }
         return font;
     }
     public void load() {
         Map<String, Font> locationmap = new HashMap<>();
         this.fontRenderer_ = getFont(locationmap, "LD.ttf", 15);
         this.iconFont_ = getFont(locationmap, "Comfortaa-Bold.ttf", 25);
         this.fontRenderer = new CFontRenderer(this.fontRenderer_, true, true);
         this.iconFont = new CFontRenderer(this.iconFont_, true, true);
     }
     public int getWidth(String str) {
         return this.fontRenderer.getStringWidth(str);
     }
     public int getHeight() {
         return this.fontRenderer.getHeight() + 2;
     }
     public void draw(String str, int x, int y, int color, float scale) {
         this.fontRenderer.drawString(str, x, y, color, scale);
     }
     public void draw(String str, int x, int y, Color color, float scale) {
         this.fontRenderer.drawString(str, x, y, color.getRGB(), scale);
     }
     public int getIconWidth() {
         return this.iconFont.getStringWidth("q");
     }
     public int getIconHeight() {
         return this.iconFont.getHeight();
     }
     public void drawIcon(int x, int y, int color, float scale) {
         this.iconFont.drawString("q", x, y, color, scale);
     }
     public void drawIcon(int x, int y, Color color, float scale) {
         this.iconFont.drawString("+", x, y, color.getRGB(), scale);
     }
 }