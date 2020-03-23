package zelix.cc.client.eventAPI.events.misc;

import zelix.cc.client.eventAPI.Event;
import net.minecraft.entity.Entity;

public class EventAttack
extends Event {
    public Entity entity;
    public EventAttack(Entity entity){
        this.entity = entity;
    }
}
