package zelix.cc.client.eventAPI.events.render;

import zelix.cc.client.eventAPI.Event;

public class Event2D
extends Event {
    public float parTicks;
    public Event2D(float partialTicks) {
        parTicks = partialTicks;
    }
}
