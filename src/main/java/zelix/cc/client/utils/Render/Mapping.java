package zelix.cc.client.utils.Render;

import net.minecraft.entity.Entity;
import net.minecraft.util.Util;

public class Mapping
extends Util {
    public Entity entity;
    public float health;
    public Mapping(Entity entity ,float health){
        this.health = health;
        this.entity = entity;
    }
    public float getHealth(){
        return health;
    }

    public Entity getEntity(){
        return entity;
    }
}
