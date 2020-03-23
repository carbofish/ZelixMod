package zelix.cc.client.modules.render;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.render.Event2D;
import zelix.cc.client.inGameGui.gamelnGui.UIClickGui;
import zelix.cc.client.inGameGui.newclickgui.NewClickgui;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class ClickGui
extends Module {
    public ClickGui() {
        super("ClickGui", ModuleType.Render);
    }
    @Override
    public void onEnable(){
        System.out.println("Clickgui Open");
        mc.displayGuiScreen(new NewClickgui());
        super.onEnable();
    }
    @Runnable
    public void on2D(Event2D event2D){
        setState(false);
    }
}
