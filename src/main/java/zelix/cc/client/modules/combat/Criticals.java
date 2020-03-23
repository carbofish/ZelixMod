package zelix.cc.client.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.scoreboard.ScoreObjective;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.misc.EventAttack;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.motion.Speed;
import zelix.cc.client.modules.world.Scaffold;
import zelix.cc.client.utils.Math.MathUtil;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Motion.MotionUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Random;

public class Criticals
extends Module {
    private Mode<Enum> mode = new Mode("Mode", "Mode",crit.values(),crit.Packet);
    public static Amount<Double> delay = new Amount<>("Postpone", "Postpone", 1500.0, 0.0, 25.0,200.0);
    public static Amount<Double> hurttime = new Amount<>("HurtPost", "HurtPost", 20.0, 0.0, 1.0,10.0);
    public Criticals() {
        super("Criticals", ModuleType.Combat);
        addSettings(mode,hurttime,delay);
    }
    TimerUtil timerUtil = new TimerUtil();
    @Runnable
    private void onAttack(EventAttack eventAttack){
        if(eventAttack.entity.hurtResistantTime <= hurttime.getValue()) {
            doCrit();
        }
    }
    boolean posUP = true;
    @Runnable
    public void onPacket(EventPacketReceive eventPacketReceive){
        if(eventPacketReceive.packet instanceof S08PacketPlayerPosLook) {
            timerUtil.delayMS((int)1000);
        }
    }
    double fall = 0;
    int motionTicks = 0;
    @Runnable
    public void onPMotion(EventPPUpdate eventMotionUpdate){
        if(eventMotionUpdate.isPre()) {
            if(mode.getValue().equals(crit.NoGround) || mode.getValue().equals(crit.Hypixel)){

                Aura aura = (Aura) Zelix.moduleManager.getModuleByClass(Aura.class);
                if(aura.isEnabled() && !aura.attackList.isEmpty() && !Zelix.moduleManager.getModuleByClass(Scaffold.class).isEnabled()) {
                    if (canCrit()) {
                        double d = eventMotionUpdate.getY();

                        motionTicks++;
                        if(motionTicks>2) {
                            eventMotionUpdate.setOnGround(false);
                        }
                        if(motionTicks == 3) {
                            eventMotionUpdate.setY(d + 0.00000000612);
                        } else if(motionTicks == 4) {
                            eventMotionUpdate.setY(d + 0.00000000081356);
                        } else if(motionTicks == 5) {
                            eventMotionUpdate.setY(d + 0.0000000012112513);
                        } else if(motionTicks == 6)
                        {
                            eventMotionUpdate.setY(d + 0.000000000075135);
                            motionTicks = 2;
                        }
                    }else{
                        motionTicks = 0;
                    }
                } else {
                    motionTicks = 0;
                    posUP = true;
                }
            }
        }
    }
    public float randomFloat(float floor, float cap){
        return floor + (new Random()).nextFloat() * (cap - floor);
    }
    @Runnable
    private void onPre(EventPPUpdate eventPPUpdate){
        setSuffix(mode.getValue().toString());
    }
    void doCrit(){
        if(timerUtil.hasReached(delay.getValue())){
            if(canCrit()){
                double x = mc.thePlayer.posX,y=mc.thePlayer.posY,z=mc.thePlayer.posZ;
                if(mode.getValue().equals(crit.CNPacket)){
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0624218713251234 + Math.random() * 2.0 / 1000.0, z, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.012511000037193298 + Math.random() * 2.0 / 10000.0, z, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    timerUtil.reset();
                }else if(mode.getValue().equals(crit.Packet)){
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.052 * randomFloat(1.08f, 1.1f), z, false));
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0125 * randomFloat(1.01f, 1.07f), z, false));
                    timerUtil.reset();
                }else if(mode.getValue().equals(crit.PosTP)){
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,y+0.02,z,false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x,y+0.01,z,false));
                    mc.thePlayer.setPosition(x,y+0.01,z);
                    mc.thePlayer.fallDistance = (float) 0.01;
                    timerUtil.reset();
                }
                else if(mode.getValue().equals(crit.Packet2)){
                    for (double d2 : new double[]{0.07943353487058669, 0.009132728666379191}) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + d2, z, false));
                    }
                    timerUtil.reset();
                }
                else if(mode.getValue().equals(crit.OPPacket)){
                    for (double d2 : new double[]{0.17943353487058669, 0.009132728666379191, 1.0E-11D}) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + d2, z, false));
                    }
                    timerUtil.reset();
                }
            }
        }
    }
    boolean canCrit(){
        return mc.thePlayer.onGround
                &&!mc.gameSettings.keyBindJump.isKeyDown()
                &&!mc.thePlayer.isInWater()
                &&!mc.thePlayer.isInLava()
                &&!Zelix.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()
                && MotionUtils.isOnGround(0.001);
    }
    enum crit{
        Packet,
        Packet2,
        OPPacket,
        CNPacket,
        Hypixel,
        PosTP,
        NoGround
    }
}
