package zelix.cc.client.command.cmds;

import zelix.cc.client.Zelix;
import zelix.cc.client.command.Command;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.modules.Module;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.notification.NotificationUtil;

import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    public Bind() {
        super("Bind");
    }
    @Override
    public void execute(String[] args){
        Module moduleToBind = null;
        for(Module module: Zelix.moduleManager.modules){
            if(args[1].toLowerCase().contains(module.name.toLowerCase()))
                moduleToBind = module;
        }
        if(moduleToBind != null){
            if(args.length < 3)
            {
            	NotificationUtil.sendClientMessage("Bind", new String[] {"Invalid args, check again..."}, Notification.Type.ERROR);
                return;
            }
            int k = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            moduleToBind.setBoundKey(k);
            Object[] arrobject = new Object[2];
            arrobject[0] = moduleToBind.getName();
            arrobject[1] = k == 0 ? "none" : args[2].toUpperCase();
        	NotificationUtil.sendClientMessage("Bind", new String[] {String.format("Bound %s to %s", arrobject)}, Notification.Type.SUCCESS);
        }else{
        	NotificationUtil.sendClientMessage("Bind", new String[] {"Invalid module name, check again..."}, Notification.Type.ERROR);
        }
    }
}
