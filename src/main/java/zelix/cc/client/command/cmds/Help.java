package zelix.cc.client.command.cmds;

import zelix.cc.client.command.Command;
import zelix.cc.client.utils.Render.Logger;
import net.minecraft.util.EnumChatFormatting;

public class Help
extends Command {
    public Help() {
        super("Help");
    }
    @Override
    public void execute(String[] args){
        String[] toShow = new String[]{
                "---------------------------------",
                "Help -> "+ EnumChatFormatting.YELLOW+"Show commands.",
                "Toggle -> "+ EnumChatFormatting.YELLOW+"Set module's state.",
                "Config -> "+ EnumChatFormatting.YELLOW+"Auto settings loader.",
                "Bind -> "+ EnumChatFormatting.YELLOW+"Set module's boundKey.",
                "Damage -> "+ EnumChatFormatting.YELLOW+"Damage yourself.",
                "Settings -> "+ EnumChatFormatting.YELLOW+"Set module's settings.",
                "Save -> "+ EnumChatFormatting.YELLOW+"Save your settings / config.",
                "Name -> "+ EnumChatFormatting.YELLOW+"Change it to your favorite client name.",
                "---------------------------------"
        };
        for(String cur : toShow){
            Logger.sendMessage(cur);
        }
    }
}
