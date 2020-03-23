package zelix.cc.client.modules.player;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.render.Animations;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Motion.MotionUtils;
import zelix.cc.injection.interfaces.IMixinC03PacketPlayer;

public class NoFall
extends Module {
    public boolean timers = false;
    enum NofallMode{
        AAC,FakeGround,NCP
    }

    public Mode<Enum> mode = new Mode("Mode", "Mode", NofallMode.values(), NofallMode.FakeGround);
    public NoFall() {
        super("NoFall", ModuleType.Player);
        this.addSettings(mode);
    }

    double fall = 0;
    @Runnable
    public void onPre(EventPPUpdate eventPPUpdate){
        if(eventPPUpdate.isPre()) {
            if(mode.getValue() == NofallMode.FakeGround) {

            }else if(mode.getValue() == NofallMode.AAC) {
                if(!mc.thePlayer.isInWater()&&!mc.thePlayer.isInLava()&&!mc.thePlayer.isOnLadder()) {
                    if(mc.thePlayer.fallDistance >1.7456236214626) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY+0.0005,mc.thePlayer.posZ,true));
                        mc.thePlayer.fallDistance = 0;
                    }else {
                        if(mc.thePlayer.motionY !=0&& !mc.thePlayer.onGround&&mc.thePlayer.fallDistance!=0&&mc.thePlayer.motionY <= -0.45) {
                            Helper.getTimer().timerSpeed = 0.45f;
                            Helper.setSpeed(0.00001);
                        }
                        else {
                            Helper.getTimer().timerSpeed = 1;
                        }
                    }
                }
            }else if(mode.getValue() == NofallMode.NCP) {
                if(!MotionUtils.isOnGround(0.001D)) {
                    if(mc.thePlayer.motionY < -0.08D) {
                        this.fall -= mc.thePlayer.motionY;
                    }

                    if(this.fall > 3.0D) {
                        this.fall = 0.0D;
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }
                } else {
                    this.fall = 0.0D;
                }
            }else {
                if (!mc.thePlayer.isOnLadder() & !mc.thePlayer.isInWater() && Minecraft.getMinecraft().thePlayer.fallDistance >= 3.0f && Minecraft.getMinecraft().thePlayer.fallDistance <= 15.0f && isBlockUnder()) {
                    Minecraft.getMinecraft().thePlayer.motionX *= 0.0;
                    Minecraft.getMinecraft().thePlayer.motionZ *= 0.0;
                    Helper.getTimer().timerSpeed = 0.25f;
                    timers = true;
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.001, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer(true));
                }else if( !mc.thePlayer.isOnLadder() & !mc.thePlayer.isInWater() && mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isSneaking()) {
                    mc.thePlayer.motionY += 0.1;
                }else if(timers) {
                    Helper.getTimer().timerSpeed = 1;
                    timers = false;
                }
            }
        }

    }

    @Runnable
    public void onPS(EventPacketReceive e ){
        if(mode.getValue() == NofallMode.FakeGround) {
            if(mc.thePlayer!=null&&mc.thePlayer.fallDistance > 3D && e.packet instanceof C03PacketPlayer) {
                mc.thePlayer.fallDistance = 0.0f;
                ((IMixinC03PacketPlayer)((C03PacketPlayer)e.packet)).setOnground(true);
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int)(mc.thePlayer.posY - 1.0); i >= 0; --i) {
            final BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if ((this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockSlime)) {
                break;
            }
            if ((this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid)) {
                break;
            }
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }
}
