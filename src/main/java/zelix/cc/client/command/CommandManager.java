package zelix.cc.client.command;

import zelix.cc.client.command.cmds.*;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commandList;
    public void init(){
        commandList = new ArrayList<>();
        commandList.add(new Bind());
        commandList.add(new Config());
        commandList.add(new Damage());
        commandList.add(new Help());
        commandList.add(new ModuleSettings());
        commandList.add(new Toggle());
        commandList.add(new Save());
        commandList.add(new ClientName());
    }
    public Command getCommandByName(String name){
        for(Command curCheck : commandList){
            if(name.toLowerCase().contains(curCheck.name.toLowerCase()))
                return curCheck;
        }
        return null;
    }
}
