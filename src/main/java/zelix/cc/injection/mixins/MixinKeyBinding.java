package zelix.cc.injection.mixins;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMixinKeyBinding;

@Mixin(KeyBinding.class)
public class MixinKeyBinding implements IMixinKeyBinding {
    @Shadow
    private boolean pressed;

    @Override
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

}
