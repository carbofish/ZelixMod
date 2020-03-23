package zelix.cc.client.eventAPI.events.network;

import zelix.cc.client.eventAPI.Event;
import net.minecraft.network.Packet;

public class EventPacketReceive
extends Event {
    public  Packet packet;
    public boolean cancel;
    public EventPacketReceive(Packet p_channelRead0_2_) {
       packet = p_channelRead0_2_;
    }
}
