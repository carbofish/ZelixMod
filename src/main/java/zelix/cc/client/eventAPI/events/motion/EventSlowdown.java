package zelix.cc.client.eventAPI.events.motion;

import zelix.cc.client.eventAPI.Event;

public class EventSlowdown
extends Event {
    public float slowValue;
    public boolean isPre,cance;


    public EventSlowdown(boolean b, float slowValue) {
        this.slowValue =slowValue;
        isPre = b;
    }
}
