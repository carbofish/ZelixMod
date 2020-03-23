package zelix.cc.client.modules.motion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.combat.Aura;
import zelix.cc.client.utils.RotationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TargetStrafe
extends Module {
    public static zelix.cc.client.eventAPI.api.Amount<Double> dist = new zelix.cc.client.eventAPI.api.Amount<Double>("Distance", "Distance", 5.0, 1.0, 0.1, 4.0);
    public static zelix.cc.client.eventAPI.api.Amount<Double> forSpeed = new zelix.cc.client.eventAPI.api.Amount<Double>("ForwardSpeed", "ForwardSpeed", 1.0, 0.05, 0.05, 1.0);
    public static Option<Boolean> keep = new Option<Boolean> ("KeepDist", "KeepDist", true);
    public TargetStrafe() {
        super("TargetStrafe", ModuleType.Motion);
        addSettings(forSpeed,dist,keep);
    }
    @Runnable
    private void onPre(EventPPUpdate eventPPUpdate)
    {
        for(Entity entity : mc.theWorld.loadedEntityList){
            if(entity.getDistanceToEntity(mc.thePlayer) < 8){
                if(isEntityAllowed(entity) && !entityList.contains(entity)) {
                    entityList.add(entity);
                }
            }
            if(isNotEntityAllowed(entity)) {
                entityList.remove(entity);
            }
        }
    }
    public boolean isNotEntityAllowed(Entity entity)
    {
        if(entityList.contains(entity))
        {
            return entity.isDead || entity.getDistanceToEntity(mc.thePlayer) > 8 || !isEntityAllowed(entity);
        }
        return false;
    }
    public boolean isEntityAllowed(Entity entity){
        if(!(entity instanceof EntityLivingBase) || !(((EntityLivingBase)entity).getHealth() > 0)) {
            return false;
        }
        if(entity.isDead) {
            return false;
        }
        if(entity.equals(mc.thePlayer)) {
            return false;
        }
        if(entity instanceof EntityPlayer) {
            return true;
        }
        return entity instanceof EntitySlime || entity instanceof EntityMob || entity instanceof EntityAnimal || entity instanceof EntityVillager;
    }
    public double getSpeedByXZ(double motionX, double motionZ) {
        final double vel = Math.sqrt(motionX * motionX + motionZ * motionZ);
        return vel;
    }
    List<Entity> entityList = new ArrayList<>();
    @Runnable
    public void onMotion(EventMove eventMove){
        if(Zelix.getInstance().getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            return;
        }
        onStrafe(eventMove);
    }
    public void onStrafe(EventMove eventMove){
        double speed  = getSpeedByXZ(eventMove.getX(),eventMove.getZ());
        if(!entityList.isEmpty())
        {
            Entity entity = entityList.get(0);
            if(isNotEntityAllowed(entity))
            {
                entityList.remove(entity);
                return;
            }
            if(!Zelix.getInstance().getModuleManager().getModuleByClass(Flight.class).isEnabled())
                setMoveSpeed(speed, RotationUtil.getYawToEntity(entity), Math.abs(mc.thePlayer.getDistanceToEntity(entity) - dist.getValue()) <= 0.4, eventMove);
        }
    }
    public void onMotion(double motionX,double motionZ){
        double speed  = getSpeedByXZ(motionX,motionZ);
        if(!entityList.isEmpty())
        {
            Entity entity = entityList.get(0);
            if(isNotEntityAllowed(entity))
            {
                entityList.remove(entity);
                return;
            }
            if(!Zelix.getInstance().getModuleManager().getModuleByClass(Flight.class).isEnabled()) {
                setMoveSpeed(speed, RotationUtil.getYawToEntity(entity), Math.abs(mc.thePlayer.getDistanceToEntity(entity) - dist.getValue()) <= 0.5);
            }
        }
    }
    public void setMoveSpeed(final double speed,float yaw,boolean forwardTo,EventMove eventMove) {
        double forward = this.mc.thePlayer.movementInput.moveForward;
        double strafe = this.mc.thePlayer.movementInput.moveStrafe;
        if(Zelix.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()){
            {
                {
                    if(keep.getValue()){
                        if(forwardTo)
                        {
                            if(forward > 0)
                            {
                                forward = 0;
                            }
                        }
                        else
                        {
                            if(mc.thePlayer.getDistanceToEntity(entityList.get(0)) < dist.getValue()) {
                                forward = -forSpeed.getValue();
                            }
                        }
                    }else{
                        forward = forward > 0 ? 1 : (forward < 0 ? -1 : 0);
                        forward *= forSpeed.getValue();
                    }
                    strafe = strafe > 0 ? 1 : (strafe < 0 ? -1 : 1);
                    eventMove.x = (forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))));
                    eventMove.z = (forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))));
                }
            }
        }
    }
    public void setMoveSpeed(final double speed,float yaw,boolean forwardTo) {
        double forward = this.mc.thePlayer.movementInput.moveForward;
        double strafe = this.mc.thePlayer.movementInput.moveStrafe;
        if(Zelix.instance.getModuleManager().getModuleByClass(TargetStrafe.class).isEnabled()){
            {
                {
                    if(keep.getValue()){
                        if(forwardTo)
                        {
                            if(forward > 0)
                            {
                                forward = 0;
                            }
                        }
                        else
                        {
                            if(mc.thePlayer.getDistanceToEntity(entityList.get(0)) < dist.getValue()) {
                                forward = -forSpeed.getValue();
                            }
                        }
                    }else{
                        forward = forward > 0 ? 1 : (forward < 0 ? -1 : 0);
                        forward *= forSpeed.getValue();
                    }
                    strafe = strafe > 0 ? 1 : (strafe < 0 ? -1 : 1);
                    mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))));
                    mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))));
                }
            }
        }
    }
}
