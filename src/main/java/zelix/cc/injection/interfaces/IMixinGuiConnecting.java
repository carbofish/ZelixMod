package zelix.cc.injection.interfaces;

import net.minecraft.network.NetworkManager;

public interface IMixinGuiConnecting {
    NetworkManager getNetworkManager();
}
