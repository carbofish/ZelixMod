package zelix.cc.client.eventAPI.events.misc;

import zelix.cc.client.eventAPI.Event;

public class EventChat
extends Event {
    public String message;
    public boolean cancelled;
    public EventChat(String msg,boolean cancelled){
        message = msg;
        this.cancelled = cancelled;
    }
}
