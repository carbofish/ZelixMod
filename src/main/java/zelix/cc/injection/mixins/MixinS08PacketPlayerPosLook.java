package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import zelix.cc.injection.interfaces.IMixinS08PacketPlayerPosLook;

@Mixin(S08PacketPlayerPosLook.class)
public class MixinS08PacketPlayerPosLook implements IMixinS08PacketPlayerPosLook {
	@Shadow
	private double x;
	@Shadow
    private double y;
	@Shadow
    private double z;
	@Shadow
    private float yaw;
	@Shadow
    private float pitch;
	
	@Override
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	@Override
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
}
