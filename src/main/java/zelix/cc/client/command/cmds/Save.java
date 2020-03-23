package zelix.cc.client.command.cmds;

import zelix.cc.client.Zelix;
import zelix.cc.client.command.Command;

public class Save
extends Command {
    public Save() {
        super("Save");
    }
    @Override
    public void execute(String[] arg){
        Zelix.getInstance().saveSettings();
    }
}
