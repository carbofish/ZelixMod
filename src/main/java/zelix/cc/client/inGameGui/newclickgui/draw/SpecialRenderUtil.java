package zelix.cc.client.inGameGui.newclickgui.draw;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

import net.utils.slick.opengl.Texture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Math.Vec2f;
import zelix.cc.client.utils.Math.Vec3f;
import zelix.cc.client.utils.Render.Tessellation;
import zelix.cc.client.utils.Render.gl.GLClientState;
import zelix.cc.injection.interfaces.IMixinEntityRenderer;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;

public class SpecialRenderUtil
{
    private static final Frustum frustum = new Frustum();
    public static float delta;
	public static Minecraft mc = Minecraft.getMinecraft();
    public static final Tessellation tessellator;
    private static final List<Integer> csBuffer;
    private static final Consumer<Integer> ENABLE_CLIENT_STATE;
    private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    
    static {
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<Integer>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }
    
    
    
    public SpecialRenderUtil() {
        super();
    }
	  public static void rectangle(double left, double top, double right, double bottom, int color)
	  {
	    if (left < right)
	    {
	      double var5 = left;
	      left = right;
	      right = var5;
	    }
	    if (top < bottom)
	    {
	      double var5 = top;
	      top = bottom;
	      bottom = var5;
	    }
	    float var11 = (color >> 24 & 0xFF) / 255.0F;
	    float var6 = (color >> 16 & 0xFF) / 255.0F;
	    float var7 = (color >> 8 & 0xFF) / 255.0F;
	    float var8 = (color & 0xFF) / 255.0F;
	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	    GlStateManager.enableBlend();
	    GlStateManager.disableTexture2D();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    GlStateManager.color(var6, var7, var8, var11);
	    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	    worldRenderer.pos(left, bottom, 0.0D).endVertex();
	    worldRenderer.pos(right, bottom, 0.0D).endVertex();
	    worldRenderer.pos(right, top, 0.0D).endVertex();
	    worldRenderer.pos(left, top, 0.0D).endVertex();
	    tessellator.draw();
	    GlStateManager.enableTexture2D();
	    GlStateManager.disableBlend();
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	  }
	    public static Vec3 interpolateRender(EntityPlayer player) {
	    	float part = Helper.getTimer().renderPartialTicks;
	        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * part;
	        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * part;
	        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * part;
	        return new Vec3(interpX, interpY, interpZ);
	    }
	    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
	        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
	    }
	    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
	        GL11.glPushMatrix();
	        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
	        GL11.glEnable(3042);
	        GL11.glBlendFunc(770, 771);
	        GL11.glEnable(2848);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.enableAlpha();
	        GlStateManager.alphaFunc(516, 0.1f);
	        GlStateManager.enableBlend();
	        GlStateManager.blendFunc(770, 771);
	        GL11.glTranslatef(x, y, 0.0f);
	        SpecialRenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
	        GlStateManager.disableAlpha();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.disableLighting();
	        GlStateManager.disableRescaleNormal();
	        GL11.glDisable(2848);
	        GlStateManager.disableBlend();
	        GL11.glPopMatrix();
	    }
	    public static void doGlScissor(int x, int y, int width, int height) {
	        Minecraft mc = Minecraft.getMinecraft();
	        int scaleFactor = 1;
	        int k = mc.gameSettings.guiScale;
	        if (k == 0) {
	            k = 1000;
	        }
	        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
	            ++scaleFactor;
	        }
	        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
	    }
	    public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
	        GL11.glScalef(0.5f, 0.5f, 0.5f);
	        r *= 2.0;
	        cx *= 2;
	        cy *= 2;
	        float f2 = (c >> 24 & 255) / 255.0f;
	        float f22 = (c >> 16 & 255) / 255.0f;
	        float f3 = (c >> 8 & 255) / 255.0f;
	        float f4 = (c & 255) / 255.0f;
	        GL11.glEnable(3042);
	        GL11.glLineWidth(lineWidth);
	        GL11.glDisable(3553);
	        GL11.glEnable(2848);
	        GL11.glBlendFunc(770, 771);
	        GL11.glColor4f(f22, f3, f4, f2);
	        GL11.glBegin(3);
	        int i = segments - part;
	        while (i <= segments) {
	            double x = Math.sin(i * 3.141592653589793 / 180.0) * r;
	            double y = Math.cos(i * 3.141592653589793 / 180.0) * r;
	            GL11.glVertex2d(cx + x, cy + y);
	            ++i;
	        }
	        GL11.glEnd();
	        GL11.glDisable(2848);
	        GL11.glEnable(3553);
	        GL11.glDisable(3042);
	        GL11.glScalef(2.0f, 2.0f, 2.0f);
	    }
	    public static void drawCircle(double x, double y, double radius, int c) {
	        float f2 = (c >> 24 & 255) / 255.0f;
	        float f22 = (c >> 16 & 255) / 255.0f;
	        float f3 = (c >> 8 & 255) / 255.0f;
	        float f4 = (c & 255) / 255.0f;
	        GlStateManager.alphaFunc(516, 0.001f);
	        GlStateManager.color(f22, f3, f4, f2);
	        GlStateManager.enableAlpha();
	        GlStateManager.enableBlend();
	        GlStateManager.disableTexture2D();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        Tessellator tes = Tessellator.getInstance();
	        double i = 0.0;
	        while (i < 360.0) {
	            double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
	            double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
	            GL11.glVertex2d(f3 + x, f4 + y);
	            i += 1.0;
	        }
	        GlStateManager.disableBlend();
	        GlStateManager.disableAlpha();
	        GlStateManager.enableTexture2D();
	        GlStateManager.alphaFunc(516, 0.1f);
	    }
	    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
	        GL11.glPushMatrix();
	        cx *= 2.0F;
	        cy *= 2.0F;
	        float f = (c >> 24 & 0xFF) / 255.0F;
	        float f1 = (c >> 16 & 0xFF) / 255.0F;
	        float f2 = (c >> 8 & 0xFF) / 255.0F;
	        float f3 = (c & 0xFF) / 255.0F;
	        float theta = (float) (6.2831852D / num_segments);
	        float p = (float) Math.cos(theta);
	        float s = (float) Math.sin(theta);
	        float x = r *= 2.0F;
	        float y = 0.0F;
	        enableGL2D();
	        GL11.glScalef(0.5F, 0.5F, 0.5F);
	        GL11.glColor4f(f1, f2, f3, f);
	        GL11.glBegin(2);
	        int ii = 0;
	        while (ii < num_segments) {
	            GL11.glVertex2f(x + cx, y + cy);
	            float t = x;
	            x = p * x - s * y;
	            y = s * t + p * y;
	            ii++;
	        }
	        GL11.glEnd();
	        GL11.glScalef(2.0F, 2.0F, 2.0F);
	        disableGL2D();
	        GlStateManager.color(1, 1, 1, 1);
	        GL11.glPopMatrix();
	    }
	    public static void enableGL2D() {
	        GL11.glDisable(2929);
	        GL11.glEnable(3042);
	        GL11.glDisable(3553);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDepthMask(true);
	        GL11.glEnable(2848);
	        GL11.glHint(3154, 4354);
	        GL11.glHint(3155, 4354);
	    }

	    public static void disableGL2D() {
	        GL11.glEnable(3553);
	        GL11.glDisable(3042);
	        GL11.glEnable(2929);
	        GL11.glDisable(2848);
	        GL11.glHint(3154, 4352);
	        GL11.glHint(3155, 4352);
	    }
    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor)
	  {
	    rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    rectangle(x + width, y, x1 - width, y + width, borderColor);
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    rectangle(x, y, x + width, y1, borderColor);
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    rectangle(x1 - width, y, x1, y1, borderColor);
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	  }
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 255) / 255.0f;
        float f1 = (col1 >> 16 & 255) / 255.0f;
        float f2 = (col1 >> 8 & 255) / 255.0f;
        float f3 = (col1 & 255) / 255.0f;
        float f4 = (col2 >> 24 & 255) / 255.0f;
        float f5 = (col2 >> 16 & 255) / 255.0f;
        float f6 = (col2 >> 8 & 255) / 255.0f;
        float f7 = (col2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    public static void color(int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        GL11.glColor4f(f1, f2, f3, f);
    }
    public static int width() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }
    public static void drawImage(float x, float y, float width, float height,ResourceLocation image,int color) {
		GlStateManager.pushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float var11 = (color >> 24 & 255) / 255.0F;
        float var6 = (color >> 16 & 255) / 255.0F;
        float var7 = (color >> 8 & 255) / 255.0F;
        float var8 = (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //GlStateManager.color(var6, var7, var8, var11);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
//        GL11.glDisable((int)3042);
//        GL11.glEnable((int)2929);
	}
    
    
    
    public static void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
	
	static Texture rrr;
	public static void rectTexture(float x, float y, float w, float h, Texture texture, int color) {
		if (texture == null) {
			return;
		}
		GlStateManager.color(0, 0, 0);
		GL11.glColor4f(0, 0, 0, 0);

		x = Math.round(x);
		w = Math.round(w);
		y = Math.round(y);
		h = Math.round(h);
		
		float var11 = (color >> 24 & 255) / 255.0F;
        float var6 = (color >> 16 & 255) / 255.0F;
        float var7 = (color >> 8 & 255) / 255.0F;
        float var8 = (color & 255) / 255.0F;
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		texture.bind();
		float tw = (w/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (h/texture.getTextureHeight())/(h/texture.getImageHeight());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(x, y);
			GL11.glTexCoord2f(0, th);
			GL11.glVertex2f(x, y+h);
			GL11.glTexCoord2f(tw, th);
			GL11.glVertex2f(x+w, y+h);
			GL11.glTexCoord2f(tw, 0);
			GL11.glVertex2f(x+w, y);
		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
            int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
            fbo.depthBuffer = -1;
        }
    }
    public static void outlineOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(4.0f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void outlineTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static void outlineThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void outlineFour() {
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        GL11.glColor4f(0.9529412f, 0.6117647f, 0.07058824f, 1.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void outlineFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
	public static void entityESPBox(EntityPlayer e, Color color, Event3D event) {
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * Helper.renderPosX();
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * Helper.renderPosY();
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * Helper.renderPosZ();
        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - e.width, posY, posZ - e.width, posX + e.width, posY + e.height + 0.2, posZ + e.width);
        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds(posX - e.width + 0.2, posY, posZ - e.width + 0.2, posX + e.width - 0.2, posY + e.height + (e.isSneaking() ? 0.02 : 0.2), posZ + e.width - 0.2);
        }
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1f);
        SpecialRenderUtil.drawOutlinedBoundingBox(box);
    }
    public static int createShader(String shaderCode, int shaderType) throws Exception {
        int shader;
        block4 : {
            shader = 0;
            try {
                shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
                if (shader != 0) {
					break block4;
				}
                return 0;
            }
            catch (Exception exc) {
                ARBShaderObjects.glDeleteObjectARB(shader);
                throw exc;
            }
        }
        ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
        ARBShaderObjects.glCompileShaderARB(shader);
        if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
            throw new RuntimeException("Error creating shader:");
        }
        return shader;
    }
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }
    public static String getShaderCode(InputStreamReader file) {
        String shaderSource = "";
        try {
            String line;
            BufferedReader reader = new BufferedReader(file);
            while ((line = reader.readLine()) != null) {
                shaderSource = String.valueOf(shaderSource) + line + "\n";
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return shaderSource.toString();
    }
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }
    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }
    public static double interpolate(double newPos, double oldPos) {
        return oldPos + (newPos - oldPos) * Helper.getTimer().renderPartialTicks;
    }
    public static void drawRect666(double d, double e, double f2, double f3, double red, double green, double blue, double alpha) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glPushMatrix();
        GL11.glLineWidth(4.5f);
        GL11.glBegin(7);
        GL11.glVertex2d(f2, e);
        GL11.glVertex2d(d, e);
        GL11.glVertex2d(d, f3);
        GL11.glVertex2d(f2, f3);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }
    public static boolean isInFrustumView(Entity ent) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        double x = SpecialRenderUtil.interpolate(current.posX, current.lastTickPosX);
        double y = SpecialRenderUtil.interpolate(current.posY, current.lastTickPosY);
        double z = SpecialRenderUtil.interpolate(current.posZ, current.lastTickPosZ);
        frustum.setPosition(x, y, z);
        if (!frustum.isBoundingBoxInFrustum(ent.getEntityBoundingBox()) && !ent.ignoreFrustumCheck) {
            return false;
        }
        return true;
    }
    public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (0.01 * speed);
		if (animation < finalState) {
			if (animation + add < finalState) {
				animation += add;
			} else {
				animation = finalState;
			}
		} else {
			if (animation - add > finalState) {
				animation -= add;
			} else {
				animation = finalState;
			}
		}
		return animation;
    }
    public static double interpolation(final double newPos, final double oldPos) {
        return oldPos + (newPos - oldPos) * Helper.getTimer().renderPartialTicks;
    }
    
    public static int getHexRGB(final int hex) {
        return 0xFF000000 | hex;
    }
    public static void drawRect(float g, float d, float x2, float e, int col1) {
        float f2 = (col1 >> 24 & 255) / 255.0f;
        float f22 = (col1 >> 16 & 255) / 255.0f;
        float f3 = (col1 >> 8 & 255) / 255.0f;
        float f4 = (col1 & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, d);
        GL11.glVertex2d(g, d);
        GL11.glVertex2d(g, e);
        GL11.glVertex2d(x2, e);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawFilledESP(Entity entity, Color color) {
        mc.getRenderManager();
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Helper.getTimer().renderPartialTicks - Helper.renderPosX();
        mc.getRenderManager();
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Helper.getTimer().renderPartialTicks - Helper.renderPosY();
        mc.getRenderManager();
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Helper.getTimer().renderPartialTicks - Helper.renderPosZ();
        final double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
        final double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
        drawEntityESP(x, y, z, width, height, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 2.0f);
    }
    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        SpecialRenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        SpecialRenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
    public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    public static void drawFilledCircle(float xx, float yy, float radius, int col)
    {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f1 = (col >> 16 & 0xFF) / 255.0F;
        float f2 = (col >> 8 & 0xFF) / 255.0F;
        float f3 = (col & 0xFF) / 255.0F;

        int sections = 50;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.sin(i * dAngle));
            float y = (float)(radius * Math.cos(i * dAngle));

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }
    
    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        ((IMixinEntityRenderer)mc.entityRenderer).setupCameraTransformwith(Helper.getTimer().renderPartialTicks, 0);
    }
    
    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }
    
    public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
        drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
    }
    
    public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
        drawLine((float)start.getX(), (float)start.getY(), (float)start.getZ(), (float)end.getX(), (float)end.getY(), (float)end.getZ(), width);
    }
    
    public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
    	drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
    }
    
    public static void drawLine(final float x, final float y, final float z, final float x1, final float y1, final float z1, final float width) {
        GL11.glLineWidth(width);
        setupRender(true);
        setupClientState(GLClientState.VERTEX, true);
        SpecialRenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
        setupClientState(GLClientState.VERTEX, false);
        setupRender(false);
    }
    
    public static void setupClientState(final GLClientState state, final boolean enabled) {
        SpecialRenderUtil.csBuffer.clear();
        if (state.ordinal() > 0) {
            SpecialRenderUtil.csBuffer.add(state.getCap());
        }
        SpecialRenderUtil.csBuffer.add(32884);
        SpecialRenderUtil.csBuffer.forEach(enabled ? SpecialRenderUtil.ENABLE_CLIENT_STATE : SpecialRenderUtil.DISABLE_CLIENT_STATE);
    }
    
    public static void setupRender(final boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }



    public static class R2DUtils{
		public static void enableGL2D() {
			GL11.glDisable(2929);
			GL11.glEnable(3042);
			GL11.glDisable(3553);
			GL11.glBlendFunc(770, 771);
			GL11.glDepthMask(true);
			GL11.glEnable(2848);
			GL11.glHint(3154, 4354);
			GL11.glHint(3155, 4354);
		}

		public static void disableGL2D() {
			GL11.glEnable(3553);
			GL11.glDisable(3042);
			GL11.glEnable(2929);
			GL11.glDisable(2848);
			GL11.glHint(3154, 4352);
			GL11.glHint(3155, 4352);
		}

		public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, posZ);
			GL11.glNormal3f(0.0f, 0.0f, 0.0f);
			GlStateManager.rotate((float) -Helper.playerViewY(), 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(-0.1, -0.1, 0.1);
			GL11.glDisable(2896);
			GL11.glDisable(2929);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GlStateManager.depthMask(true);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			GL11.glDisable(3042);
			GL11.glEnable(2929);
			GlStateManager.popMatrix();
		}

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef(2.0f, 2.0f, 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}

		public static void drawRect(double x2, double y2, double x1, double y1, int color) {
			R2DUtils.enableGL2D();
			R2DUtils.glColor(color);
			R2DUtils.drawRect(x2, y2, x1, y1);
			R2DUtils.disableGL2D();
		}

		private static void drawRect(double x2, double y2, double x1, double y1) {
			GL11.glBegin(7);
			GL11.glVertex2d(x2, y1);
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x1, y2);
			GL11.glVertex2d(x2, y2);
			GL11.glEnd();
		}

		public static void glColor(int hex) {
			float alpha = (hex >> 24 & 255) / 255.0f;
			float red = (hex >> 16 & 255) / 255.0f;
			float green = (hex >> 8 & 255) / 255.0f;
			float blue = (hex & 255) / 255.0f;
			GL11.glColor4f(red, green, blue, alpha);
		}

		public static void drawRect(float x, float y, float x1, float y1, int color) {
			R2DUtils.enableGL2D();
			glColor(color);
			R2DUtils.drawRect(x, y, x1, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
			R2DUtils.enableGL2D();
			glColor(borderColor);
			R2DUtils.drawRect(x + width, y, x1 - width, y + width);
			R2DUtils.drawRect(x, y, x + width, y1);
			R2DUtils.drawRect(x1 - width, y, x1, y1);
			R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
			R2DUtils.enableGL2D();
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
			R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
			R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef(2.0f, 2.0f, 2.0f);
			R2DUtils.disableGL2D();
		}

		public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
			R2DUtils.enableGL2D();
			GL11.glShadeModel(7425);
			GL11.glBegin(7);
			glColor(topColor);
			GL11.glVertex2f(x, y1);
			GL11.glVertex2f(x1, y1);
			glColor(bottomColor);
			GL11.glVertex2f(x1, y);
			GL11.glVertex2f(x, y);
			GL11.glEnd();
			GL11.glShadeModel(7424);
			R2DUtils.disableGL2D();
		}

		public static void drawHLine(float x, float y, float x1, int y1) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
		}

		public static void drawVLine(float x, float y, float x1, int y1) {
			if (x1 < y) {
				float var5 = y;
				y = x1;
				x1 = var5;
			}
			R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
		}

		public static void drawHLine(float x, float y, float x1, int y1, int y2) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
		}

		public static void drawRect(float x, float y, float x1, float y1) {
			GL11.glBegin(7);
			GL11.glVertex2f(x, y1);
			GL11.glVertex2f(x1, y1);
			GL11.glVertex2f(x1, y);
			GL11.glVertex2f(x, y);
			GL11.glEnd();
		}
    }
}


