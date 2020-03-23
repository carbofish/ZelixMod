package zelix.cc.client.eventAPI.events.network;

import zelix.cc.client.eventAPI.Event;
import net.minecraft.network.Packet;

public class EventPacketSend
extends Event {
    public Packet packet;
    public boolean cancel;
    public EventPacketSend(Packet packet){
        this.packet = packet;
    }
}
