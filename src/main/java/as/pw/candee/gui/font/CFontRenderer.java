package as.pw.candee.gui.font;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
    protected final CharData[] boldChars;
    protected final CharData[] italicChars;
    protected final CharData[] boldItalicChars;
    DynamicTexture texBold;
    DynamicTexture texItalic;
    DynamicTexture texItalicBold;
    private final int[] colorCode;
    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.colorCode = new int[32];
        setupMinecraftColorcodes();
        setupBoldItalicIDs();
    }
    public CFontRenderer(CustomFont font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.colorCode = new int[32];
        setupMinecraftColorcodes();
        setupBoldItalicIDs();
    }
    public float drawStringWithShadow(String text, double x, double y, int color, float scale) {
        float shadowWidth = drawString(text, x + 1.0D, y + 1.0D, color, true, scale);
        return Math.max(shadowWidth, drawString(text, x, y, color, false, scale));
    }
    public float drawString(String text, float x, float y, int color, float scale) {
        return drawString(text, x, y, color, false, scale);
    }
    public float drawCenteredString(String text, float x, float y, int color, float scale) {
        return drawString(text, x - ((float) getStringWidth(text) / 2), y, color, scale);
    }
    public float drawCenteredStringWithShadow(String text, float x, float y, int color, float scale) {
        float shadowWidth = drawString(text, (x - ((double) getStringWidth(text) / 2)) + 1.0D, y + 1.0D, color, true, scale);
        return drawString(text, x - ((float) getStringWidth(text) / 2), y, color, scale);
    }
    public float drawString(String text, double x, double y, int color, boolean shadow, float scale) {
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
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean render = true;
        x *= 2.0D;
        y = (y - 3.0D) * 2.0D;
        GL11.glPushMatrix();
        GL11.glShadeModel(7425);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
        int size = text.length();
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GL11.glBindTexture(3553, this.tex.getGlTextureId());
        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if (character == '§' && i < size) {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                    if (colorIndex < 0 || colorIndex > 15) {
                        colorIndex = 15;
                    }
                    if (shadow) {
                        colorIndex += 16;
                    }
                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F, (colorcode & 0xFF) / 255.0F, alpha);
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        if (this.texItalicBold != null) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                    } else if (this.texBold != null) {
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
                        if (this.texItalicBold != null) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                    } else if (this.texBold != null) {
                        GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                        currentData = this.italicChars;
                    }
                } else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, alpha);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                }
                i++;
            }
            else if (character < currentData.length && character >= '\000') {
                GL11.glBegin(4);
                drawChar(currentData, character, (float)x, (float)y);
                GL11.glEnd();
                if (strikethrough) {
                    drawLine(x, y + ((double) (currentData[character]).height / 2), x + (currentData[character]).width - 8.0D, y + ((double) (currentData[character]).height / 2), 1.0F);
                }
                if (underline) {
                    drawLine(x, y + (currentData[character]).height - 2.0D, x + (currentData[character]).width - 8.0D, y + (currentData[character]).height - 2.0D, 1.0F);
                }
                x += (((currentData[character]).width - 8 + this.charOffset) * scale);
            }
        }
        GlStateManager.disableBlend();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glShadeModel(7424);
        GL11.glDisable(2848);
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        return (float)x / 2.0F;
    }
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; i++) {
            char character = text.charAt(i);
            if (character == '§') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    currentData = italic ? this.boldItalicChars : this.boldChars;
                }
                else if (colorIndex == 20) {
                    italic = true;
                    currentData = bold ? this.boldItalicChars : this.italicChars;
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                i++;
            }
            else if (character < currentData.length && character >= '\000') {
                width += (currentData[character]).width - 8 + this.charOffset;
            }
        }
        return width / 2;
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
    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }
    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<>();
        if (getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = Character.MAX_VALUE;
            for (String word : words) {
                for (int i = 0; i < word.length(); i++) {
                    char c = word.toCharArray()[i];
                    if (c == '§' && i < word.length() - 1) {
                        lastColorCode = word.toCharArray()[i + 1];
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                if (getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width) {
                    currentWord = currentWord + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = "§" + lastColorCode + word + " ";
                }
            }
            if (!currentWord.isEmpty()) {
                if (getStringWidth(currentWord) < width) {
                    finalWords.add("§" + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    finalWords.addAll(formatString(currentWord, width));
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }
    public List<String> formatString(String string, double width) {
        ArrayList<String> finalWords = new ArrayList<>();
        String currentWord = "";
        char lastColorCode = Character.MAX_VALUE;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
            } else {
                finalWords.add(currentWord);
                currentWord = "§" + lastColorCode + c;
            }
        }
        if (!currentWord.isEmpty()) {
            finalWords.add(currentWord);
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
}