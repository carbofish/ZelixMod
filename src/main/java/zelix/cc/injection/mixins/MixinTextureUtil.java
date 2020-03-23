package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.texture.TextureUtil;
import zelix.cc.injection.interfaces.IMixinTextureUtil;

@Mixin(TextureUtil.class)
public abstract class MixinTextureUtil implements IMixinTextureUtil {

	@Shadow
	abstract void bindTexture(int p_94277_0_);
	
	@Override
	public void tobindTexture(int p_94277_0_) {
		this.bindTexture(p_94277_0_);
	}
}
