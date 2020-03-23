package zelix.cc.injection.mixins;

import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C09PacketHeldItemChange.class)
public class MixinC09PacketHeldItemChange implements zelix.cc.injection.interfaces.IMixinC09PacketHeldItemChange {
    @Shadow
    private int slotId;

    @Override
    public void setSlotId(int slotId){
        this.slotId = slotId;
    }
}
