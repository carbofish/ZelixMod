package zelix.cc.injection.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMixinPlayerControllerMP;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP implements IMixinPlayerControllerMP {
    @Shadow
    private int blockHitDelay;

    @Override
    public int getBlockHitDelay(){
        return this.blockHitDelay;
    }

    @Override
    public void setBlockHitDelay(int blockHitDelay){
        this.blockHitDelay = blockHitDelay;
    }
}
