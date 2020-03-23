package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import zelix.cc.injection.interfaces.IMixinEntityPlayer;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase implements IMixinEntityPlayer {
	public MixinEntityPlayer() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Shadow
	private BlockPos spawnChunk;
	
	@Shadow
    private int itemInUseCount;
    @Shadow
    protected float speedInAir;
    @Shadow
    public PlayerCapabilities capabilities;
    @Shadow
    protected boolean sleeping;
    @Shadow
    private int sleepTimer;
    @Shadow
    protected int flyToggleTimer;
    
    @Shadow
    protected abstract boolean canTriggerWalking();

    @Shadow
    public abstract boolean isUsingItem();
    @Shadow
    protected abstract String getSwimSound();
    @Shadow
    public abstract ItemStack getItemInUse();

    @Override
	@Shadow
    public abstract ItemStack getHeldItem();

    @Shadow
    public abstract GameProfile getGameProfile();
    
    @Shadow
    public abstract int getItemInUseDuration();

    @Shadow
    public abstract FoodStats getFoodStats();
	
	@Override
    public void setitemInUseCount(int i){
        this.itemInUseCount = i;
    }
	
	@Override
    public int getitemInUseCount(){
        return this.itemInUseCount;
    }
	
	@Override
    public BlockPos getspawnChunk(){
        return this.spawnChunk;
    }
}
