package zelix.cc.client.command.cmds;

import org.lwjgl.input.Keyboard;

import zelix.cc.client.Zelix;
import zelix.cc.client.command.Command;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.modules.Module;
import zelix.cc.client.utils.notification.NotificationUtil;

public class ClientName extends Command {
    public ClientName() {
        super("Name");
    }
    
    @Override
    public void execute(String[] args){
    	StringBuffer sBuffer = new StringBuffer("");
        if(args.length < 2) {
        	NotificationUtil.sendClientMessage("ClientName", new String[] {"Invalid args, check again..."}, Notification.Type.ERROR);
            return;
        }
        if(args.length >2) {
        	for(int i = 1; i< args.length; i++) {
        		if(i == 1)
        			sBuffer.append(args[1]);
        		else
        		sBuffer.append(" "+args[i]);
        	}
        	Zelix.instance.clientName = sBuffer.toString();
        }else {
            Zelix.instance.clientName = args[1];
        }
    	NotificationUtil.sendClientMessage("ClientName", new String[] {"You change the client name to", Zelix.instance.clientName}, Notification.Type.SUCCESS);
    }
}
