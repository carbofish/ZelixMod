package zelix.cc.client.modules.combat;

import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class AimAssist
extends Module {
    public static Option<Boolean> Right = new Option<Boolean> ("RightClick", "RightClick", true);
    public AimAssist() {
        super("AimAssist", ModuleType.Combat);
    }
}
