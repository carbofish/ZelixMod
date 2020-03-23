package zelix.cc.client.modules.player;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import zelix.cc.injection.interfaces.IMixinS27PacketExplosion;
import zelix.cc.injection.interfaces.IMxinS12PacketEntityVelocity;

public class AntiKB
extends Module {
    public static Amount<Double> YPerc = new Amount<>("Y%", "Y%", 300.0,-300.0,5.0,0.0);
    public static Amount<Double> XZPerc = new Amount<>("XZ%", "XZ%", 300.0,-300.0,5.0,0.0);
    public AntiKB() {
        super("AntiKB", ModuleType.Player);
        addSettings(XZPerc,YPerc);
    }
    @Runnable
    public void packetReceive(EventPacketReceive eventPacketReceive){
        setSuffix(XZPerc.getValue()+"%, " + YPerc.getValue()+"%");
        Packet packet = eventPacketReceive.packet;
        boolean debugOut = false;
        try{
            if(packet instanceof S12PacketEntityVelocity){
                S12PacketEntityVelocity s12packet = (S12PacketEntityVelocity)packet;
                if(s12packet.getEntityID() == mc.thePlayer.getEntityId()){
                    if(YPerc.value.intValue() == 0 && XZPerc.value.intValue() == 0){
                        eventPacketReceive.cancel = true;
                        debugOut = true;
                    }else{
                        ((IMxinS12PacketEntityVelocity)s12packet).setMotionX((int)(s12packet.getMotionX() * XZPerc.value/100));
                        ((IMxinS12PacketEntityVelocity)s12packet).setMotionY((int)(s12packet.getMotionY() * YPerc.value/100));
                        ((IMxinS12PacketEntityVelocity)s12packet).setMotionZ((int)(s12packet.getMotionZ() * XZPerc.value/100));
                        debugOut=true;
                    }
                }
            }
            if(packet instanceof S27PacketExplosion){
                S27PacketExplosion s27packet = (S27PacketExplosion)packet;
                if(YPerc.value.intValue() == 0 && XZPerc.value.intValue() == 0){
                    eventPacketReceive.cancel = true;
                    debugOut = true;
                }else{
                    ((IMixinS27PacketExplosion)s27packet).setField_149152_f((int)(s27packet.func_149149_c() * XZPerc.value/100));
                    ((IMixinS27PacketExplosion)s27packet).setField_149153_g((int)(s27packet.func_149144_d() * YPerc.value/100));
                    ((IMixinS27PacketExplosion)s27packet).setField_149159_h((int)(s27packet.func_149147_e() * XZPerc.value/100));
                    debugOut=true;
                }
            }

        }catch (Exception excep){

        }
    }
}
