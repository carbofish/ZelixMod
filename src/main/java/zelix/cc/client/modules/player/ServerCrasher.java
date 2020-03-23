package zelix.cc.client.modules.player;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;

public class ServerCrasher
extends Module {
    public ServerCrasher() {
        super("ServerCrasher", ModuleType.Player);
    }
    @Runnable
    private void onCrash(EventPacketSend eventPacketSend){

    }
}
