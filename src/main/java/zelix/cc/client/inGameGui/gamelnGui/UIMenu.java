package zelix.cc.client.inGameGui.gamelnGui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import zelix.cc.client.Zelix;
import zelix.cc.client.utils.Render.FontRendererUtil;

public class UIMenu  extends GuiScreen implements GuiYesNoCallback{
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
