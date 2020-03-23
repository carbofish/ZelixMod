package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.renderer.entity.RenderManager;
import zelix.cc.injection.interfaces.IMixinRenderManager;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager implements IMixinRenderManager {
	@Shadow
	private double renderPosX;
	@Shadow
    private double renderPosY;
	@Shadow
    private double renderPosZ;
	@Shadow
    public float playerViewY;
	@Shadow
    public float playerViewX;
	
	@Override
    public double getrenderPosX() {
        return this.renderPosX;
    }
	
	@Override
    public double getrenderPosY() {
        return this.renderPosY;
    }
	
	@Override
    public double getrenderPosZ() {
        return this.renderPosZ;
    }
	
	@Override
    public double playerViewY() {
        return this.playerViewY;
    }
	
	@Override
    public double playerViewX() {
        return this.playerViewX;
    }
	
}
