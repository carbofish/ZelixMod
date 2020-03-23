package zelix.cc.client.modules.world;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import zelix.cc.injection.interfaces.IMixinPlayerControllerMP;

public class SpeedMine
extends Module {
    private boolean isMining = false;
    private float progress = 0.0f;
    public BlockPos blockPos;
    public EnumFacing facing;
    public static zelix.cc.client.eventAPI.api.Amount<Double> speed = new zelix.cc.client.eventAPI.api.Amount<Double>("Speed", "Speed", 1.5, 1.0, 0.05, 1.0);
    public SpeedMine() {
        super("SpeedMine", ModuleType.World);
        addSettings(speed);
    }

    @Runnable
    public void onDamageBlock(EventPacketSend event) {
        if (event.packet instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.packet;
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.isMining = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.progress = 0.0f;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.isMining = false;
                this.blockPos = null;
                this.facing = null;
                this.progress = 0f;
            }
        }
    }

    @Runnable
    public void onUpdate(EventPPUpdate event) {
        if (mc.playerController.extendedReach()) {
            ((IMixinPlayerControllerMP)mc.playerController).setBlockHitDelay(0);
        } else if (this.isMining) {
            Block block = this.mc.theWorld.getBlockState(this.blockPos).getBlock();
            this.progress += (float)((double)block.getPlayerRelativeBlockHardness(mc.thePlayer, this.mc.theWorld, this.blockPos) * speed.getValue());
            if (this.progress >= 1.0f) {
                this.mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.progress = 0.0f;
                this.isMining = false;
            }
        }
    }
}
