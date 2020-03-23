package zelix.cc.client.modules.player;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiFall
extends Module {
    double fall = 0;
    public AntiFall() {
        super("AntiFall", ModuleType.Player);
    }
    @Runnable
    private void onPre(EventPPUpdate eventPreUpdate){
        setSuffix("Packet");
        boolean blockUnderneath = false;
        int checkedY = 0;
        int i = 0;
        while ((double)i < this.mc.thePlayer.posY + 2.0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ);
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
                checkedY = i;
            }
            ++i;
        }


        if (blockUnderneath) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)checkedY, this.mc.thePlayer.posZ);
            if(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockLiquid
                    || this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockDynamicLiquid)
            {
                int antiLiquid = 0;
                while (antiLiquid < this.mc.thePlayer.posY){
                    BlockPos posTest = new BlockPos(this.mc.thePlayer.posX, (double)this.mc.thePlayer.posY-antiLiquid, this.mc.thePlayer.posZ);
                    Block block = mc.theWorld.getBlockState(posTest).getBlock();
                    if (!(block instanceof BlockLiquid)
                            && !(block instanceof BlockDynamicLiquid)
                            && !(block instanceof BlockAir)) {
                        return;
                    }
                    antiLiquid++;
                }
            } else {
                return;
            }
        }

        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {

            if(mc.thePlayer.motionY < -0.08D) {
                this.fall -= mc.thePlayer.motionY;
            }

            if(this.fall > 7D) {
                this.fall = 0.0D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, (double)mc.thePlayer.posY + 12, this.mc.thePlayer.posZ, false));

                this.mc.thePlayer.fallDistance =0;
            }

        }else {
            fall =0;
        }
    }
}
