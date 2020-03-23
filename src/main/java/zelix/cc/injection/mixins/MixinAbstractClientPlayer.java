package zelix.cc.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer{

	public MixinAbstractClientPlayer() {
		super();
		// TODO Auto-generated constructor stub
	}

}
