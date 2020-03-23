package zelix.cc.client.inGameGui.notification;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.omg.CORBA.PUBLIC_MEMBER;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import zelix.cc.client.Zelix;
import zelix.cc.client.modules.combat.Aura;
import zelix.cc.client.utils.Color.ColorUtil;
import zelix.cc.client.utils.Color.Colors;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.animation.Translate;
import zelix.cc.client.utils.notification.NotificationUtil;

public class Notification {
	String titleMessage;
	public String[] message;
	TimerUtil timerUtil = new TimerUtil();
	FontRendererUtil titleFont = Zelix.instance.fontManager.getFont("CONS 24");
	FontRendererUtil infoFont = Zelix.instance.fontManager.getFont("CONS 20");
	ResourceLocation notification = new ResourceLocation("Zelix/texture/notification/notification.png");
	ResourceLocation textureType;
	public boolean shouldshow = false; 
	float x,y;
	float y2, opc = 255;
	long stayTime;
	int color;
	Type type;
	Translate translate = new Translate(0, 0);
	public Notification(String titleMessage, String[] message,Type type) {
		this.type = type;
		this.titleMessage = titleMessage;
		this.message = message;
		this.x = RenderUtil.width()/2- 100;
		this.y = -(23+4+13*this.message.length);
		this.y2 = 0;
		switch(type) {
		case ERROR:
			this.color = Colors.RED.color;
			stayTime = 1000L;
			break;
		case INFO:
			this.color = new Color(35,35,35).getRGB();
			stayTime = 1500L;
			break;
		case SUCCESS:
			this.color = Colors.GREEN.color;
			stayTime = 1000L;
			break;
		case WARNING:
			this.color = Colors.YELLOW.color;
			stayTime = 1000L;
			break;
		case CHAT:
			this.color = Colors.BLUE.color;
			stayTime = 1500L;
			break;
			}
		timerUtil.reset();
	}
	int opc2 = 75;
	public void drawNotification(int addY) {
			GlStateManager.pushMatrix();
			if(this.y != 0) {
				this.y = (float) RenderUtil.getAnimationState(this.y, 0, 120);
				GL11.glTranslatef(0.0f, this.y, 0.0f);	
			} else if(this.isFinished()){
				this.y2 = (float) RenderUtil.getAnimationState(this.y2, -(23+4+13*this.message.length), 120);
				GL11.glTranslatef(0.0f, this.y2, 0.0f);	
			}
			RenderUtil.R2DUtils.drawRoundedRect(this.x, addY+5,this.x+200.0f, addY+23+4+13*this.message.length,	this.color, this.color);
			RenderUtil.drawImage(this.notification, this.x+1, addY+6, 16, 16);
			titleFont.drawString(this.titleMessage,this.x+16, addY+7, ColorUtil.getColor(-1, (int)this.opc));	
			for(int i = 0; i<this.message.length; i++) {
				infoFont.drawString(this.message[i],this.x+1, addY+25+i*13, ColorUtil.getColor(-1, (int)this.opc));
			}

			GlStateManager.popMatrix();		
	}
	public boolean shouldRemove() {
		return isFinished()&& this.y2 == -(23+4+13*this.message.length);
	}
	public boolean isFinished() {
			return this.timerUtil.hasReached(stayTime);	
	}
	public boolean getShow() {
		return this.shouldshow;
	}
	public enum Type{
		SUCCESS,
		ERROR,
		WARNING,
		INFO,
		CHAT;
	}
}
