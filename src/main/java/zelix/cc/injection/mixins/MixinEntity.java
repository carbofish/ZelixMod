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
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zelix.cc.injection.interfaces.IMixinEntity;

import java.util.Random;

@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinEntity {
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float stepHeight;
    @Shadow
    public boolean noClip;
    @Shadow
    public boolean isInWeb;
    @Shadow
    public abstract void setEntityBoundingBox(AxisAlignedBB bb);
    @Shadow
    public World worldObj;
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    @Shadow
    public abstract boolean isSneaking();
    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean onGround;
    @Shadow
    public boolean isCollidedHorizontally;
    @Shadow
    public boolean isCollidedVertically;
    @Shadow
    public boolean isCollided;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float distanceWalkedOnStepModified;
    @Shadow
    public abstract boolean isInWater();
    @Shadow
    private int nextStepDistance;
    @Shadow
    private int fire;
    @Shadow
    public abstract void addEntityCrashInfo(CrashReportCategory category);
    @Shadow
    protected abstract void dealFireDamage(int amount);
    @Shadow
    public abstract void setFire(int seconds);
    @Shadow
    public int fireResistance;
    @Shadow
    protected abstract void playStepSound(BlockPos pos, Block blockIn);
    @Shadow
    protected abstract void doBlockCollisions();
    @Shadow
    public abstract boolean isWet();
    @Shadow
    protected Random rand;
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public int timeUntilPortal;
    @Shadow
    protected boolean inPortal;

    public int getNextStepDistance() {
        return nextStepDistance;
    }

    public void setNextStepDistance(int nextStepDistance) {
        this.nextStepDistance = nextStepDistance;
    }

    public int getFire() {
        return fire;
    }

    @Override
    public boolean isInWeb(){
        return this.isInWeb;
    }

    @Override
    public void setInWeb(boolean in){
        this.isInWeb = in;
    }
}
