package zelix.cc.client.eventAPI.events.misc;

import zelix.cc.client.eventAPI.Event;

public class EventKey
        extends Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}


