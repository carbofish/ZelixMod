package zelix.cc.client.command.cmds;

import zelix.cc.client.Zelix;
import zelix.cc.client.command.Command;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.modules.Module;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.notification.NotificationUtil;
import net.minecraft.util.EnumChatFormatting;

public class Toggle
extends Command {
    public Toggle() {
        super("Toggle");
    }
    @Override
    public void execute(String[] args){
        Module mod = Zelix.getInstance().moduleManager.getModuleByName(args[1]);
        if(mod != null){
            mod.setState(!mod.state);
        	NotificationUtil.sendClientMessage("Toggle", new String[] {"> Set " +mod.name + " to "+(mod.state?(EnumChatFormatting.GREEN+"Enabled"):(EnumChatFormatting.RED+"Disabled"))}, Notification.Type.SUCCESS);
            if(mod.name.toLowerCase().contains("command"))
            	NotificationUtil.sendClientMessage("Toggle", new String[] {"Type [Command] to make command enabled again."}, Notification.Type.ERROR);
        }else{
        	NotificationUtil.sendClientMessage("Toggle", new String[] {"Invalid module name."}, Notification.Type.ERROR);
        }
    }
}
