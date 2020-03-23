package zelix.cc.client.modules.motion;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.world.Scaffold;
import zelix.cc.client.utils.BlockHelper;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Math.MathUtil;
import zelix.cc.client.utils.Motion.MotionUtils;
import net.minecraft.potion.Potion;
import zelix.cc.injection.interfaces.IMixinEntity;

import java.util.List;
enum timer{
    Random,
    Basic,
    None
}
enum motion{
    Random,
    Basic,
    High,
    High2,
    Low
}
public class Speed
extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])SpeedMode.values(), (Enum)SpeedMode.HypixelF);
    private Mode<Enum> Timer = new Mode("Timer", "Timer", timer.values(), timer.None);
    private Mode<Enum> Motion = new Mode("Motion", "Motion", motion.values(), motion.Basic);
    public static Option<Boolean>  Random = new Option<Boolean> ("Random", "Random", true);
    public Speed() {
        super("Speed", ModuleType.Motion);
        addSettings(Timer,Motion,mode,Random);
    }
    int stage;
    double movedDist,motionSpeed;
    @Override
    public void onEnable(){
        movedDist = 0;
        Helper.getTimer().timerSpeed = 1f;
        stage = 0;
        motionSpeed = defaultSpeed();
        super.onEnable();
    }
    @Override
    public void onDisable(){
        Helper.getTimer().timerSpeed = 1f;
        super.onDisable();
    }
    // Anti Flag. (ONLY FOR HYPIXELF)
    @Runnable
    public void onAntiFlag(EventPacketReceive eventPacketReceive){
        if(eventPacketReceive.packet instanceof S08PacketPlayerPosLook) {
            stage = -5;
        }
    }
    @Runnable
    public void eventPPUpdate(EventPPUpdate eventPPUpdate){
        if (eventPPUpdate.isPre()) {
            setSuffix(mode.getValue().toString());
            double xDist = (mc.thePlayer.posX - mc.thePlayer.prevPosX);
            double zDist = (mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
            this.movedDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
        boolean tryToDoMotion =
                mc.gameSettings.keyBindBack.isKeyDown()||
                        mc.gameSettings.keyBindForward.isKeyDown()||
                        mc.gameSettings.keyBindRight.isKeyDown()||
                        mc.gameSettings.keyBindLeft.isKeyDown();
        if(mode.getValue().equals(SpeedMode.AAC)){
            if(tryToDoMotion){
                if(mc.thePlayer.onGround){
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = 0.407 + MotionUtils.getJumpEffect() * 0.1;
                }else {
                    mc.thePlayer.motionY -= 0.00147;
                }
                setMoveSpeed(movedDist);
                ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onMotion(mc.thePlayer.motionX,mc.thePlayer.motionZ);
            }else{
                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
            }
        }else if(mode.getValue().equals(SpeedMode.Hypixel)){
            /*if(tryToDoMotion)
            {
                if(mc.thePlayer.onGround)
                {
                    mc.thePlayer
                            .jump();
                    boolean scaffold= Zelix.getInstance().getModuleManager().getModuleByClass(Scaffold.class).isEnabled();
                    mc.thePlayer.motionY = 0.408666 + MotionUtils.getJumpEffect() * 0.1 + (scaffold ? 0.011 : 0);
                }
                motionSpeed = getMotionSpeed() * 1.1587548;
                if(mc.thePlayer.isPotionActive(Potion.moveSpeed))
                    motionSpeed *= 0.96;
                setMoveSpeed(motionSpeed);
                ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onMotion(mc.thePlayer.motionX,mc.thePlayer.motionZ);
            }
            else
            {
                motionSpeed = defaultSpeed();
            }*/
            if(tryToDoMotion){
                if(mc.thePlayer.onGround)
                {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionY = getMotion() + MotionUtils.getJumpEffect() * 0.1;
                    motionSpeed *= 1.32;
                    motionSpeed = Math.max(defaultSpeed(),motionSpeed);
                    setMoveSpeed(motionSpeed);
                }
                motionSpeed = Math.max(defaultSpeed(),motionSpeed);
                setMoveSpeed(motionSpeed * 0.991);
            }
        }
    }
    //For a new speedup method 2020/2/4
    double lastToDec = 0, maxSpeed = 0, decSpeed = 0;
    //Get speed by motionX & motionZ
    public double getSpeedByXZ(double motionX, double motionZ) {
        final double vel = Math.sqrt(motionX * motionX + motionZ * motionZ);
        return vel;
    }
    //EventPreUpdate supported
    public double getMotionSpeed(){
        double playerSpeed = getSpeedByXZ(mc.thePlayer.motionX, mc.thePlayer.motionZ);
        double finalSpeed = playerSpeed;
        double Default = defaultSpeed();
        int speedValue = MotionUtils.getSpeedEffect();
        int jumpValue = MotionUtils.getJumpEffect();
        //random (-0.0025 0.0025)
        double random = (Math.random() - 0.5) * 50000 / 10000000;
        if(mc.thePlayer.onGround)
        {
            //In test.
            //double minBoost = Default * (1.4 + random * 17.9992346);
            double maxBoost = playerSpeed * (1.27 + random * 5);
            //Limit the speed
            finalSpeed = maxBoost;
            if(decSpeed <= lastToDec) {
                finalSpeed *= 0.96;
            }
            if(speedValue == 0) {
                finalSpeed *= 1.05;
            } else {
                finalSpeed *= 1.1;
            }
            lastToDec = (finalSpeed - Default) * 0.379;
            maxSpeed = finalSpeed;
            decSpeed = 0;
        }
        else
        {
            double decSpeedA =  finalSpeed;
            if(jumpValue == 0)
            {
                finalSpeed *= 0.997;
                if(speedValue == 0)
                {
                    finalSpeed *= 0.987 - random * 1.011216364575;
                }
                else
                {
                    finalSpeed *= 0.982 + speedValue >= 2 ? 0.015 : 0.1 + random * 1.012512453675475685;
                }
                finalSpeed *= 0.99 + random / 6.1249975 + speedValue > 0 ? 0.05 : 0;
            }
            else
            {
                finalSpeed *= 0.995;
                if(speedValue == 0)
                {
                    finalSpeed *= 0.9897 + random * 1.01967248637867389063798;
                }
                else
                {
                    //Make speed faster but not too much
                    finalSpeed *= 0.98 + speedValue >= 2 ? 0.015 : 0.1 + random * 1.01967248637867389063798;
                }
                finalSpeed *= 0.99 + random / 5.1499999 + speedValue > 0 ? 0.05 : 0;
            }
            /*if(decSpeed <= lastToDec)
            {
                decSpeed += (decSpeedA - finalSpeed);
                //finalSpeed -= (decSpeedA - finalSpeed) * (random);
                finalSpeed -= 0.0015 + random / 60;
                decSpeed += 0.0015 + random / 60;
            }
            else
            {
                decSpeed += (decSpeedA - finalSpeed);
                //finalSpeed -= (decSpeedA - finalSpeed) * (random);
                finalSpeed += 0.0005 + random / 60;
                decSpeed -= 0.0005 + random / 60;
            }*/
            finalSpeed *= 1.04 + speedValue > 0 ? 0.015 : 0.024;
        }
        while(finalSpeed >= defaultSpeed() * (1.44 - MotionUtils.getSpeedEffect() * 0.005)) {
            finalSpeed *= 0.99 + random / 30;
        }
        finalSpeed = Math.max(finalSpeed,defaultSpeed());
        return finalSpeed;
    }
    //EventMove support.
    public double getMotionSpeed(int stage,double motionSpeed){
        boolean swift = mc.thePlayer.isPotionActive(Potion.moveSpeed);
        double moveSpeed = motionSpeed;
        double random = Random.getValue() ? MathUtil.randomDouble(1000,50000 - (swift ? 30000 : 0)) / 1000000 : 0;
        if(stage == 2 && mc.thePlayer.onGround){
            moveSpeed *= 1.479665 + (random * 1.0012415122114514);
            maxSpeed = moveSpeed;
            lastToDec = (moveSpeed - defaultSpeed()) * 0.66;
            decSpeed = 0;
        }else{
            double decSpeedA =  moveSpeed;
            boolean jumpPotion = mc.thePlayer.isPotionActive(Potion.jump);
            if(jumpPotion)
            {
                moveSpeed *= 0.997;
                if(swift) {
                    moveSpeed *= 0.981 + random/12.77;
                } else {
                    moveSpeed *= 0.98 + random/11.06;
                }
                moveSpeed *= 0.991;
            }
            else
            {
                double boostVal = defaultSpeed() /10;
                if(boostVal > 0.042) {
                    boostVal = 0.042;
                }
                if(swift) {
                    moveSpeed *= 0.937399541 + boostVal + random / 10.142915136246357547865789;
                } else {
                    moveSpeed *= 0.9745 + random / 10.2413624635746856867987807890890;
                }
            }
            /*if(decSpeed <= lastToDec)
            {
                decSpeed += decSpeedA - moveSpeed;
                moveSpeed -= 0.0015 + random / 400.12342345;
                decSpeed += 0.0015 + random / 400.12342345;
            }
            else
            {
                decSpeed += decSpeedA - moveSpeed;
                moveSpeed += 0.0005 + random / 400.12342345;
                decSpeed -= 0.0005 + random / 400.12342345;
            }*/
        }
        while(moveSpeed >= defaultSpeed() * (1.41 - MotionUtils.getSpeedEffect() * 0.005) && stage != 2) {
            moveSpeed *= 0.99 + random / 30;
        }
        return moveSpeed;
    }
    void setTimer(){
        Enum curMode = Timer.getValue();
        if(curMode.equals(timer.Basic))
        {
            Helper.getTimer().timerSpeed = 1f;
        }
        else if(curMode.equals(timer.Basic))
        {
            Helper.getTimer().timerSpeed = 1.030247027351f;
        }
        else if(curMode.equals(timer.Random))
        {
            Helper.getTimer().timerSpeed = (float) (1.03 + MathUtil.randomDouble(0,99)/100000);
        }
    }
    public double getMotion(){
        Enum curMode = Motion.getValue();
        if(curMode.equals(motion.Basic)) {
            return 0.405412D;
        } else if(curMode.equals(motion.High)) {
            return 0.408976666;
        } else if(curMode.equals(motion.Low)) {
            return 0.4001754672;
        } else if(curMode.equals(motion.Random)) {
            return 0.405412D + MathUtil.randomDouble(-20,20) / 10000;
        } else if(curMode.equals(motion.High2)) {
            return 0.41999675;
        }
        return 0.408666666d;
    }
    @Runnable
    public void eventMove(EventMove eventMove){
        setTimer();
        boolean tryToDoMotion =
                mc.gameSettings.keyBindBack.isKeyDown()||
                        mc.gameSettings.keyBindForward.isKeyDown()||
                        mc.gameSettings.keyBindRight.isKeyDown()||
                        mc.gameSettings.keyBindLeft.isKeyDown();
        if(mode.getValue().equals(SpeedMode.Bhop)){
            if(tryToDoMotion){
                if(mc.thePlayer.onGround){
                    eventMove.setY(mc.thePlayer.motionY = getMotion()+MotionUtils.getJumpEffect()*0.1f);
                    motionSpeed = movedDist > defaultSpeed() * (1.35 + MotionUtils.getSpeedEffect() * 0.05) ?
                            defaultSpeed() * (1.35 + MotionUtils.getSpeedEffect() * 0.05) : movedDist * 1.58444;
                    setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
                    return;
                }
                this.motionSpeed = Math.max(this.motionSpeed, defaultSpeed());
                setMoveSpeedNoStrafeEdit(motionSpeed * 1.012,eventMove);
                ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
            }
        }else if(mode.getValue().equals(SpeedMode.HypixelPort)){
            if(mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || ((IMixinEntity)mc.thePlayer).isInWeb() || !tryToDoMotion)
                return;
            double gay2 = getMotion();
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
            }
            if (this.mc.thePlayer.onGround&&tryToDoMotion && this.stage == 2&&(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                if(!BlockHelper.isInLiquid()){
                    this.mc.thePlayer.motionY = gay2;
                    eventMove.setY(mc.thePlayer.motionY);
                }
                this.motionSpeed *=  1.7999;
            } else if (this.stage == 3) {
                double diff = (0.681+(mc.thePlayer.ticksExisted%2)/50) * (this.movedDist - this.defaultSpeed());
                this.motionSpeed = this.movedDist - diff;
            } else {
                if (getCollidingList(eventMove.getY()).size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = tryToDoMotion ? 1 : 0;
                }
                this.motionSpeed = this.movedDist - this.movedDist / ((mc.thePlayer.ticksExisted%2 == 0 ? -0.5 : -1)+159.21);
            }
            if(!mc.thePlayer.onGround)
                eventMove.setY(mc.thePlayer.motionY -= 1D);
            this.motionSpeed = Math.max(this.motionSpeed, defaultSpeed());
            if(BlockHelper.isInLiquid()) {
                motionSpeed=0.12;
            }
            setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
            ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
            stage++;
            mc.thePlayer.stepHeight = 0.6F;
        }else if(mode.getValue().equals(SpeedMode.FastPort)){
            if(mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || ((IMixinEntity)mc.thePlayer).isInWeb() || !tryToDoMotion)
                return;
            double gay2 = getMotion();
            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
            }
            if (this.mc.thePlayer.onGround&&tryToDoMotion && this.stage == 2&&(mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                if(!BlockHelper.isInLiquid()){
                    this.mc.thePlayer.motionY = gay2;
                    eventMove.setY(mc.thePlayer.motionY);
                }
                this.motionSpeed *=  2.149976;
            } else if (this.stage == 3) {
                double diff = (0.681+(mc.thePlayer.ticksExisted%2)/50) * (this.movedDist - this.defaultSpeed());
                this.motionSpeed = this.movedDist - diff;
            } else {
                if (getCollidingList(eventMove.getY()).size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = tryToDoMotion ? 1 : 0;
                }
                this.motionSpeed = this.movedDist - this.movedDist / ((mc.thePlayer.ticksExisted%2 == 0 ? -0.5 : -1)+159.21);
            }
            if(!mc.thePlayer.onGround) {
                eventMove.setY(mc.thePlayer.motionY -= 1D);
            }
            this.motionSpeed = Math.max(this.motionSpeed, defaultSpeed());
            if(BlockHelper.isInLiquid()) {
                motionSpeed=0.12;
            }
            setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
            ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
            stage++;
            mc.thePlayer.stepHeight = 0.6F;
        }else if( mode.getValue().equals(SpeedMode.CNHypixel)){
            this.motionSpeed = defaultSpeed();
            if (this.stage < 1) {
                ++this.stage;
                this.motionSpeed = 0.0;
            }
            if (this.stage == 2 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && this.mc.thePlayer.onGround) {
                double y = getMotion();
                if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                    y += (this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                }
                eventMove.setY(this.mc.thePlayer.motionY = y);
                this.motionSpeed *= 2.1498624684;
            }
            else if (this.stage == 3) {
                double diff = ((this.stage % 3 == 0) ? 0.678994565156 : 0.719499495154) * (this.movedDist - defaultSpeed());
                this.motionSpeed = this.movedDist - diff;
            }
            else {
                if ((this.mc.theWorld.getCollidingBoundingBoxes((Entity)this.mc.thePlayer, (this.mc.thePlayer).getEntityBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                    this.stage = ((this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                }
                this.motionSpeed = this.movedDist - this.movedDist / 159.0;
            }
            this.motionSpeed = Math.max(this.motionSpeed, defaultSpeed());
            setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
            ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
            ++this.stage;
        }else if(mode.getValue().equals(SpeedMode.HypixelF)){
            if(!mc.thePlayer.isInWater() && !BlockHelper.isInLiquid()){
                if(mc.thePlayer.onGround && Helper.moving()) {
                    eventMove.setY(mc.thePlayer.motionY = (getMotion() + MotionUtils.getJumpEffect() * 0.1));
                    this.motionSpeed = getMotionSpeed(2,motionSpeed);
                }else{
                    this.motionSpeed = getMotionSpeed(3,movedDist);
                }
                if(stage > 0){
                    motionSpeed = Math.max(defaultSpeed(),motionSpeed);
                    if(Helper.moving())
                    {
                        setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
                        ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
                    }
                }
                if(Helper.moving()) {
                    stage++;
                }
            }
        }else if(mode.getValue().equals(SpeedMode.HypixelO)){
            if(!mc.thePlayer.isInWater() && !BlockHelper.isInLiquid() && tryToDoMotion){
                if(mc.thePlayer.onGround)
                {
                    eventMove.setY(mc.thePlayer.motionY = (getMotion() + MotionUtils.getJumpEffect() * 0.1));
                    this.motionSpeed = getMotionSpeed(2,motionSpeed);
                }
                motionSpeed = Math.max(defaultSpeed(),motionSpeed);
                setMoveSpeedNoStrafeEdit(motionSpeed,eventMove);
                ((TargetStrafe)Zelix.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).onStrafe(eventMove);
            }
        }
    }
    List getCollidingList(double motionY){
        return this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, motionY, 0.0));
    }
    public int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    private double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
        }

        return baseSpeed;
    }
    public void setMoveSpeed(final double speed) {
        double forward = (double)this.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)this.mc.thePlayer.movementInput.moveStrafe;
        float yaw;
        {
            yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                mc.thePlayer.motionX = (0.0);
                mc.thePlayer.motionZ = (0.0);
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
                mc.thePlayer.motionX = ((forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f)))));
                mc.thePlayer.motionZ = ((forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f)))));
            }
        }
    }
    public void setMoveSpeedNoStrafeEdit(final double speed,EventMove eventMove) {
        double forward = (double)this.mc.thePlayer.movementInput.moveForward;
        double strafe = (double)this.mc.thePlayer.movementInput.moveStrafe;
        float yaw;
        {
            yaw = mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                mc.thePlayer.motionX = (0.0);
                mc.thePlayer.motionZ = (0.0);
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
                eventMove.setX((forward * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f)))));
                eventMove.setZ((forward * speed * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * speed * Math.cos(Math.toRadians((double)(yaw + 90.0f)))));
            }
        }
    }
}
enum SpeedMode{
    Hypixel,
    HypixelF,
    HypixelO,
    CNHypixel,
    HypixelPort,
    FastPort,
    AAC,
    Bhop
}
