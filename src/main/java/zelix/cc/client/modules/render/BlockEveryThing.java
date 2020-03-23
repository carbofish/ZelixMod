package zelix.cc.client.modules.render;

import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class BlockEveryThing extends Module {

    public static boolean block = false;
    public BlockEveryThing() {
        super("BlockAll",ModuleType.Render);
    }

    @Override
    public void onEnable(){
        block = true;
    }

    @Override
    public void onDisable(){
        block = false;
    }
}
