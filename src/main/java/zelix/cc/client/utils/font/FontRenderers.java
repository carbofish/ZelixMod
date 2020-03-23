package zelix.cc.client.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class FontRenderers {

    public static void drawCenteredString(final String s, final float f, final float g, final int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(s, f - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) / 2, (float) (g + 1.5), color, false);
    }
}
