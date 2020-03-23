package zelix.cc.injection.mixins;

import net.minecraft.network.play.server.S27PacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMixinS27PacketExplosion;

@Mixin(S27PacketExplosion.class)
public class MixinS27PacketExplosion implements IMixinS27PacketExplosion {
    @Shadow
    private float field_149152_f;
    @Shadow
    private float field_149153_g;
    @Shadow
    private float field_149159_h;

    @Override
    public void setField_149152_f(float field_149152_f){
        this.field_149152_f = field_149152_f;
    }

    @Override
    public void setField_149153_g(float field_149153_g){
        this.field_149153_g = field_149153_g;
    }

    @Override
    public void setField_149159_h(float field_149159_h){
        this.field_149159_h = field_149159_h;
    }

}
