package zelix.cc.client.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class BlockHelper {

    public static boolean isInLiquid() {
        boolean inLiquid = false;
        final int y = (int)Instances.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(Instances.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Instances.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(Instances.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Instances.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(x, y, z);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static Block getBlock(final double x, final double y, final double z) {
        return Instances.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
}
