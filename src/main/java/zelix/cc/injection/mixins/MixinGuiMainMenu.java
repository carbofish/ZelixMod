package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;


@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
	
	@Override
	@Overwrite
	public void initGui() {
		mc.displayGuiScreen(new zelix.cc.client.inGameGui.GuiMainMenu.GuiMainMenu());
		/*GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double)this.updateCounter < 1.0E-4D)
        {
            this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 99, 44);
            this.drawTexturedModalRect(j + 99, k + 0, 129, 0, 27, 44);
            this.drawTexturedModalRect(j + 99 + 26, k + 0, 126, 0, 3, 44);
            this.drawTexturedModalRect(j + 99 + 26 + 3, k + 0, 99, 0, 26, 44);
            this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
        }
        else
        {
            this.drawTexturedModalRect(j + 0, k + 0, 0, 0, 155, 44);
            this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.9.8";

        if (this.mc.isDemo())
        {
            s = s + " Demo";
        }

        this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);
        String s1 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRendererObj, s1, this.width - this.fontRendererObj.getStringWidth(s1) - 2, this.height - 10, -1);

        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0)
        {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
		int alpha = 150;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf2 = new SimpleDateFormat("HH:mm");
        cal = Calendar.getInstance();
        int h = new ScaledResolution(this.mc).getScaledHeight();
        int x = new ScaledResolution(this.mc).getScaleFactor();
        int y = new ScaledResolution(this.mc).getScaledWidth();
        Color color = new Color(55,55,55,180),color2 = new Color(55,55,55,180),color3 = new Color(55,55,55,180),color4= new Color(55,55,55,180),color5 = new Color(55,55,55,180);
        int backy= 0;
        int backx= 0;
        int gao = new ScaledResolution(this.mc).getScaledHeight();
        int kuan = new ScaledResolution(this.mc).getScaledWidth();
        new ScaledResolution(this.mc).getScaledHeight();
    	this.mc.getTextureManager().bindTexture(ResourceManager.mainmenu);
    	new net.minecraft.client.gui.ScaledResolution(this.mc);
    	GuiMainMenu.drawScaledCustomSizeModalRect((int)0, (int)0, (float)0.0f, (float)0.0f, (int)this.width, (int)this.height, (int)this.width, (int)this.height, (float)this.width, (float)this.height);
    	//float render =(mouseX > 2 && mouseX < y-2 && mouseY > 0 && mouseY < h/2 + 90)?(this.opacity + 10.0f <h/2 + 90? (this.opacity +=10.0f) :h/2 + 90) : (this.opacity - 12.0f > 0.0f ? (this.opacity -= 12.0f) : 0.0f);
    	boolean isOverSingleplayer = mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80 && mouseY < 100;
    	boolean isOverMultiplayer =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30 && mouseY < 100+30;
    	boolean isOverSettings =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30 && mouseY < 100+30+30;
    	boolean isOverAltManager =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30+30 && mouseY < 100+30+30+30;
    	boolean isOverExit =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30+30+30 && mouseY < 100+30+30+30+30;
    	boolean shouldXXX = false;
//    this.bigFont.drawStringWithShadow("Neon", 2, 12, new Color(185,185,185,255).getRGB());
//    this.opacity = 0;
    	GlStateManager.pushMatrix();
    	this.fontRenderer2.drawStringWithShadow(sdf2.format(cal.getTime()), y/2-this.bigFont.getStringWidth("Zelix Client")/2+26, 10, new Color(255,255,255,180).getRGB());  
//		this.opacity = 0;
    	GlStateManager.popMatrix();
  
    	GlStateManager.pushMatrix();
//    this.anim1 = (this.anim1 + 222 < 885? (this.anim1 += 222) : 885);
    	this.bigFont.drawStringWithShadow("Zelix Client", y/2-this.bigFont.getStringWidth("Zelix Client")/2, 40, new Color(255,255,255,100).getRGB());
    	if(isOverSingleplayer) {
    		if(timer.isDelayComplete(1L) && alpha<=255) {
    			alpha += 20;
    		}
    		color = new Color(15,15,15,alpha);
    	}else if(isOverMultiplayer) {
    		if(timer.isDelayComplete(1L) && alpha<=255) {
    			alpha += 20;
    		}
    		color2 = new Color(15,15,15,alpha);
    	}else if(isOverSettings) {
    		if(timer.isDelayComplete(1L) && alpha<=255) {
    			alpha += 20;
    		}
    		color3 = new Color(15,15,15,alpha);
    	}else if(isOverAltManager) {
    		if(timer.isDelayComplete(1L) && alpha<=255) {
    			alpha += 20;
    		}
    		color4 = new Color(15,15,15,alpha);
    	}else if(isOverExit) {
    		if(timer.isDelayComplete(1L) && alpha<=255) {
    			alpha += 20;
    		}
    		color5 = new Color(15,15,15,alpha);
    	}
    	RenderUtils.drawRoundedRect(y/2-this.bigFont.getStringWidth("Zelix Client")/2, 80, y/2+this.bigFont.getStringWidth("Zelix Client")/2, 100, color.getRGB(), color.getRGB());
    	RenderUtils.drawRoundedRect(y/2-this.bigFont.getStringWidth("Zelix Client")/2, 80+30, y/2+this.bigFont.getStringWidth("Zelix Client")/2, 100+30, color2.getRGB(), color2.getRGB());
    	RenderUtils.drawRoundedRect(y/2-this.bigFont.getStringWidth("Zelix Client")/2, 80+30+30, y/2+this.bigFont.getStringWidth("Zelix Client")/2, 100+30+30, color3.getRGB(), color3.getRGB());
    	RenderUtils.drawRoundedRect(y/2-this.bigFont.getStringWidth("Zelix Client")/2, 80+30+30+30, y/2+this.bigFont.getStringWidth("Zelix Client")/2, 100+30+30+30 ,color4.getRGB(), color4.getRGB());
    	RenderUtils.drawRoundedRect(y/2-this.bigFont.getStringWidth("Zelix Client")/2, 80+30+30+30+30, y/2+this.bigFont.getStringWidth("Zelix Client")/2, 100+30+30+30+30, color5.getRGB(), color5.getRGB());
    	this.fontRenderer.drawStringWithShadow("Singleplayer", y/2-this.bigFont.getStringWidth("Zelix Client")/2+13, 88, new Color(255,255,255,100).getRGB());
    	this.fontRenderer.drawStringWithShadow("Multiplayer", y/2-this.bigFont.getStringWidth("Zelix Client")/2+13, 118, new Color(255,255,255,100).getRGB());
    	this.fontRenderer.drawStringWithShadow("Settings", y/2-this.bigFont.getStringWidth("Zelix Client")/2+13, 148, new Color(255,255,255,100).getRGB());
    	this.fontRenderer.drawStringWithShadow("AltManager", y/2-this.bigFont.getStringWidth("Zelix Client")/2+13, 178, new Color(255,255,255,100).getRGB());
    	this.fontRenderer.drawStringWithShadow("Exit", y/2-this.bigFont.getStringWidth("Zelix Client")/2+13, 208, new Color(255,255,255,100).getRGB());
    	GlStateManager.popMatrix();
    	this.fontRenderer3.drawStringWithShadow((Object)EnumChatFormatting.GRAY+"Owner:"+(Object)EnumChatFormatting.WHITE+"NivalC & Exit_Zero", kuan-fontRenderer3.getStringWidth("Owner:NivalC & Exit_Zero")-10, 5, new Color(255,255,255,100).getRGB());
    	this.fontRenderer3.drawStringWithShadow((Object)EnumChatFormatting.GRAY+"Powered by "+(Object)EnumChatFormatting.WHITE+"PokeS", kuan-fontRenderer3.getStringWidth("Powered by PokeS")-10, 15, new Color(255,255,255,100).getRGB());
		*/
		
	}
	
	
	/*@Overwrite
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int h = new ScaledResolution(this.mc).getScaledHeight();
        int x = new ScaledResolution(this.mc).getScaleFactor();
        int y = new ScaledResolution(this.mc).getScaledWidth();
        boolean isOverSingleplayer = mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80 && mouseY < 100;
        boolean isOverMultiplayer =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30 && mouseY < 100+30;
        boolean isOverSettings =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30 && mouseY < 100+30+30;
        boolean isOverAltManager =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30+30 && mouseY < 100+30+30+30;
        boolean isOverExit =mouseX > y/2-this.bigFont.getStringWidth("Zelix Client")/2 && mouseX < y/2+this.bigFont.getStringWidth("Zelix Client")/2 && mouseY > 80+30+30+30+30 && mouseY < 100+30+30+30+30;
        if (mouseButton == 0 && isOverSingleplayer) {
            
            this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
            
        }
        if (mouseButton == 0 && isOverMultiplayer) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (mouseButton == 0 && isOverAltManager) {
        	this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
            this.mc.displayGuiScreen((GuiScreen)new GuiAltManager());
        }
        if (mouseButton == 0 && isOverSettings) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverExit) {
            this.mc.shutdown();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }*/
}
