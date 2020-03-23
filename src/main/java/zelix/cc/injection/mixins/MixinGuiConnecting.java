package zelix.cc.injection.mixins;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMixinGuiConnecting;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting implements IMixinGuiConnecting {
    @Shadow
    private NetworkManager networkManager;

    @Override
    public NetworkManager getNetworkManager(){
        return networkManager;
    }
}
