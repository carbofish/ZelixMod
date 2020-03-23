package zelix.cc.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.injection.interfaces.IMixinMinecraft;
import zelix.cc.injection.interfaces.IMixinRenderManager;

public class Helper {

    public static Timer getTimer() {
        return ((IMixinMinecraft) Minecraft.getMinecraft()).getTimer();
    }

    public static double renderPosX() {
        return ((IMixinRenderManager)Minecraft.getMinecraft().getRenderManager()).getrenderPosX();
    }

    public static double renderPosY() {
        return ((IMixinRenderManager)Minecraft.getMinecraft().getRenderManager()).getrenderPosY();
    }

    public static double renderPosZ() {
        return ((IMixinRenderManager)Minecraft.getMinecraft().getRenderManager()).getrenderPosZ();
    }

    public static double playerViewX() {
        return ((IMixinRenderManager)Minecraft.getMinecraft().getRenderManager()).playerViewX();
    }

    public static double playerViewY() {
        return ((IMixinRenderManager)Minecraft.getMinecraft().getRenderManager()).playerViewY();
    }

    public static float getrenderPartialTicks() {
        return getTimer().renderPartialTicks;
    }

    public static void setSession(Session s) {
        ((IMixinMinecraft)Minecraft.getMinecraft()).setSession(s);
    }

    public static void setMoveSpeed(EventMove event, double speed) {
        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            //  event.setX(0.0);
            //  event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static void setSpeed(double speed) {
        Minecraft.getMinecraft().thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
        Minecraft.getMinecraft().thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
    }

    public static float getDirection() {
        float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        final float forward = Minecraft.getMinecraft().thePlayer.moveForward;
        final float strafe = Minecraft.getMinecraft().thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45.0f : ((forward == 0.0f) ? 90.0f : 45.0f));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45.0f : ((forward == 0.0f) ? 90.0f : 45.0f));
        }
        return yaw * 0.017453292f;
    }

    public static boolean moving() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0.0f || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0f;
    }
}
