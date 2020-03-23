package zelix.cc.client.modules.render;

import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class Animations extends Module {
    enum animations{
        Swang,OneDotSeven,Vanilla,Avatar,Edit,Remix,Sigma
    }
    public Mode<Enum> mode = new Mode("Mode", "Mode", animations.values(), animations.Sigma);
    public zelix.cc.client.eventAPI.api.Amount<Double> pull = new zelix.cc.client.eventAPI.api.Amount<Double>("PullSize", "PullSize", 15.0, -15.0, 0.5, 0.5);
    public zelix.cc.client.eventAPI.api.Amount<Double> x = new zelix.cc.client.eventAPI.api.Amount<Double>("X", "X", 10.0, -10.0, 0.05, 0.05);
    public zelix.cc.client.eventAPI.api.Amount<Double> y = new zelix.cc.client.eventAPI.api.Amount<Double>("Y", "Y", 10.0, -10.0, 0.05, 0.05);
    public zelix.cc.client.eventAPI.api.Amount<Double> z = new zelix.cc.client.eventAPI.api.Amount<Double>("Z", "Z", 10.0, -10.0, 0.05, 0.05);

    public Animations() {
        super("Animations", ModuleType.Render);
        addSettings(mode,pull,x,y,z);
    }
}
