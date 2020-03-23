package zelix.cc.injection.interfaces;

import net.minecraft.util.BlockPos;

public interface IMixinEntityPlayer {
    void setitemInUseCount(int i);
    int getitemInUseCount();
    BlockPos getspawnChunk();
}
