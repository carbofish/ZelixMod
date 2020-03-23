package zelix.cc.client.utils.notification;

import java.util.ArrayList;

import net.minecraft.client.gui.ScaledResolution;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.utils.Util;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.animation.Translate;

public class NotificationUtil extends Util{
	   public static ArrayList notifications = new ArrayList();
	     static Translate translate = new Translate(0, 0);
	   public static void drawNotifications() {
		   int addY = 0;
		   ScaledResolution res = new ScaledResolution(mc);
		   for(int i = 0; i < notifications.size(); ++i) {
			   Notification notification = (Notification)notifications.get(i);
			   if (notification.shouldRemove()) {
				   notifications.remove(i);
			   }
			   notification.drawNotification(addY);
			   addY+= (23+4+13*notification.message.length) - 5 +4;
		   }
	   }
	   
	   public static void sendClientMessage(String titleMessage, String[] message, Notification.Type type) {
		      notifications.add(new Notification(titleMessage,message,type));
		   }
}
