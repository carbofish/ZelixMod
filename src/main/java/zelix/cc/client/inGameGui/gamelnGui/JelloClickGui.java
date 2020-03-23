package zelix.cc.client.inGameGui.gamelnGui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.eventAPI.events.execute.EventTick;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.animation.Translate;
public class JelloClickGui extends GuiScreen {
	int x, y, x2, y2, x3, y3, x4, y4, x5, y5;
	int MenuMode = 0;
	public float scroll;
	public float scrollTicks;
	public float lastScrollTicks;
	public float smoothScroll;
	String modName, valueName;
	ModuleType[] moduleTypes = ModuleType.values();
	ArrayList< ModuleType> typeList = new ArrayList<ModuleType>();
//	
    @Override
	public void initGui() {
        super.initGui();
    }
    @Override
	public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        this.mc.entityRenderer.isShaderActive();
    }
    public float smoothTrans(double current, double last){
		return (float) (current * Helper.getTimer().renderPartialTicks + last * (1.0f - Helper.getTimer().renderPartialTicks));
	}
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	Translate translate = new Translate(0, 0);
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
    	FontRendererUtil titlefont = Zelix.instance.fontManager.getFont("CONS 24");
    	FontRendererUtil modfont = Zelix.instance.fontManager.getFont("CONS 20");
    	String valueName = "";
    	this.x4 = RenderUtil.width()/2-100;
    	this.y4 = 100;
    	this.x5 = RenderUtil.width()/2-100;
    	this.y5 = 100;
    	this.x = 60;
    	this.x2=60;
    	this.y = 40;
    	boolean once = true, once2 = true, once3 = true;
        GlStateManager.pushMatrix();
        for (ModuleType moduleType : moduleTypes) {
        	this.y2 = this.y+22; 
        	if(MenuMode != 1) {
        		RenderUtil.R2DUtils.drawRoundedRect(this.x-1, this.y-2, this.x+101, this.y+23,new Color(255,255,255,0).getRGB(),new Color(240,240,240).getRGB());//�װ�
        		font.drawString(moduleType.name(), this.x+10, this.y+5, new Color(100,100,100).getRGB());
    			RenderUtil.R2DUtils.drawRoundedRect(this.x-1, this.y+22, this.x+101, this.y+22+15*Zelix.moduleManager.getModules().size()/2, new Color(255,255,255,0).getRGB(),new Color(255,255,255).getRGB());
        	}
			for(Module module : Zelix.instance.getModuleManager().getModules()) {
				if(module.moduleType != moduleType) {
					continue;
				}
				if(MenuMode != 1) {
		    		if(module.isEnabled()) {
						RenderUtil.R2DUtils.drawRoundedRect(this.x-1f, this.y2, this.x+101f, this.y2+7+modfont.getHeight(module.getName()), new Color(0,138,255,0).getRGB(),new Color(10,138,255).getRGB());
					}
		    		modfont.drawString(module.getName(), module.isEnabled()?this.x+4 : this.x+2, this.y2+4, module.isEnabled()?new Color(255,255,255).getRGB():new Color(100,100,100).getRGB());
		    		if(mouseX >= this.x && mouseX<= this.x+100 && mouseY >= this.y2+2 && mouseY <= this.y2+6+modfont.getHeight(module.getName())) {
		    			if(this.MenuMode != 1) {
							RenderUtil.R2DUtils.drawRoundedRect(this.x-1f, this.y2, this.x+101, this.y2+7+modfont.getHeight(module.getName()), new Color(100,100,100,0).getRGB(),new Color(100,100,100,120).getRGB());
						}
		    			if(Mouse.isButtonDown(1)) {
		    				System.out.println("true");
		    				this.MenuMode = 1;
		    				this.modName = module.getName();
		    			}
			    			if(Mouse.isButtonDown(0) && this.MenuMode != 1) {
			    				System.out.println("true");
//			    				this.MenuMode = 0;
			    				module.setState(!module.isEnabled());
			                    Mouse.destroy();
			                    try {
									Mouse.create();
								} catch (LWJGLException e) {
									e.printStackTrace();
								}
			    			}
		    		}
				}else {
					if(this.MenuMode == 1 && once) {
						once = false;
						RenderUtil.R2DUtils.drawRoundedRect(0, 0, RenderUtil.width(), RenderUtil.height(), new Color(0,0,0,125).getRGB(), new Color(0,0,0,125).getRGB());
						RenderUtil.R2DUtils.drawRoundedRect(this.x4, this.y4, this.x4+200, RenderUtil.height()-100,new Color(255,255,255).getRGB(),new Color(255,255,255).getRGB());		
						titlefont.drawString(this.modName, this.x4, this.y4+2, new Color(100,100,100).getRGB());

					}
					if(this.MenuMode == 1) {
					        GL11.glEnable(3089);
				        if(once2) {
				        	once2 = false;
				        	RenderUtil.doGlScissor(0, this.y4+15, RenderUtil.width(), RenderUtil.height()-210);
				            GL11.glTranslatef(0.0f, this.smoothScroll, 0.0f);
				            mouseY = (int) (mouseY - this.smoothScroll);
				        }
							for(Value val : module.getValues()) {
					    				if(module.getName() != this.modName) continue;
					    				this.drawValue(val, mouseX, mouseY, partialTicks);
							    		this.y5+=20;
					    			}
//							if(mouseX >= this.x4 && mouseX <= this.x4+200 && mouseY >= this.y4 && mouseY <= RenderUtil.height() - 100) {
							if(Mouse.hasWheel()) {
				                final float wheel = Mouse.getDWheel();
				                if(wheel < 0) {
				                	this.smoothScroll += this.smoothTrans(wheel /2.0f, 0);
				                }else {
				                	this.smoothScroll += this.smoothTrans(wheel /2.0f, 0);
				                }
				                if (this.smoothScroll > 0.0f) {
				                    this.smoothScroll = 0.0f;
				                }
							}
						        GL11.glDisable(3089);
					}
				}

	    		
	    		this.y2+=15;
			}
            this.x += 130;
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public void drawValue( Value val, int mouseX, int mouseY, float partialTicks) {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
    	FontRendererUtil titlefont = Zelix.instance.fontManager.getFont("CONS 24");
    	FontRendererUtil modfont = Zelix.instance.fontManager.getFont("CONS 20");
		if(val instanceof Option) {
			modfont.drawString(val.getDisplayName(), this.x4+2, this.y5+22, new Color(100,100,100).getRGB());
			if((boolean) val.getValue()) {
				RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/Option_On.png"), this.x4+200-15, this.y5+22, 10, 10);	
			}else {
				RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/Option_Off.png"), this.x4+200-15, this.y5+22, 10, 10);
			}
			if(mouseX >= this.x4+200-15 && mouseX <= this.x4+200-5 && mouseY >= this.y5+22 && mouseY <= this.y5+32) {
				if(Mouse.isButtonDown(0)) {
					val.setValue(!(boolean)val.getValue());
					Mouse.destroy();
					try {
						Mouse.create();
					}catch (Exception e) {
						
					}

				}
			}
		}else if(val instanceof Mode) {
			modfont.drawString(val.getDisplayName(), this.x4+2, this.y5+22, new Color(100,100,100).getRGB());
            final Mode m2 = (Mode)val;
            final Enum current2 = (Enum)m2.getValue();
            final int current3 = current2.ordinal()+1;
//            RenderUtil.R2DUtils.drawRect(this.x+25, this.y2+8, this.x+198, this.y2+24,new Color(75,75,75,135).getRGB());
            this.valueName = "" + ((Mode)val).getValue();
            modfont.drawString(this.valueName+" "+current3+"/"+m2.getModes().length, this.x4+195-modfont.getWidth(this.valueName+" "+current3+"/"+m2.getModes().length), this.y5+22, new Color(165,165,165).getRGB());

//            modfont.drawStringWithShadow(, this.x+196-font.getWidth(""+current3+"/"+m2.getModes().length), this.y2+14, new Color(255,255,255).getRGB());
            try {
                if(mouseX >= this.x4+195-modfont.getWidth(this.valueName+" "+current3+"/"+m2.getModes().length) && mouseX <= this.x4+195 && mouseY >= this.y5+22 && mouseY <= this.y5+32 && Mouse.isButtonDown(0)) {
                    final Mode m = (Mode)val;
                    final Enum current = (Enum)m.getValue();
                    final int next = (current.ordinal() + 1 >= m.getModes().length) ? 0 : (current.ordinal() + 1);
                    val.setValue(m.getModes()[next]);
//                    System.out.println("DILRHGUISRTHU");//debug
                    Mouse.destroy();
                    Mouse.create();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
		}
		Amount v2;
	        if(val instanceof Amount) {
	            this.valueName = String.valueOf(String.valueOf(this.valueName)) + ((v2 = (Amount)val).isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
	            v2 = (Amount)val;
	            this.valueName = "" + (v2.isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
	            if (Mouse.isButtonDown(0) && mouseX >= this.x4+75 && mouseX <= this.x4+200 && mouseY >= this.y5+20 && mouseY <= this.y5+30) {
	                double min = ((Number)v2.min).doubleValue();
	                double max = ((Number)v2.max).doubleValue();
	                double inc = ((Number)v2.inc).doubleValue();
	                double valAbs = mouseX - (this.x4+80);
	                double perc = valAbs / 115;
	                perc = Math.min(Math.max(0.0, perc), 1.0);
	                double valRel = (max - min) * perc;
	                double val2 = min + valRel;
	                val2 = (double)Math.round(val2 * (1.0 / inc)) / (1.0 / inc);
	                v2.setValue(val2);
	            }
	        }
		
		if(val instanceof Amount) {
            v2 = (Amount)val;
            float render = 115f * (((Number)v2.getValue()).floatValue() - ((Number)v2.min).floatValue()) / (((Number)v2.max).floatValue() - ((Number)v2.min).floatValue());
//            RenderUtil.R2DUtils.drawRect(this.x4+80, this.y5+23, this.x4+195, this.y5+27,new Color(125,125,125).getRGB());
//            RenderUtil.R2DUtils.drawRect(this.x4+80, this.y5+23, this.x4+80+render, this.y5+27,new Color(0,138,255).getRGB());
//            RenderUtil.R2DUtils.drawRect(this.x4+80+render, this.y5+21, this.x4+84+render, this.y5+29,new Color(0,138,255).getRGB());
            RenderUtil.drawImage(new ResourceLocation("Eliru/Amount_Line2.png"), this.x4+80, this.y5+23, 115, 4);
            RenderUtil.drawImage(new ResourceLocation("Eliru/Amount_On.png"), this.x4+80, this.y5+23, (int)render, 4);
            RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"), this.x4+80+(int)render-6, this.y5+19, 11, 11);
			modfont.drawString(val.getDisplayName() +": ", this.x4+2, this.y5+22, new Color(100,100,100).getRGB());
			modfont.drawString(this.valueName, this.x4+2+modfont.getWidth(val.getDisplayName() +": "), this.y5+22, new Color(0,138,255).getRGB());
		}
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
    	if(mouseX > 0 && mouseX < RenderUtil.width() && mouseY > 0 && mouseY < this.y4
    			|| mouseX > 0 && mouseX < this.x4 && mouseY > 0 && mouseY < RenderUtil.height()
    			|| mouseX > 0 && mouseX < RenderUtil.width() && mouseY > RenderUtil.height()-100 && mouseY < RenderUtil.height()
    			|| mouseX > this.x4+200 && mouseX < RenderUtil.width() && mouseY > 0 && mouseY < RenderUtil.height()) {
        	if(mouseButton == 0) {
        		this.smoothScroll = 0.0f;
        		this.MenuMode = 0;	
        	}
    	}
//    	this.x4+200-15, this.y4+2, 10, 10
    	
    	/*
    	if(mouseX >= this.x4+185 && mouseX <= this.x4+195 && mouseY >= this.y4+2 && mouseY <= this.y4+12) {
        	if(mouseButton == 0) {
        		this.smoothScroll = 0.0f;
        		this.MenuMode = 0;	
        	}
    	}
    	*/
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
