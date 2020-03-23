package zelix.cc.client.inGameGui.newclickgui.Font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    private static FontManager instance;
    private Map<String, CFontRenderer> fontMap = new HashMap<String, CFontRenderer>();
    public static final PureFontRenderer fontRendererGUI;
    public static final PureFontRenderer fontRendererScale;

    static {
        fontRendererGUI = new PureFontRenderer(FontManager.getFont(40), true, 8);
        fontRendererScale = new PureFontRenderer(FontManager.getFont(36), true, 8);
    }

    public FontManager() {
        this.registerFonts();
    }

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    public Map<String, CFontRenderer> getFontMap() {
        return this.fontMap;
    }

    public CFontRenderer getFont(String font) {
        return this.fontMap.get(font);
    }

    private void registerFonts() {
        this.fontMap.put("tabui", new CFontRenderer(new Font("Comfortaa", 0, 18), true, false));
        this.fontMap.put("bariol", new CFontRenderer(new Font("Bariol-Regular", 0, 18), true, false));
        this.fontMap.put("40", new CFontRenderer(new Font("Comfortaa", 0, 40), true, false));
        this.fontMap.put("title", new CFontRenderer(new Font("Comfortaa", 0, 62), true, false));
        this.fontMap.put("10", new CFontRenderer(new Font("Comfortaa", 0, 20), true, false));
        this.fontMap.put("BAROND", new CFontRenderer(new Font("Baron-Neue-Bold", 0, 45), true, false));
    }

    private static Font getFont(int size) {
        Font font = null;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/comfortaa.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        System.out.println("Fonts loading?");
        return font;
    }
}

