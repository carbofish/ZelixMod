package zelix.cc.client.modules.player;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.motion.EventSlowdown;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.combat.Aura;
import zelix.cc.client.utils.Motion.MotionUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown
extends Module {
    public static Amount<Double> slowValue = new Amount<>("Slowdown", "Slowdown", 100.0, 0.0, 5.0, 95.0);
    public NoSlowdown() {
        super("NoSlowdown", ModuleType.Player);
        addSettings(slowValue);
    }
    @Runnable
    public void EventPP(EventPPUpdate eventPPUpdate){
        if (Aura.attackList.isEmpty()&&mc.thePlayer.isBlocking() && MotionUtils.isOnGround(0.42)){
            if(eventPPUpdate.isPre()) {
                mc.getNetHandler().getNetworkManager().sendPacket((
                        new C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                new BlockPos(-.9, -.9, -.9),
                                EnumFacing.DOWN)));
            } else if(eventPPUpdate.isPost()) {
                mc.getNetHandler().getNetworkManager().sendPacket(
                        new C08PacketPlayerBlockPlacement(
                                new BlockPos(-.9, -.9, -.9),
                                255,
                                mc.thePlayer.getCurrentEquippedItem(),
                                0,
                                0,
                                0));
            }
        }
    }
    @Runnable
    private void onSlowdown(EventSlowdown eventSlowdown){
        if(eventSlowdown.isPre){
            eventSlowdown.slowValue = slowValue.getValue().floatValue() / 100F;
        }
    }
}
