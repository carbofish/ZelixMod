package zelix.cc.client.modules.motion;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", ModuleType.Motion);
        addSettings(omni);
    }
    private Option<Boolean> omni = new Option<Boolean>("Omni", "Omni", true);

    boolean test;
    @Override
    public void onEnable() {
        // TODO Auto-generated method stub
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub
        super.onDisable();
    }

    @Runnable
    private void onPre(EventPPUpdate event) {
        if(event.isPre()) {
            if ((omni.getValue() || mc.thePlayer.moveForward > 0) && !mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }
}
