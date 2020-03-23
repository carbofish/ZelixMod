package zelix.cc.client.eventAPI.events.execute;

import zelix.cc.client.eventAPI.Event;

public class EventPPUpdate
extends Event {
    public static  float yaw;
    public static float pitch;
    public double y;
    private boolean ground;
    private boolean isPre;
    public EventPPUpdate(float yaw, float pitch, double y, boolean ground,boolean pre) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.isPre = pre;
        if(isPre){
            EventPPUpdate.pitch = pitch;
        }
        this.ground = ground;
    }

    public boolean isPre(){
        return isPre;
    }
    public boolean isPost(){
        return !isPre;
    }
    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public boolean isOnGround() {
        return this.ground;
    }
    public void setOnGround(boolean ground) {
        this.ground = ground;
    }

}
