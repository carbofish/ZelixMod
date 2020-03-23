package zelix.cc.client.inGameGui.newclickgui.Font;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import net.utils.slick.SlickException;
import net.utils.slick.UnicodeFont;
import net.utils.slick.font.effects.ColorEffect;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
/**
 * 
 * @author SnowFlake Skidder
 * 
 */
public class UnicodeFontRenderer extends FontRenderer {
	 public HashMap<String, Float> widthMap = new HashMap();//����string�ĳ���
    private final UnicodeFont font;
    
    /**
     * 
     * @param awtFont Loaded Font ttf 
     */
    public UnicodeFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.addGlyphs(22, 65535);
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public int drawString(String text, float x, float y, int color) {
        String[] array;
        text = "\u00a7r" + text;
        float len = -1.0f;
        for (String str : array = text.split("\u00a7")) {
            if (str.length() < 1) {
                continue;
            }
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.drawStringWithOutColor(str, x + len + 0.5f, y + 0.5f, Color.BLACK.getRGB());
            this.drawStringWithOutColor(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()));
            len += this.GetLength(str) + 1;
        }
        return (int)len;
    }
    
    /**
     * 
     * @param string
     * @return �ַ�������
     */
    public int GetLength(String string) {
       if(this.widthMap.containsKey(string)) {
          return this.widthMap.get(string).intValue();
       } else {
          float width = this.font.getWidth(string) / 2;
          this.widthMap.put(string, Float.valueOf(width));
          return (int)width;
       }
    }


	public int getColor(int red, int green, int blue, int alpha) {
        byte color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        color1 |= blue;
        return color1;
     }


	/**
	 * 
	 * @param string ���������
	 * @param x POSX
	 * @param y POSY
	 * @param color ��ɫ
	 * @return X?
	 */
	public int drawStringWithOutColor(String string, float x, float y, final int color) {
		if (string == null) {
            return (int) 0.0f;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean lighting = GL11.glIsEnabled(2896);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        x *= 2.0f;
        y *= 2.0f;
        this.font.drawString(x, y, string, new net.utils.slick.Color(color));
        if (texture) {
            GL11.glEnable(3553);
        }
        if (lighting) {
            GL11.glEnable(2896);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
        return (int)x;
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        this.drawStringWithOutColor(text, x + 1.0f, y + 1.0f, -16777216);
        return this.drawStringWithOutColor(text, x, y, color);
    }

    @Override
    public int getCharWidth(final char c) {
        return this.getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(final String string) {
        return this.font.getWidth(string) / 2;
    }
    /**
     * 
     * @param string
     * @return ��ȡ�߶�
     */
    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }
    /**
     * 
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawStringWithOutColor(text, x - this.getStringWidth(text) / 2, y, color);
    }
}
