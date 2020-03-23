package zelix.cc.client.eventAPI.events.render;

import zelix.cc.client.eventAPI.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventLoadingBlock extends Event
{
    private int x;
    private int y;
    private int z;
    private Block block;
    public BlockPos blockPos;

    public EventLoadingBlock(int x, int y, int z, Block block,BlockPos blockPos) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
        this.blockPos = blockPos;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Block getBlock() {
        return this.block;
    }
    public BlockPos getBlockPos(){
        return this.blockPos;
    }
}
