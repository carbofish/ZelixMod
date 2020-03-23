package zelix.cc.client.inGameGui.gamelnGui;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;

public class UIDrag extends GuiScreen{
	ResourceLocation customUI = new ResourceLocation("Zelix/texture/ClickGui/customUI.png");
	public static boolean drag = false;
	public static float x, y, dragX, dragY;
	@Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        this.mc.entityRenderer.isShaderActive();
        super.onGuiClosed();
    }
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 18");
		ScaledResolution sr = new ScaledResolution(this.mc);
		int witdh = sr.getScaledWidth();
		int height = sr.getScaledHeight();
        RenderUtil.R2DUtils.drawRect(2, height-23, 23, height-3,  new Color(100,100,100).getRGB());
        if(mouseX >= 2 && mouseX <= 23 && mouseY >= height-23 && mouseY <= height - 3) {
            RenderUtil.R2DUtils.drawRect(2, height-23, 23, height-3,  new Color(0,138,255).getRGB());
            if(Mouse.isButtonDown(0)) {
            	mc.displayGuiScreen(new UIClickGui());
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
            }
        }
        RenderUtil.drawImage(customUI, 5, height - 21, 16, 16);
        RenderUtil.drawBorderedRect(this.x - 5, this.y - 5, this.x+90+5, this.y+81+5,3, new Color(5,5,5).getRGB(), new Color(5,5,5,0).getRGB());
        if (this.drag) {
            if (!Mouse.isButtonDown((int)0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
            font.drawStringWithShadow("X:"+this.x, this.x, this.y - 18, -1);
            font.drawStringWithShadow("Y:"+this.y, this.x+98, this.y , -1);
        }
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public boolean isKeybinds(int mouseX, int mouseY) {
		return mouseX >= this.x-2 && mouseX <= this.x+90+2 && mouseY >= this.y-2 && mouseY <= this.y+81;
	}
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(isKeybinds(mouseX, mouseY)) {
			if(mouseButton == 0) {
                this.drag = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
