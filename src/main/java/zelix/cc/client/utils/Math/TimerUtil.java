package zelix.cc.client.utils.Math;


import zelix.cc.client.utils.Util;

public class TimerUtil
extends Util {
    private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    public  boolean hit(long milliseconds) {
        return (getCurrentMS() - lastMS) >= milliseconds;
    }
    public boolean hasReached(double milliseconds) {
        if ((double)(this.getCurrentMS() - this.lastMS) >= milliseconds) {
            return true;
        }
        return false;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
    public void delayMS(int value){
        lastMS = getCurrentMS() - value;
    }
    public boolean delay(float milliSec) {
        return (float) (getTime() - this.lastMS) >= milliSec;
    }
    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMS > delay) {
            return true;
        }
        return false;
    }
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
}
