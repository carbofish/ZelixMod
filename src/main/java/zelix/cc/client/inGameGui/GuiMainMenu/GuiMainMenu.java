package zelix.cc.client.inGameGui.GuiMainMenu;

import net.minecraft.client.gui.*;
import zelix.cc.client.Zelix;
import zelix.cc.client.inGameGui.altLogin.source.GuiAccountManager;
import zelix.cc.client.inGameGui.gamelnGui.UIShutdown;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.animation.Translate;

import com.google.common.collect.Lists;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	Translate translate = new Translate(0, 0);
	int x, y;
    public int backy1;
    public int clockx;
	float animY[] = {0,0,0,0,0};
    public Boolean top1,left1,left2,right1,right2,down1,middle = false;
    TimerUtil time = new TimerUtil();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.clockx = RenderUtil.width()/2- 72;
    	String[] icon = new String[] {"singleplayer", "multiplayer", "settings", "key", "exit"};
    	x = 34;
    	y = 25;
    	int opc = 3;
    	boolean shouldShutdown = false;
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 20");
    	boolean singleplayer = mouseX > x+9 && mouseX < x+27 && mouseY > y+6-animY[0] && mouseY < y+24+animY[0];
    	boolean multiplayer = mouseX > x+9+40 && mouseX < x+27+40 && mouseY > y+6-animY[1] && mouseY < y+24+animY[1];
    	boolean settings = mouseX > x+9+40+40 && mouseX < x+27+40+40 && mouseY > y+6-animY[2] && mouseY < y+24+animY[2];
    	boolean alt = mouseX > x+9+40+40+40 && mouseX < x+27+40+40+40 && mouseY > y+6-animY[3] && mouseY < y+24+animY[3];
    	boolean exit = mouseX > x+RenderUtil.width()-90-1&& mouseX < x+RenderUtil.width()-90+16+1 && mouseY > y+6-animY[4] && mouseY < y+24+animY[4];
    	GlStateManager.pushMatrix();
    	GL11.glColor3f(1.0f, 1.0f, 1.0f);
        if(singleplayer) {
        	if(Mouse.isButtonDown(0)) {
            	mc.displayGuiScreen(new GuiSelectWorld(this));
            	Mouse.destroy();
            	try {
                	Mouse.create();
				} catch (Exception e) {
				}
        	}
        	if(animY[0] <3) {
                ++animY[0];
            }
        }else {
        	if(animY[0] > 0) {
                --animY[0];
            }
        }
        if(multiplayer) {
        	if(Mouse.isButtonDown(0)) {
        		mc.displayGuiScreen(new GuiMultiplayer(this));
            	Mouse.destroy();
            	try {
                	Mouse.create();
				} catch (Exception e) {
				}
        	}
        	if(animY[1] <3) {
                ++animY[1];
            }
        }else {
        	if(animY[1] >0) {
                --animY[1];
            }
        }
        if(settings) {
        	if(Mouse.isButtonDown(0)) {
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            	Mouse.destroy();
            	try {
                	Mouse.create();
				} catch (Exception e) {
				}
        	}
        	if(animY[2] <3) {
                ++animY[2];
            }
        }else {
        	if(animY[2] >0) {
                --animY[2];
            }
        }
        if(alt) {
        	if(Mouse.isButtonDown(0)) {
                mc.displayGuiScreen(new GuiAccountManager());
            	Mouse.destroy();
            	try {
                	Mouse.create();
				} catch (Exception e) {
				}
        	}
        	if(animY[3] <3) {
                ++animY[3];
            }
        }else {
        	if(animY[3] >0) {
                --animY[3];
            }
        }
        if(exit) {
        	if(Mouse.isButtonDown(0)) {
        		mc.displayGuiScreen(new UIShutdown());
            	Mouse.destroy();
            	try {
                	Mouse.create();
				} catch (Exception e) {
				}
        	}
        	if(animY[4] <3) {
                ++animY[4];
            }
        }else {
        	if(animY[4] >0) {
                --animY[4];
            }
        }
        RenderUtil.drawImage(new ResourceLocation("Zelix/texture/GuiMenu/mainmenu3.png"), 0, 0, RenderUtil.width(), RenderUtil.height());
        RenderUtil.R2DUtils.drawRect(x, y, x+RenderUtil.width()-68, y+30, new Color(5,5,5,125).getRGB());
        for(int i = 0; i<=4; i++) {
        	if(i<=3) {
                RenderUtil.drawImage(new ResourceLocation("Zelix/texture/GuiMenu/"+icon[i]+".png"), x+10+40*i, y+7+animY[i], 16, 16);
            } else {
                RenderUtil.drawImage(new ResourceLocation("Zelix/texture/GuiMenu/"+icon[i]+".png"), x+RenderUtil.width()-90, y+7+animY[i], 16, 16);
            }
        }
        RenderClock();
        font.drawString("Zelix - Copyright@2020 Zeon-Core Group.", 0, RenderUtil.height()  - font.getHeight("Zelix - Copyright@2020 Zeon-Core Group."), -1);
        GlStateManager.popMatrix();
    	super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public  void RenderClock() {
        ScaledResolution s1 = new ScaledResolution(this.mc);
        Clock(clockx,(int)s1.getScaledHeight_double() / 2 - 10,2,new Color(255,255,255,255).getRGB());
    }
    public void  Clock(int x,int y,int size, int color) {
	    ScaledResolution s1 = new ScaledResolution(this.mc);
        Calendar cal=Calendar.getInstance();
        int h =cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        Gui.drawRect((int)(s1.getScaledWidth_double() / 2 - s1.getScaledWidth_double() / 5.55D),(int)(y + 50),(int)(s1.getScaledWidth() / 2 + s1.getScaledWidth() / 5.55D),(int)(y + 50 + 1),color);
        
         this.drawnumber(x, y, h/10%10, color, size);

        this.drawnumber(x + 40,y,h%10,color,size);
        if(m > 9 || m/10%10 == 0) {
            this.drawnumber(x + 80,y,m/10%10,color,size);
        }
        this.drawnumber(x + 120,y,m%10,color,size);
        
        /*
        if(s > 9 || s/10%10 == 0) {
            this.drawnumber(x + 160,y,s/10%10,color,size);
        }
        his.drawnumber(x + 200,y,s%10,color,size);
        */
    }
    
    public  void drawnumber(int x,int y,int number,int color,int size) {
        
        int background = new Color(0, 0, 0, 120).getRGB();
        
        Rect(x, y, 20,size,background);
        Rect(x, y, size,20,background);
        Rect(x, y + 20, size,20,background);
        Rect(x + 20,y,size,20,background);
        Rect(x + 20 ,y + 20 ,size,22,background);
        Rect(x,y + 20,20,size,background);
        Rect(x,y + 40,20,size,background);
        
        switch (number) {
            case 0:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = true;
                break;
            case 1:
                this.top1 = false;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = false;
                break;
            case 2:
                this.top1 = true;
                this.left1 = false;
                this.left2 = true;
                this.right1 = true;
                this.right2 = false;
                this.middle = true;
                this.down1 = true;
                break;
            case 3:
                this.top1 = true;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 4:
                this.top1 = false;
                this.left1 = true;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = false;
                break;
            case 5:
                this.top1 = true;
                this.left1 = true;
                this.left2 = false;
                this.right1 = false;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 6:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = false;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 7:
                this.top1 = true;
                this.left1 = false;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = false;
                this.down1 = false;
                break;
            case 8:
                this.top1 = true;
                this.left1 = true;
                this.left2 = true;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
            case 9:
                this.top1 = true;
                this.left1 = true;
                this.left2 = false;
                this.right1 = true;
                this.right2 = true;
                this.middle = true;
                this.down1 = true;
                break;
        }

        
        if(this.top1) {
            Rect(x,y,20,size,color);
        }
        if(this.left1){
            Rect(x,y,size,20,color);
        }
        if(this.left2){
            Rect(x,y + 20,size,20,color);
        }
        if(this.right1){
            Rect(x + 20,y,size,20,color);
        }
        if(this.right2){
            Rect(x + 20,y + 20,size,20 +size,color);
        }
        if(this.middle) {
            Rect(x,y + 20,20 + size,size,color);
        }
        if(this.down1){
            Rect(x,y + 40,20,size,color);
        }
    }
    public static void Rect(double x, double y, double w, double h, int color) {
        Gui.drawRect((int)x, (int)y, (int)(w + x), (int)(h + y), color);
    }
}
