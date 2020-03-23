package zelix.cc.client.utils.Render;

import net.minecraft.entity.Entity;
import zelix.cc.client.utils.Math.TimerUtil;

public class Particles {
    public double renderPosX,renderPosY,renderPosZ;
    public float damage;
    public Entity entity;
    public TimerUtil timerUtil = new TimerUtil();
    public Particles(double renderPosX,double renderPosY,double renderPosZ,float damage,Entity entity)
    {
        this.renderPosX = renderPosX;
        this.renderPosY = renderPosY;
        this.renderPosZ = renderPosZ;
        this.damage = damage;
        this.entity = entity;
        timerUtil.reset();
    }
}
