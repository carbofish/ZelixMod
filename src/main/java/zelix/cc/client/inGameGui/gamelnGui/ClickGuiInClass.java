package zelix.cc.client.inGameGui.gamelnGui;

import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
//import javafx.scene.transform.Translate;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class ClickGuiInClass
extends GuiScreen {
    public ModuleType category;
    public Module cheat;
    public String name;
    public boolean change;
    public int index;
    public int animX = RenderUtil.width() ,
            anim2 = RenderUtil.width() -230+8+getPPFontWidth("Menu")/2,
            anim3 = RenderUtil.width() -230+8+getPPFontWidth("Menu")/2,
            anim4 = RenderUtil.width() -230+222-getPPFontWidth("Settings")/2,
            anim5 = RenderUtil.width() -230+222-getPPFontWidth("Settings")/2,
            anim6 = 15,
            anim7 = 15,
            anim8,
            anim9,
            anim10,
            anim11;
    public int remander;
    public static boolean shouldshow = false;
    int CatValue = 0;// 1:combat 2:move 3:render 4:player 5:world
    int ModValue = 0;
    int x,y,x2,y2,valueX,valueY;
    private float scrollY , scrollY1 , scrollY2 , scrollY3 , scrollY4;
    public boolean bind;
    public boolean custom = false;
    private final ResourceLocation combat = new ResourceLocation("Eliru/clickgui/Combat_64.png");
    private final ResourceLocation render = new ResourceLocation("Eliru/clickgui/Render_64.png");
    private final ResourceLocation move = new ResourceLocation("Eliru/clickgui/Movement_64.png");
    private final ResourceLocation player = new ResourceLocation("Eliru/clickgui/Player_64.png");
    private final ResourceLocation world = new ResourceLocation("Eliru/clickgui/world.png");
    TimerUtil timer = new TimerUtil();
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        this.mc.entityRenderer.isShaderActive();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
        this.x = RenderUtil.width() -230;
        this.y = 0;
        if(this.timer.isDelayComplete(1L) && this.animX >=this.x) {
            this.animX -=8;
            this.x = this.animX;
            timer.reset();
        }
        GlStateManager.pushMatrix();
        RenderUtil.R2DUtils.drawRoundedRect(this.animX, this.y, RenderUtil.width(), RenderUtil.height(), new Color(25,25,25,200).getRGB(), new Color(25,25,25,200).getRGB());
        RenderUtil.drawBorderedRect(this.animX, this.y+25, RenderUtil.width(), RenderUtil.height(), 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        GlStateManager.popMatrix();

//        RenderUtil.drawGradientSideways(this.x, this.y+16, this.x+150, this.y+17, new Color(72,172,255).getRGB(), new Color(102,172,255).getRGB());
        font.drawString("Menu", this.x+8, this.y+7, this.ModValue != 1 ? new Color(255,255,255).getRGB() : new Color(102,172,255).getRGB());
        font.drawString("Settings", this.x+222-font.getWidth("Settings"), this.y+7,this.ModValue != 2 ? new Color(255,255,255).getRGB() : new Color(102,172,255).getRGB());
//    	RenderUtil.drawBorderedRect(this.x+8, this.y+4, this.x+8+font.getWidth("Menu"), this.y +7+font.getHeight("Menu"), 1, new Color(102,172,255).getRGB(), new Color(102,172,255).getRGB());

        if(this.ModValue == 1) {
            this.anim4 = RenderUtil.width() -230+222-getPPFontWidth("Settings")/2;
            this.anim5 = RenderUtil.width() -230+222-getPPFontWidth("Settings")/2;
            if(this.timer.isDelayComplete(1L)) {
                if(this.anim2>=this.x+8) {
                    this.anim2 -= 3;
                }
                if(this.anim3<=this.x+8+font.getWidth("Menu")) {
                    this.anim3 += 3;
                }
                timer.reset();
            }
            RenderUtil.drawBorderedRect(this.anim2, this.y+18, this.anim3, this.y+19, 1, new Color(102,172,255).getRGB(), new Color(102,172,255).getRGB());
        }else if(this.ModValue == 2){
            this.anim2 = RenderUtil.width() -230+8+getPPFontWidth("Menu")/2;
            this.anim3 = RenderUtil.width() -230+8+getPPFontWidth("Menu")/2;
            if(this.timer.isDelayComplete(1L)) {
                if(this.anim4 >= this.x+222-font.getWidth("Settings")) {
                    this.anim4 -= 3;
                }
                if(this.anim5 <= this.x+222) {
                    this.anim5 += 3;
                }
                timer.reset();
            }
            RenderUtil.drawBorderedRect(this.anim4, this.y+18, this.anim5, this.y+19, 1, new Color(102,172,255).getRGB(), new Color(102,172,255).getRGB());
        }
        if(ModValue == 2) {
            drawSettings(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public int getPPFontWidth(String name) {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 20");
        int width= (int) font.getWidth(name);
        return width;
    }
    public void drawValue(Value value, int mouseX , int mouseY, int x, int y) {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 18");
    	FontRendererUtil numbersFont = Zelix.instance.fontManager.getFont("CONS 16");
//        CFontRenderer font = FontLoaders.fy18;
//        CFontRenderer numbersFont = FontLoaders.fy16;
        RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+font.getHeight(value.displayName)+18, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
        if(value instanceof Option) {
            font.drawStringWithShadow(value.displayName, this.x+25, this.y2, new Color(255,255,255).getRGB());
        } else if(value instanceof Mode) {
            font.drawStringWithShadow(value.displayName + ":", this.x+25, this.y2, new Color(255,255,255).getRGB());
        } else if(value instanceof zelix.cc.client.eventAPI.api.Amount) {
            font.drawStringWithShadow(value.displayName + ":", this.x+25, this.y2, new Color(255,255,255).getRGB());
        }
        if(value instanceof Option) {
            if((Boolean)((Option)value).getValue()) {
                RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/Option_On.png"), this.x+27, this.y2+10, 10, 10);
            } else if(!(Boolean)((Option)value).getValue()){
                RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/Option_Off.png"), this.x+27, this.y2+10, 10, 10);
            }
            try {
                if(mouseX >= this.x+27 && mouseX <= this.x+57 && mouseY >= this.y2+9 && mouseY <= this.y2+22.5f && Mouse.isButtonDown(0)) {
                    final Option v = (Option)value;
                    v.setValue(!(boolean)v.getValue());
                    Mouse.destroy();
                    Mouse.create();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        if(value instanceof Mode) {
            final Mode m2 = (Mode)value;
            final Enum current2 = (Enum)m2.getValue();
            final int current3 = current2.ordinal()+1;
            RenderUtil.R2DUtils.drawRoundedRect(this.x+25, this.y2+8, this.x+198, this.y2+24,  new Color(75,75,75,135).getRGB(), new Color(75,75,75,135).getRGB());
            this.name = "" + ((Mode)value).getValue();
            font.drawStringWithShadow(this.name, this.x+27, this.y2+14, new Color(72,192,255).getRGB());

            font.drawStringWithShadow(""+current3+"/"+m2.getModes().length, this.x+196-font.getWidth(""+current3+"/"+m2.getModes().length), this.y2+14, new Color(255,255,255).getRGB());
            try {
                if(mouseX >= this.x+25 && mouseX <= this.x+198 && mouseY >= this.y2+6 && mouseY <= this.y2+25f && Mouse.isButtonDown(0)) {
                    final Mode m = (Mode)value;
                    final Enum current = (Enum)m.getValue();
                    final int next = (current.ordinal() + 1 >= m.getModes().length) ? 0 : (current.ordinal() + 1);
                    value.setValue(m.getModes()[next]);
//                    System.out.println("DILRHGUISRTHU");//debug
                    Mouse.destroy();
                    Mouse.create();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        zelix.cc.client.eventAPI.api.Amount v2;
        if(value instanceof zelix.cc.client.eventAPI.api.Amount) {
            this.name = String.valueOf(String.valueOf(this.name)) + ((v2 = (zelix.cc.client.eventAPI.api.Amount)value).isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
            v2 = (zelix.cc.client.eventAPI.api.Amount)value;
            this.name = "" + (v2.isInteger() ? (double)((Number)v2.getValue()).intValue() : ((Number)v2.getValue()).doubleValue());
            if (Mouse.isButtonDown(0) && mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= this.y2-6 && mouseY <= y2+4+font.getHeight(value.displayName)+18) {
                double min = ((Number)v2.min).doubleValue();
                double max = ((Number)v2.max).doubleValue();
                double inc = ((Number)v2.inc).doubleValue();
                double valAbs = (double)mouseX - ((double)this.x+25);
                double perc = valAbs / 150;
                perc = Math.min(Math.max(0.0, perc), 1.0);
                double valRel = (max - min) * perc;
                double val = min + valRel;
                val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                v2.setValue(val);
            }
        }
        if(value instanceof zelix.cc.client.eventAPI.api.Amount) {
            v2 = (zelix.cc.client.eventAPI.api.Amount)value;
            float render = 150f * (((Number)v2.getValue()).floatValue() - ((Number)v2.min).floatValue()) / (((Number)v2.max).floatValue() - ((Number)v2.min).floatValue());
//             RenderUtil.drawRect((float) (this.x + render+1), this.y + mfont.getHeight(this.value.getName()) +1.5, (float)(this.x + render+5), this.y + mfont.getHeight(this.value.getName())+5.5,new Color(255,255,255).getRGB());
            RenderUtil.drawBorderedRect(this.x+25, this.y2+13, this.x+26+150, this.y2 +14,1,
                    new Color(62, 132, 255,0).getRGB(),new Color(205,205,205,175).getRGB());


            RenderUtil.drawBorderedRect(this.x+25, this.y2+13, this.x+26+render, this.y2 +14,1,
                    new Color(62, 132, 255,0).getRGB(),new Color(255,255,255).getRGB());//������


            RenderUtil.drawBorderedRect(this.x+23+render, this.y2+10, this.x+30+render, this.y2 +17,1,
                    new Color(62, 132, 255,0).getRGB(),new Color(0,168,255).getRGB());//����
            numbersFont.drawStringWithShadow(this.name, this.x+25+font.getWidth(value.displayName+": "), this.y2+1,new Color(72,192,255).getRGB());
        }
    }
    public void drawCat(int mouseX, int mouseY, float partialTicks) {
//        CFontRenderer font = FontLoaders.fy28;
        RenderUtil.drawBorderedRect(this.animX, this.y+25, this.animX+22, RenderUtil.height(), 1,new Color(50,50,50,0).getRGB(), new Color(50,50,50,80).getRGB());
        boolean onCombat = mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+30 && mouseY < this.y + 30+16;
        boolean onMove = mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+56 && mouseY < this.y + 56+16;
        boolean onRender = mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+82 && mouseY < this.y + 82+16;
        boolean onPlayer = mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+108 && mouseY < this.y + 108+16;
        boolean onWorld = mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+134 && mouseY < this.y + 134+16;
        if(CatValue == 1) {
            RenderUtil.drawBorderedRect(this.x-2, this.y+28, this.x, this.y+48, 1, new Color(102,172,255,0).getRGB(), new Color(102,172,255).getRGB());
//        	RenderUtil.drawBorderedRect(this.x, this.y+28, this.x+20, this.y+48, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        }else if(CatValue == 2) {
            RenderUtil.drawBorderedRect(this.x-2, this.y+54, this.x, this.y+74, 1, new Color(102,172,255,0).getRGB(), new Color(102,172,255).getRGB());
//        	RenderUtil.drawBorderedRect(this.x, this.y+54, this.x+20, this.y+74, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        }else if(CatValue == 3) {
            RenderUtil.drawBorderedRect(this.x-2, this.y+80, this.x, this.y + 84+16, 1, new Color(102,172,255,0).getRGB(), new Color(102,172,255).getRGB());
//        	RenderUtil.drawBorderedRect(this.x, this.y+80, this.x+20, this.y + 84+16, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        }else if(CatValue == 4) {
            RenderUtil.drawBorderedRect(this.x-2, this.y+106, this.x, this.y+110+16, 1, new Color(102,172,255,0).getRGB(), new Color(102,172,255).getRGB());
//        	RenderUtil.drawBorderedRect(this.x, this.y+106, this.x+20, this.y+110+16, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        }else if(CatValue == 5) {
            RenderUtil.drawBorderedRect(this.x-2, this.y+132, this.x, this.y+136+16, 1, new Color(102,172,255,0).getRGB(), new Color(102,172,255).getRGB());
//        	RenderUtil.drawBorderedRect(this.x, this.y+132, this.x+20, this.y+136+16, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,200,30).getRGB());
        }
        if(onCombat) {
            RenderUtil.drawBorderedRect(this.x, this.y+28, this.x+20, this.y+48, 1,new Color(200,200,200,0).getRGB(), new Color(200,200,199,80).getRGB());
        } else if(onMove) {
            RenderUtil.drawBorderedRect(this.x, this.y+54, this.x+20, this.y+74, 1,new Color(200,200,200,0).getRGB(), new Color(200,199,200,80).getRGB());
        } else if(onRender) {
            RenderUtil.drawBorderedRect(this.x, this.y+80, this.x+20, this.y + 84+16, 1,new Color(200,200,200,0).getRGB(), new Color(199,200,200,80).getRGB());
        } else if(onPlayer) {
            RenderUtil.drawBorderedRect(this.x, this.y+106, this.x+20, this.y+110+16, 1,new Color(200,200,200,0).getRGB(), new Color(198,200,200,80).getRGB());
        } else if(onWorld) {
            RenderUtil.drawBorderedRect(this.x, this.y+132, this.x+20, this.y+136+16, 1,new Color(200,200,200,0).getRGB(), new Color(200,198,200,80).getRGB());
        }
        GlStateManager.pushMatrix();
        GL11.glColor3d(1.0, 1.0, 1.0);
        if(CatValue == 1) {
            GL11.glColor3d(0.4, 0.67, 1.0);
        }
        if(this.ModValue == 2) {
            RenderUtil.drawIcon(this.x+2, this.y+30, 16, 16, this.combat);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glColor3d(1.0, 1.0, 1.0);
        if(CatValue == 2) {
            GL11.glColor3d(0.4, 0.67, 1.0);
        }
        if(this.ModValue == 2) {
            RenderUtil.drawIcon(this.x+2, this.y+56, 16, 16, this.move);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glColor3d(1.0, 1.0, 1.0);
        if(CatValue == 3) {
            GL11.glColor3d(0.4, 0.67, 1.0);
        }
        if(this.ModValue == 2) {
            RenderUtil.drawIcon(this.x+2, this.y+82, 16, 16, this.render);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glColor3d(1.0, 1.0, 1.0);
        if(CatValue == 4) {
            GL11.glColor3d(0.4, 0.67, 1.0);
        }
        if(this.ModValue == 2) {
            RenderUtil.drawIcon(this.x+2, this.y+108, 16, 16, this.player);
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glColor3d(1.0, 1.0, 1.0);
        if(CatValue == 5) {
            GL11.glColor3d(0.4, 0.67, 1.0);
        }
        if(this.ModValue == 2) {
            RenderUtil.drawIcon(this.x+2, this.y+134, 16, 16, this.world);
        }
        GlStateManager.popMatrix();
    }
//    Translate translate = new Translate(0, 0);
    public void drawSettings(int mouseX, int mouseY, float partialTicks) {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 18");
    	FontRendererUtil numbersFont = Zelix.instance.fontManager.getFont("CONS 16");
//        CFontRenderer font = FontLoaders.fy18;
//        CFontRenderer numbersFont = FontLoaders.fy16;
        int y222;
        this.y2 = this.y+55;
        boolean hasNumbers = false;
        int ButtonX = 0;
        this.drawCat(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(0, 25, RenderUtil.width(), RenderUtil.height());
        if(CatValue == 1) {
            GL11.glTranslatef(0.0f, this.scrollY, 0.0f);
            mouseY -= (int)this.scrollY;
        }else if(CatValue == 2) {
            GL11.glTranslatef(0.0f, this.scrollY1, 0.0f);
            mouseY -= (int)this.scrollY1;
        }else if(CatValue == 3) {
            GL11.glTranslatef(0.0f, this.scrollY2, 0.0f);
            mouseY -= (int)this.scrollY2;
        }else if(CatValue == 4) {
            GL11.glTranslatef(0.0f, this.scrollY3, 0.0f);
            mouseY -= (int)this.scrollY3;
        }else if(CatValue == 5) {
            GL11.glTranslatef(0.0f, this.scrollY4, 0.0f);
            mouseY -= (int)this.scrollY4;
        }
        for (Module c2 : Zelix.moduleManager.getModules()) {
            this.cheat = c2;
            if(CatValue == 1 && this.cheat.moduleType.equals(ModuleType.Combat)) {
                for(Value value : this.cheat.value) {
                    this.drawValue(value,mouseX,mouseY,this.x,this.y2);
                    this.y2+=35;
                }
                if(this.cheat.value.size() == 0) {
                    RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+14, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
                    font.drawStringWithShadow("No Settings", this.x+25, y2+6,  new Color(152,152,152,155).getRGB());
                }
//                	this.valueY = this.cheat.value.size()*15-15;
                RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(17,17,17,157).getRGB(), new Color(17,17,17,157).getRGB());
//            		this.anim6 = 0;
//            		this.anim7 = 0;
                if(this.cheat.isEnabled()) {
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_On.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+170+this.anim6,  y2-this.cheat.value.size()*35-18, 10, 10);
//                        	RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(102,172,255,35).getRGB(), new Color(102,172,255,35).getRGB());
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(0,168,255).getRGB());
                }else if(!this.cheat.isEnabled()){
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_Off.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+185-this.anim7,  y2-this.cheat.value.size()*35-18, 10, 10);
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(157,157,157).getRGB());
                }
//                	font.drawStringWithShadow(""+this.cheat.value.size(), 25, y2, -1);
                final float scroll = Mouse.getDWheel();
                this.scrollY += scroll / 5.0f;
                if (this.scrollY > 0.0f) {
                    this.scrollY = 0.0f;
                }
//                    if(this.scrollY <=-1640) {
//                    	this.scrollY =-1640;
//                    }
//                    System.out.println(""+this.scrollY);
                try {
                    if(mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= y2-6-this.cheat.value.size()*35-15 && mouseY <= y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15 && Mouse.isButtonDown(0)) {
                        this.cheat.setState(!this.cheat.isEnabled());
                        Mouse.destroy();
                        Mouse.create();
                    }
                } catch (Exception e) {
                }
                y2+= 60;

            }else if(CatValue == 2 && this.cheat.moduleType.equals(ModuleType.Motion)) {
                for(Value value : this.cheat.value) {
                    this.drawValue(value,mouseX,mouseY,this.x,this.y2);
                    this.y2+=35;
                }
                if(this.cheat.value.size() == 0) {
                    RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+14, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
                    font.drawStringWithShadow("No Settings", this.x+25, y2+6,  new Color(152,152,152,155).getRGB());
                }
//                	this.valueY = this.cheat.value.size()*15-15;
                RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(17,17,17,157).getRGB(), new Color(17,17,17,157).getRGB());
//            		this.anim6 = 0;
//            		this.anim7 = 0;
                if(this.cheat.isEnabled()) {
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_On.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+170+this.anim6,  y2-this.cheat.value.size()*35-18, 10, 10);
//                        	RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(102,172,255,35).getRGB(), new Color(102,172,255,35).getRGB());
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(0,168,255).getRGB());
                }else if(!this.cheat.isEnabled()){
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_Off.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+185-this.anim7,  y2-this.cheat.value.size()*35-18, 10, 10);
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(157,157,157).getRGB());
                }
//                	font.drawStringWithShadow(""+this.cheat.value.size(), 25, y2, -1);
                final float scroll = Mouse.getDWheel();
                this.scrollY1 += scroll / 5.0f;
                if (this.scrollY1 > 0.0f) {
                    this.scrollY1 = 0.0f;
                }
//                    if(this.scrollY1 <=-1280) {
//                    	this.scrollY1 =-1280;
//                    }
//                    System.out.println(""+this.scrollY);
                try {
                    if(mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= y2-6-this.cheat.value.size()*35-15 && mouseY <= y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15 && Mouse.isButtonDown(0)) {
                        this.cheat.setState(!this.cheat.isEnabled());
                        Mouse.destroy();
                        Mouse.create();
                    }
                } catch (Exception e) {
                }
                y2+= 60;
            }else if(CatValue == 3 && this.cheat.moduleType.equals(ModuleType.Render)) {
                for(Value value : this.cheat.value) {
                    this.drawValue(value,mouseX,mouseY,this.x,this.y2);
                    this.y2+=35;
                }
                if(this.cheat.value.size() == 0) {
                    RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+14, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
                    font.drawStringWithShadow("No Settings", this.x+25, y2+6,  new Color(152,152,152,155).getRGB());
                }
//                	this.valueY = this.cheat.value.size()*15-15;
                RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(17,17,17,157).getRGB(), new Color(17,17,17,157).getRGB());
//            		this.anim6 = 0;
//            		this.anim7 = 0;
                if(this.cheat.isEnabled()) {
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_On.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+170+this.anim6,  y2-this.cheat.value.size()*35-18, 10, 10);
//                        	RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(102,172,255,35).getRGB(), new Color(102,172,255,35).getRGB());
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(0,168,255).getRGB());
                }else if(!this.cheat.isEnabled()){
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_Off.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+185-this.anim7,  y2-this.cheat.value.size()*35-18, 10, 10);
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(157,157,157).getRGB());
                }
//                	font.drawStringWithShadow(""+this.cheat.value.size(), 25, y2, -1);
                final float scroll = Mouse.getDWheel();
                this.scrollY2 += scroll / 5.0f;
                if (this.scrollY2 > 0.0f) {
                    this.scrollY2 = 0.0f;
                }
//                    if(this.scrollY2 <=-1616) {
//                    	this.scrollY2 =-1616;
//                    }
//                    System.out.println(""+this.scrollY);
                try {
                    if(mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= y2-6-this.cheat.value.size()*35-15 && mouseY <= y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15 && Mouse.isButtonDown(0)) {
                        this.cheat.setState(!this.cheat.isEnabled());
                        Mouse.destroy();
                        Mouse.create();
                    }
                } catch (Exception e) {
                }
                y2+= 60;
            }else if(CatValue == 4 && this.cheat.moduleType.equals(ModuleType.Player)) {
                for(Value value : this.cheat.value) {
                    this.drawValue(value,mouseX,mouseY,this.x,this.y2);
                    this.y2+=35;
                }
                if(this.cheat.value.size() == 0) {
                    RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+14, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
                    font.drawStringWithShadow("No Settings", this.x+25, y2+6,  new Color(152,152,152,155).getRGB());
                }
//                	this.valueY = this.cheat.value.size()*15-15;
                RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(17,17,17,157).getRGB(), new Color(17,17,17,157).getRGB());
//            		this.anim6 = 0;
//            		this.anim7 = 0;
                if(this.cheat.isEnabled()) {
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_On.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+170+this.anim6,  y2-this.cheat.value.size()*35-18, 10, 10);
//                        	RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(102,172,255,35).getRGB(), new Color(102,172,255,35).getRGB());
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(0,168,255).getRGB());
                }else if(!this.cheat.isEnabled()){
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_Off.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+185-this.anim7,  y2-this.cheat.value.size()*35-18, 10, 10);
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(157,157,157).getRGB());
                }
//                	font.drawStringWithShadow(""+this.cheat.value.size(), 25, y2, -1);
                final float scroll = Mouse.getDWheel();
                this.scrollY3 += scroll / 5.0f;
                if (this.scrollY3 > 0.0f) {
                    this.scrollY3 = 0.0f;
                }
//                    if(this.scrollY3 <=-896) {
//                    	this.scrollY3 =-896;
//                    }
//                    System.out.println(""+this.scrollY);
                try {
                    if(mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= y2-6-this.cheat.value.size()*35-15 && mouseY <= y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15 && Mouse.isButtonDown(0)) {
                        this.cheat.setState(!this.cheat.isEnabled());
                        Mouse.destroy();
                        Mouse.create();
                    }
                } catch (Exception e) {
                }
                y2+= 60;
            }else if(CatValue == 5 && this.cheat.moduleType.equals(ModuleType.World)) {
                for(Value value : this.cheat.value) {
                    this.drawValue(value,mouseX,mouseY,this.x,this.y2);
                    this.y2+=35;
                }
                if(this.cheat.value.size() == 0) {
                    RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6, this.x+200, y2+4+14, new Color(45,45,45,155).getRGB(), new Color(45,45,45,155).getRGB());
                    font.drawStringWithShadow("No Settings", this.x+25, y2+6,  new Color(152,152,152,155).getRGB());
                }
//                	this.valueY = this.cheat.value.size()*15-15;
                RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(17,17,17,157).getRGB(), new Color(17,17,17,157).getRGB());
//            		this.anim6 = 0;
//            		this.anim7 = 0;
                if(this.cheat.isEnabled()) {
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_On.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+170+this.anim6,  y2-this.cheat.value.size()*35-18, 10, 10);
//                        	RenderUtil.R2DUtils.drawRoundedRect(this.x+23, y2-6-this.cheat.value.size()*35-15, this.x+200, y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15, new Color(102,172,255,35).getRGB(), new Color(102,172,255,35).getRGB());
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(0,168,255).getRGB());
                }else if(!this.cheat.isEnabled()){
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ButtonRect_Off.png"), this.x+170,  y2-this.cheat.value.size()*35-16, 25, 6);
                    RenderUtil.drawImage(new ResourceLocation("Eliru/ZelixClickgui/ModButton.png"),this.x+185-this.anim7,  y2-this.cheat.value.size()*35-18, 10, 10);
                    font.drawStringWithShadow(this.cheat.getName(), this.x+25, y2-this.cheat.value.size()*35-15, new Color(157,157,157).getRGB());
                }
//                	font.drawStringWithShadow(""+this.cheat.value.size(), 25, y2, -1);
                final float scroll = Mouse.getDWheel();
                this.scrollY4 += scroll / 5.0f;
                if (this.scrollY4 > 0.0f) {
                    this.scrollY4 = 0.0f;
                }
//                    if(this.scrollY4 <=-1000) {
//                    	this.scrollY4 =-1000;
//                    }
//                    System.out.println(""+this.scrollY);
                try {
                    if(mouseX >= this.x+23 && mouseX <= this.x+200 && mouseY >= y2-6-this.cheat.value.size()*35-15 && mouseY <= y2+4+font.getHeight(this.cheat.getName())-this.cheat.value.size()*35-15 && Mouse.isButtonDown(0)) {
                        this.cheat.setState(!this.cheat.isEnabled());
                        Mouse.destroy();
                        Mouse.create();
                    }
                } catch (Exception e) {
                }
                y2+= 60;
            }
        }

        GL11.glDisable(3089);
        GlStateManager.popMatrix();
//            if(this.scrollY<-620) {
//                this.scrollY = -620;
//            }

//        RenderUtil.R2DUtils.drawRoundedRect(this.x+22, this.y+25, RenderUtil.width(), RenderUtil.height(), -1, -1);
    }
    public void drawMusic(int mouseX, int mouseY, float partialTicks) {
    	/*
    	CFontRenderer font = FontLoaders.kiona25;
    	CFontRenderer font2 = FontLoaders.kiona20;
    	CFontRenderer font3 = FontLoaders.kiona16;
        Music muisc2 = (Music) Client.getModuleManager().getModuleByClass(Music.class);
    	boolean sound = false;
    	boolean musicFeel = false;
    	if(muisc2.isEnabled())
    		sound = true;
    	else {
			sound = false;
		}
    	RenderUtil.drawBorderedRect(this.x+20, this.y+90, this.x+200, this.y+290, 1, new Color(2,2,125,0).getRGB(), new Color(1,2,3,85).getRGB());
    	RenderUtil.drawBorderedRect(this.x+20, this.y+110, this.x+200, this.y+111, 1, new Color(2,2,125,0).getRGB(), new Color(67,157,253,235).getRGB());
    	RenderUtil.drawBorderedRect(this.x+20, this.y+250, this.x+200, this.y+290, 1, new Color(2,2,125,0).getRGB(), new Color(110,110,110,145).getRGB());
    	font.drawStringWithShadow("Zelix", this.x+25, this.y+98, -1);
    	font2.drawStringWithShadow("music", this.x+57, this.y+100, new Color(255,255,255,155).getRGB());
    	font.drawStringWithShadow(sound ?"ON" : "OFF", this.x+106, this.y+268, -1);
    	mc.fontRendererObj.drawStringWithShadow(Music.songName,this.x+100-font3.getWidth(Music.songName)/2, this.y+120, -1);
    	mc.fontRendererObj.drawStringWithShadow("---"+Music.songSinger,this.x+100-font3.getWidth("---"+Music.songSinger)/2, this.y+130, new Color(255,255,255,155).getRGB());
//        RenderUtil.drawFullCircle(this.x+110, this.y+270, 12, 270, 2, 360,new Color(155,155,155,155).getRGB());

        try {
			if(mouseX >= this.x+106 && mouseX <= this.x+140 && mouseY>=this.y+268 && mouseY <= this.y+278) {
				if(Mouse.isButtonDown(0)) {
                    muisc2.setEnabled(!muisc2.isEnabled());
                    Mouse.destroy();
                    Mouse.create();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
    }
    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.bind) {
            this.cheat.setBoundKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setBoundKey(0);
            }
            this.bind = false;
        }
        try {
            super.keyTyped(typedChar,keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
        try {
            if(mouseX > this.x+8 && mouseX < this.x + 8 +font.getWidth("Menu") && mouseY > this.y +1 && mouseY < this.y +10+font.getHeight("Menu")) {
                if(Mouse.isButtonDown(0)) {
                    this.ModValue = 1;
                    Mouse.destroy();
                    Mouse.create();
//                	System.out.println("debuggggg");
                }
            }else
            if(mouseX > this.x+222-font.getWidth("Settings") && mouseX < this.x+222 && mouseY > this.y +1 && mouseY < this.y +10+font.getHeight("Settings")) {
                if(Mouse.isButtonDown(0)) {
                    this.ModValue = 2;
                    Mouse.destroy();
                    Mouse.create();
//                	System.out.println("debuggggg");
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        if(ModValue == 2) {
            try {
                if(mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+30 && mouseY < this.y + 30+16 && Mouse.isButtonDown(0)) {
                    this.CatValue = 1;
                    Mouse.destroy();
                    Mouse.create();
                }else if(mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+56 && mouseY < this.y + 56+16 && Mouse.isButtonDown(0)) {
                    this.CatValue = 2;
                    Mouse.destroy();
                    Mouse.create();
                }else if(mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+82 && mouseY < this.y + 82+16 && Mouse.isButtonDown(0)) {
                    this.CatValue = 3;
                    Mouse.destroy();
                    Mouse.create();
                }else if(mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+108 && mouseY < this.y + 108+16 && Mouse.isButtonDown(0)) {
                    this.CatValue = 4;
                    Mouse.destroy();
                    Mouse.create();
                }else if(mouseX > this.x && mouseX < this.x+4+16 && mouseY > this.y+134 && mouseY < this.y + 134+16 && Mouse.isButtonDown(0)) {
                    this.CatValue = 5;
                    Mouse.destroy();
                    Mouse.create();
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

}
