package zelix.cc.client.inGameGui.newclickgui;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.inGameGui.newclickgui.Font.CFontRenderer;
import zelix.cc.client.inGameGui.newclickgui.Font.FontLoaders;
import zelix.cc.client.inGameGui.newclickgui.draw.Draw;
import zelix.cc.client.inGameGui.newclickgui.draw.SpecialRenderUtil;
import zelix.cc.client.inGameGui.newclickgui.draw.TextureManager;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;

public class NewClickgui extends GuiScreen{
	
	ArrayList<Position> list = new ArrayList();
	TextureManager tm = Zelix.tm;
	int height = 355;
	int weight = 365;
	float x = 10;
	float y = 10;
	int addy = 10;
	ModuleType clickModuletype = ModuleType.Combat;
	Module clickModule = null;
	CFontRenderer sy18 = Zelix.getInstance().fontloader.sy14;
	CFontRenderer sy20 = Zelix.getInstance().fontloader.sy18;
	CFontRenderer bigfont = Zelix.getInstance().fontloader.sy24;
	CFontRenderer normalfont = Zelix.getInstance().fontloader.sy16;
	CFontRenderer superbigfont = Zelix.getInstance().fontloader.sy30;
	Value clickedValue;
	int clickstartX;
	boolean isModeListAlive;
	Value ShowListMode = null;
	Position listvalue;
	Boolean mainbackgroundclicked;
	float backgroundmoveX,backgroundmoveY;
	float diffx,diffy;
	float addenX,addenY;
	public NewClickgui() {
		mainbackgroundclicked = false;
	}
	
	public void setUpModuleTypeIconsPosition() {
		int offset = 0;
		Position cate = null;
		
		cate = new Position(x+offset+41,addy+18+y,24,24,ModuleType.Combat);
		if(!list.contains(cate) && !mainbackgroundclicked) {
			this.list.add(cate);
		}
		offset += 65;
		
		cate = new Position(x+offset+41,addy+18+y,24,24, ModuleType.Motion);
		if(!list.contains(cate) && !mainbackgroundclicked) {
			this.list.add(cate);
		}
		offset += 65;
		
		cate = new Position(x+offset+41,addy+18+y,24,24,ModuleType.Player);
		if(!list.contains(cate) && !mainbackgroundclicked) {
			this.list.add(cate);
		}
		offset += 65;
		
		cate = new Position(x+offset+41,addy+18+y,24,24,ModuleType.Render);
		if(!list.contains(cate) && !mainbackgroundclicked) {
			this.list.add(cate);
		}
		offset += 65;
		
		cate = new Position(x+offset+41,addy+18+y,24,24,ModuleType.World);
		if(!list.contains(cate) && !mainbackgroundclicked) {
			this.list.add(cate);
		}
	}
	
	float scrollY = 3;
	float scrollYForModeList = 2;
	float nowY = 0;
	Position back = null;
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.setUpModuleTypeIconsPosition();
		this.drawModuleIcons();
		this.drawSettingsBackground();
		this.listModules();
		back = new Position(x+6,y+6,353,12,"BackGround");
		
		/*Auto Repair Window Position*/
		if(this.x + 10.5 < 0) {
			this.x = -10.5F;
		}
		
		if(this.y + 11 < 0 ) {
			this.y = -11;
		}
		 
		if(this.x + 364 > RenderUtil.width()) {
			this.x = RenderUtil.width() - 364.5F;
		}
		 
		if(this.y + 353.5 > RenderUtil.height()) {
			if(RenderUtil.height() - 353.5 >= -11){
				this.y = RenderUtil.height() - 353.5F;
			}else{
				this.y = -11;
			}
		}
		
		/*Pre Draw Settings.,*/
		if(this.clickModule == null) {
			return;
		}
		
		float startX = x+27+79+18;
		float startY = addy+44+y+17+10+10;
		
		Module m = this.clickModule;
		this.bigfont.drawString(m.getName(), startX, startY, new Color(255,255,255).getRGB());
		if(m.isEnabled()) {
			SpecialRenderUtil.drawImage(x+27+79+213-24,startY-7,24,24,tm.module_toggle_open,-1);
			Position pl = new Position(x+27+79+213-24+2,startY-7+7,20,10,"Option",m,m.moduleType);
			if(!this.list.contains(pl) && !mainbackgroundclicked) {
				this.list.add(pl);
			}
		}else {
			SpecialRenderUtil.drawImage(x+27+79+213-24,startY-7,24,24,tm.module_toggle_close,-1);
			Position pl = new Position(x+27+79+213-24+2,startY-7+7,20,10,"Option",m,m.moduleType);
			if(!this.list.contains(pl) && !mainbackgroundclicked) {
				this.list.add(pl);
			}
		}
		
		List<Value> values = m.getValues();
		List<Value> booleanvalue = new ArrayList();
		List<Value> numbervalue = new ArrayList();
		List<Value> modevalue = new ArrayList();
		
		/* Sort Value */
		for(Value v : values) {
			if(v instanceof Option) {
				booleanvalue.add(v);
			}
			if(v instanceof Amount) {
				numbervalue.add(v);
			}
			if(v instanceof Mode) {
				modevalue.add(v);
			}
		}
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		//x+27+79+8, addy+44+y+17+10, 223, 245
		float ny = addy+44+y+17+10;
		float ScissorStartY = addy+44+y+17+10+10 + bigfont.getStringHeight(m.getName()) + 6;
		float ScissorSize = ny+245-(ScissorStartY);
		doGlScissor(0,(int)ScissorStartY,RenderUtil.width(),(int)ScissorSize);
		GL11.glTranslatef(0.0f, this.scrollY, 0.0f);
		
		if(values.size() == 0) {
			superbigfont.drawString("No Settings.",x+27+79+8+((223-superbigfont.getStringWidth("No Settings."))/2) , ScissorStartY+((ScissorSize-superbigfont.getStringHeight("No Settings."))/2), new Color(136,136,136).getRGB());
		}
		
		/* Draw Values*/
		float dlx = startX + 8;
		float dly = addy+44+y+17+10+10 + bigfont.getStringHeight(m.getName()) + 6;
		float offset = 0;
		
		for(Value bv : booleanvalue) {
			normalfont.drawString(bv.getDisplayName(), dlx, dly+offset, new Color(255,255,255).getRGB());
			Position vp = new Position(x+27+79+213-24+6+1,offset+dly-3+1,10,10,m,bv);
			if(!this.list.contains(vp) && !mainbackgroundclicked) {
				this.list.add(vp);
			}
			SpecialRenderUtil.drawImage(x+27+79+213-24+6,offset+dly-3,12,12, (Boolean)bv.getValue() ? tm.boolean_open : tm.boolean_close,-1);
			offset += 12;
		}
		offset += 3;
		for(Value bv : numbervalue) {
			normalfont.drawString(bv.getDisplayName(), dlx, dly+offset, new Color(255,255,255).getRGB());
			DecimalFormat decimalFormat=new DecimalFormat("#.#");
	        String text = String.valueOf(decimalFormat.format(((Amount<Double>)bv).getValue()));
	        if(!text.contains(".")) {
	        	text += ".0";
	        }
			normalfont.drawString(text, dlx+178-normalfont.getStringWidth(String.valueOf(text)), dly+offset, new Color(255,255,255).getRGB());
			offset += normalfont.getStringHeight(bv.getDisplayName())+3;
			SpecialRenderUtil.drawImage(dlx,offset+dly,178,4, tm.number_value_background,-1);
			Amount<Double> vv = (Amount<Double>)bv;
			double jd = vv.getValue()/(vv.max);
			int cd = (int) (178 * jd);
			SpecialRenderUtil.drawImage(dlx,offset+dly,cd,4, tm.number_value_chose,-1);
			Draw.circle(dlx+cd-2,offset+dly+2,4,new Color(94,114,228).getRGB());
			Position vp = new Position(dlx+cd-2-2-1,offset+dly,8,8,m,bv);
			//SpecialRenderUtil.drawRect(dlx+cd-2-2,offset+dly+2,dlx+cd-2-2+8,offset+dly+2+8,Color.BLACK.getRGB());
			if(!this.list.contains(vp) && !mainbackgroundclicked) {
				this.list.add(vp);
			}
			offset += 12;
		}
		offset += 2;
		for(Value bv : modevalue) {
			normalfont.drawString(bv.getDisplayName(), dlx, dly+offset, new Color(255,255,255).getRGB());
			Mode<Enum> mv = (Mode<Enum>) bv;
			SpecialRenderUtil.drawImage(x+27+79+213-67-8,offset+dly-4,67,15, tm.mode_value_background,-1);
			//SpecialRenderUtil.drawImage(x+27+79+213-67-8+66-2-16,offset+dly-4-2,16,16, this.ShowListMode != mv ? tm.modebuttleDown : tm.modebuttleUp,-1);
			normalfont.drawString(mv.getValue().toString(), x+27+79+213-67-8+3, offset+dly-4+this.getbestx(15, (int)normalfont.getStringHeight(mv.getValue().toString())), new Color(255,255,255).getRGB());
			//wdwd

			String showtext = String.valueOf(this.indexOf(mv.getModes(),mv.getValue())+1)+"/"+String.valueOf(mv.getModes().length);
			normalfont.drawString(showtext, x+27+79+213-67-8+67-normalfont.getStringWidth(showtext)-3, offset+dly-4+this.getbestx(15, (int)normalfont.getStringHeight(showtext)), new Color(255,255,255).getRGB());
			Position pl = new Position(x+27+79+213-67-8,offset+dly-4,67,15,m,mv);
			if(!this.list.contains(pl) && !mainbackgroundclicked) {
				this.list.add(pl);
			}
			offset += 18;
		}
		
		/*Wheel*/
		if(mouseX >= x+27+79+8 && mouseX <= x+27+79+8+223 && mouseY >= ScissorStartY && mouseY <= ScissorStartY+ScissorSize) {
			if(offset > ScissorSize) {
				float scroll = Mouse.getDWheel();
				this.scrollY += scroll / 5.0f;
		        if (this.scrollY > 3) {
		        this.scrollY = 3;
		    	}
		        if(this.scrollY <-(offset - ScissorSize)) {
		        	this.scrollY =-(offset - ScissorSize);
		        }  
			}
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public int indexOf(Enum[] arr,Enum e){
		for(int i=0;i<arr.length;i++){
			if(arr[i].equals(e)){
				return i;
			}
		}
		return -1;
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
        GL11.glScissor((int)(x * scaleFactor), (int)(mc.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }
	
	public void drawSettingsBackground() {
		SpecialRenderUtil.drawImage(x+27, addy+44+y+17+10, 79, 245, tm.module_list_background, -1); //background
		SpecialRenderUtil.drawImage(x+27+79+8, addy+44+y+17+10, 223, 245, tm.module_setting_background, -1); //background
	}
	
	public void listModules() {
		float starty = addy+44+y+17+10+10;
		float startx = x+27+10;
		float offset = 0;
		ArrayList<Module> right = new ArrayList();
		right.clear();
		for(Module m : Zelix.getInstance().getModuleManager().getModules()) {
			if(m.moduleType == this.clickModuletype) {
				right.add(m);
			}
		}
		for(Module m : right) {
			Color co = null;
			if(m.isEnabled()) {
				co = new Color(255,255,255);
				//Draw.circle(startx-9, starty+offset, 5, new Color(94,114,228).getRGB());
				//SpecialRenderUtil.rectTexture(startx-9, starty+offset, 8, 8, tm.modulelist_show_open, -1); //open
			}else {
				co = new Color(255,255,255,100);
				//Draw.circle(startx-9, starty+offset, 5, new Color(94,114,225).getRGB());
				//Draw.circle(startx-8, starty+offset+1, 4, -1);
				//SpecialRenderUtil.rectTexture(startx-9, starty+offset, 8, 8, tm.modulelist_show_close, -1); //close
			}
			Position modulelist = new Position(startx,starty+offset,sy20.getStringWidth(m.getName()),sy20.getStringHeight(m.getName()),m);
			if(!this.list.contains(modulelist) && !mainbackgroundclicked) {
				this.list.add(modulelist);
			}
			sy20.drawString(m.getName(), startx, starty+offset, co.getRGB());
			//RenderUtil.R2DUtils.drawHLine(startx, starty+offset-5, startx+79, starty+offset-6, new Color(255,255,255).getRGB());
			offset += sy20.getStringHeight(m.getName()) + 5;
		}
	}
	
	public void drawModuleIcons() {
		int offset = 0;
		SpecialRenderUtil.drawImage(x-10, y-10, 375, 365, tm.clickgui_background, -1); //background
		int fak = 0;
		switch(clickModuletype) {
			case Combat:
				fak = 0;
				break;
			case Motion:
				fak = 65;
				break;
			case Player:
				fak = 130;
				break;
			case Render:
				fak = 195;
				break;
			case World:
				fak = 260;
				break;
			default:
				break;
		}	
		SpecialRenderUtil.drawImage(x+41-8+fak,addy+y+18-5,39,44,tm.module_clicked,-1);
		
		SpecialRenderUtil.drawImage(x+offset+41,addy+18+y,24,24,tm.clickgui_combat,-1);
		sy18.drawString("Combat", 1+x+41-8+offset+getbestx(39,(int)sy18.getStringWidth("Combat")), addy+18+y+2+24, new Color(255,255,255).getRGB());
		offset += 65;
		SpecialRenderUtil.drawImage(x+offset+41,addy+18+y,24,24,tm.clickgui_movement,-1);
		sy18.drawString("Movement", 1+x+41-8+offset+getbestx(39,(int)sy18.getStringWidth("Movement")), addy+18+y+2+24, new Color(255,255,255).getRGB());
		offset += 65;
		SpecialRenderUtil.drawImage(x+offset+41,addy+18+y,24,24,tm.clickgui_player,-1);
		sy18.drawString("Player", 1+x+41-8+offset+getbestx(39,(int)sy18.getStringWidth("Player")), addy+18+y+2+24, new Color(255,255,255).getRGB());
		offset += 65;
		SpecialRenderUtil.drawImage(x+offset+41,addy+18+y,24,24,tm.clickgui_render,-1);
		sy18.drawString("Render", 1+x+41-8+offset+getbestx(39,(int)sy18.getStringWidth("Render")), addy+18+y+2+24, new Color(255,255,255).getRGB());
		offset += 65;
		SpecialRenderUtil.drawImage(x+offset+41,addy+18+y,24,24,tm.clickgui_world,-1);
		sy18.drawString("World", 1+x+41-8+offset+getbestx(39,(int)sy18.getStringWidth("World")), addy+18+y+2+24, new Color(255,255,255).getRGB());
	}
	
	public int getbestx(int size,int textsize) {
		return (size - textsize)/2;
	}

	double oldValue = 0;
	boolean allow = false;
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseButton == 0) {
			if(mouseX >= x+6+10 && mouseY >= y+16 && mouseX <= x+359 && mouseY <= y+18) {
				this.list.clear();
				this.diffx = mouseX - this.x;
				this.diffy = mouseY - this.y;
				this.mainbackgroundclicked = true;	
			}
			doCheckMove(mouseX,mouseY);
		}
    }
	
	public void doCheckMove(int mouseX, int mouseY) {
		int mouseYY = mouseY;
		
		/*if(this.listvalue != null) {
			if((this.ShowListMode != null || this.isModeListAlive != false || listvalue != null) && this.listvalue.istouch(mouseX, mouseYY)) {
				this.ShowListMode = null;
				this.listvalue = null;
				this.isModeListAlive = true;
			}
		}*/
		float ScissorStartY = addy+44+y+17+10+10 + bigfont.getStringHeight("Module") + 6;
		float ny = addy+44+y+17+10;
		float ScissorSize = ny+245-(ScissorStartY);
		if(mouseX >= x+27+79+8 && mouseX <= x+27+79+8+223 && mouseY >= ScissorStartY && mouseY <= ScissorStartY+ScissorSize && this.scrollY != 0) {
			mouseYY = (int) (mouseY - this.scrollY);
		}else {
			mouseYY = mouseY;
		}
		for(Position po : this.list) {
			Boolean couldexit = false;
			if(po.istouch(mouseX, mouseYY)) {
				switch(po.PositionType) {
				case "ModuleCategory":
					this.clickModuletype = po.type;
					this.clickModule = null;
					couldexit = true;
					break;
				case "Module":
					if(po.mod.moduleType == this.clickModuletype) {
						this.clickModule = po.mod;
						this.scrollY = 3;
						couldexit = true;
					}
					break;
				case "Value":
					if(po.mod != this.clickModule) {
						break;
					}
					couldexit = true;
					if(po.v instanceof Option) {
						po.v.setValue(!((boolean)po.v.getValue()));
					}
					if(po.v instanceof Amount) {
						this.clickstartX = mouseX;
						this.oldValue = ((Amount<Double>)po.v).getValue();
						this.clickedValue = po.v;
					}
					if(po.v instanceof Mode) {
						Mode<Enum> mod = (Mode<Enum>)po.v;
						if(this.indexOf(mod.getModes(),mod.getValue())+2 <= mod.getModes().length) {
							mod.setValue(mod.getModes()[this.indexOf(mod.getModes(),mod.getValue())+1]);
						}else {
							mod.setValue(mod.getModes()[0]);
						}
					}
					break;
				case "Both":
					if(po.text == "Option" && po.type == this.clickModuletype && po.mod == this.clickModule) {
						po.mod.setState(!po.mod.isEnabled());
						couldexit = true;
					}
					break;
				}
				if(couldexit) {
					break;
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state)
	{
		if(this.mainbackgroundclicked) {
			this.mainbackgroundclicked = false;
			this.list.clear();
		}
		this.clickedValue = null;
		this.clickstartX = 0;
		this.oldValue = 0;
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override 
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		 if(this.clickedValue instanceof Amount) {
			 Amount<Double> nv = (Amount<Double>)clickedValue;
	         double xdiff = mouseX - this.clickstartX;
	         double oneincneedx = 178 /((nv.max - nv.min) / nv.inc);
	         int moveinc = (int) (xdiff / oneincneedx);
	         double addvalue = moveinc * nv.inc;
	         double newValue = Math.max(Math.min(addvalue + oldValue, nv.max), nv.min);
	         nv.setValue(newValue);
		 }
		 if(mainbackgroundclicked) {
			 float newX = mouseX - diffx;
			 float newY = mouseY - diffy;
			 if(newX + 6 < 0) {
				 newX = -6;
			 }
			 
			 if(newY + 6 < 0 ) {
				 newY = -6;
			 }
			 
			 if(newX + 364 > RenderUtil.width()) {
				 newX = RenderUtil.width() - 364.5F;
			 }

			 if(newY + 353.5 > RenderUtil.height()) {
				 if(RenderUtil.height() - 354 >= -11){
					 newY = RenderUtil.height() - 354;
				 }else{
					 newY = -11;
				 }
			 }		 
			 float diffx = newX - this.x;
			 float diffy = newY - this.y;
			 this.x = newX;
			 this.y = newY;
			 this.list.clear();
		 }
		 super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	/*
	 * Bye Bye.Dead ModeList.
	 * 
	 * if(this.ShowListMode == bv) {
				SpecialRenderUtil.drawImage(x+27+79+213-67-8,offset+dly+12,67,68, tm.mode_value_background_open,-1);
				GlStateManager.pushAttrib();
				int startdrawY = offset+dly+12+2;
				doGlScissor(0, startdrawY, RenderUtil.width(), startdrawY+68-2);
				GL11.glTranslatef(0.0f, this.scrollYForModeList, 0.0f);
				int stringoffset = 0;
				for(String mode : mv.getModes()) {
					Color stringcolor = Color.WHITE;
					int drawX = x+27+79+213-67-8+this.getbestx(67, normalfont.getStringWidth(mode));
					if(mode == mv.getStringMode()) {
						stringcolor = new Color(3,131,255);
					}
					normalfont.drawString(mode, drawX, startdrawY + stringoffset, stringcolor.getRGB());
				}
				
				int sizex = x+27+79+213-67-8;
				int sizey = offset+dly+12;
				if(mouseX >= sizex && mouseX <= sizex+67 && mouseY >= sizey && mouseY <= sizey+68) {
					if(stringoffset > 66) {
						float scroll = Mouse.getDWheel();
						this.scrollYForModeList += scroll / 5.0f;
				        if (this.scrollYForModeList > 2) {
				        this.scrollYForModeList = 2;
				    	}
				        if(this.scrollYForModeList <-(stringoffset - 66)) {
				        	this.scrollYForModeList =-(stringoffset - 66);
				        }  
					}
				}
				GlStateManager.popMatrix();
			}
	 */
	
}
