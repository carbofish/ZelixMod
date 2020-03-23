package zelix.cc.client.modules.render;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventTick;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright
extends Module {
    public Fullbright() {
        super("FullBright", ModuleType.Render);
    }
    @Runnable
    public void onTick(EventTick eventTick){
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(),5000, (int) 1));
    }
}
