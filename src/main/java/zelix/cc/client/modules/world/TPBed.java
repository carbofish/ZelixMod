package zelix.cc.client.modules.world;

import net.minecraft.item.Item;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.render.EventLoadingBlock;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.RotationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//Coded by nivalC. 2020/1/29
/*
*
* 获取 bed的blockPos, ->
* 移除自己队伍的床 ->
* TP到床 ->
* Fuck it ->
* Next bed.
*
 */
public class TPBed
extends Module {
    public List<BlockPos> bedlist = new ArrayList<>();
    public BlockPos teamBed;
    public BlockPos curBed;
    public TPBed() {
        super("TPBed", ModuleType.World);
    }
    @Runnable
    public void onLoadingWorld(EventLoadingBlock eventLoadingBlock){
        if(Item.getItemFromBlock(eventLoadingBlock.getBlock()).equals(26)){
            if(!bedlist.contains(eventLoadingBlock.getBlockPos())) {
                bedlist.add(eventLoadingBlock.getBlockPos());
            }
        }else{
            if(bedlist.contains(eventLoadingBlock.getBlockPos())) {
                bedlist.remove(eventLoadingBlock.getBlockPos());
            }
        }
        //将自己的床移除 这个检测方式有可能误判 但总是准确的
        if(teamBed != null &&
                eventLoadingBlock.getBlockPos().equals(teamBed) &&
                bedlist.contains(eventLoadingBlock) &&
                bedlist.size() > 1){
            bedlist.remove(eventLoadingBlock.getBlockPos());
        }
    }
    double getDistance(BlockPos blockPos1 , BlockPos blockPos2){
        double d0 = blockPos1.getX() - blockPos2.getX();
        double d1 = blockPos1.getY() - blockPos2.getY();
        double d2 = blockPos1.getZ() - blockPos2.getZ();
        return (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }
    public void tp2Pos(BlockPos blockPos){
        //每个packet前进的距离
        double distanceForEachPacket = 3;
        float yawToPos = RotationUtil.getYawToPos(blockPos);
        BlockPos currentPosition = new BlockPos(mc.thePlayer.posX, blockPos.getY(), mc.thePlayer.posZ);
        while (getDistance(currentPosition,blockPos) > 3){
            //下一次发包到达的地方
            BlockPos nextPos = getNextPos(currentPosition, distanceForEachPacket, yawToPos);
            //填写参数
            int nextPosX = nextPos.getX();
            int nextPosY = nextPos.getY();
            int nextPosZ = nextPos.getZ();
            //发包 & 设置本地位置
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(nextPosX, nextPosY, nextPosZ,false));
            mc.thePlayer.setPosition(nextPosX, nextPosY, nextPosZ);
            //重置并进行下一次检测
            currentPosition = nextPos;
        }
    }
    public BlockPos getNextPos(BlockPos blockPos , double distance , float yaw){
        //用setSpeed的方法进行position调整
        double forward = 1;
        double strafe = 0;
        double addX =
                ((forward * distance * Math.cos(Math.toRadians((double)(yaw + 90.0f))) + strafe * distance * Math.sin(Math.toRadians((double)(yaw + 90.0f)))));
        double addZ =
                ((forward * distance * Math.sin(Math.toRadians((double)(yaw + 90.0f))) - strafe * distance * Math.cos(Math.toRadians((double)(yaw + 90.0f)))));
        return blockPos.add(addX,0,addZ);
    }
    @Runnable
    public void onPP(EventPPUpdate eventPPUpdate){
        if(teamBed == null){
            //使自家的床不被tp和挖。
            bedlist.sort(Comparator.comparingDouble(o -> mc.thePlayer.getDistanceSq(o)));
            teamBed = bedlist.get(0);
        }
        if(eventPPUpdate.isPre()){
            if(!bedlist.isEmpty()){
                curBed = bedlist.get(0);
                tp2Pos(curBed);
                //方法尚未编写. 在此处拆床
                //fuckBed(curBed);
                bedlist.remove(curBed);
            } else{
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        setState(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
