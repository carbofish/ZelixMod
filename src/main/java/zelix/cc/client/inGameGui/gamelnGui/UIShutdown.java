package zelix.cc.client.inGameGui.gamelnGui;

import java.awt.Color;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;

public class UIShutdown extends GuiScreen{
	int opcAnim[] = {100,0,0};
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		opcAnim = 100;
		FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 24");
		GlStateManager.pushMatrix();
    	GL11.glColor3f(1.0f, 1.0f, 1.0f);
    	boolean yes = mouseX >= RenderUtil.width()/2-90 && mouseX <= RenderUtil.width()/2-10 && mouseY >= RenderUtil.height()/2-20-opcAnim[1] && mouseY <= RenderUtil.height()/2+20+opcAnim[1] ;
    	boolean no = mouseX >= RenderUtil.width()/2 && mouseX <= RenderUtil.width()/2+80 && mouseY >= RenderUtil.height()/2-20-opcAnim[2]  && mouseY <= RenderUtil.height()/2+20+opcAnim[2];
        RenderUtil.drawImage(new ResourceLocation("Zelix/texture/GuiMenu/mainmenu.png"), 0, 0, RenderUtil.width(), RenderUtil.height());
    	if(opcAnim[0] <= 155) {
			++opcAnim[0];
		}
		if(yes) {
			if(Mouse.isButtonDown(0)) {
				mc.shutdown();
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
			if(opcAnim[1] <= 5) {
				++opcAnim[1];
			}
		}else {
			if(opcAnim[1] >0) {
				--opcAnim[1];
			}
			
		}
		if(no) {
			if(Mouse.isButtonDown(0)) {
				mc.displayGuiScreen(new GuiMainMenu());
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
			if(opcAnim[2] <= 5) {
				++opcAnim[2];
			}
		}else {
			if(opcAnim[2] >0) {
				--opcAnim[2];
			}
		}
		RenderUtil.R2DUtils.drawRect(0.0, 0.0, RenderUtil.width(), RenderUtil.height(), new Color(0,0,0,opcAnim[0]).getRGB());
		RenderUtil.R2DUtils.drawRect(RenderUtil.width()/2-90, RenderUtil.height()/2-20+opcAnim[1], RenderUtil.width()/2-10, RenderUtil.height()/2+20+opcAnim[1], new Color(5,5,5,145).getRGB());
		RenderUtil.R2DUtils.drawRect(RenderUtil.width()/2, RenderUtil.height()/2-20+opcAnim[2], RenderUtil.width()/2+80, RenderUtil.height()/2+20+opcAnim[2], new Color(5,5,5,145).getRGB());
		font.drawString("Are you sure you want to exit?", RenderUtil.width()/2 - font.getWidth("Are you sure yo"), RenderUtil.height()/2 - 50, new Color(255,255,255).getRGB());
		font.drawString("Yes", RenderUtil.width()/2 - 60, RenderUtil.height()/2-7+opcAnim[1], new Color(255,255,255).getRGB());
		font.drawString("No", RenderUtil.width()/2+32, RenderUtil.height()/2 - 7+opcAnim[2], new Color(255,255,255).getRGB());
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
