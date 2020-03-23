package zelix.cc.client.manager;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.TextureData;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
public class FontManager {
    private FontRendererUtil defaultFont;
    private FontManager instance;
    private HashMap<String, FontRendererUtil> fonts = new HashMap<>();
    
    public FontManager getInstance() {
        return instance;
    }

    public FontRendererUtil getFont(String key) {
        return fonts.getOrDefault(key, defaultFont);
    }

    public FontManager() {
        instance = this;
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
//        defaultFont = new FontRendererUtil(executorService, textureQueue, new Font("Verdana", Font.PLAIN, 18));
        try {
            for (int i : new int[]{10, 12, 14, 16, 18, 20, 22, 24, 26, 28}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/Zelix/fonts/nmsl.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("CONS " + i, new FontRendererUtil(executorService, textureQueue, myFont));
            }
            for (int i : new int[]{10, 12, 14, 16, 18, 20, 22, 24, 26, 28}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/Zelix/fonts/comfortaa-bold.ttf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("COM " + i, new FontRendererUtil(executorService, textureQueue, myFont));
            }
        } catch (Exception ignored) {

        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());

                // Sets the texture parameter stuff.
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Uploads the texture to opengl.
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }
    }
}
