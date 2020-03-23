package zelix.cc.client.inGameGui.newclickgui.Font;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class PureFontRenderer
extends FontRenderer {
    public final Random fontRandom = new Random();
    private final Color[] customColorCodes = new Color[256];
    private final int[] colorCode = new int[32];
    private PureFont font;
    private PureFont boldFont;
    private PureFont italicFont;
    private PureFont boldItalicFont;
    private String colorcodeIdentifiers = "0123456789abcdefklmnor";
    private boolean bidi;

    public PureFontRenderer(Font font, boolean antiAlias, int charOffset) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.setFont(font, antiAlias, charOffset);
        this.customColorCodes[113] = new Color(0, 90, 163);
        this.colorcodeIdentifiers = this.setupColorcodeIdentifier();
        this.setupMinecraftColorcodes();
        this.FONT_HEIGHT = this.getHeight();
    }

    public int drawString(String s, float x, float y, int color) {
        return this.drawString(s, x, y, color, false);
    }

    @Override
    public int drawStringWithShadow(String s, float x, float y, int color) {
        return this.drawString(s, x, y, color, false);
    }

    public void drawCenteredString(String s, int x, int y, int color, boolean shadow) {
        if (shadow) {
            this.drawStringWithShadow(s, x - this.getStringWidth(s) / 2, y, color);
        } else {
            this.drawString(s, x - this.getStringWidth(s) / 2, y, color);
        }
    }

    public void drawCenteredStringXY(String s, int x, int y, int color, boolean shadow) {
        this.drawCenteredString(s, x, y - this.getHeight() / 2, color, shadow);
    }


    public void drawCenteredString(String s, int x, int y, int color) {
        this.drawStringWithShadow(s, x - this.getStringWidth(s) / 2, y, color);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean shadow) {
        int result = 0;
        String[] lines = text.split("\n");
        int i = 0;
        while (i < lines.length) {
            result = this.drawLine(lines[i], x, y + i * this.getHeight(), color, shadow);
            ++i;
        }
        return result;
    }

    private int drawLine(String text, float x, float y, int color, boolean shadow) {
        if (text == null) {
            return 0;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x - 1.5, y + 0.5, 0.0);
        boolean wasBlend = GL11.glGetBoolean(3042);
        GlStateManager.enableAlpha();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3553);
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }
        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        float alpha = (color >> 24 & 255) / 255.0f;
        Color c = new Color(red, green, blue, alpha);
        if (text.contains("\u00a7")) {
            String[] parts = text.split("\u00a7");
            Color currentColor = c;
            PureFont currentFont = this.font;
            int width = 0;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            int index = 0;
            while (index < parts.length) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        currentFont.drawString(parts[index], width, 0.0, currentColor, shadow);
                        width += currentFont.getStringWidth(parts[index]);
                    } else {
                        String words = parts[index].substring(1);
                        char type = parts[index].charAt(0);
                        int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                int colorcode = this.colorCode[colorIndex];
                                currentColor = this.getColor(colorcode, alpha);
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikethrough = false;
                            } else if (colorIndex == 16) {
                                randomCase = true;
                            } else if (colorIndex == 17) {
                                bold = true;
                            } else if (colorIndex == 18) {
                                strikethrough = true;
                            } else if (colorIndex == 19) {
                                underline = true;
                            } else if (colorIndex == 20) {
                                italic = true;
                            } else if (colorIndex == 21) {
                                bold = false;
                                italic = false;
                                randomCase = false;
                                underline = false;
                                strikethrough = false;
                                currentColor = c;
                            } else if (colorIndex > 21) {
                                Color customColor = this.customColorCodes[type];
                                currentColor = new Color(customColor.getRed() / 255.0f, customColor.getGreen() / 255.0f, customColor.getBlue() / 255.0f, alpha);
                            }
                        }
                        if (bold && italic) {
                            this.boldItalicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.boldItalicFont;
                        } else if (bold) {
                            this.boldFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.boldFont;
                        } else if (italic) {
                            this.italicFont.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.italicFont;
                        } else {
                            this.font.drawString(randomCase ? this.toRandom(currentFont, words) : words, width, 0.0, currentColor, shadow);
                            currentFont = this.font;
                        }
                        float u = this.font.getHeight() / 16.0f;
                        int h = currentFont.getStringHeight(words);
                        if (strikethrough) {
                            this.drawLine(width / 2.0 + 1.0, h / 3, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 3, u);
                        }
                        if (underline) {
                            this.drawLine(width / 2.0 + 1.0, h / 2, (width + currentFont.getStringWidth(words)) / 2.0 + 1.0, h / 2, u);
                        }
                        width += currentFont.getStringWidth(words);
                    }
                }
                ++index;
            }
        } else {
            this.font.drawString(text, 0.0, 0.0, c, shadow);
        }
        if (!wasBlend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        return (int)(x + this.getStringWidth(text));
    }

    private String toRandom(PureFont font, String text) {
        String newText = "";
        String allowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
        char[] arrc = text.toCharArray();
        int n = arrc.length;
        int n2 = 0;
        while (n2 < n) {
            char c = arrc[n2];
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                int index = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
                newText = String.valueOf(newText) + "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".toCharArray()[index];
            }
            ++n2;
        }
        return newText;
    }

    public int getStringHeight(String text) {
        if (text == null) {
            return 0;
        }
        return this.font.getStringHeight(text) / 2;
    }

    public int getHeight() {
        return this.font.getHeight() / 2;
    }

    public static String getFormatFromString(String p_78282_0_) {
        String var1 = "";
        int var2 = -1;
        int var3 = p_78282_0_.length();
        while ((var2 = p_78282_0_.indexOf(167, var2 + 1)) != -1) {
            if (var2 >= var3 - 1) {
                continue;
            }
            char var4 = p_78282_0_.charAt(var2 + 1);
            if (PureFontRenderer.isFormatColor(var4)) {
                var1 = "\u00a7" + var4;
                continue;
            }
            if (!PureFontRenderer.isFormatSpecial(var4)) {
                continue;
            }
            var1 = String.valueOf(var1) + "\u00a7" + var4;
        }
        return var1;
    }

    private static boolean isFormatSpecial(char formatChar) {
        if (!(formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R')) {
            return false;
        }
        return true;
    }

    @Override
    public int getColorCode(char p_175064_1_) {
        return this.colorCode["0123456789abcdef".indexOf(p_175064_1_)];
    }

    @Override
    public void setBidiFlag(boolean state) {
        this.bidi = state;
    }

    @Override
    public boolean getBidiFlag() {
        return this.bidi;
    }

    private int sizeStringToWidth(String str, int wrapWidth) {
        int var3 = str.length();
        int var4 = 0;
        int var5 = 0;
        int var6 = -1;
        boolean var7 = false;
        while (var5 < var3) {
            char var8 = str.charAt(var5);
            switch (var8) {
                case '\n': {
                    --var5;
                    break;
                }
                case '\u00a7': {
                    char var9;
                    if (var5 >= var3 - 1) {
                        break;
                    }
                    if ((var9 = str.charAt(++var5)) != 'l' && var9 != 'L') {
                        if (var9 != 'r' && var9 != 'R' && !PureFontRenderer.isFormatColor(var9)) {
                            break;
                        }
                        var7 = false;
                        break;
                    }
                    var7 = true;
                    break;
                }
                case ' ': {
                    var6 = var5;
                }
                default: {
                    var4 += this.getStringWidth(Character.toString(var8));
                    if (!var7) {
                        break;
                    }
                    ++var4;
                }
            }
            if (var8 == '\n') {
                var6 = ++var5;
                break;
            }
            if (var4 > wrapWidth) {
                break;
            }
            ++var5;
        }
        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    private static boolean isFormatColor(char colorChar) {
        if (!(colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F')) {
            return false;
        }
        return true;
    }

    @Override
    public int getCharWidth(char c) {
        return this.getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        if (text.contains("\u00a7")) {
            String[] parts = text.split("\u00a7");
            PureFont currentFont = this.font;
            int width = 0;
            boolean bold = false;
            boolean italic = false;
            int index = 0;
            while (index < parts.length) {
                if (parts[index].length() > 0) {
                    if (index == 0) {
                        width += currentFont.getStringWidth(parts[index]);
                    } else {
                        String words = parts[index].substring(1);
                        char type = parts[index].charAt(0);
                        int colorIndex = this.colorcodeIdentifiers.indexOf(type);
                        if (colorIndex != -1) {
                            if (colorIndex < 16) {
                                bold = false;
                                italic = false;
                            } else if (colorIndex != 16) {
                                if (colorIndex == 17) {
                                    bold = true;
                                } else if (colorIndex != 18 && colorIndex != 19) {
                                    if (colorIndex == 20) {
                                        italic = true;
                                    } else if (colorIndex == 21) {
                                        bold = false;
                                        italic = false;
                                    }
                                }
                            }
                        }
                        currentFont = bold && italic ? this.boldItalicFont : (bold ? this.boldFont : (italic ? this.italicFont : this.font));
                        width += currentFont.getStringWidth(words);
                    }
                }
                ++index;
            }
            return width / 2;
        }
        return this.font.getStringWidth(text) / 2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setFont(Font font, boolean antiAlias, int charOffset) {
        PureFontRenderer pureFontRenderer = this;
        synchronized (pureFontRenderer) {
            this.font = new PureFont(font, antiAlias, charOffset);
            this.boldFont = new PureFont(font.deriveFont(1), antiAlias, charOffset);
            this.italicFont = new PureFont(font.deriveFont(2), antiAlias, charOffset);
            this.boldItalicFont = new PureFont(font.deriveFont(3), antiAlias, charOffset);
            this.FONT_HEIGHT = this.getHeight();
        }
    }

    public PureFont getFont() {
        return this.font;
    }

    public String getFontName() {
        return this.font.getFont().getFontName();
    }

    public int getSize() {
        return this.font.getFont().getSize();
    }

    public List<String> wrapWords(String text, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        if (this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            String[] arrstring = words;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String word = arrstring[n2];
                int i = 0;
                while (i < word.toCharArray().length) {
                    char c = word.toCharArray()[i];
                    if (c == '\u00a7' && i < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i + 1];
                    }
                    ++i;
                }
                if (this.getStringWidth(String.valueOf(currentWord) + word + " ") < width) {
                    currentWord = String.valueOf(currentWord) + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = lastColorCode == -1 ? String.valueOf(word) + " " : "\u00a7" + lastColorCode + word + " ";
                }
                ++n2;
            }
            if (!currentWord.equals("")) {
                if (this.getStringWidth(currentWord) < width) {
                    finalWords.add(lastColorCode == -1 ? String.valueOf(currentWord) + " " : "\u00a7" + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    for (String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String s, double width) {
        ArrayList<String> finalWords = new ArrayList<String>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        int i = 0;
        while (i < s.toCharArray().length) {
            char c = s.toCharArray()[i];
            if (c == '\u00a7' && i < s.toCharArray().length - 1) {
                lastColorCode = s.toCharArray()[i + 1];
            }
            if (this.getStringWidth(String.valueOf(currentWord) + c) < width) {
                currentWord = String.valueOf(currentWord) + c;
            } else {
                finalWords.add(currentWord);
                currentWord = lastColorCode == -1 ? String.valueOf(c) : "\u00a7" + lastColorCode + String.valueOf(c);
            }
            ++i;
        }
        if (!currentWord.equals("")) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public boolean isAntiAliasing() {
        if (this.font.isAntiAlias() && this.boldFont.isAntiAlias() && this.italicFont.isAntiAlias() && this.boldItalicFont.isAntiAlias()) {
            return true;
        }
        return false;
    }

    public void setAntiAliasing(boolean antiAlias) {
        this.font.setAntiAlias(antiAlias);
        this.boldFont.setAntiAlias(antiAlias);
        this.italicFont.setAntiAlias(antiAlias);
        this.boldItalicFont.setAntiAlias(antiAlias);
    }

    private void setupMinecraftColorcodes() {
        int index = 0;
        while (index < 32) {
            int var6 = (index >> 3 & 1) * 85;
            int var7 = (index >> 2 & 1) * 170 + var6;
            int var8 = (index >> 1 & 1) * 170 + var6;
            int var9 = (index >> 0 & 1) * 170 + var6;
            if (index == 6) {
                var7 += 85;
            }
            if (index >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }
            this.colorCode[index] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
            ++index;
        }
    }

    @Override
    public String trimStringToWidth(String p_78269_1_, int p_78269_2_) {
        return this.trimStringToWidth(p_78269_1_, p_78269_2_, false);
    }

    @Override
    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
        StringBuilder var4 = new StringBuilder();
        int var5 = 0;
        int var6 = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
        int var7 = p_78262_3_ ? -1 : 1;
        boolean var8 = false;
        boolean var9 = false;
        int var10 = var6;
        while (var10 >= 0 && var10 < p_78262_1_.length() && var5 < p_78262_2_) {
            char var11 = p_78262_1_.charAt(var10);
            int var12 = this.getStringWidth(Character.toString(var11));
            if (var8) {
                var8 = false;
                if (var11 != 'l' && var11 != 'L') {
                    if (var11 == 'r' || var11 == 'R') {
                        var9 = false;
                    }
                } else {
                    var9 = true;
                }
            } else if (var12 < 0) {
                var8 = true;
            } else {
                var5 += var12;
                if (var9) {
                    ++var5;
                }
            }
            if (var5 > p_78262_2_) {
                break;
            }
            if (p_78262_3_) {
                var4.insert(0, var11);
            } else {
                var4.append(var11);
            }
            var10 += var7;
        }
        return var4.toString();
    }

    @Override
    public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    public String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int var3 = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= var3) {
            return str;
        }
        String var4 = str.substring(0, var3);
        char var5 = str.charAt(var3);
        boolean var6 = var5 == ' ' || var5 == '\n';
        String var7 = String.valueOf(PureFontRenderer.getFormatFromString(var4)) + str.substring(var3 + (var6 ? 1 : 0));
        return String.valueOf(var4) + "\n" + this.wrapFormattedStringToWidth(var7, wrapWidth);
    }

    public Color getColor(int colorCode, float alpha) {
        return new Color((colorCode >> 16) / 255.0f, (colorCode >> 8 & 255) / 255.0f, (colorCode & 255) / 255.0f, alpha);
    }

    private String setupColorcodeIdentifier() {
        String minecraftColorCodes = "0123456789abcdefklmnor";
        int i = 0;
        while (i < this.customColorCodes.length) {
            if (this.customColorCodes[i] != null) {
                minecraftColorCodes = String.valueOf(minecraftColorCodes) + (char)i;
            }
            ++i;
        }
        return minecraftColorCodes;
    }

    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
    }
}

