package zelix.cc.client.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import zelix.cc.client.Zelix;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class Teams
extends Module {
    public Teams(){
        super("Teams", ModuleType.World);
    }
    public static boolean isOnSameTeam(Entity entity) {
        if(!Zelix.moduleManager.getModuleByClass(Teams.class).isEnabled()) {
            return false;
        } else {
            boolean team = false;
            String n = entity.getDisplayName().getFormattedText();
            if(n.startsWith('\u00a7' + "f") && !n.equalsIgnoreCase(entity.getName())) {
                team = (n.substring(0, 6).equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0, 6)));
            } else {
                team = (n.substring(0,2).equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0,2)));
            }
            return team;
        }
    }
}
