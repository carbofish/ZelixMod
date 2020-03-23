/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package zelix.cc.injection.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scala.collection.parallel.ParIterableLike;
import zelix.cc.client.Zelix;
import zelix.cc.client.command.cmds.Help;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.misc.EventChat;
import zelix.cc.client.eventAPI.events.motion.EventMove;
import zelix.cc.client.eventAPI.events.motion.EventSlowdown;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.player.Command;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.SafeWalkUtil;
import zelix.cc.injection.interfaces.IMixinEntityPlayerSP;

import java.util.List;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer implements IMixinEntityPlayerSP {

    //Filed Shadow
    @Shadow
    public int sprintingTicksLeft;
    @Shadow
    protected int sprintToggleTimer;
    @Shadow
    public float timeInPortal;
    @Shadow
    public float prevTimeInPortal;
    @Shadow
    protected abstract boolean pushOutOfBlocks(double x, double y, double z);
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    protected abstract void sendHorseJump();
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    public int horseJumpPowerCounter;
    //Method Shadow
    @Shadow
    protected abstract boolean isCurrentViewEntity();
    @Shadow
    public abstract void playSound(String name, float volume, float pitch);
    @Shadow
    public float horseJumpPower;
    private double cachedX;
    private double cachedY;
    private double cachedZ;

    private float cachedRotationPitch;
    private float cachedRotationYaw;

    EventPPUpdate eventPPUpdate_PRE;

    public MixinEntityPlayerSP() {
    }

    @Overwrite
    public void onUpdateWalkingPlayer() {
        boolean flag = Minecraft.getMinecraft().thePlayer.isSprinting();
        EventPPUpdate eventPPUpdate_PRE = new EventPPUpdate(this.rotationYaw, this.rotationPitch, Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().minY, this.onGround, true);
        EventController.call(eventPPUpdate_PRE);
        if (flag != this.serverSprintState)
        {
            if (flag)
            {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = Minecraft.getMinecraft().thePlayer.isSneaking();

        if (flag1 != this.serverSneakState)
        {
            if (flag1)
            {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.getMinecraft().thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity())
        {
            double d0 = Minecraft.getMinecraft().thePlayer.posX - this.lastReportedPosX;
            double d1 = eventPPUpdate_PRE.getY() - this.lastReportedPosY;
            double d2 = Minecraft.getMinecraft().thePlayer.posZ - this.lastReportedPosZ;
            double d3 = (double)(Minecraft.getMinecraft().thePlayer.rotationYaw - this.lastReportedYaw);
            double d4 = (double)(Minecraft.getMinecraft().thePlayer.rotationPitch - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (Minecraft.getMinecraft().thePlayer.ridingEntity == null)
            {
                if (flag2 && flag3)
                {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, eventPPUpdate_PRE.getY(), this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
                }
                else if (flag2)
                {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, eventPPUpdate_PRE.getY(), this.posZ, this.onGround));
                }
                else if (flag3)
                {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(eventPPUpdate_PRE.getYaw(), eventPPUpdate_PRE.getPitch(), eventPPUpdate_PRE.isOnGround()));
                }
                else
                {
                    Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(eventPPUpdate_PRE.isOnGround()));
                }
            }
            else
            {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.motionX, -999.0D, Minecraft.getMinecraft().thePlayer.motionZ, eventPPUpdate_PRE.getYaw(), eventPPUpdate_PRE.getPitch(), eventPPUpdate_PRE.isOnGround()));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2)
            {
                this.lastReportedPosX = this.posX;
                this.lastReportedPosY = eventPPUpdate_PRE.getY();
                this.lastReportedPosZ = this.posZ;
                this.positionUpdateTicks = 0;
            }

            if (flag3)
            {
                this.lastReportedYaw = this.rotationYaw;
                this.lastReportedPitch = this.rotationPitch;
            }

            EventController.call(new EventPPUpdate(eventPPUpdate_PRE.getYaw(),eventPPUpdate_PRE.getPitch(),eventPPUpdate_PRE.getY(),eventPPUpdate_PRE.isOnGround(),false));
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        EventMove event = new EventMove(x,y,z);
        EventController.call(event);

        x = event.getX();
        y = event.getY();
        z = event.getZ();

        if(this.noClip) {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
            this.posY = this.getEntityBoundingBox().minY;
            this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
        }else{
            this.worldObj.theProfiler.startSection("move");
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;

            if(this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d3 = x;
            double d4 = y;
            double d5 = z;
            boolean flag = this.onGround && this.isSneaking();

            if (flag || SafeWalkUtil.safewalk) {
                double d6;

                for(d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes((Entity) (Object) this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x) {
                    if(x < d6 && x >= -d6) {
                        x = 0.0D;
                    }else if(x > 0.0D) {
                        x -= d6;
                    }else{
                        x += d6;
                    }
                }

                for(; z != 0.0D && this.worldObj.getCollidingBoundingBoxes((Entity) (Object) this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z) {
                    if(z < d6 && z >= -d6) {
                        z = 0.0D;
                    }else if(z > 0.0D) {
                        z -= d6;
                    }else{
                        z += d6;
                    }
                }

                for(; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes((Entity) (Object) this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z) {
                    if(x < d6 && x >= -d6) {
                        x = 0.0D;
                    }else if(x > 0.0D) {
                        x -= d6;
                    }else{
                        x += d6;
                    }

                    d3 = x;

                    if(z < d6 && z >= -d6) {
                        z = 0.0D;
                    }else if(z > 0.0D) {
                        z -= d6;
                    }else{
                        z += d6;
                    }
                }
            }

            List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes((Entity) (Object) this, this.getEntityBoundingBox().addCoord(x, y, z));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            for(AxisAlignedBB axisalignedbb1 : list1) {
                y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
            boolean flag1 = this.onGround || d4 != y && d4 < 0.0D;

            for(AxisAlignedBB axisalignedbb2 : list1) {
                x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

            for(AxisAlignedBB axisalignedbb13 : list1) {
                z = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z);
            }

            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

            if(this.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
                double d11 = x;
                double d7 = y;
                double d8 = z;
                AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = this.stepHeight;
                List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes((Entity) (Object) this, this.getEntityBoundingBox().addCoord(d3, y, d5));
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
                double d9 = y;

                for(AxisAlignedBB axisalignedbb6 : list) {
                    d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
                double d15 = d3;

                for(AxisAlignedBB axisalignedbb7 : list) {
                    d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
                }

                axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
                double d16 = d5;

                for(AxisAlignedBB axisalignedbb8 : list) {
                    d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
                AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
                double d17 = y;

                for(AxisAlignedBB axisalignedbb9 : list) {
                    d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
                double d18 = d3;

                for(AxisAlignedBB axisalignedbb10 : list) {
                    d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
                }

                axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
                double d19 = d5;

                for(AxisAlignedBB axisalignedbb11 : list) {
                    d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
                }

                axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
                double d20 = d15 * d15 + d16 * d16;
                double d10 = d18 * d18 + d19 * d19;

                if(d20 > d10) {
                    x = d15;
                    z = d16;
                    y = -d9;
                    this.setEntityBoundingBox(axisalignedbb4);
                }else{
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.setEntityBoundingBox(axisalignedbb14);
                }

                for(AxisAlignedBB axisalignedbb12 : list) {
                    y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

                if(d11 * d11 + d8 * d8 >= x * x + z * z) {
                    x = d11;
                    y = d7;
                    z = d8;
                    this.setEntityBoundingBox(axisalignedbb3);
                }
            }

            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
            this.posY = this.getEntityBoundingBox().minY;
            this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
            this.isCollidedHorizontally = d3 != x || d5 != z;
            this.isCollidedVertically = d4 != y;
            this.onGround = this.isCollidedVertically && d4 < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor_double(this.posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            Block block1 = this.worldObj.getBlockState(blockpos).getBlock();

            if(block1.getMaterial() == Material.air) {
                Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();

                if(block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
                    block1 = block;
                    blockpos = blockpos.down();
                }
            }

            this.updateFallState(y, this.onGround, block1, blockpos);

            if(d3 != x) {
                this.motionX = 0.0D;
            }

            if(d5 != z) {
                this.motionZ = 0.0D;
            }

            if(d4 != y) {
                block1.onLanded(this.worldObj, (Entity) (Object) this);
            }

            if(this.canTriggerWalking() && !flag && this.ridingEntity == null) {
                double d12 = this.posX - d0;
                double d13 = this.posY - d1;
                double d14 = this.posZ - d2;

                if(block1 != Blocks.ladder) {
                    d13 = 0.0D;
                }

                if(block1 != null && this.onGround) {
                    block1.onEntityCollidedWithBlock(this.worldObj, blockpos, (Entity) (Object) this);
                }

                this.distanceWalkedModified = (float) (this.distanceWalkedModified + MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
                this.distanceWalkedOnStepModified = (float) (this.distanceWalkedOnStepModified + MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);

                if(this.distanceWalkedOnStepModified > getNextStepDistance() && block1.getMaterial() != Material.air) {
                    setNextStepDistance((int) this.distanceWalkedOnStepModified + 1);

                    if(this.isInWater()) {
                        float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;

                        if(f > 1.0F) {
                            f = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }

                    this.playStepSound(blockpos, block1);
                }
            }

            try {
                this.doBlockCollisions();
            }catch(Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag2 = this.isWet();

            if(this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
                this.dealFireDamage(1);

                if(!flag2) {
                    setFire(getFire() + 1);

                    if(getFire() == 0) {
                        this.setFire(8);
                    }
                }
            }else if(getFire() <= 0) {
                setFire(-this.fireResistance);
            }

            if(flag2 && getFire() > 0) {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                setFire(-this.fireResistance);
            }

            this.worldObj.theProfiler.endSection();
        }
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.sprintingTicksLeft > 0)
        {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0)
            {
                Minecraft.getMinecraft().thePlayer.setSprinting(false);
            }
        }

        if (this.sprintToggleTimer > 0)
        {
            --this.sprintToggleTimer;
        }

        this.prevTimeInPortal = this.timeInPortal;

        if (this.inPortal)
        {
            if (Minecraft.getMinecraft().currentScreen != null && !Minecraft.getMinecraft().currentScreen.doesGuiPauseGame())
            {
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
            }

            if (this.timeInPortal == 0.0F)
            {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F)
            {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        }
        else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F)
            {
                this.timeInPortal = 1.0F;
            }
        }
        else
        {
            if (this.timeInPortal > 0.0F)
            {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F)
            {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }

        boolean flag = Minecraft.getMinecraft().thePlayer.movementInput.jump;
        boolean flag1 = Minecraft.getMinecraft().thePlayer.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = Minecraft.getMinecraft().thePlayer.movementInput.moveForward >= f;
        Minecraft.getMinecraft().thePlayer.movementInput.updatePlayerMoveState();
        EventSlowdown eventSlowdown = new EventSlowdown(true ,0.2f);
        if (this.isUsingItem() && !Minecraft.getMinecraft().thePlayer.isRiding())
        {
            EventController.call(eventSlowdown);
            Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe *= 0.2F;
            Minecraft.getMinecraft().thePlayer.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }
        EventSlowdown eventSlowdown2 = new EventSlowdown(false , eventSlowdown.slowValue);
        EventController.call(eventSlowdown2);
        if(eventSlowdown2.isPre && !Minecraft.getMinecraft().thePlayer.isRiding()){
            Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe *= eventSlowdown2.slowValue;
            Minecraft.getMinecraft().thePlayer.movementInput.moveForward *= eventSlowdown2.slowValue;
            this.sprintToggleTimer = 0;
        }
        this.pushOutOfBlocks(this.posX - (double)Minecraft.getMinecraft().thePlayer.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)Minecraft.getMinecraft().thePlayer.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double)Minecraft.getMinecraft().thePlayer.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)Minecraft.getMinecraft().thePlayer.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)Minecraft.getMinecraft().thePlayer.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)Minecraft.getMinecraft().thePlayer.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double)Minecraft.getMinecraft().thePlayer.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)Minecraft.getMinecraft().thePlayer.width * 0.35D);
        boolean flag3 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && Minecraft.getMinecraft().thePlayer.movementInput.moveForward >= f && !Minecraft.getMinecraft().thePlayer.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
        {
            if (this.sprintToggleTimer <= 0 && !Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown())
            {
                this.sprintToggleTimer = 7;
            }
            else
            {
                Minecraft.getMinecraft().thePlayer.setSprinting(true);
            }
        }

        if (!Minecraft.getMinecraft().thePlayer.isSprinting() && Minecraft.getMinecraft().thePlayer.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown())
        {
            Minecraft.getMinecraft().thePlayer.setSprinting(true);
        }

        if (Minecraft.getMinecraft().thePlayer.isSprinting() && (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
        {
            Minecraft.getMinecraft().thePlayer.setSprinting(false);
        }

        if (this.capabilities.allowFlying)
        {
            if (Minecraft.getMinecraft().playerController.isSpectatorMode())
            {
                if (!this.capabilities.isFlying)
                {
                    this.capabilities.isFlying = true;
                    Minecraft.getMinecraft().thePlayer.sendPlayerAbilities();
                }
            }
            else if (!flag && Minecraft.getMinecraft().thePlayer.movementInput.jump)
            {
                if (this.flyToggleTimer == 0)
                {
                    this.flyToggleTimer = 7;
                }
                else
                {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    Minecraft.getMinecraft().thePlayer.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.capabilities.isFlying && this.isCurrentViewEntity())
        {
            if (Minecraft.getMinecraft().thePlayer.movementInput.sneak)
            {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
            }

            if (Minecraft.getMinecraft().thePlayer.movementInput.jump)
            {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
            }
        }

        if (Minecraft.getMinecraft().thePlayer.isRidingHorse())
        {
            if (this.horseJumpPowerCounter < 0)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0)
                {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !Minecraft.getMinecraft().thePlayer.movementInput.jump)
            {
                this.horseJumpPowerCounter = -10;
                this.sendHorseJump();
            }
            else if (!flag && Minecraft.getMinecraft().thePlayer.movementInput.jump)
            {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            }
            else if (flag)
            {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10)
                {
                    this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
                }
                else
                {
                    this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        }
        else
        {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.capabilities.isFlying && !Minecraft.getMinecraft().playerController.isSpectatorMode())
        {
            this.capabilities.isFlying = false;
            Minecraft.getMinecraft().thePlayer.sendPlayerAbilities();
        }
    }


    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    void setModuleConfig(String[] args){
        String ModName = args[0];
        Module module = Zelix.getInstance().getModuleManager().getModuleByName(ModName);
        if(module != null){
            if (args.length>1){
                if(args.length>2){
                    if(args.length > 3)
                    {
                        Logger.sendMessage("> Invalid arguments found.");
                        return;
                    }
                    //参数及详细配置都有
                    Value value =module.getValueByName(args[1]);
                    if(value instanceof Mode){
                        Mode mode = (Mode)value;
                        String enumToSet = args[2];
                        //检测参数是否正常
                        if(!mode.isValid(enumToSet))
                        {
                            Logger.sendMessage("> Invalid arguments found.");
                        }else {
                            mode.setMode(enumToSet);
                            Logger.sendMessage("> Setted "+mode.displayName+" to "+mode.getValue().toString());
                        }
                    }else if(value instanceof zelix.cc.client.eventAPI.api.Amount){
                        Amount Amount = (Amount)value;
                        String arg = args[2];
                        if(!isDigit(arg)){
                            Logger.sendMessage("> Invalid Amount.");
                            return;
                        }else{
                            Amount.setValue(Double.valueOf(arg));
                            Logger.sendMessage("> Set the value to " +Amount.getValue());
                        }
                    }else if(value instanceof Option){
                        Option option = (Option)value;
                        if(args[2].toLowerCase().equals("true")){
                            option.setValue(true);
                            Logger.sendMessage("> Set the value to true");
                        }else if(args[2].toLowerCase().equals("false")){
                            option.setValue(false);
                            Logger.sendMessage("> Set the value to false");
                        }else {
                            Logger.sendMessage("> Invalid Arguments for option.");
                        }
                    }
                    return;
                }
                Value value =module.getValueByName(args[1]);
                if(value instanceof Mode){
                    Mode mode = (Mode) value;
                    StringBuilder sb = new StringBuilder();
                    for(Enum enumS : mode.modes){
                        sb.append(enumS.toString() + " , ");
                    }
                    sb.delete(sb.length()-3,sb.length());
                    Logger.sendMessage("> The Value has : " + sb.toString() + ".");
                }else if(value instanceof zelix.cc.client.eventAPI.api.Amount){
                    Amount number = (Amount) value;
                    Logger.sendMessage("-----------------------------");
                    Logger.sendMessage("> The Value's config:");
                    Logger.sendMessage("> Current value: "+number.getValue());
                    Logger.sendMessage("> Max value: "+number.max);
                    Logger.sendMessage("> Min value: "+number.min);
                    Logger.sendMessage("-----------------------------");
                }else if(value instanceof Option){
                    Logger.sendMessage("> The value is a boolean,");
                    Logger.sendMessage("> it can be setted to true or false.");
                }
            }else{
                StringBuilder valueToOutPutShow = new StringBuilder();
                for (Value v : module.value){
                    valueToOutPutShow.append(EnumChatFormatting.GOLD+v.displayName + ", ");
                }
                if(valueToOutPutShow.length() >3){
                    valueToOutPutShow.delete(valueToOutPutShow.length()-2,valueToOutPutShow.length());
                    valueToOutPutShow.append(".");
                    Logger.sendMessage("The Module "+module.name
                            +" has settings of "+valueToOutPutShow.toString()
                    );
                }else{
                    Logger.sendMessage("The Module "+module.name
                            +" has no setting."
                    );
                }
            }
        }else{
            Logger.sendMessage("> Module not exist.");
        }
    }

    boolean checkIsSettingModule(String str){
        boolean re = false;
        for (Module module : Zelix.moduleManager.modules){
            if(str.toLowerCase().contains(module.name.toLowerCase())) {
                re = true;
            }
        }
        return re;
    }


    @Overwrite
    public void sendChatMessage(String message)
    {
        EventChat event = new EventChat(message,false);
        EventController.getEventController().call(event);
        if(Zelix.getInstance().moduleManager.getModuleByClass(Command.class).isEnabled()){
            if(message.startsWith(".")){
                if(message.length() > 1){

                    String[] args = message.substring(1).split(" ");
                    if(checkIsSettingModule(args[0])){
                        setModuleConfig(args);
                        return;
                    }else{
                        final zelix.cc.client.command.Command finalCheck = Zelix.commandManager.getCommandByName(args[0]);
                        if(finalCheck != null&&(finalCheck instanceof Help || args.length>1)){
                            finalCheck.execute(args);
                        }else{
                            Logger.sendMessage("Invalid arguments, try again...");
                        }
                        return;
                    }
                }
            }
        }else if(message.startsWith("[")) {
            if (message.toLowerCase().contains("command")){
                Logger.sendMessage("> Set Command to "+EnumChatFormatting.GREEN+"Enabled");
                Zelix.getInstance().moduleManager.getModuleByClass(Command.class).setState(true);
                return;
            }
        }
        if(event.cancelled) {
            return;
        }
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }

}
