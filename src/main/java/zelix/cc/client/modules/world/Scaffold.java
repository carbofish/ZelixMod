package zelix.cc.client.modules.world;

import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.network.EventPacketReceive;
import zelix.cc.client.eventAPI.events.network.EventPacketSend;
import zelix.cc.client.eventAPI.events.render.Event2D;
import zelix.cc.client.eventAPI.events.render.Event3D;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Helper;
import zelix.cc.client.utils.Motion.MotionUtils;
import zelix.cc.client.utils.Render.FontRendererUtil;
import zelix.cc.client.utils.Render.RenderUtil;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import zelix.cc.client.utils.SafeWalkUtil;
import zelix.cc.injection.interfaces.IMixinC09PacketHeldItemChange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Scaffold
        extends Module {
    public static zelix.cc.client.eventAPI.api.Amount<Double> expand = new zelix.cc.client.eventAPI.api.Amount<Double>("ExpandDist", "ExpandDist", 1.5, 0.0, 0.1, 0.1);
    public static Mode mode = new Mode("Mode", "mode", (Enum[]) Smode.values(), (Enum) Smode.Hypixel);
    private Option<Boolean> tower = new Option<Boolean>("Tower", "tower", Boolean.valueOf(true));
    private Option<Boolean> down = new Option<Boolean>("DownWards", "DownWards", Boolean.valueOf(true));
    private Option<Boolean> Swing = new Option<Boolean>("Swing", "Swing", Boolean.valueOf(true));
    private Option<Boolean> sprint = new Option<Boolean>("Sprint", "Sprint", Boolean.valueOf(false));

    enum Smode {
        Cubecraft,
        Hypixel,
        AAC,
        Normal;
    }
    BlockData blockData;
    List<Block> blacklistedBlocks;
    public static boolean canPlace, setSlot;
    int count;
    public Scaffold() {
        super("Scaffold",ModuleType.World);
        addSettings(mode,expand,tower,down,Swing,sprint);
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
                Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace,
                Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence,Blocks.ender_chest);

    }
    @Override
    public void onEnable(){
        keepPitchRender = mc.thePlayer.rotationPitch;
        keepYawRender = mc.thePlayer.rotationYaw;
        canPlace=true;
        setSlot = false;
        super.onEnable();
    }
    @Override
    public void onDisable(){
        SafeWalkUtil.safewalk = false;
        canPlace=false;
        setSlot = false;
        super.onDisable();
    }
    double x,y,z;
    float keepYawRender,keepPitchRender;
    @Runnable
    public void onPP(EventPPUpdate e){
        setSuffix(mode.value.toString());
        if(!sprint.value) {
            mc.thePlayer.setSprinting(false);
        }
        SafeWalkUtil.safewalk = true;
        canPlace=true;
        if(getBlockCount()==0) {
            return;
        }
        if(down.getValue()){
            if(mc.gameSettings.keyBindSprint.isKeyDown()){
                SafeWalkUtil.safewalk = false;
                if(mc.thePlayer.onGround) {
                    canPlace=false;
                }
            }
        }
        x=mc.thePlayer.posX;
        y=mc.thePlayer.posY;
        z=mc.thePlayer.posZ;
        // if(!mc.thePlayer.isCollidedHorizontally){
        double x = mc.thePlayer.posX;
        double z = mc.thePlayer.posZ;
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float YAW = mc.thePlayer.rotationYaw;
        if (!mc.thePlayer.isCollidedHorizontally){
            double[] coords = expandPos(x,z,forward,strafe,YAW);
            x = coords[0];
            z = coords[1];
        }


        if (isAirBlock(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock())) {
            x = mc.thePlayer.posX;
            z = mc.thePlayer.posZ;
        }
        this.x = x;
        this.z = z;
        if(!mode.getValue().equals(Smode.Cubecraft)) {
            setSlot = false;
        } else
        {
            Helper.setSpeed(0.2F);
            mc.thePlayer.motionY = 0;
        }
        if (e.isPre()) {
            mc.thePlayer.renderYawOffset = keepYawRender;
            mc.thePlayer.rotationYawHead = keepYawRender;
            blockData=getBlockData(new BlockPos(x, y-1, z),(int)y-1);
            if(blockData!=null){
                /*
                #Tower : could be improved
                */
                tower();
                e.setYaw(keepYawRender = getRotations(blockData.getBlockPos(),blockData.getEnumFacing())[0]);
                e.setPitch(keepPitchRender = getRotations(blockData.getBlockPos(),blockData.getEnumFacing())[1]);
            }else{
                e.setYaw(keepYawRender);
                e.setPitch(keepPitchRender);
            }
            if(mc.thePlayer.onGround&& MotionUtils.isOnGround(0.001)&&!mc.gameSettings.keyBindJump.isKeyDown()) {
                e.setOnGround(false);
            }
            if(down.getValue()&&mc.gameSettings.keyBindSprint.isKeyDown()) {
                e.setPitch(82.0015f);
            }
        }else {
            if(getBlockCount()==0) {
                return;
            }
            if(!hotbarContainBlock()) {
                getBestBlock();
            }
            if(hotbarContainBlock()&&blockData != null&&canPlace){
                int slot = this.getBlockSlot();
                int n = mc.thePlayer.inventory.currentItem;
                if (mc.thePlayer.inventoryContainer.getSlot(slot).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();
                    Item item = is.getItem();
                    if (is.getItem() instanceof ItemBlock && this.isValid(item)&&!(is.stackSize>0)) {
                        return;
                    }
                }

                if(slot != -1){

                    if(mode.getValue().equals(Smode.Cubecraft)){
                        this.mc.thePlayer.inventory.currentItem = slot;
                        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockData.blockPos, blockData.enumFacing, getVec3(blockData.blockPos,blockData.enumFacing))) {
                            if(((Boolean)Swing.getValue()).booleanValue()) {
                                mc.thePlayer.swingItem();
                            } else {
                                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            }
                        }
                        //mc.thePlayer.inventory.currentItem = n;
                        //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                        //Reset blockData
                        setSlot = true;
                        blockData=null;
                        mc.playerController.updateController();
                    }else{
                        this.mc.thePlayer.inventory.currentItem = slot;
                        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), blockData.blockPos, blockData.enumFacing, getVec3(blockData.blockPos,blockData.enumFacing))) {
                            if(((Boolean)Swing.getValue()).booleanValue()) {
                                mc.thePlayer.swingItem();
                            } else {
                                this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                            }
                        }
                        mc.thePlayer.inventory.currentItem = n;
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                        blockData=null;
                        mc.playerController.updateController();
                    }
                }
            }else{
                setSlot = false;
            }
        }
    }
    double lastDist;
    int currentSlot = -1;
    @Runnable
    public void onPacket(EventPacketReceive e){
        Packet packet = e.packet;
        int k;
        if (packet instanceof S2FPacketSetSlot)
        {
            e.cancel = (true);
            S2FPacketSetSlot localS2FPacketSetSlot = (S2FPacketSetSlot)packet;
            k = localS2FPacketSetSlot.func_149173_d();
            ItemStack localItemStack1 = localS2FPacketSetSlot.func_149174_e();
            if(k!=-1) {
                if(localItemStack1 != null) {
                    currentSlot = localS2FPacketSetSlot.func_149173_d();
                }
                mc.playerController.updateController();
            }
        }
    }
    @Runnable
    private void onPSend(EventPacketSend eventPacketSend){
        if(eventPacketSend.packet instanceof C09PacketHeldItemChange)
        {
            if(setSlot)
            {
                if(getBlockSlot() != -1)
                {
                    C09PacketHeldItemChange heldItemChange = (C09PacketHeldItemChange)eventPacketSend.packet;
                    ((IMixinC09PacketHeldItemChange)heldItemChange).setSlotId(getBlockSlot());
                    mc.thePlayer.inventory.currentItem = heldItemChange.getSlotId();
                }
            }
        }
    }
    void getBestBlock() {
        if(getBlockCount() != 0) {
            ItemStack is = new ItemStack(Item.getItemById(261));
            int bestInvSlot = getBiggestBlockSlotInv();
            int bestHotbarSlot = getBiggestBlockSlotHotbar();
            int bestSlot = getBiggestBlockSlotHotbar() > 0 ? getBiggestBlockSlotHotbar() : getBiggestBlockSlotInv();
            int spoofSlot = 42;
            if(bestHotbarSlot > 0 && bestInvSlot > 0){
                if (mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack() ) {
                    if(mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack().stackSize < mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack().stackSize){
                        bestSlot = bestInvSlot;
                    }
                }
            }
            if(hotbarContainBlock()){
                for (int a = 36; a < 45; a++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(a).getStack().getItem();
                        if(item instanceof ItemBlock && isValid(item)){
                            spoofSlot = a;
                            break;
                        }
                    }
                }
            }else{
                for (int a = 36; a < 45; a++) {
                    if (!mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                        spoofSlot = a;
                        break;
                    }
                }
            }
            if (mc.thePlayer.inventoryContainer.getSlot(spoofSlot).slotNumber != bestSlot) {
                swap(bestSlot, spoofSlot - 36);
                mc.playerController.updateController();
            }
        }

    }
    void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }
    private boolean rayTrace(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        int steps = 10;
        double x = difference.xCoord / (double) steps;
        double y = difference.yCoord / (double) steps;
        double z = difference.zCoord / (double) steps;
        Vec3 point = origin;
        for (int i = 0; i < steps; ++i) {
            BlockPos blockPosition = new BlockPos(point = point.addVector(x, y, z));
            IBlockState blockState = mc.theWorld.getBlockState(blockPosition);
            if (blockState.getBlock() instanceof BlockLiquid || blockState.getBlock() instanceof BlockAir) {
                continue;
            }
            AxisAlignedBB boundingBox = blockState.getBlock().getCollisionBoundingBox(mc.theWorld, blockPosition, blockState);
            if (boundingBox == null) {
                boundingBox = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!boundingBox.offset(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).isVecInside(point)) {
                continue;
            }
            return true;
        }
        return false;
    }
    public int getBiggestBlockSlotInv(){
        int slot = -1;
        int size = 0;
        if(getBlockCount() == 0) {
            return - 1;
        }
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && isValid(item)) {
                    if(is.stackSize > size){
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }
    public int getBiggestBlockSlotHotbar(){
        int slot = -1;
        int size = 0;
        if(getBlockCount() == 0) {
            return - 1;
        }
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (item instanceof ItemBlock && isValid(item)) {
                    if(is.stackSize > size){
                        size = is.stackSize;
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }

    void tower(){
        GameSettings gameSettings = mc.gameSettings;
        boolean trytomove = gameSettings.keyBindBack.isKeyDown()||gameSettings.keyBindForward.isKeyDown()||gameSettings.keyBindLeft.isKeyDown()||gameSettings.keyBindRight.isKeyDown();
        if(canTowerlnBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ))&&canTowerlnBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY-1, mc.thePlayer.posZ))) {
            if(mc.gameSettings.keyBindJump.isKeyDown()){
                if(trytomove){
                    if(mode.getValue().equals(Smode.Hypixel)){
                        double gay2 = 0.419999215712222222222531351325;
                        double lit = 0.42d;
                        double baseSpeed = 0.2873D;
                        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
                            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
                        }
                        if (mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                            lit -= (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)*0.075;
                            baseSpeed -= 0.05*(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1);
                        }

                        if(mc.thePlayer.motionY <gay2*lit){
                            mc.thePlayer.motionY=gay2;
                        }
                        if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                            mc.thePlayer.motionY = 0;
                        }

                        Helper.setSpeed(baseSpeed);
                    }else if(mode.getValue().equals(Smode.Normal)){
                        double gay2 = 0.419999215712222222222531351325;
                        if (mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                        }
                        mc.thePlayer.motionY=gay2;
                    }else if(mode.getValue().equals(Smode.AAC)){
                        if (MotionUtils.isOnGround(0.76) && !MotionUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                            mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
                        }
                        if(!trytomove){
                            mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                            mc.thePlayer.jumpMovementFactor = 0;
                        }
                        if (MotionUtils.isOnGround(0.0001)) {
                            mc.thePlayer.motionY = 0.42;
                            mc.thePlayer.motionX *= 0.9;
                            mc.thePlayer.motionZ *= 0.9;
                        }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                            mc.thePlayer.motionY = 0;
                            Helper.getTimer().timerSpeed = 0.3f;
                        }
                        if (Helper.getTimer().timerSpeed == 0.3f) {
                            Helper.getTimer().timerSpeed = 1;
                        }
                    }else{

                    }
                }else{
                    if(mode.getValue().equals(Smode.Hypixel)){
                        BlockPos blockBelow = new BlockPos(x, mc.thePlayer.posY - 1, z);
                        if (this.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                            mc.thePlayer.motionY = 0.4196;
                        }
                        Helper.setSpeed(0);
                    }else if(mode.getValue().equals(Smode.Normal)){
                        double gay2 = 0.419999215712222222222531351325;
                        if (mc.thePlayer.isPotionActive(Potion.jump)) {
                            gay2 += (double)((float)(mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f);
                        }
                        mc.thePlayer.motionY=gay2;
                    }else if(mode.getValue().equals(Smode.AAC)){
                        if (MotionUtils.isOnGround(0.76) && !MotionUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                            mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
                        }
                        if(!trytomove){
                            mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                            mc.thePlayer.jumpMovementFactor = 0;
                        }
                        if (MotionUtils.isOnGround(0.0001)) {
                            mc.thePlayer.motionY = 0.42;
                            mc.thePlayer.motionX *= 0.9;
                            mc.thePlayer.motionZ *= 0.9;
                        }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                            mc.thePlayer.motionY = 0;
                            Helper.getTimer().timerSpeed = 0.3f;
                        }
                        if (Helper.getTimer().timerSpeed == 0.3f) {
                            Helper.getTimer().timerSpeed = 1;
                        }
                    }else{
                        count ++;
                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                        mc.thePlayer.jumpMovementFactor = 0;
                        if(MotionUtils.isOnGround(2)) {
                            if(count == 1){
                                mc.thePlayer.motionY = 0.41;
                            }else{

                                mc.thePlayer.motionY = 0.47;
                                count = 0;
                            }
                        }
                    }
                }

            }
        }
    }
    private boolean hotbarContainBlock() {
        int i = 36;

        while (i < 45) {
            try {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((stack == null) || (stack.getItem() == null) || !(stack.getItem() instanceof ItemBlock) || !isValid(stack.getItem())) {
                    i++;
                    continue;
                }
                return true;
            } catch (Exception e) {

            }
        }

        return false;

    }
    private void renderBlock2(BlockPos pos) {
        double x = (double)pos.getX() - Helper.renderPosX();
        double y = (double)pos.getY() - Helper.renderPosY();
        double z = (double)pos.getZ() - Helper.renderPosZ();
        RenderUtil.drawSolidBlockESP(x, y, z, 0.0f, 0.5f, 1.0f, 0.25f);
    }
    @Runnable
    public void on2D(Event2D e){
        ScaledResolution res = new ScaledResolution(this.mc);
        FontRendererUtil font = Zelix.instance.fontManager.getFont("CONS 22");
        font.drawStringWithShadow("" + EnumChatFormatting.GREEN + this.getBlockCount(), (res.getScaledWidth() / 2 + this.mc.fontRendererObj.getStringWidth("" + this.getBlockCount()) / 2), (res.getScaledHeight() / 2), -1);
    }
    @Runnable
    public void on3D(Event3D e){
        for(int i = 0; i < (expand.getValue() + 1); i++) {
            final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0), mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D), mc.thePlayer.posZ + (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0));
            if(mc.theWorld.getBlockState(blockPos).getBlock().isReplaceable(mc.theWorld,blockPos) && getBlockData(blockPos,1000) != null) {
                renderBlock(blockPos);
                break;
            }
        }
    }

    public double[] expandPos(double x, double z, double forward, double strafe, float YAW){
        BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = expand.getValue()*2;
        while(!isAirBlock(underBlock)){
            xCalc = x;
            zCalc = z;
            dist ++;
            if(dist > expandDist){
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if(dist == expandDist){
                break;
            }
            underPos = new BlockPos(xCalc, mc.thePlayer.posY - 1, zCalc);
            underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc,zCalc};
    }
    private int getBlockSlot() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                continue;
            }
            if(mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize<=0) {
                continue;
            }
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isValid(stack.getItem())) {
                return i - 36;
            }
        }
        return -1;
    }
    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
                return false;
            }
            return true;
        }

        return false;
    }
    class BlockData{
        public BlockPos blockPos;
        public EnumFacing enumFacing;
        BlockData(BlockPos bp,EnumFacing ef){
            blockPos=bp;
            enumFacing=ef;
        }
        public BlockPos getBlockPos(){
            return blockPos;
        }
        public EnumFacing getEnumFacing(){
            return enumFacing;
        }
    }
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && this.isValid(item)) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private boolean isValid( Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        ItemBlock iBlock = (ItemBlock)item;
        Block block = iBlock.getBlock();
        return !this.blacklistedBlocks.contains(block);
    }
    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX +  (double) face.getFrontOffsetX()/2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ +  (double) face.getFrontOffsetZ()/2;
        double y = (block.getY() + 0.5);
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }
    public static Vec3 getVec3( BlockPos pos,  EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber(0.3, -0.3);
            z += randomNumber(0.3, -0.3);
        }
        else {
            y += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    public Vec3 getPositionByFace(BlockPos position, EnumFacing facing) {
        Vec3 offset = new Vec3((double) facing.getDirectionVec().getX() / 2.0, (double) facing.getDirectionVec().getY() / 2.0, (double) facing.getDirectionVec().getZ() / 2.0);
        Vec3 point = new Vec3((double) position.getX() + 0.5, (double) position.getY() + 0.5, (double) position.getZ() + 0.5);
        return point.add(offset);
    }
    public static double randomNumber( double max,  double min) {
        return Math.random() * (max - min) + min;
    }

    private BlockData getBlockData(BlockPos pos,int down) {
        if(mc.gameSettings.keyBindSprint.isKeyDown()) {
            if(isPosSolid(pos.add(0,-pos.getY()+down-1,0))) {
                return new BlockData(pos.add(0,-pos.getY()+down-1,0),EnumFacing.DOWN);
            }
        }
        {
            EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
            BlockPos position = pos.add(0,1,0).offset(EnumFacing.DOWN);
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos offset = position.offset(facing);
                if (mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.0f), getPositionByFace(offset, invert[facing.ordinal()]))) {
                    continue;
                }
                return new BlockData(offset, invert[facing.ordinal()]);
            }
            {
                BlockPos[] offsets = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1), new BlockPos(0, 0, 2), new BlockPos(0, 0, -2), new BlockPos(2, 0, 0), new BlockPos(-2, 0, 0)};
                for (BlockPos offset : offsets) {
                    BlockPos offsetPos = position.add(offset.getX(), 0, offset.getZ());
                    if (!(mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir)) {
                        continue;
                    }
                    for (EnumFacing facing : EnumFacing.values()) {
                        BlockPos offset2 = offsetPos.offset(facing);
                        if (mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir || rayTrace(mc.thePlayer.getLook(0.01f), getPositionByFace(offset, invert[facing.ordinal()]))) {
                            continue;
                        }
                        return new BlockData(offset2, invert[facing.ordinal()]);
                    }
                }
            }
        }
        EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};

        BlockPos position1 = new BlockPos(new Vec3(pos.getX(),pos.getY()+1,pos.getZ())).offset(EnumFacing.DOWN);
        BlockPos xd = new BlockPos(pos.getX(),pos.getY()+0.9,pos.getZ());
        if (!(mc.theWorld.getBlockState(position1).getBlock() instanceof BlockAir)) {
            return null;
        }
        EnumFacing[] var6 = EnumFacing.values();
        int var5 = var6.length;
        int offset = 0;
        while (offset < var5) {
            EnumFacing offsets = var6[offset];
            BlockPos offset1 = position1.offset(offsets);
            mc.theWorld.getBlockState(offset1);
            if (!(mc.theWorld.getBlockState(offset1).getBlock() instanceof BlockAir)) {
                return new BlockData( offset1, invert[offsets.ordinal()]);
            }
            ++offset;
        }

        BlockPos[] var16;
        BlockPos[] var19 = var16 = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0)};
        int var18 = var16.length;
        var5 = 0;
        while (var5 < var18) {
            BlockPos var17 = var19[var5];
            BlockPos offsetPos = position1.add(var17.getX(), 0, var17.getZ());
            mc.theWorld.getBlockState(offsetPos);
            if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                EnumFacing[] var13 = EnumFacing.values();
                int var12 = var13.length;
                int var11 = 0;
                while (var11 < var12) {
                    EnumFacing facing2 = var13[var11];
                    BlockPos offset2 = offsetPos.offset(facing2);
                    mc.theWorld.getBlockState(offset2);
                    if (!(mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir)) {
                        return new BlockData( offset2, invert[facing2.ordinal()]);
                    }
                    if(!(mc.theWorld.getBlockState(xd).getBlock() instanceof BlockAir)) {
                        return new BlockData( xd, invert[facing2.ordinal()]);
                    }
                    ++var11;
                }
            }
            ++var5;

        }
        if (isValidBlock(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos1 = pos.add(-1, 0, 0);
        if (isValidBlock(localBlockPos1.add(0, -1, 0))) {
            return new BlockData(localBlockPos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos1.add(-1, 0, 0))) {
            return new BlockData(localBlockPos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos1.add(1, 0, 0))) {
            return new BlockData(localBlockPos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos1.add(0, 0, 1))) {
            return new BlockData(localBlockPos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos1.add(0, 0, -1))) {
            return new BlockData(localBlockPos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos2 = pos.add(1, 0, 0);
        if (isValidBlock(localBlockPos2.add(0, -1, 0))) {
            return new BlockData(localBlockPos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos2.add(-1, 0, 0))) {
            return new BlockData(localBlockPos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos2.add(1, 0, 0))) {
            return new BlockData(localBlockPos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos2.add(0, 0, 1))) {
            return new BlockData(localBlockPos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos2.add(0, 0, -1))) {
            return new BlockData(localBlockPos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos3 = pos.add(0, 0, 1);
        if (isValidBlock(localBlockPos3.add(0, -1, 0))) {
            return new BlockData(localBlockPos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos3.add(-1, 0, 0))) {
            return new BlockData(localBlockPos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos3.add(1, 0, 0))) {
            return new BlockData(localBlockPos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos3.add(0, 0, 1))) {
            return new BlockData(localBlockPos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos3.add(0, 0, -1))) {
            return new BlockData(localBlockPos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos4 = pos.add(0, 0, -1);
        if (isValidBlock(localBlockPos4.add(0, -1, 0))) {
            return new BlockData(localBlockPos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos4.add(-1, 0, 0))) {
            return new BlockData(localBlockPos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos4.add(1, 0, 0))) {
            return new BlockData(localBlockPos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos4.add(0, 0, 1))) {
            return new BlockData(localBlockPos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos4.add(0, 0, -1))) {
            return new BlockData(localBlockPos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos5 = pos.add(0, -1, 0);
        if (isValidBlock(localBlockPos5.add(0, -1, 0))) {
            return new BlockData(localBlockPos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos5.add(-1, 0, 0))) {
            return new BlockData(localBlockPos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos5.add(1, 0, 0))) {
            return new BlockData(localBlockPos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos5.add(0, 0, 1))) {
            return new BlockData(localBlockPos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos5.add(0, 0, -1))) {
            return new BlockData(localBlockPos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos6 = localBlockPos5.add(1, 0, 0);
        if (isValidBlock(localBlockPos6.add(0, -1, 0))) {
            return new BlockData(localBlockPos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos6.add(-1, 0, 0))) {
            return new BlockData(localBlockPos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos6.add(1, 0, 0))) {
            return new BlockData(localBlockPos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos6.add(0, 0, 1))) {
            return new BlockData(localBlockPos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos6.add(0, 0, -1))) {
            return new BlockData(localBlockPos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos7 = localBlockPos5.add(-1, 0, 0);
        if (isValidBlock(localBlockPos7.add(0, -1, 0))) {
            return new BlockData(localBlockPos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos7.add(-1, 0, 0))) {
            return new BlockData(localBlockPos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos7.add(1, 0, 0))) {
            return new BlockData(localBlockPos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos7.add(0, 0, 1))) {
            return new BlockData(localBlockPos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos7.add(0, 0, -1))) {
            return new BlockData(localBlockPos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos8 = localBlockPos5.add(0, 0, 1);
        if (isValidBlock(localBlockPos8.add(0, -1, 0))) {
            return new BlockData(localBlockPos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos8.add(-1, 0, 0))) {
            return new BlockData(localBlockPos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos8.add(1, 0, 0))) {
            return new BlockData(localBlockPos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos8.add(0, 0, 1))) {
            return new BlockData(localBlockPos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos8.add(0, 0, -1))) {
            return new BlockData(localBlockPos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos localBlockPos9 = localBlockPos5.add(0, 0, -1);
        if (isValidBlock(localBlockPos9.add(0, -1, 0))) {
            return new BlockData(localBlockPos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(localBlockPos9.add(-1, 0, 0))) {
            return new BlockData(localBlockPos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(localBlockPos9.add(1, 0, 0))) {
            return new BlockData(localBlockPos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(localBlockPos9.add(0, 0, 1))) {
            return new BlockData(localBlockPos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(localBlockPos9.add(0, 0, -1))) {
            return new BlockData(localBlockPos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    private boolean isValidBlock(BlockPos paramBlockPos)
    {
        Block localBlock = mc.theWorld.getBlockState(paramBlockPos).getBlock();
        if ((localBlock.getMaterial().isSolid()) || (!localBlock.isTranslucent()) || (localBlock.isBlockNormalCube()) || ((localBlock instanceof BlockLadder)) || ((localBlock instanceof BlockCarpet)) || ((localBlock instanceof BlockSnow)) || ((localBlock instanceof BlockSkull))) {
            if (!localBlock.getMaterial().isLiquid()) {
                return true;
            }
        }
        return false;
    }
    boolean canTowerlnBlock(BlockPos block){
        //if(block instanceof BlockAir)return true;
        //if(block instanceof BlockGrass||block instanceof BlockTallGrass||block instanceof BlockFlower||block instanceof BlockFlowerPot||block instanceof BlockTorch||block instanceof BlockRedstoneTorch||block instanceof BlockBed)return true;
        //if(block.isSolidFullCube()&&!(block instanceof BlockStairs))return true;
        Block b = mc.theWorld.getBlockState(block).getBlock();
        if(isValid2(b.getItem(mc.theWorld,block))||(b instanceof BlockAir))return true;
        return false;
    }
    private boolean isValid2(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        } else {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.getBlock();
            if (blacklistedBlocks.contains(block)) {
                return false;
            }
        }
        return true;
    }
    private void renderBlock(BlockPos pos) {
        this.mc.getRenderManager();
        double x = (double)pos.getX() - Helper.renderPosX();
        this.mc.getRenderManager();
        double y = (double)pos.getY() - Helper.renderPosY();
        this.mc.getRenderManager();
        double z = (double)pos.getZ() - Helper.renderPosZ();
        drawSolidBlockESP(x, y, z, 0.0f, 0.5f, 1.0f, 0.25f);
    }
    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }
    private boolean isPosSolid( BlockPos pos) {
        Block block =mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isBlockNormalCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }
}
