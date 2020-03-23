package zelix.cc.client.modules.motion;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Motion.MotionUtils;
import zelix.cc.client.utils.Render.Logger;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import zelix.cc.injection.interfaces.IMixinC03PacketPlayer;
import zelix.cc.injection.interfaces.IMixinS08PacketPlayerPosLook;

public class Flight
extends Module {
    public zelix.cc.client.eventAPI.api.Amount<Double> speed = new zelix.cc.client.eventAPI.api.Amount<Double>("Speed", "Speed", 5.0, 0.1, 0.05, 0.1);
    public Mode hypixelmode = new Mode("BoostMode", "BoostMode", (Enum[])HypixelMode.values(), (Enum)HypixelMode.Float);
    public Mode mode = new Mode("Mode", "mode", (Enum[])FlightMode.values(), (Enum)FlightMode.Hypixel);
    private Option<Boolean> UHC = new Option("UHC", "UHC", Boolean.valueOf(true));
    public Flight() {
        super("Flight", ModuleType.Motion);
        addSettings(mode,hypixelmode,UHC,speed);
    }
    //Vars for the module /BEGIN/
    int counter = 0;
    int stage = 0;
    int motionStage = 0;
    int damageTickes = 0;
    double motionSpeed = 0;
    double movedDist = 0;
    double positionYToAdd = 0;
    double startY = 0;
    //Damage delay.
    TimerUtil damageTimer = new TimerUtil();
    boolean shortBoost = false, updateMotionY = false;
    ///END/
    //Boolean to check for disable
    boolean toDisable,toDamage;
    //Cube counter.
    int cubeCounter;
    //onEnable init.
    @Override
    public void onEnable(){
        stage = 0;
        motionStage = 1;
        toDisable = false;
        cubeCounter = 0;
        startY = 0;
        toDamage = false;
        shortBoost = false;
        if(mode.getValue().equals(FlightMode.Hypixel)
                && hypixelmode.getValue().equals(HypixelMode.Zoom))
        {
            if(mc.thePlayer.onGround){
                //Damage player to get free as bird.
                updateMotionY = true;
                damage();
                //if(UHC.getValue().booleanValue())
                    stage = 1;
                //else
                //{
                    //mc.thePlayer.motionY = 0.419753197531 + MotionUtils.getJumpEffect() * 0.1;
                //}
            }else{
                //Disable due to the reason of ban.
                Logger.sendMessage("> [Flight] Disabled.");
                toDisable = true;
            }
        }else if(mode.getValue().equals(FlightMode.Hypixel)
                &&hypixelmode.getValue().equals(HypixelMode.OP)){
            //1 means pre.
            stage = 1;
            //Boolean needed
            toDamage = false;
            //Send player to air to disable watchdog.
            sendPacket(0.2,false);
            sendPacket(0.0751,false);
        }
        if(mode.getValue().equals(FlightMode.Cube))
        {
            up = true;
            if(!mc.thePlayer.onGround){
                //Disable due to the reason of ban.
                Logger.sendMessage("> [Flight] Disabled.");
                toDisable = true;
            }
        }
        movedDist = 0;
        motionSpeed = 0;
        Helper.getTimer().timerSpeed = 1f;
        positionYToAdd = 0;
        super.onEnable();
    }
    //onDisable stop motion
    @Override
    public void onDisable(){
        damageTimer.reset();
        toDamage = false;
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
        if(mode.getValue().equals(FlightMode.Cube))
        {
            if(positionYToAdd == 0.2)
            {
                new Thread(() -> {
                    try {
                        mc.thePlayer.motionY = 0;
                        Thread.sleep(50L);
                        mc.thePlayer.motionY = -0.2;
                        Thread.sleep(50L);
                        mc.thePlayer.motionY = -0.2;
                        Thread.sleep(50L);
                        mc.thePlayer.motionY -= startY - 0.417;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            else
            {
                mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY+0.2 - positionYToAdd - startY,mc.thePlayer.posZ);
                sendPacket(0.2,false);
                new Thread(() -> {
                    try {
                        mc.thePlayer.motionY = 0;
                        Thread.sleep(50L);
                        mc.thePlayer.motionY = -startY * 0.5 - 0.1;
                        Thread.sleep(50L);
                        mc.thePlayer.motionY = -startY * 0.5 - 0.1;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        Helper.getTimer().timerSpeed = 1f;
        up = false;
        super.onDisable();
    }
    //void to do Motion Fly & hypixel's bypass
    @Runnable
    private void pre_PostUpdate(EventPPUpdate eventPPUpdate){
        setSuffix(mode.getValue().toString() + startY);
        //Disable the module.
        if(toDisable)
        {
            toDisable=false;
            this.setState(false);
        }
        if(eventPPUpdate.isPre()) {
            if (toDamage) {
                Helper.getTimer().timerSpeed = 10f;
                double neededFallDist = 3.5 + MotionUtils.getJumpEffect() + (UHC.getValue().booleanValue() ? 1f : 0);
                if (damageTickes == 1) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.4, mc.thePlayer.posZ);
                    startY += 0.4;
                    damageTickes = 0;
                    return;
                }
                if (MotionUtils.isOnGround(0.001)) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4, mc.thePlayer.posZ);
                    damageTickes = 1;
                }
                if (startY > neededFallDist) {
                    if (damageTickes == 1) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.4, mc.thePlayer.posZ);
                    }
                    startY = 0;
                    toDamage = false;
                    mc.thePlayer.onGround = true;
                }
                return;
            } else {
                Helper.getTimer().timerSpeed = 1f;
            }
        }
        else if(mode.getValue().equals(FlightMode.Hypixel) &&
                hypixelmode.getValue().equals(HypixelMode.Zoom)) {
            Helper.getTimer().timerSpeed = 1;
        }
        //Damage detector.
        if(hypixelmode.getValue().equals(HypixelMode.Zoom) &&
                mode.getValue().equals(FlightMode.Hypixel) ||
                hypixelmode.getValue().equals(HypixelMode.OP) &&
                        mode.getValue().equals(FlightMode.Hypixel))
        {

            if(mc.thePlayer.hurtResistantTime >= 19 && stage != 3 || shortBoost)
            {
                stage = 2;
                toDamage = false;
            }
        }
        if(eventPPUpdate.isPre()){
            if(mode.getValue().equals(FlightMode.Hypixel))
            {
                if(stage == 1) {
                    return;
                }
                counter++;
                mc.thePlayer.motionY = 0;
                if(stage == 2 && updateMotionY){
                    stage = 3;
                    updateMotionY = false;
                    mc.thePlayer.motionY = 0.419753197531 + MotionUtils.getJumpEffect() * 0.1;
                }
                if(counter %2 == 0) {
                    positionYToAdd += 1.0E-7D;
                }
                eventPPUpdate.setY(eventPPUpdate.getY()+positionYToAdd);
            }
            else if(mode.getValue().equals(FlightMode.Motion))
            {
                mc.thePlayer.motionY = 0;
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = speed.getValue() * 0.5;
                } else if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -speed.getValue() * 0.5;
                }
            }
            else if(mode.getValue().equals(FlightMode.Test))
            {
                counter++;
                if(counter%5==0) {
                    mc.thePlayer.motionY=0.01;
                }
            }
            else if(mode.getValue().equals(FlightMode.Cube)){
                mc.thePlayer.onGround = false;
                mc.thePlayer.motionY = 0;
                Helper.getTimer().timerSpeed = 0.3f;
                mc.thePlayer.cameraYaw = (float)(0.3);
                cubeCounter ++;
            }
            double xDist = (mc.thePlayer.posX - mc.thePlayer.prevPosX);
            double zDist = (mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
            this.movedDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }
    //Set speed to have boost.
    @Runnable
    public void onMotion(EventMove eventMove){
        boolean tryToPreStop =
                hypixelmode.getValue().equals(HypixelMode.Zoom)
                        || hypixelmode.getValue().equals(HypixelMode.OP);
        if(mode.getValue().equals(FlightMode.Hypixel)){
            if(tryToPreStop){
                if(stage == 1){
                    eventMove.setX(eventMove.z = 0);
                    mc.thePlayer.jumpMovementFactor *= 0;
                    if(toDamage) {
                        eventMove.setY(mc.thePlayer.motionY = 0);
                    }
                    return;
                }
            }
        }
        if(hypixelmode.getValue().equals(HypixelMode.Zoom) &&
                mode.getValue().equals(FlightMode.Hypixel) &&
                stage != 1)
        {
            if(!shortBoost || shortBoost && counter <= 30)
            {
                if (motionStage != 1 || mc.thePlayer.moveForward == 0.0F
                        && mc.thePlayer.moveStrafing == 0.0F) {
                    if (motionStage == 2) {
                        motionStage = 3;
                        motionSpeed *= 2.149D;
                    } else if (motionStage == 3) {
                        motionStage = 4;
                        double difference = (0.0108D)
                                * (movedDist - defaultSpeed());
                        motionSpeed = movedDist - difference;
                    } else {
                        if (mc.theWorld
                                .getCollidingBoundingBoxes(mc.thePlayer,
                                        mc.thePlayer.getEntityBoundingBox().offset(0.0D,
                                                mc.thePlayer.motionY, 0.0D))
                                .size() > 0 || mc.thePlayer.isCollidedVertically) {
                            motionStage = 1;
                        }
                        motionSpeed = movedDist - movedDist / 159.0D;
                    }
                } else {
                    motionStage = 2;
                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.82
                            : 2.15;
                    motionSpeed = boost * defaultSpeed();
                }
            }
            motionSpeed = Math.max(motionSpeed, defaultSpeed());
            setMotionSpeed(motionSpeed,eventMove);
        }
        if(hypixelmode.getValue().equals(HypixelMode.OP)
                &&mode.getValue().equals(FlightMode.Hypixel)
                && stage != 1){
            setMotionSpeed(speed.getValue(),eventMove);
        }
        if(hypixelmode.getValue().equals(HypixelMode.Float)
                &&mode.getValue().equals(FlightMode.Hypixel)) {
            setMotionSpeed(defaultSpeed(),eventMove);
        }
        if(mode.getValue().equals(FlightMode.Cube)) {
            if (cubeCounter % 4 == 0) {
                setMotionSpeed(2, eventMove);
                if (up) {
                    eventMove.setY(mc.thePlayer.motionY = 0.417 + Math.random() / 1000 * 3);
                    startY = mc.thePlayer.motionY;
                    positionYToAdd = 0.2;
                    up = false;
                } else {
                    eventMove.setY(mc.thePlayer.motionY = -startY);
                    startY = 0;
                    positionYToAdd = 0;
                    up = true;
                }
            } else {
                setMotionSpeed(0.2, eventMove);
            }
        }
        if(mode.getValue().equals(FlightMode.Motion)) {
            setMotionSpeed(speed.getValue(),eventMove);
        }
    }
    boolean up = false;
    //PacketSend: cancel player's living packet to disable watchdog.
    @Runnable
    public void onPacketSend(EventPacketSend eventPacketSend){
        if (eventPacketSend.packet instanceof C03PacketPlayer
                && hypixelmode.getValue().equals(HypixelMode.OP)
                && mode.getValue().equals(FlightMode.Hypixel)
                && stage == 1){
            eventPacketSend.cancel = true;
            return;
        }
        if (eventPacketSend.packet instanceof C03PacketPlayer
                && hypixelmode.getValue().equals(HypixelMode.Zoom)
                && mode.getValue().equals(FlightMode.Hypixel)
                && toDamage){
            ((IMixinC03PacketPlayer)((C03PacketPlayer)eventPacketSend.packet)).setOnground(false);
            return;
        }
    }
    @Runnable
    public void onPacketReceive(EventPacketReceive eventPacketReceive){
        if(eventPacketReceive.packet instanceof S08PacketPlayerPosLook){
            if(stage == 1
                    && hypixelmode.getValue().equals(HypixelMode.OP)
                    && mode.getValue().equals(FlightMode.Hypixel)
                    && stage == 1)
            {
                Logger.sendMessage("> 笑川天皇提醒宁 看门狗④了");
                stage = 2;
                toDamage = false;
                ((IMixinS08PacketPlayerPosLook)((S08PacketPlayerPosLook) eventPacketReceive.packet)).setPitch(mc.thePlayer.rotationPitch);
                ((IMixinS08PacketPlayerPosLook)((S08PacketPlayerPosLook) eventPacketReceive.packet)).setYaw(mc.thePlayer.rotationYawHead);
            }
            else if(!mode.getValue().equals(FlightMode.Motion))
            {
                Logger.sendMessage("> hxd笑川飞行回弹了");
                setState(false);
            }
        }
    }
    void setMotionSpeed(double speed , EventMove eventMove){
        double forward = (double)this.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)this.mc.thePlayer.movementInput.moveStrafe;
        double yaw = mc.thePlayer.rotationYaw;
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
        eventMove.setX(forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))));
        eventMove.setZ(forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))));
    }
    //Damage method. It can only take 1 heart of damage.
    //2020/2/3 Jump Potion supported.
    public void damage(){
        double fallDistance = 0;
        double offset = 0.41999998688698;
        while (fallDistance < 4)
        {
            sendPacket(offset,false);
            sendPacket(0, fallDistance + offset >= 4);
            fallDistance += offset;
        }
    }
    void sendPacket(double addY,boolean ground){
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX,mc.thePlayer.posY+addY,mc.thePlayer.posZ,ground
        ));
    }
    private double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
        }

        return baseSpeed;
    }
}
enum HypixelMode{
    Zoom,
    OP,
    Float,
}
enum FlightMode{
    Hypixel,
    Motion,
    Test,
    Cube
}
