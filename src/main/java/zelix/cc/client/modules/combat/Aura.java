package zelix.cc.client.modules.combat;

import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.misc.EventAttack;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.client.eventAPI.events.motion.EventSlowdown;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.eventAPI.events.render.Event2D;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.modules.world.Teams;
import zelix.cc.client.utils.BlockRender;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Math.LocationUtil;
import zelix.cc.client.utils.Math.MathUtil;
import zelix.cc.client.utils.Math.TimerUtil;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.Render.RenderUtil;
import zelix.cc.client.utils.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Aura
extends Module {
    public static EntityLivingBase curTarget;
    private Option<Boolean> targethud = new Option<Boolean>("TargetHUD", "TargetHUD", true);
    private Mode<Enum> espmode = new Mode("TargetESP", "TargetESP", (Enum[])ESPMode.values(), (Enum)ESPMode.Other2D);
    public static Option<Boolean> aimautoblock = new Option<Boolean>("Aim-Blocking", "Aim-Blocking", true);
    public static Option<Boolean> autoblock = new Option<Boolean>("Autoblock", "Autoblock", true);
    public static Option<Boolean> slowdown = new Option<Boolean>("Slowdown", "slow", true);
    public static Option<Boolean> players = new Option<Boolean> ("Players", "Players", true);
    public static Option<Boolean>  animals = new Option<Boolean> ("Animals", "Animals", false);
    public static Option<Boolean>  monsters = new Option<Boolean> ("Monsters", "Monsters", false);
    public static Option<Boolean>  boss = new Option<Boolean> ("Boss", "Boss", false);
    public static Option<Boolean>  invisibles = new Option<Boolean> ("Invisibles", "Invisibles", true);
    public static Amount<Double> reach = new Amount<Double>("Range", "Range", 8.0, 1.0, 0.2, 4.4);
    public static Amount<Double> switchs = new Amount<Double>("SwitchPost", "SwitchPost", 2000.0, 50.0, 10.0, 1200.0);
    public static Amount<Double> MinAPS = new Amount<Double>("MinAPS", "MinAPS", 20.0, 1.0, 0.5,11.0);
    public static Amount<Double> MaxAPS = new Amount<Double>("MaxAPS", "MaxAPS", 20.0, 1.0, 0.5,13.0);
    public Aura() {
        super("KillAura", ModuleType.Combat);
        addSettings(MaxAPS,MinAPS,switchs,reach,autoblock,aimautoblock,slowdown,players,animals,monsters,invisibles,boss,targethud,espmode);
    }
    static enum ESPMode {
        None,
        Box,
        Other2D;
    }
    public static ArrayList<EntityLivingBase> attackList = new ArrayList<>();
    public static ArrayList<EntityLivingBase> blockList = new ArrayList<>();
    public static boolean isValidEntity(Entity e){
        if (animals.value && e instanceof EntityAnimal ||animals.value &&  e instanceof EntityVillager) {
            return true;
        }

        if (players.value && e instanceof EntityPlayer) {
            return true;
        }

        if (monsters.value && e instanceof EntityMob || monsters.value &&e instanceof EntitySlime) {
            return true;
        }
        if(boss.value&&  e instanceof EntityWither ||boss.value&&  e instanceof EntityDragon)return true;
        return false;
    }public boolean isValidTarget(Entity en){

        return mc.thePlayer.getDistanceToEntity(en) <= reach.getValue() && (en.isInvisible() ? invisibles.value : true) && ((EntityLivingBase)en).getHealth() > 0 && !en.isDead&&
                isValidEntity(en) && (en != mc.thePlayer) && !AntiBot.isBlackListed(en) && !Teams.isOnSameTeam(en);
    }

    void collectTargets(){


        mc.theWorld.loadedEntityList.forEach(o -> {
            if(isValidEntity(o)){
                EntityLivingBase e = (EntityLivingBase) o;
                if(isValidTarget(e) && !attackList.contains(e)) {
                    attackList.add(e);
                }
                if(!isValidTarget(e) && attackList.contains(e)) {
                    attackList.remove(e);
                }
                if(mc.thePlayer.getDistanceToEntity(e) > 15 && attackList.contains(e)) {
                    attackList.remove(e);
                }
            }


        });

    }

    public void sortTargets() {
        attackList.sort(Comparator.comparingDouble(o -> RotationUtil.getYawToEntity(o)));
    }

    public static float lastYaw;
    int curTargetInt
            ,switchdelay;
    void unBlock(){
        mc.getNetHandler().getNetworkManager().sendPacket((
                new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                        new BlockPos(-.9, -.9, -.9),
                        EnumFacing.DOWN)));
        isBlocking=false;
    }
    double randomNumber(double var1, double var2) {
        return (Math.random() * (double)(var1 - var2)) + var2;
    }
    boolean blockingByPlayer = false;
    @Runnable
    private void onSlowdown(EventSlowdown eventSlowdown){
        if(eventSlowdown.isPre) {
            return;
        }
        if ((autoblock.getValue() && !attackList.isEmpty())){
            eventSlowdown.isPre = true;
            eventSlowdown.slowValue = 0.95f;
        }
    }
    @Runnable
    public void blockListener(EventPacketSend e){
        Packet packet = e.packet;
        if(packet instanceof C08PacketPlayerBlockPlacement)
        {
            C08PacketPlayerBlockPlacement packetPlayerBlockPlacement = (C08PacketPlayerBlockPlacement) packet;
            if(packetPlayerBlockPlacement.getStack() != null
                    && packetPlayerBlockPlacement.getStack().getItem() instanceof ItemSword
                    && packetPlayerBlockPlacement.getPosition().equals(new BlockPos(-1,-1,-1))
                    && packetPlayerBlockPlacement.getPlacedBlockDirection() == 255
            );
        }
        if(packet instanceof C07PacketPlayerDigging)
        {
            C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging) packet;
            if(packetPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM
                    && packetPlayerDigging.getFacing() == EnumFacing.DOWN
                    && packetPlayerDigging.getPosition() == BlockPos.ORIGIN
            );
        }
    }
    private static double randomCPS = 0;
    public static void setSlowdown(EventMove eventMove){
        if(slowdown.getValue()){
            if(!attackList.isEmpty()){
                if(Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
                {
                    eventMove.x *= 0.91;
                    eventMove.z *= 0.91;
                }
                else
                {
                    eventMove.x *= 0.94;
                    eventMove.z *= 0.94;
                }
            }
        }
    }
    public static boolean canSlowdown(){
        return slowdown.getValue()&&!attackList.isEmpty();
    }
    public static void setSlowdown(){
        if(slowdown.getValue()){
            if(!attackList.isEmpty()){
                if(Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
                {
                    Minecraft.getMinecraft().thePlayer.motionX *= 0.91;
                    Minecraft.getMinecraft().thePlayer.motionZ *= 0.91;
                }
                else
                {
                    Minecraft.getMinecraft().thePlayer.motionX *= 0.94;
                    Minecraft.getMinecraft().thePlayer.motionZ *= 0.94;
                }
            }
        }
    }
    TimerUtil blockingTimer = new TimerUtil();
    @Runnable
    public void onPacketR(EventPacketReceive eventPacketReceive){
        boolean flagged = eventPacketReceive.packet instanceof S08PacketPlayerPosLook;
        if(flagged){
            blockingTimer.reset();
        }
    }
    @Runnable
    public void onPPUpdate(EventPPUpdate e ){
        if (curTargetInt >= attackList.size())
            curTargetInt = 0;
        collectTargets();
        sortTargets();
        setSuffix("Switch-In "+attackList.size());
        if(MaxAPS.getValue() < MinAPS.getValue()) {
            MaxAPS.setValue(MinAPS.getValue());
        }
        if(MaxAPS.getValue().equals(MinAPS.getValue())) {
            randomCPS = MinAPS.getValue();
        } else {
            randomCPS = MathUtil.randomDouble(MinAPS.getValue(),MaxAPS.getValue());
        }
        if(e.isPre()){
            if(mc.currentScreen instanceof GuiDownloadTerrain)
            {
                Logger.sendMessage("Aura disabled due to mc.theWorld's changing");
                setState(false);
            }
            if(!attackList.isEmpty()) {
                curTarget = getTarget();
                float[] NeedRotation = getRotationByBoundingBox(curTarget,true);
                if(!AutoPotion.isPotting()){
                    e.setYaw(NeedRotation[0]);
                    e.setPitch(NeedRotation[1]);
                    mc.thePlayer.renderYawOffset=e.getYaw();
                    mc.thePlayer.rotationYawHead=e.getYaw();
                    lastYaw = mc.thePlayer.rotationYawHead;
                }
            }else{
                lastYaw=mc.thePlayer.rotationYaw;
                if(isBlocking){
                    unBlock();
                }
            }
        }else{
            if(!attackList.isEmpty()&&curTarget!=null){
                if(!AutoPotion.isPotting()){
                    if(reachedAttack()){
                        attack(curTarget);
                    }
                }
                if(autoblock.value){
                    BlockRender.block = true;
                }
                if(autoblock.getValue() && canBlock() && allowBlocking()){
                    blockIt();
                }

            }else{
                if(BlockRender.block){
                    BlockRender.block = false;
                }

                if(isBlocking){
                    unBlock();
                }
            }
        }
    }
    public boolean allowBlocking(){
        double speed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX 
                + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
        boolean swift = mc.thePlayer.isPotionActive(Potion.moveSpeed);
        double decChance = swift ? speed * 60 : 0;
        //System.out.println(isAnyEntityAimingAtMe());
        boolean toBlocking = MathUtil.randomDouble(0,100) <= (100 - decChance) && isAnyEntityAimingAtMe();
        return blockingTimer.hasReached(100) && toBlocking;
    }
    public boolean isAnyEntityAimingAtMe(){
        if(aimautoblock.getValue()){
            final List<Entity> entityList = new ArrayList<>();
            for(Entity entityPlayer : mc.theWorld.loadedEntityList){
                if(!entityPlayer.equals(mc.thePlayer) && entityPlayer.getDistanceToEntity(mc.thePlayer) <= 8 && isEntityAllowed(entityPlayer)){
                    if(!entityList.contains(entityPlayer)) {
                        entityList.add(entityPlayer);
                    }
                }
            }
            if(!entityList.isEmpty()) {
                return entityList.stream().anyMatch(entity -> RotationUtil.isAimAtMe(entity));
            }
            return false;
        }else {
            return true;
        }
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
        if(entity instanceof EntitySlime || entity instanceof EntityMob || entity instanceof EntityAnimal || entity instanceof EntityVillager)
            return true;
        return false;
    }
    /*
     * Rotation :
     * Changelog:
     * bug fixed(11/01/20)
     */
    public float[] getRotationByBoundingBox(Entity ent,boolean random){
        if(ent == null)
            return new float[]{0,0};
        AxisAlignedBB boundingBox = ent.getEntityBoundingBox();
        double boundingX = (boundingBox.maxX-boundingBox.minX)/8;
        double boundingY = (boundingBox.maxY-boundingBox.minY)/8;
        double boundingZ = (boundingBox.maxZ-boundingBox.minZ)/8;
        double orPosX = (boundingBox.maxX-boundingBox.minX)/2+boundingBox.minX,
                orPosY= boundingBox.minY,
                orPosZ = (boundingBox.maxZ-boundingBox.minZ)/2+boundingBox.minZ;
        if(random){
            orPosX+=randomNumber(boundingX,-boundingX);
            orPosY+=randomNumber(boundingY,-boundingY);
            orPosZ+=randomNumber(boundingZ,-boundingZ);
        }
        double pX = mc.thePlayer.posX;
        double pZ = mc.thePlayer.posZ;
        double dX = pX - orPosX;
        double dZ = pZ - orPosZ;
        double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        LocationUtil BestPos = new LocationUtil(ent.getEntityBoundingBox().minX, orPosY, ent.getEntityBoundingBox().minZ);
        LocationUtil myEyePos = new LocationUtil(mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY+ (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.getEntityBoundingBox().minZ);
        double diffY;
        for(diffY = ent.getEntityBoundingBox().minY + 0.7D; diffY < ent.getEntityBoundingBox().maxY - 0.1D; diffY += 0.1D) {
            if (myEyePos.distanceTo(new LocationUtil(ent.getEntityBoundingBox().minX, diffY, ent.getEntityBoundingBox().minZ)) < myEyePos.distanceTo(BestPos)) {
                BestPos = new LocationUtil(ent.getEntityBoundingBox().minX, diffY, ent.getEntityBoundingBox().minZ);
            }
        }
        diffY = BestPos.getY() - (mc.thePlayer.getEntityBoundingBox().minY + (double)mc.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(dX * dX + dZ * dZ);
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
        return new float[]{(float) yaw,pitch};
    }
    boolean isBlocking;
    boolean attack(Entity e){
        boolean unBlock = false;
        if(isBlocking){
            unBlock();
            unBlock = true;
        }
        mc.thePlayer.swingItem();
        /*
         * EventAttack calls:
         * for Criticals.class to do packetCrit.
         * cancelled: cancel the attack.
         */
        EventAttack ea = new EventAttack(e);
        EventController.getEventController().call(ea);
        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
        changeTargets();
        blockTick = 0;
        attackTimer.reset();
        return unBlock;
    }
    public EntityLivingBase getTarget() {
        try {
            return  attackList.get(curTargetInt);
        } catch (Throwable x) {
            return null;
        }
    }
    private void changeTargets(){

        if (attackList.size() == 1) {
            return;
        }
        if (switchTimer.hasReached(switchs.getValue())) {
            curTargetInt += 1;
            switchTimer.reset();
        }
    }
    static TimerUtil attackTimer = new TimerUtil();
    static TimerUtil switchTimer = new TimerUtil();
    public static boolean reachedAttack(){
        return attackTimer.hasReached(1000/ randomCPS);
    }
    boolean doAnim;
    int blockTick = 0;
    public boolean canBlock() {
        return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }

    void blockIt(){
        mc.getNetHandler().getNetworkManager().sendPacket(
                new C08PacketPlayerBlockPlacement(
                        new BlockPos(-.9, -.9, -.9),
                        255,
                        mc.thePlayer.getCurrentEquippedItem(),
                        0,
                        0,
                        0));
        isBlocking = true;
    }

    @Override
    public void onEnable(){
        //renderPanelAtX=RenderUtil.width();
        if(antiException()){
            //renderPanelAtX = RenderUtil.width();
            doAnim=false;
            switchdelay=0;
            attackList.clear();
            blockList.clear();
            curTargetInt=0;
            curTarget=null;
            lastYaw=mc.thePlayer.rotationYaw;
            isBlocking=mc.thePlayer.isBlocking()?true:false;
            blockingByPlayer=isBlocking;
        }
        super.onEnable();
    }

    @Override
    public void onDisable(){
        //renderPanelAtX=RenderUtil.width();
        if(antiException()){
            attackList.clear();
            blockList.clear();
            curTarget=null;
            lastYaw=mc.thePlayer.rotationYaw;
            if(isBlocking){
                unBlock();
            }
            if(BlockRender.block){
                BlockRender.block =false;
            }
        }
        super.onDisable();
    }
    public Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format((double)red) + "; " + nf.format((double)green) + "; " + nf.format((double)blue));
            exp.printStackTrace();
        }
        return color3;
    }
    public boolean isFinished() {
        return attackList.isEmpty();
    }
    float y=0,y2=0,maxHealthRecorded=0,totalArmorValue=0;
    String playerName = "";
    boolean antiException(){
        return mc.theWorld!=null&&mc.thePlayer!=null;
    }
    @Runnable
    public void on2D(Event2D e){
        FontRendererUtil titleFont = Zelix.instance.fontManager.getFont("CONS 24");
        FontRendererUtil infoFont = Zelix.instance.fontManager.getFont("CONS 20");
        ResourceLocation attack = new ResourceLocation("Zelix/texture/HUD/attack.png");
        float x2 = RenderUtil.width()/2- 100;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if(y != 0) {
            y = (float) RenderUtil.getAnimationState(y, 0, 200);
            GL11.glTranslatef(0.0f, y, 0.0f);
        }
        if(this.isFinished()){
            y2 = (float) RenderUtil.getAnimationState(y2, -30, 200);
            GL11.glTranslatef(0.0f, y2, 0.0f);
        }
        RenderUtil.R2DUtils.drawRoundedRect(x2, 5,x2+200.0f, 23+4+20, new Color(240,240,240).getRGB(), new Color(240,240,240).getRGB());
        RenderUtil.drawImage(attack, x2+1, 6, 16, 16);
        if((!attackList.isEmpty()&&curTarget!=null)){
            y2 = 0;
            EntityLivingBase curTarget = (EntityLivingBase) this.curTarget;
            titleFont.drawString(curTarget.getName(),x2+16, 7, new Color(30,30,30).getRGB());
            infoFont.drawString("HP: "+(int)curTarget.getHealth(),x2+1,25, new Color(30,30,30).getRGB());
            RenderUtil.R2DUtils.drawRect(x2+1, 40, x2+1+198, 43,new Color(150,150,150).getRGB());
            RenderUtil.R2DUtils.drawRect(x2+1, 40, x2+1+198.0f*(curTarget.getHealth() / curTarget.getMaxHealth()), 43,new Color(30,30,30).getRGB());
        } else {
            y = -30;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    @Runnable
    public void onEvent3D(Event3D event) {
        Color color =new Color(5,5,5);
        if(!attackList.isEmpty()) {
            RenderManager renderMgr = mc.getRenderManager();
            if(espmode.getValue().equals(ESPMode.None)) return;
            EntityLivingBase curTarget = (EntityLivingBase)this.curTarget;
            if(espmode.getValue().equals(ESPMode.Box)) {
                double x2 = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * (double) Helper.getrenderPartialTicks() - Helper.renderPosX();
                double y2 = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * (double) Helper.getrenderPartialTicks() - Helper.renderPosY();
                double z2 = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * (double) Helper.getrenderPartialTicks() - Helper.renderPosZ();
                if (curTarget instanceof EntityPlayer) {
                    double width = curTarget.getEntityBoundingBox().maxX - curTarget.getEntityBoundingBox().minX;
                    double height = curTarget.getEntityBoundingBox().maxY - curTarget.getEntityBoundingBox().minY + 0.25;
                    float red = curTarget.hurtTime > 0 ? 1.0f : 0.0f;
                    float green = curTarget.hurtTime > 0 ? 0.2f : 0.0f;
                    float blue = curTarget.hurtTime > 0 ? 0.0f : 0.0f;
                    float alpha = 0.0f;
                    float lineRed = curTarget.hurtTime > 0 ? 1.0f : 0.0f;
                    float lineGreen = curTarget.hurtTime > 0 ? 0.2f : 0.0f;
                    float lineBlue = curTarget.hurtTime > 0 ? 0.0f : 0.0f;
                    float lineAlpha = 1.0f;
                    float lineWdith = 2.0f;
                    RenderUtil.drawEntityESP(x2, y2, z2, width, height, red, green, blue, 0.0f, lineRed, lineGreen, lineBlue, 1.0f, 2.0f);
                } else {
                    double width = curTarget.getEntityBoundingBox().maxX - curTarget.getEntityBoundingBox().minX + 0.1;
                    double height = curTarget.getEntityBoundingBox().maxY - curTarget.getEntityBoundingBox().minY + 0.25;
                    float red = curTarget.hurtTime > 0 ? 1.0f : 0.0f;
                    float green = curTarget.hurtTime > 0 ? 0.2f : 0.0f;
                    float blue = curTarget.hurtTime > 0 ? 0.0f : 0.0f;
                    float alpha = 0.0f;
                    float lineRed = curTarget.hurtTime > 0 ? 1.0f : 0.0f;
                    float lineGreen = curTarget.hurtTime > 0 ? 0.2f : 0.0f;
                    float lineBlue = curTarget.hurtTime > 0 ? 0.0f : 0.0f;
                    float lineAlpha = 1.0f;
                    float lineWdith = 2.0f;
                    RenderUtil.drawEntityESP(x2, y2, z2, width, height, red, green, blue, 0.0f, lineRed, lineGreen, lineBlue, 1.0f, 2.0f);
                }
            } else {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.enableBlend();
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                float partialTicks = Helper.getTimer().renderPartialTicks;
                double x = curTarget.lastTickPosX + (curTarget.posX - curTarget.lastTickPosX) * partialTicks - Helper.renderPosX();
                double y = curTarget.lastTickPosY + (curTarget.posY - curTarget.lastTickPosY) * partialTicks - Helper.renderPosY();
                double z = curTarget.lastTickPosZ + (curTarget.posZ - curTarget.lastTickPosZ) * partialTicks - Helper.renderPosZ();
                float DISTANCE = this.mc.thePlayer.getDistanceToEntity(curTarget);
                float DISTANCE_SCALE = Math.min(DISTANCE * 0.15f, 0.15f);
                float SCALE = 0.035f;
                SCALE /= 2.0f;
                float xMid = (float)x;
                float yMid = (float)y + curTarget.height + 0.5f - (curTarget.isChild() ? (curTarget.height / 2.0f) : 0.0f);
                float zMid = (float)z;
                GlStateManager.translate((float)x, (float)y + curTarget.height + 0.5f - (curTarget.isChild() ? (curTarget.height / 2.0f) : 0.0f), (float)z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-renderMgr.playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glScalef(-SCALE, -SCALE, -SCALE);
                Tessellator tesselator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tesselator.getWorldRenderer();
                float HEALTH = curTarget.getHealth();
                int COLOR = -1;
                if (HEALTH > 20.0) {
                    COLOR = -65292;
                }
                else if (HEALTH >= 10.0) {
                    COLOR = -16711936;
                }
                else if (HEALTH >= 3.0) {
                    COLOR = -23296;
                }
                else {
                    COLOR = -65536;
                }
                Color gray = new Color(0, 0, 0);

                double xLeft = -20.0;
                double xRight = 20.0;
                double yUp = 27.0;
                double yDown = 130.0;
                double size = 10.0;
                if (curTarget.hurtTime > 0) {
                    color = new Color(205, 0, 0,250);
                }
                RenderUtil.drawBorderedRect((float)xLeft-20.0f, (float)yUp-19.0f, (float)xRight+20.0f, (float)yDown+20.0f, 2.1f, color.getRGB(),  new Color(2,2,2,0).getRGB());
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GlStateManager.disableBlend();
                GL11.glDisable(3042);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glNormal3f(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
}
