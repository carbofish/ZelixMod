package zelix.cc.client.inGameGui.gamelnGui;

import java.awt.Color;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.nio.file.OpenOption;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Color.Colors;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.animation.Translate;

public class UIClickGui extends GuiScreen{
	ModuleType moduleType;
	Module module;
	String valueName = "";
	Translate[] translates = {new Translate(0,0), new Translate(0,0), new Translate(0,0), new Translate(0,0), new Translate(0,0), new Translate(0,0)};
	String[] icon = new String[]{"combat", "exploit", "motion", "Player", "render", "world"};
	ResourceLocation customUI = new ResourceLocation("Zelix/texture/ClickGui/customUI.png");
	float screenX, screenY, modX, modY, valueX, valueY;
	float[] wheelY = {0,0,0,0,0,0,0,0,0,0};
	float[] smoothWheelY = {0,0,0,0,0,0};
	String CatValue = ""; //combat 1 exploit 2 motion 3 player 4 render 5 world 6
	float iconX;
	@Override
    public void initGui() {
        super.initGui();
    }
	@Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        this.mc.entityRenderer.isShaderActive();
        super.onGuiClosed();
    }
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 20");
		ScaledResolution sr = new ScaledResolution(this.mc);
		int witdh = sr.getScaledWidth();
		int height = sr.getScaledHeight();
		float animX = 200;
		screenY = 45;
		translates[0].interpolate(animX, screenY, 5);
		screenX = witdh-translates[0].getX();
		iconX = screenX;
		modX = screenX+20;
		modY = screenY;
		GlStateManager.pushMatrix();
		RenderUtil.R2DUtils.drawRect(witdh-translates[0].getX(), 0,witdh,22, new Color(245,245,245).getRGB());
		RenderUtil.R2DUtils.drawRect(witdh-translates[0].getX(), 22,witdh,height, new Color(255,255,255).getRGB());
        RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/panelbottom.png"), witdh-translates[0].getX(), 22, 200, 4);
        RenderUtil.R2DUtils.drawRect(2, height-23, 23, height-3,  new Color(0,138,255).getRGB());
        if(mouseX >= 2 && mouseX <= 23 && mouseY >= height-23 && mouseY <= height - 3) {
            RenderUtil.R2DUtils.drawRect(2, height-23, 23, height-3,  new Color(100,100,100).getRGB());
            if(Mouse.isButtonDown(0)) {
            	mc.displayGuiScreen(new UIDrag());
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
            }
        }
        RenderUtil.drawImage(customUI, 5, height - 21, 16, 16);
		for(int i = 0; i<=5; i++) {
			RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/"+icon[i]+".png"),iconX+15, 2, 16, 16);
			iconX+=30;
		}
		boolean combat = mouseX >= screenX + 15 && mouseX <= screenX+31 && mouseY >= 2 && mouseY <= 18;
		boolean exploit = mouseX >= screenX + 15+30 && mouseX <= screenX+31+30 && mouseY >= 2 && mouseY <= 18;
		boolean motion = mouseX >= screenX + 15+30+30 && mouseX <= screenX+31+30+30 && mouseY >= 2 && mouseY <= 18;
		boolean player = mouseX >= screenX + 15+30+30+30 && mouseX <= screenX+31+30+30+30 && mouseY >= 2 && mouseY <= 18;
		boolean render = mouseX >= screenX + 15+30+30+30+30 && mouseX <= screenX+31+30+30+30+30 && mouseY >= 2 && mouseY <= 18;
		boolean world = mouseX >= screenX + 15+30+30+30+30+30 && mouseX <= screenX+31+30+30+30+30+30 && mouseY >= 2 && mouseY <= 18;
		if(combat) {
			RenderUtil.R2DUtils.drawRect(screenX+13, 19, screenX+34, 21, new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue = "Combat";
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		if(exploit) {
			RenderUtil.R2DUtils.drawRect(screenX+13+30, 19, screenX+34+30, 21, new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue ="Exploit";
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		if(motion) {
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30, 19, screenX+34+30+30, 21,new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue = "Motion";
				Mouse.destroy();
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		if(player) {
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30, 19, screenX+34+30+30+30, 21, new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue = "Player";
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		if(render) {
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30+30, 19, screenX+34+30+30+30+30, 21, new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue = "Render";
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		if(world) {
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30+30+30, 19, screenX+34+30+30+30+30+30, 21, new Color(5,5,5).getRGB());
			if(Mouse.isButtonDown(0)) {
				this.CatValue = "World";
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		switch (this.CatValue) {
		case "Combat":
			RenderUtil.R2DUtils.drawRect(screenX+13, 19, screenX+34, 21, new Color(5,5,5).getRGB());
			break;
		case "Exploit":
			RenderUtil.R2DUtils.drawRect(screenX+13+30, 19, screenX+34+30, 21,new Color(5,5,5).getRGB());
			break;
		case "Motion":
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30, 19, screenX+34+30+30, 21,new Color(5,5,5).getRGB());
			break;
		case "Player":
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30, 19, screenX+34+30+30+30, 21, new Color(5,5,5).getRGB());
			break;
		case "Render":
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30+30, 19, screenX+34+30+30+30+30, 21, new Color(5,5,5).getRGB());
			break;
		case "World":
			RenderUtil.R2DUtils.drawRect(screenX+13+30+30+30+30+30, 19, screenX+34+30+30+30+30+30, 21, new Color(5,5,5).getRGB());
			break;
		}
        GlStateManager.pushMatrix();
        if(CatValue != "") {
    		RenderUtil.R2DUtils.drawRect(modX - 3, screenY - 8, modX + 163, RenderUtil.height() - 15, new Color(200,200,200).getRGB());
            GL11.glEnable(3089);
            RenderUtil.doGlScissor(witdh - 200, (int) (screenY-6), RenderUtil.width(), RenderUtil.height() - 57);	
        }
		switch (this.CatValue) {
		case "Combat":
			this.smoothWheelY[0] = (float) RenderUtil.getAnimationState(this.smoothWheelY[0] , this.wheelY[0]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[0], 0.0f);
            mouseY -= this.smoothWheelY[0];
			break;
		case "Exploit":
			this.smoothWheelY[1] = (float) RenderUtil.getAnimationState(this.smoothWheelY[1] , this.wheelY[1]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[1], 0.0f);
            mouseY -= this.smoothWheelY[1];
			break;
		case "Motion":
			this.smoothWheelY[2] = (float) RenderUtil.getAnimationState(this.smoothWheelY[2] , this.wheelY[2]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[2], 0.0f);
            mouseY -= this.smoothWheelY[2];
			break;
		case "Player":
			this.smoothWheelY[3] = (float) RenderUtil.getAnimationState(this.smoothWheelY[3] , this.wheelY[3]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[3], 0.0f);
            mouseY -= this.smoothWheelY[3];
			break;
		case "Render":
			this.smoothWheelY[4] = (float) RenderUtil.getAnimationState(this.smoothWheelY[4] , this.wheelY[4]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[4], 0.0f);
            mouseY -= this.smoothWheelY[4];
			break;
		case "World":
			this.smoothWheelY[5] = (float) RenderUtil.getAnimationState(this.smoothWheelY[5] , this.wheelY[5]*20,350.0);
            GL11.glTranslatef(0.0f, this.smoothWheelY[5], 0.0f);
            mouseY -= this.smoothWheelY[5];
			break;
		}
		for(Module mod : Zelix.instance.getModuleManager().getModules()) {
			if(mod.moduleType.name() != this.CatValue) continue;
			float modNameWitdh = font.getWidth(mod.getName());
			RenderUtil.R2DUtils.drawRect(modX-1, modY-6, modX+161,modY+17, new Color(230,230,230).getRGB());
			if(mod.isEnabled()) {
				RenderUtil.R2DUtils.drawRect(modX-1, modY-6, modX+161,modY+17, new Color(0,138,255).getRGB());
				font.drawString(mod.getName(), modX+5, modY, new Color(255,255,255).getRGB());
			}else {
				font.drawString(mod.getName(), modX+5, modY, new Color(100,100,100).getRGB());
			}
			RenderUtil.R2DUtils.drawRect(modX - 1, modY+17, modX+161, modY+30+(mod.getValues().size())*25, new Color(255,255,255).getRGB());
			this.valueY = this.modY+25;
			this.valueX = this.modX + 5;
			for(Value value : mod.getValues()) {
				this.drawValue(value, mouseX, mouseY, partialTicks, this.valueX, this.valueY);
				this.valueY+=25;
			}
            float scroll = Mouse.getDWheel();
            float anim = 1;
			switch (this.CatValue) {
			case "Combat":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[0] < 0) {
							++this.wheelY[0];
						}
					} else if(scroll < 0) {
						--this.wheelY[0];
					}
				}
				break;
			case "Exploit":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[1] < 0) {
							++this.wheelY[1];
						}
					} else if(scroll < 0) {
						--this.wheelY[1];
					}
				}
				break;
			case "Motion":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[2] < 0) {
							++this.wheelY[2];
						}
					} else if(scroll < 0) {
						--this.wheelY[2];
					}
				}
				break;
			case "Player":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[3] < 0) {
							++this.wheelY[3];
						}
					} else if(scroll < 0) {
						--this.wheelY[3];
					}
				}
				break;
			case "Render":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[4] < 0) {
							++this.wheelY[4];
						}
					} else if(scroll < 0) {
						--this.wheelY[4];
					}
				}
				break;
			case "World":
				if(Mouse.hasWheel() && mouseX >= witdh - 200 && mouseX <= witdh) {
					if(scroll > 0) {
						if(this.wheelY[5] < 0) {
							++this.wheelY[5];
						}
					} else if(scroll < 0) {
						--this.wheelY[5];
					}
				}
				break;
			}
			if(mouseX >= modX - 1 && mouseX <= modX +161 && mouseY >= modY - 6 && mouseY <= modY + 17) {
				RenderUtil.R2DUtils.drawRect(modX-1, modY-6, modX+161,modY+17, new Color(125,125,125,125).getRGB());
				if(Mouse.isButtonDown(0)) {
					mod.setState(!mod.isEnabled());
					Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
				modY+=17+(mod.getValues().size()+2)*25;
		}
        if(CatValue != "") {
            GL11.glDisable(3089);
        }
        GlStateManager.popMatrix();
		GlStateManager.popMatrix();
//		RenderUtil.R2DUtils.drawRect(screenX, 0, screenX+200, translates[0].getY(), new Color(0,0,0,155).getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void drawValue(Value value, int mouseX, int mouseY, float partialTicks, float x, float y) {
		FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 20");
		if(value instanceof Option) {
			font.drawString(value.getDisplayName(), x, y, new Color(50,50,50).getRGB());
			if((boolean) value.getValue()) {
				RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/Option_On.png"), x, y+11, 10, 10);
			} else {
				RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/Option_Off.png"), x, y+11, 10, 10);
			}
			if(mouseX >= x && mouseX <= x+10 && mouseY >= y+11 && mouseY <= y+21) {
				if(Mouse.isButtonDown(0)) {
					value.setValue(!(boolean)value.getValue());
					Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Amount v2;
		if(value instanceof Amount) {
            this.valueName = String.valueOf(String.valueOf(this.valueName)) + ((v2 = (Amount)value).isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
            v2 = (Amount)value;
            this.valueName = "" + (v2.isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
            if (mouseX >= x-4 && mouseX <= x+155 && mouseY >= y+11 && mouseY <= y+25) {
            	if(Mouse.isButtonDown(0)) {
            		double i = 0;
                    double min = ((Number)v2.min).doubleValue();
                    double max = ((Number)v2.max).doubleValue();
                    double inc = ((Number)v2.inc).doubleValue();
                    double valAbs = mouseX - (x);
                    double perc = valAbs / 151;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    double valRel = (max - min) * perc;
                    double val2 = min + valRel;
                    val2 = (double)Math.round(val2 * (1.0 / inc)) / (1.0 / inc);
                    v2.setValue(val2);
            	}
            }
            v2 = (Amount)value;
            float render = 151f * (((Number)v2.getValue()).floatValue() - ((Number)v2.min).floatValue()) / (((Number)v2.max).floatValue() - ((Number)v2.min).floatValue());
            RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/Numbers_Line2.png"), x, y+15, 151, 4);
            RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/Numbers_On.png"), x, y+15, (int)render, 4);
            RenderUtil.drawImage(new ResourceLocation("Zelix/texture/ClickGui/Numbers_Button.png"), x+(int)render-6, y+11, 11, 11);
			font.drawString(value.getDisplayName()+": ", x, y, new Color(50,50,50).getRGB());
			font.drawString(this.valueName, x+font.getWidth(value.getDisplayName()+": "), y, new Color(0,138,255).getRGB());	
		}
		if(value instanceof Mode) {
			font.drawString(value.getDisplayName(), x, y, new Color(50,50,50).getRGB());
            final Mode m2 = (Mode)value;
            final Enum current2 = (Enum)m2.getValue();
            final int current3 = current2.ordinal()+1;
//            RenderUtil.R2DUtils.drawRect(this.x+25, this.y2+8, this.x+198, this.y2+24,new Color(75,75,75,135).getRGB());
            this.valueName = "" + ((Mode)value).getValue();
            font.drawString(this.valueName+" "+current3+"/"+m2.getModes().length, x+font.getWidth(value.getDisplayName()), y, new Color(100,100,100).getRGB());
            if(mouseX >= x && mouseX <= x+156 && mouseY >= y && mouseY <= y+11) {
                final Mode m = (Mode)value;
                final Enum current = (Enum)m.getValue();
                final int next = (current.ordinal() + 1 >= m.getModes().length) ? 0 : (current.ordinal() + 1);
                final int last = (current.ordinal() - 1 < 0) ? m.getModes().length-1 : (current.ordinal() - 1);
           	 if(Mouse.isButtonDown(0)) {
                   value.setValue(m.getModes()[next]);
//                   System.out.println("DILRHGUISRTHU");//debug
                    Mouse.destroy();
					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
           	 }
           	 if(Mouse.isButtonDown(1)) {
                    value.setValue(m.getModes()[last]);
//                    System.out.println("DILRHGUISRTHU");//debug
                     Mouse.destroy();
 					try {
						Mouse.create();
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
            	 }
            }
		}
	}
}
