package zelix.cc.injection.mixins;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMxinS12PacketEntityVelocity;

@Mixin(S12PacketEntityVelocity.class)
public class MixinS12PacketEntityVelocity implements IMxinS12PacketEntityVelocity {
    @Shadow
    private int motionX;
    @Shadow
    private int motionY;
    @Shadow
    private int motionZ;

    @Override
    public int getMotionX(){
        return this.motionX;
    }

    @Override
    public int getMotionY(){
        return this.motionY;
    }

    @Override
    public int getMotionZ(){
        return this.motionZ;
    }

    @Override
    public void setMotionX(int motionX){
        this.motionX = motionX;
    }

    @Override
    public void setMotionY(int motionY){
        this.motionY = motionY;
    }

    @Override
    public void setMotionZ(int motionZ){
        this.motionZ = motionZ;
    }



}
