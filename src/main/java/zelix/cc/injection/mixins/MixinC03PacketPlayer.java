package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.play.client.C03PacketPlayer;
import zelix.cc.injection.interfaces.IMixinC03PacketPlayer;

@Mixin(C03PacketPlayer.class)
public class MixinC03PacketPlayer implements IMixinC03PacketPlayer {
	
	@Shadow
	protected double x;
	@Shadow
    protected double y;
	@Shadow
    protected double z;
	@Shadow
    protected float yaw;
	@Shadow
    protected float pitch;
	@Shadow
    protected boolean onGround;
	@Shadow
    protected boolean moving;
	@Shadow
    protected boolean rotating;
	
	@Override
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	@Override
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	@Override
	public void setOnground(boolean ground) {
		this.onGround = ground;
	}
	
	@Override
	public float getYaw() {
		return this.yaw;
	}
	@Override
	public float getPitch() {
		return this.pitch;
	}
	@Override
	public boolean getOnground() {
		return this.onGround;
	}
}
