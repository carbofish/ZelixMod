package zelix.cc.client.inGameGui.newclickgui.Font;

import java.awt.Font;

import net.minecraft.client.Minecraft;

public class CFontFonts {
    public static final CFontFonts instance = new CFontFonts();
    public CFontRenderer tahomaSM = new CFontRenderer(new Font("Helvetica", 0, 12), true, true);

    public static void renderStringComfortaa(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("tabui");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidthComfortaa(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("tabui");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentredComfortaa(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("tabui");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static void renderStringBariol(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("bariol");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidthBariol(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("bariol");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentredBariol(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("bariol");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static void renderString10(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("10");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidth10(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("10");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentred10(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("10");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static void renderString50(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("40");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidth50(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("40");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentred50(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("40");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static void renderStringtitle(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("title");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidthtitle(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("title");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentredtitle(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("title");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static void renderStringBARON(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("BAROND");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }

    public static float getStringWidthBARON(String text) {
        float width = 0.0f;
        CFontRenderer font = FontManager.getInstance().getFont("BAROND");
        width = font == null ? (float)Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) : (float)font.getStringWidth(text);
        return width;
    }

    public static void renderStringCentredBARON(String text, float x, float y, int color) {
        CFontRenderer font = FontManager.getInstance().getFont("BAROND");
        if (font == null) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        } else {
            font.drawString(text, x, y, color);
        }
    }
}

