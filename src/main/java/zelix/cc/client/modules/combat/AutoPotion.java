package zelix.cc.client.modules.combat;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Amount;
import zelix.cc.client.eventAPI.api.Option;

import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Math.TimerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class AutoPotion
extends Module {
    public static Option<Boolean> SPEED = new Option<Boolean> ("Speed", "Speed", false);
    public static Option<Boolean>  REGEN = new Option<Boolean> ("Regen", "Regen", false);
    public static Amount<Double> HEALTH = new Amount<>("Health", "Health", 20.0, 1.0, 1.0,8.0);
    public static Option<Boolean> PREDICT = new Option<Boolean> ("Predict", "Predict", false);

    public AutoPotion() {
        super("AutoPotion",  ModuleType.Combat);
        addSettings(SPEED,REGEN,HEALTH,PREDICT);
    }

    public static boolean potting;
    TimerUtil timer = new TimerUtil();
    @Override
    public void onEnable() {
        super.onEnable();
    }
    public static boolean isPotting(){
        return potting;
    }
    @Runnable
    public void onEvent(EventPPUpdate event) {
        if(event.isPre()){
            final boolean speed = (SPEED).getValue();
            final boolean regen = (REGEN).getValue();
            if(timer.hasReached(200)){
                if(potting) {
                    potting = false;
                }
            }
            int spoofSlot = getBestSpoofSlot();
            int pots[] = {6,-1,-1};
            if(regen) {
                pots[1] = 10;
            }
            if(speed) {
                pots[2] = 1;
            }

            for(int i = 0; i < pots.length; i ++){
                if(pots[i] == -1) {
                    continue;
                }
                if(pots[i] == 6 || pots[i] == 10){
                    if(timer.hasReached(900) && !mc.thePlayer.isPotionActive(pots[i])){
                        if(mc.thePlayer.getHealth() < ((HEALTH).getValue()).doubleValue()*2){
                            getBestPot(spoofSlot, pots[i]);
                        }
                    }
                }else
                if(timer.hasReached(1000) && !mc.thePlayer.isPotionActive(pots[i])){
                    getBestPot(spoofSlot, pots[i]);
                }

            }
        }
    }

    public float[] getRotationFromPosition(double posX,double posY,double posZ) {
        double xDiff = posX - mc.thePlayer.posX;
        double zDiff = posY - mc.thePlayer.posZ;
        double yDiff = posZ - mc.thePlayer.posY-1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
    public void swap(int slot1, int hotbarSlot){
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }
    float[] getRotations(){
        double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 26.0D;
        double movedPosY = mc.thePlayer.getEntityBoundingBox().minY - 3.6D;
        double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 26.0D;
        if((PREDICT).getValue()) {
            return getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
        } else {
            return new float[]{mc.thePlayer.rotationYaw, 90};
        }
    }
    int getBestSpoofSlot(){
        int spoofSlot = 5;
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            }else if(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
                spoofSlot = i - 36;
                break;
            }
        }
        return spoofSlot;
    }
    void getBestPot(int hotbarSlot, int potID){
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() &&(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if(pot.getEffects(is).isEmpty()) {
                        return;
                    }
                    PotionEffect effect = (PotionEffect) pot.getEffects(is).get(0);
                    int potionID = effect.getPotionID();
                    if(potionID == potID) {
                        if(ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)){
                            if(36 + hotbarSlot != i) {
                                swap(i, hotbarSlot);
                            }
                            timer.reset();
                            boolean canpot = true;
                            int oldSlot = mc.thePlayer.inventory.currentItem;
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
                            potting = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    boolean isBestPot(ItemPotion potion, ItemStack stack){
        if(potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1)
            return false;
        PotionEffect effect = (PotionEffect) potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if (pot.getEffects(is) != null) {
                        for (Object o : pot.getEffects(is)) {
                            PotionEffect effects = (PotionEffect) o;
                            int id = effects.getPotionID();
                            int ampl = effects.getAmplifier();
                            int dur = effects.getDuration();
                            if (id == potionID && ItemPotion.isSplash(is.getItemDamage())){
                                if(ampl > amplifier){
                                    return false;
                                }else if (ampl == amplifier && dur > duration){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
