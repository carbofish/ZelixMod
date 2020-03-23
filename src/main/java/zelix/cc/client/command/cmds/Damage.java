package zelix.cc.client.command.cmds;

import zelix.cc.client.command.Command;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.notification.NotificationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Damage
extends Command {
    public Damage() {
        super("Damage");
    }
    @Override
    public void execute(String[] args){
        if(args[1].length()>=1){
            Minecraft mc = Minecraft.getMinecraft();
            if(args[1].equals("true")){
                mc.thePlayer.motionY = 0.4;
                damage(1);
            }
            else if(args[1].equals("false"))
            {
                damage(1);
            }
            else{
            	NotificationUtil.sendClientMessage("Damage", new String[] {"Invalid argument.", "Motion up or not?(true or false)"}, Notification.Type.ERROR);
            }
        }
    }
    void damage(int damage){
        Minecraft mc = Minecraft.getMinecraft();
        double fallDistance = 0;
        while (fallDistance <(2.5+(double)damage)){
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY+0.04999999997,
                    mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY+0.000001,
                    mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    false));
            fallDistance+=0.05;
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
                mc.thePlayer.posX,
                mc.thePlayer.posY,
                mc.thePlayer.posZ,
                mc.thePlayer.rotationYaw,
                mc.thePlayer.rotationPitch,
                true));
    }
}
