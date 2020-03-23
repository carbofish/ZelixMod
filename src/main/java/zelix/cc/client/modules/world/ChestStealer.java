package zelix.cc.client.modules.world;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventTick;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Math.MathUtil;
import zelix.cc.client.utils.Math.TimerUtil;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

public class ChestStealer
extends Module {
    private zelix.cc.client.eventAPI.api.Amount<Double> delay = new zelix.cc.client.eventAPI.api.Amount<Double>("Delay", "delay", 1000.0, 0.0, 10.0, 100.0);
    private TimerUtil timer = new TimerUtil();

    public ChestStealer() {
        super("ChestStealer", ModuleType.World);
        this.addSettings(this.delay);
    }

    @Runnable
    private void onUpdate(EventTick event) {
        if (this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest)this.mc.thePlayer.openContainer;
            boolean isChest = false;
            if(StatCollector.translateToLocal("container.chest").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText())) {
                isChest=true;
            }
            if(StatCollector.translateToLocal("container.chestDouble").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText())) {
                isChest=true;
            }
            if(container.getLowerChestInventory().getDisplayName().getUnformattedText().contains("LOW")) {
                isChest=true;
            }
            if(isChest){
                int i = 0;
                while(i < container.getLowerChestInventory().getSizeInventory()) {

                    if (container.getLowerChestInventory().getStackInSlot(i) != null&&!isTrash(container.getLowerChestInventory().getStackInSlot(i)) && this.timer.hasReached(this.delay.getValue() + MathUtil.randomDouble(0, 15))) {
                        this.mc.playerController.windowClick(container.windowId, i, 0, 1, this.mc.thePlayer);
                        this.timer.reset();
                    }
                    if(container.getLowerChestInventory().getStackInSlot(i) != null) {
                        i += (int)MathUtil.randomDouble(1, 5);

                    }else if(container.getLowerChestInventory().getStackInSlot(i) == null){
                        i += (int)MathUtil.randomDouble(1, 5);
                    }else if(container.getLowerChestInventory().getStackInSlot(1) != null) {
                        i= 1;
                    }else if(container.getLowerChestInventory().getStackInSlot(i-1) != null&&i >1) {
                        i-=1;
                    }

                }
                if (this.isEmpty()) {
                    this.mc.thePlayer.closeScreen();
                }
            }

        }
    }
    public static boolean isTrash(ItemStack item) {
        String unlocalizedName = item.getItem().getUnlocalizedName();
        return ((unlocalizedName.contains("tnt")) || item.getDisplayName().contains("frog") ||
                (unlocalizedName.contains("stick"))||
                (unlocalizedName.contains("string")) || (unlocalizedName.contains("flint")) ||
                (unlocalizedName.contains("feather")) || (unlocalizedName.contains("bucket")) ||
                (unlocalizedName.contains("snow")) || (unlocalizedName.contains("enchant")) ||
                (unlocalizedName.contains("exp")) || (unlocalizedName.contains("shears")) ||
                (unlocalizedName.contains("arrow")) || (unlocalizedName.contains("anvil")) ||
                (unlocalizedName.contains("torch")) || (unlocalizedName.contains("seeds")) ||
                (unlocalizedName.contains("leather")) || (unlocalizedName.contains("boat")) ||
                (unlocalizedName.contains("fishing")) || (unlocalizedName.contains("wheat")) ||
                (unlocalizedName.contains("flower")) || (unlocalizedName.contains("record")) ||
                (unlocalizedName.contains("note")) || (unlocalizedName.contains("sugar")) ||
                (unlocalizedName.contains("wire")) || (unlocalizedName.contains("trip")) ||
                (unlocalizedName.contains("slime")) || (unlocalizedName.contains("web")) ||
                ((item.getItem() instanceof ItemGlassBottle)) || (unlocalizedName.contains("piston")) ||
                (unlocalizedName.contains("potion") && (isBadPotion(item))) ||
                //   ((item.getItem() instanceof ItemArmor) && isBestArmor(item)) ||
                (item.getItem() instanceof ItemEgg || (unlocalizedName.contains("bow")) && !item.getDisplayName().contains("Kit")) ||
                //   ((item.getItem() instanceof ItemSword) && !isBestSword(item)) ||
                (unlocalizedName.contains("Raw")));
    }
    public static boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isEmpty() {
        ContainerChest chest = (ContainerChest)this.mc.thePlayer.openContainer;
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                if (!isTrash(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
}
