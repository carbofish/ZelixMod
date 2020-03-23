package zelix.cc.client.modules.render;

import net.minecraft.client.Minecraft;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.render.Event2D;
import zelix.cc.client.inGameGui.gamelnGui.UIDrag;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Potion.PotionTime;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class HUD
extends Module {

    static enum FontMode{
        Minecraft,
        Zelix;
    }
    private Option<Boolean> info = new Option<Boolean>("Info", "Info", true);
    private Option<Boolean> arraylist = new Option<Boolean>("ArrayList","ArrayList",true);
    private Option<Boolean> suffix = new Option<Boolean>("Suffix","Suffix",true);
    public HUD() {
        super("HUD",ModuleType.Render);
        this.setState(true);
        this.addSettings(this.info,this.arraylist,this.suffix);
    }
    @Override
    public void onEnable() {
        super.onEnable();
    }
    @Override
    public void onDisable() {
        super.onDisable();
    }
    @Runnable
    public void onRender(Event2D event) {
        //mc.fontRendererObj.drawString("111111",1,1,Color.white.getRGB(),true);
        FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 16");
        FontRendererUtil font2 = Zelix.instance.fontManager.getFont("CONS 18");
        boolean shouldShowINFO = this.info.getValue().booleanValue();
        if (!this.mc.gameSettings.showDebugInfo) {
        	String clientName = Zelix.instance.clientName;
        	String pingtext = "PING: "+mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime()+"ms";
            String xyztext ="X " +"Y "+ "Z" +": "+MathHelper.floor_double(this.mc.thePlayer.posX) + " " + MathHelper.floor_double(this.mc.thePlayer.posY) + " " + MathHelper.floor_double(this.mc.thePlayer.posZ);
        	font2.drawString(!shouldShowINFO ?clientName : clientName+"   "+pingtext+"    FPS: "+mc.getDebugFPS()+"    "+xyztext, 2, 1, new Color(255,255,255).getRGB());
            if(arraylist.getValue().booleanValue()) {
                int y = 1 , y2=1;
                ArrayList<Module> mods = (ArrayList<Module>) ((ArrayList<Module>) Zelix.instance.getModuleManager().modules).clone();
                if(!this.suffix.getValue().booleanValue()) {
                    	Collections.sort(mods, new Comparator<Module>() { @Override
                        public int compare(Module m1, Module m2) { if (font.getWidth(m1.getName()+m1.getBindName()) > font.getWidth(m2.getName()+m2.getBindName())) { return -1; } if (font.getWidth(m1.getName()+m1.getBindName()) < font.getWidth(m2.getName()+m2.getBindName())) { return 1; } return 0; } });
                    
                }else if(this.suffix.getValue().booleanValue()) {
                        Collections.sort(mods, new Comparator<Module>() { @Override
                        public int compare(Module m1, Module m2) { if (font.getWidth(m1.getName()+m1.getSuffix()+m1.getBindName()) > font.getWidth(m2.getName()+m2.getSuffix()+m2.getBindName())) { return -1; } if (font.getWidth(m1.getName()+m1.getSuffix()+m1.getBindName()) < font.getWidth(m2.getName()+m2.getSuffix()+m2.getBindName())) { return 1; } return 0; } });
                }
                float rectX = 90f;
                int i = 0 , i2 = 0, i3 = 0;
                float anim = 0;
                float keyBindsX = UIDrag.x;
                float keyBindsY = UIDrag.y;
                for (Module m : mods) {
                    if(m.hasBind()) {
                    	if(m.isEnabled()) {
                        	++anim;
                        	i3++;	
                    	} else {
                    		--anim;
                    	}
                    }
                }
                RenderUtil.R2DUtils.drawRoundedRect(keyBindsX-2, keyBindsY - 2,keyBindsX+90+2, keyBindsY+11+2+i3*9,new Color(5,5,5,95).getRGB(), new Color(5,5,5,95).getRGB());
                for (Module m : mods) {
                    String text =  m.getName();
                    String text2 =  m.getName()+m.getSuffix();
//            		 if(!m.getType().equals(ModuleType.Render))
                    if(m.hasBind()) {
                    	i2++;
                    }
                    if(m.isEnabled() && m.hasBind()) {
                    	i++;
                        if(this.suffix.getValue().booleanValue()) {
                            	font.drawString(m.getBindName()+text2, keyBindsX+90-font.getWidth(text2+m.getBindName()), keyBindsY+11+y2, -1);
                        }else if(!this.suffix.getValue().booleanValue()){
                            	font.drawString(m.getBindName()+text, keyBindsX+90-font.getWidth(m.getBindName()+text), keyBindsY+11+y2, -1);
                        }
                        y2+=9;
                    }
                }
                RenderUtil.R2DUtils.drawRoundedRect(keyBindsX, keyBindsY,keyBindsX+90, keyBindsY+11, new Color(245,245,245).getRGB(), new Color(245,245,245).getRGB());
                font.drawString(i+"/"+i2+" Keybinds", keyBindsX +20, keyBindsY+2, new Color(3,3,3).getRGB());
            }
            if(shouldShowINFO) {
                drawPotionStatus(new ScaledResolution(this.mc));
            }
        }
    }

    public ArrayList<PotionTime> potionlist = new ArrayList<PotionTime>();

    public PotionTime getPotionTimeByPotion(Potion potion){
        if(potionlist == null) {
            return null;
        }
        for(PotionTime po : potionlist){
            if(po.po.getId() == potion.getId()){
                return po;
            }
        }
        return null;
    }

    private void drawPotionStatus(ScaledResolution sr) {
    	
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 16");
        List<PotionEffect> potions = new ArrayList<>();
        for (Object o : mc.thePlayer.getActivePotionEffects()) {
            potions.add((PotionEffect) o);
        }
        potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj
                .getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

        float pX = -2;
        for (PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            String PType = "";
                PType = PType + " " + Potion.getDurationString(effect);
            	String thingy = "no";
            	double minutes = -1.0;
                double seconds = -2.0;
                try {
                    minutes = Integer.parseInt((String)Potion.getDurationString((PotionEffect)effect).split(":")[0]);
                    seconds = Integer.parseInt((String)Potion.getDurationString((PotionEffect)effect).split(":")[1]);
                } catch (Exception e) {
                    thingy = "**:**";
                }
                double total = minutes * 60.0 + seconds;
                PotionTime po = new PotionTime(potion,(int)total);
                if(!potionlist.contains(po)){
                    potionlist.add(po);
                }
                if(getPotionTimeByPotion(potion) == null){
                    return;
                }
                if (getPotionTimeByPotion(potion).maxtime == 0 || total > getPotionTimeByPotion(potion).maxtime) {
                    getPotionTimeByPotion(potion).maxtime = (int)total;
                }
                double max = 0.0;
                double percentofmax = 0.0;
                double percentof25 = 25.0;
                if (total >= 0.0) {
                    max = getPotionTimeByPotion(potion).maxtime;
                    percentofmax = total / max;
                    percentof25 = 25.0;
                    if(percentofmax >= 1.0) {
                    	percentofmax = 1.0;
                    }
                    if (percentofmax < 1.0) {
                        percentof25 =percentofmax;
                    }
                }
//                System.out.println(percentofmax);
            RenderUtil.R2DUtils.drawRect(sr.getScaledWidth()/2 +96+pX,  sr.getScaledHeight()-25, sr.getScaledWidth()/2 +122+pX, sr.getScaledHeight(), new Color(5,5,5,100).getRGB());
            RenderUtil.R2DUtils.drawRect(sr.getScaledWidth()/2 +96+pX,  sr.getScaledHeight()-25*percentof25, sr.getScaledWidth()/2 +122+pX, sr.getScaledHeight(), new Color(255,255,255,250).getRGB());
            
            if (potion.hasStatusIcon()) {
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                int var10 = potion.getStatusIconIndex();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                mc.getTextureManager().bindTexture(location);
                mc.ingameGUI.drawTexturedModalRect(sr.getScaledWidth()/2 +100+pX, sr.getScaledHeight()-20, var10 % 8 * 18, 198 + var10 / 8 * 18, 18, 18);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
            pX += 30;
        }
        /*
		int var1 = 1;
		int var2 = 1;
		Collection c = this.mc.thePlayer.getActivePotionEffects();
		if (!c.isEmpty()) {
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glDisable(2896);
			int var6 = -10;
            Iterator i = this.mc.thePlayer.getActivePotionEffects().iterator();
            while (i.hasNext()) {
            	PotionEffect pe = (PotionEffect) i.next();
            	Potion var9 = Potion.potionTypes[pe.getPotionID()];
            	GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            	String thingy = "no";
            	double minutes = -1.0;
                double seconds = -2.0;
                try {
                    minutes = Integer.parseInt((String)Potion.getDurationString((PotionEffect)pe).split(":")[0]);
                    seconds = Integer.parseInt((String)Potion.getDurationString((PotionEffect)pe).split(":")[1]);
                } catch (Exception e) {
                    thingy = "**:**";
                }
                double total = minutes * 60.0 + seconds;
                if (var9.maxtimer == 0 || total > (double)var9.maxtimer) {
                	var9.maxtimer = (int)total;
                }
                double max = 0.0;
                double percentofmax = 0.0;
                double percentof360 = 360.0;
                if (total >= 0.0) {
                    max = var9.maxtimer;
                    percentofmax = total / max * 100.0;
                    percentof360 = 360.0;
                    if (percentofmax < 100.0) {
                        percentof360 = 3.6 * percentofmax;
                    }
                }
                if (var9.hasStatusIcon()) {
                    int n = var9.getStatusIconIndex();
                }
                String var12 = StatCollector.translateToLocal((String)var9.getName());
                String var11 = Potion.getDurationString((PotionEffect)pe);
                String name = String.valueOf((Object)var12) + " ?(?" + var11 + "?)";
                var2 += var6;
            }
		}
		 */
    }
}
