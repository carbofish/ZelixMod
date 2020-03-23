/*
 * Decompiled with CFR 0_132.
 */
package zelix.cc.client.inGameGui.newclickgui.Font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontLoaders {
	public CFontRenderer kiona16;
    public CFontRenderer kiona18;
    public CFontRenderer kiona20;
    public CFontRenderer kiona22;
    public CFontRenderer kiona24;
    public CFontRenderer kiona25;
    public CFontRenderer kiona26;
    public CFontRenderer kiona28;
    public CFontRenderer kiona40;

	public CFontRenderer fy16;
    public CFontRenderer fy18;
    public CFontRenderer fy20;
    public CFontRenderer fy22;
    public CFontRenderer fy24;
    public CFontRenderer fy26;
    public CFontRenderer fy28;
    public CFontRenderer fy40;

	public CFontRenderer zeroday16;
    public CFontRenderer zeroday18;
    public CFontRenderer zeroday20;
    public CFontRenderer zeroday22;
    public CFontRenderer zeroday24;
    public CFontRenderer zeroday26;
    public CFontRenderer zeroday28;
    public CFontRenderer zeroday40;

    public CFontRenderer sy12;
    public CFontRenderer sy14;
    public CFontRenderer sy16;
    public CFontRenderer sy18;
    public CFontRenderer sy20;
    public CFontRenderer sy22;
    public CFontRenderer sy24;
    public CFontRenderer sy26;
    public CFontRenderer sy28;
    public CFontRenderer sy30;
    public CFontRenderer sy32;
    public CFontRenderer sy40;

    public UnicodeFontRenderer zw18;
    //public static UnicodeFontRenderer zw28 = new UnicodeFontRenderer(FontLoaders.getWryh2(28));
    //public static UnicodeFontRenderer zw14 = new UnicodeFontRenderer(FontLoaders.getWryh2(14));
    public UnicodeFontRenderer zw24;

    public CFontRenderer pp22;
    public CFontRenderer pp24;

    public void init()  {
        System.out.println("Start Loading Font.....");
        kiona16 = new CFontRenderer(getKiona(16), true, true);;
        kiona18 = new CFontRenderer(getKiona(18), true, true);
        kiona20 = new CFontRenderer(getKiona(20), true, true);
        kiona22 = new CFontRenderer(getKiona(22), true, true);
        kiona24 = new CFontRenderer(getKiona(24), true, true);
        kiona25 = new CFontRenderer(getKiona(25), true, true);
        kiona26 = new CFontRenderer(getKiona(26), true, true);
        kiona28 = new CFontRenderer(getKiona(28), true, true);
        kiona40 =new CFontRenderer(getKiona(40), true, true);
        fy16 = new CFontRenderer(getKiona2(16), true, true);
        fy18 = new CFontRenderer(getKiona2(18), true, true);
        fy20 = new CFontRenderer(getKiona2(20), true, true);
        fy22 = new CFontRenderer(getKiona2(22), true, true);
        fy24 = new CFontRenderer(getKiona2(24), true, true);
        fy26 = new CFontRenderer(getKiona2(26), true, true);
        fy28 = new CFontRenderer(getKiona2(28), true, true);
        fy40 = new CFontRenderer(getKiona2(40), true, true);

        zeroday16 = new CFontRenderer(getKiona1(16), true, true);
        zeroday18 = new CFontRenderer(getKiona1(18), true, true);
        zeroday20 = new CFontRenderer(getKiona1(20), true, true);
        zeroday22 = new CFontRenderer(getKiona1(22), true, true);
        zeroday24 = new CFontRenderer(getKiona1(24), true, true);
        zeroday26 = new CFontRenderer(getKiona1(26), true, true);
        zeroday28 = new CFontRenderer(getKiona1(28), true, true);
        zeroday40 = new CFontRenderer(getKiona1(40), true, true);

        sy12 = new CFontRenderer(getKiona3(12), true, true);
        sy14 = new CFontRenderer(getKiona3(14), true, true);
        sy16 = new CFontRenderer(getKiona3(16), true, true);
        sy18 = new CFontRenderer(getKiona3(18), true, true);
        sy20 = new CFontRenderer(getKiona3(20), true, true);
        sy22 = new CFontRenderer(getKiona3(22), true, true);
        sy24 = new CFontRenderer(getKiona3(24), true, true);
        sy26 = new CFontRenderer(getKiona3(26), true, true);
        sy28 = new CFontRenderer(getKiona3(28), true, true);
        sy30 = new CFontRenderer(getKiona3(28), true, true);
        sy32 = new CFontRenderer(getKiona3(28), true, true);
        sy40 = new CFontRenderer(getKiona3(40), true, true);

        zw18 = new UnicodeFontRenderer(getWryh2(18));
        //static UnicodeFontRenderer zw28 = new UnicodeFontRenderer(getWryh2(28));
        //static UnicodeFontRenderer zw14 = new UnicodeFontRenderer(getWryh2(14));
        zw24 = new UnicodeFontRenderer(getWryh2(24));

        pp22 = new CFontRenderer(getKiona7(22), true, true);
        pp24 = new CFontRenderer(getKiona7(24), true, true);
    }

    private Font getKiona(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/Aldrich-Regular.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }    
    private Font getKiona1(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/cfontbold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private Font getKiona2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/consolasbold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    } 
    private Font getKiona3(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/sy.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    } 
    private Font getKiona7(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/Pirulin.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private Font getWryh2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Eliru/font/sy.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}

