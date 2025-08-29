 package as.pw.candee.utils;
 import java.awt.Font;
 import java.util.ArrayList;
 import java.util.List;
 import as.pw.candee.gui.font.CFont;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.texture.DynamicTexture;
 import org.lwjgl.opengl.GL11;
 public class MinecraftFontRenderer
     extends CFont {
     final CharData[] boldChars;
     final CharData[] italicChars;
     final CharData[] boldItalicChars;
     final int[] colorCode;
     final String colorcodeIdentifiers;
     DynamicTexture texBold;
     DynamicTexture texItalic;
     DynamicTexture texItalicBold;
     public MinecraftFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
         super(font, antiAlias, fractionalMetrics);
         this.boldChars = new CharData[256];
         this.italicChars = new CharData[256];
         this.boldItalicChars = new CharData[256];
         this.colorCode = new int[32];
         this.colorcodeIdentifiers = "0123456789abcdefklmnor";
         setupMinecraftColorcodes();
         setupBoldItalicIDs();
     }
     public int drawStringWithShadow(String text, double x2, float y2, int color) {
         float shadowWidth = drawString(text, x2 + 0.8999999761581421D, y2 + 0.5D, color, true, 8.3F);
         return (int)Math.max(shadowWidth, drawString(text, x2, y2, color, false, 8.3F));
     }
     public int drawString(String text, double x2, float y2, int color) {
         return (int)drawString(text, x2, y2, color, false, 8.3F);
     }
     public int drawPassword(String text, double x2, float y2, int color) {
         return (int)drawString(text.replaceAll(".", "."), x2, y2, color, false, 8.0F);
     }
     public int drawNoBSString(String text, double x2, float y2, int color) {
         return (int)drawNoBSString(text, x2, y2, color, false);
     }
     public int drawSmoothString(String text, double x2, float y2, int color) {
         return (int)drawSmoothString(text, x2, y2, color, false);
     }
     public double getPasswordWidth(String text) {
         return getStringWidth(text.replaceAll(".", "."), 8.0F);
     }
     public float drawCenteredString(String text, float x2, float y2, int color) {
         return drawString(text, (x2 - ((double) getStringWidth(text) / 2)), y2, color);
     }
     public float drawNoBSCenteredString(String text, float x2, float y2, int color) {
         return drawNoBSString(text, (x2 - ((double) getStringWidth(text) / 2)), y2, color);
     }
     public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
         return drawStringWithShadow(text, (x2 - ((double) getStringWidth(text) / 2)), y2, color);
     }
     public float drawString(String text, double x, double y, int color, boolean shadow, float kerning) {
         x--;
         if (text == null) {
             return 0.0F;
         }
         if (color == 553648127) {
             color = 16777215;
         }
         if ((color & 0xFC000000) == 0) {
             color |= 0xFF000000;
         }
         if (shadow) {
             color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
         }
         CharData[] currentData = this.charData;
         float alpha = (color >> 24 & 0xFF) / 255.0F;
         boolean randomCase = false;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         boolean render = true;
         x *= 2.0D;
         y = (y - 3.0D) * 2.0D;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(3553, this.tex.getGlTextureId());
         for (int index = 0; index < text.length(); index++) {
             char character = text.charAt(index);
             if (character == '§') {
                 int colorIndex = 21;
                 try {
                     colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                 }
                 if (colorIndex < 16) {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                     if (colorIndex < 0) {
                         colorIndex = 15;
                     }
                     if (shadow) {
                         colorIndex += 16;
                     }
                     int colorcode = this.colorCode[colorIndex];
                     GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
                 }
                 else if (colorIndex == 16) {
                     randomCase = true;
                 }
                 else if (colorIndex == 17) {
                     bold = true;
                     if (italic) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texBold.getGlTextureId());
                         currentData = this.boldChars;
                     }
                 } else if (colorIndex == 18) {
                     strikethrough = true;
                 }
                 else if (colorIndex == 19) {
                     underline = true;
                 }
                 else if (colorIndex == 20) {
                     italic = true;
                     if (bold) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                         currentData = this.italicChars;
                     }
                 } else {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                 }
                 index++;
             }
             else if (character < currentData.length) {
                 GL11.glBegin(4);
                 drawChar(currentData, character, (float)x, (float)y);
                 GL11.glEnd();
                 if (strikethrough) {
                     drawLine(x, y + ((double) (currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((double) (currentData[character]).height / 2), 1.0F);
                 }
                 if (underline) {
                     drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
                 }
                 x += ((currentData[character]).width - kerning + this.charOffset);
             }
         }
         GL11.glHint(3155, 4352);
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (float)x / 2.0F;
     }
     public float drawSmoothString(String text, double x, double y, int color, boolean shadow) {
         x--;
         if (text == null) {
             return 0.0F;
         }
         CharData[] currentData = this.charData;
         float alpha = (color >> 24 & 0xFF) / 255.0F;
         boolean randomCase = false;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         boolean render = true;
         x *= 2.0D;
         y = (y - 3.0D) * 2.0D;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(3553, this.tex.getGlTextureId());
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
         for (int index = 0; index < text.length(); index++) {
             char character = text.charAt(index);
             if (character == '§') {
                 int colorIndex = 21;
                 try {
                     colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                 }
                 if (colorIndex < 16) {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                     if (colorIndex < 0) {
                         colorIndex = 15;
                     }
                     if (shadow) {
                         colorIndex += 16;
                     }
                     int colorcode = this.colorCode[colorIndex];
                     GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
                 }
                 else if (colorIndex == 16) {
                     randomCase = true;
                 }
                 else if (colorIndex == 17) {
                     bold = true;
                     if (italic) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texBold.getGlTextureId());
                         currentData = this.boldChars;
                     }
                 } else if (colorIndex == 18) {
                     strikethrough = true;
                 }
                 else if (colorIndex == 19) {
                     underline = true;
                 }
                 else if (colorIndex == 20) {
                     italic = true;
                     if (bold) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                         currentData = this.italicChars;
                     }
                 } else {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                 }
                 index++;
             }
             else if (character < currentData.length) {
                 GL11.glBegin(4);
                 drawChar(currentData, character, (float)x, (float)y);
                 GL11.glEnd();
                 if (strikethrough) {
                     drawLine(x, y + ((double) (currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((double) (currentData[character]).height / 2), 1.0F);
                 }
                 if (underline) {
                     drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
                 }
                 x += ((currentData[character]).width - 8.3F + this.charOffset);
             }
         }
         GL11.glHint(3155, 4352);
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (float)x / 2.0F;
     }
     public float drawNoBSString(String text, double x, double y, int color, boolean shadow) {
         x--;
         if (text == null) {
             return 0.0F;
         }
         CharData[] currentData = this.charData;
         float alpha = (color >> 24 & 0xFF) / 255.0F;
         boolean randomCase = false;
         boolean bold = false;
         boolean italic = false;
         boolean strikethrough = false;
         boolean underline = false;
         boolean render = true;
         x *= 2.0D;
         y = (y - 3.0D) * 2.0D;
         GL11.glPushMatrix();
         GlStateManager.scale(0.5D, 0.5D, 0.5D);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(770, 771);
         GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(this.tex.getGlTextureId());
         GL11.glBindTexture(3553, this.tex.getGlTextureId());
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
         for (int index = 0; index < text.length(); index++) {
             char character = text.charAt(index);
             if (character == '§') {
                 int colorIndex = 21;
                 try {
                     colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                 }
                 if (colorIndex < 16) {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                     if (colorIndex < 0) {
                         colorIndex = 15;
                     }
                     if (shadow) {
                         colorIndex += 16;
                     }
                     int colorcode = this.colorCode[colorIndex];
                     GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
                 }
                 else if (colorIndex == 16) {
                     randomCase = true;
                 }
                 else if (colorIndex == 17) {
                     bold = true;
                     if (italic) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texBold.getGlTextureId());
                         currentData = this.boldChars;
                     }
                 } else if (colorIndex == 18) {
                     strikethrough = true;
                 }
                 else if (colorIndex == 19) {
                     underline = true;
                 }
                 else if (colorIndex == 20) {
                     italic = true;
                     if (bold) {
                         GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                         currentData = this.boldItalicChars;
                     } else {
                         GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                         currentData = this.italicChars;
                     }
                 } else {
                     bold = false;
                     italic = false;
                     randomCase = false;
                     underline = false;
                     strikethrough = false;
                     GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
                     GlStateManager.bindTexture(this.tex.getGlTextureId());
                     currentData = this.charData;
                 }
                 index++;
             }
             else if (character < currentData.length) {
                 GL11.glBegin(4);
                 drawChar(currentData, character, (float)x, (float)y);
                 GL11.glEnd();
                 if (strikethrough) {
                     drawLine(x, y + ((double) (currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((double) (currentData[character]).height / 2), 1.0F);
                 }
                 if (underline) {
                     drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
                 }
                 x += ((currentData[character]).width - 8.3F + this.charOffset);
             }
         }
         GL11.glHint(3155, 4352);
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return (float)x / 2.0F;
     }
     public int getStringWidth(String text) {
         if (text == null) {
             return 0;
         }
         float width = 0.0F;
         CharData[] currentData = this.charData;
         boolean bold = false;
         boolean italic = false;
         for (int index = 0; index < text.length(); index++) {
             char character = text.charAt(index);
             if (character == '§') {
                 int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                 bold = false;
                 italic = false;
                 index++;
             }
             else if (character < currentData.length) {
                 width += (currentData[character]).width - 8.3F + this.charOffset;
             }
         }
         return (int)(width / 2.0F);
     }
     public double getStringWidth(String text, float kerning) {
         if (text == null) {
             return 0.0D;
         }
         float width = 0.0F;
         CharData[] currentData = this.charData;
         boolean bold = false;
         boolean italic = false;
         for (int index = 0; index < text.length(); index++) {
             char c = text.charAt(index);
             if (c == '§') {
                 int colorIndex = "0123456789abcdefklmnor".indexOf(c);
                 bold = false;
                 italic = false;
                 index++;
             }
             else if (c < currentData.length) {
                 width += (currentData[c]).width - kerning + this.charOffset;
             }
         }
         return (width / 2.0F);
     }

     public void setFont(Font font) {
         super.setFont(font);
         setupBoldItalicIDs();
     }
     public void setAntiAlias(boolean antiAlias) {
         super.setAntiAlias(antiAlias);
         setupBoldItalicIDs();
     }
     public void setFractionalMetrics(boolean fractionalMetrics) {
         super.setFractionalMetrics(fractionalMetrics);
         setupBoldItalicIDs();
     }
     private void setupBoldItalicIDs() {
         this.texBold = setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
         this.texItalic = setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
         this.texItalicBold = setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
     }
     private void drawLine(double x2, double y2, double x1, double y1, float width) {
         GL11.glDisable(3553);
         GL11.glLineWidth(width);
         GL11.glBegin(1);
         GL11.glVertex2d(x2, y2);
         GL11.glVertex2d(x1, y1);
         GL11.glEnd();
         GL11.glEnable(3553);
     }
     public List<String> wrapWords(String text, double width) {
         ArrayList<String> finalWords = new ArrayList<>();
         if (getStringWidth(text) > width) {
             String[] words = text.split(" ");
             StringBuilder currentWord = new StringBuilder();
             char lastColorCode = Character.MAX_VALUE;
             for (String word : words) {
                 for (int innerIndex = 0; innerIndex < word.length(); innerIndex++) {
                     char c = word.toCharArray()[innerIndex];
                     if (c == '§' && innerIndex < word.length() - 1) {
                         lastColorCode = word.toCharArray()[innerIndex + 1];
                     }
                 }
                 if (getStringWidth(currentWord + word + " ") < width) {
                     currentWord.append(word).append(" ");
                 } else {
                     finalWords.add(currentWord.toString());
                     currentWord = new StringBuilder("§" + lastColorCode + word + " ");
                 }
             }
             if (currentWord.length() > 0) {
                 if (getStringWidth(currentWord.toString()) < width) {
                     finalWords.add("§" + lastColorCode + currentWord + " ");
                     currentWord = new StringBuilder();
                 } else {
                     finalWords.addAll(formatString(currentWord.toString(), width));
                 }
             }
         } else {
             finalWords.add(text);
         }
         return finalWords;
     }
     public List<String> formatString(String string, double width) {
         ArrayList<String> finalWords = new ArrayList<>();
         StringBuilder currentWord = new StringBuilder();
         char lastColorCode = Character.MAX_VALUE;
         char[] chars = string.toCharArray();
         for (int index = 0; index < chars.length; index++) {
             char c = chars[index];
             if (c == '§' && index < chars.length - 1) {
                 lastColorCode = chars[index + 1];
             }
             if (getStringWidth(currentWord.toString() + c) < width) {
                 currentWord.append(c);
             } else {
                 finalWords.add(currentWord.toString());
                 currentWord = new StringBuilder("§" + lastColorCode + c);
             }
         }
         if (currentWord.length() > 0) {
             finalWords.add(currentWord.toString());
         }
         return finalWords;
     }
     private void setupMinecraftColorcodes() {
         for (int index = 0; index < 32; index++) {
             int noClue = (index >> 3 & 0x1) * 85;
             int red = (index >> 2 & 0x1) * 170 + noClue;
             int green = (index >> 1 & 0x1) * 170 + noClue;
             int blue = (index & 0x1) * 170 + noClue;
             if (index == 6) {
                 red += 85;
             }
             if (index >= 16) {
                 red /= 4;
                 green /= 4;
                 blue /= 4;
             }
             this.colorCode[index] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
         }
     }
     public String trimStringToWidth(String text, int width) {
         return trimStringToWidth(text, width, false);
     }
     public String trimStringToWidthPassword(String text, int width, boolean custom) {
         text = text.replaceAll(".", ".");
         return trimStringToWidth(text, width, custom);
     }
     private float getCharWidthFloat(char c) {
         if (c == '§') {
             return -1.0F;
         }
         if (c == ' ') {
             return 2.0F;
         }
         int var2 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000".indexOf(c);
         if (c > '\000' && var2 != -1) {
             return (this.charData[var2]).width / 2.0F - 4.0F;
         }
         if ((this.charData[c]).width / 2.0F - 4.0F != 0.0F) {
             int var3 = (int)((this.charData[c]).width / 2.0F - 4.0F) >>> 4;
             int var4 = (int)((this.charData[c]).width / 2.0F - 4.0F) & 0xF;
             var3 &= 0xF;
             return ((float) (++var4 - var3) / 2 + 1);
         }
         return 0.0F;
     }
     public String trimStringToWidth(String text, int width, boolean custom) {
         StringBuilder buffer = new StringBuilder();
         float lineWidth = 0.0F;
         int offset = custom ? (text.length() - 1) : 0;
         int increment = custom ? -1 : 1;
         boolean var8 = false;
         boolean var9 = false; int index;
         for (index = offset; index >= 0 && index < text.length() && lineWidth < width; index += increment) {
             char character = text.charAt(index);
             float charWidth = getCharWidthFloat(character);
             if (var8) {
                 var8 = false;
                 if (character != 'l' && character != 'L') {
                     if (character == 'r' || character == 'R') {
                         var9 = false;
                     }
                 } else {
                     var9 = true;
                 }
             } else if (charWidth < 0.0F) {
                 var8 = true;
             } else {
                 lineWidth += charWidth;
                 if (var9) {
                     lineWidth++;
                 }
             }
             if (lineWidth > width) {
                 break;
             }
             if (custom) {
                 buffer.insert(0, character);
             } else {
                 buffer.append(character);
             }
         }
         return buffer.toString();
     }
 }