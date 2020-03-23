package zelix.cc.client.eventAPI.events.render;

import zelix.cc.client.eventAPI.Event;

public class Event3D
extends Event {
    public float parTicks;
    public Event3D(float partialTicks) {
        parTicks = partialTicks;
    }

    public float getPartialTicks() {
        return parTicks;
    }
}
