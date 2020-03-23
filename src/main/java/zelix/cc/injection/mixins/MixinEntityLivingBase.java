package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import zelix.cc.injection.interfaces.IMixinEntityLivingBase;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase extends MixinEntity implements IMixinEntityLivingBase {
	@Shadow
    protected abstract float getJumpUpwardsMotion();

    @Shadow
    public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    @Shadow
    public abstract boolean isPotionActive(Potion potionIn);

    @Shadow
    protected float lastDamage;
    
    @Shadow
    private int jumpTicks;

    @Shadow
    protected boolean isJumping;


    @Shadow
    public void onLivingUpdate() {
    }

    @Shadow
    protected abstract void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract ItemStack getHeldItem();

    @Shadow protected abstract void updateAITick();

    @Shadow
    public float moveStrafing;
    
    @Shadow
    public float moveForward;
    
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {

    }

    @Override
    public void setJumpTicks(int jumpTicks){
        this.jumpTicks = jumpTicks;
    }
    
    @Override
    public float getLastDamage() {
    	return lastDamage;
    }
}
