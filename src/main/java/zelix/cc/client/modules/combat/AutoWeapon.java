package zelix.cc.client.modules.combat;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Inventory.InventoryUtils;
import zelix.cc.client.utils.Math.TimerUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AutoWeapon
extends Module {
    private ItemStack bestSword;
    private ItemStack prevBestSword;
    private boolean shouldSwitch = false;
    public TimerUtil timer = new TimerUtil();

    public AutoWeapon() {
        super("AutoSword",  ModuleType.Combat);
    }

    public ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
        Optional<ItemStack> bestItem = ((List<Slot>)mc.thePlayer.inventoryContainer.inventorySlots)
                .stream()
                .map(Slot::getStack)
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getItem().getClass().equals(itemType))
                .max(comparator);

        return bestItem.orElse(null);
    }public double getDura(ItemStack e) {
        return e.getMaxDamage() - e.getItemDamage();

    }
    public double getRankedPoint(ItemStack e){
        int fireAccept = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,e);
        return getDura(e)*0.5+getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*0.4+fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept;
    }

    public boolean isItemBlackListed(ItemStack itemStack) {
        Item item = itemStack.getItem();

        return
                item instanceof ItemSword && !this.bestSword.equals(itemStack);
    }
    @Runnable
    private void onUpdate(EventPPUpdate event) {
        if (this.mc.thePlayer.ticksExisted % 7 == 0) {
            if (this.mc.thePlayer.capabilities.isCreativeMode || this.mc.thePlayer.openContainer != null && this.mc.thePlayer.openContainer.windowId != 0) {
                return;
            }
            this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getRankedPoint));
            if (this.bestSword == null) {
                return;
            }
            boolean isInHBSlot = InventoryUtils.hotbarHas(this.bestSword.getItem(), 0);
            if (isInHBSlot) {
                if (InventoryUtils.getItemBySlotID(0) != null) {
                    if (InventoryUtils.getItemBySlotID(0).getItem() instanceof ItemSword) {
                        isInHBSlot = this.getSwordDamage(InventoryUtils.getItemBySlotID(0)) >= this.getSwordDamage(this.bestSword);
                    }
                } else {
                    isInHBSlot = false;
                }
            }
            if (this.prevBestSword == null || !this.prevBestSword.equals(this.bestSword) || !isInHBSlot) {
                this.shouldSwitch = true;
                this.prevBestSword = this.bestSword;
            } else {
                this.shouldSwitch = false;
            }
            if (this.shouldSwitch && this.timer.hasReached(1.0)) {
                int slotHB = InventoryUtils.getBestSwordSlotID(this.bestSword, this.getSwordDamage(this.bestSword));
                switch (slotHB) {
                    case 0: {
                        slotHB = 36;
                        break;
                    }
                    case 1: {
                        slotHB = 37;
                        break;
                    }
                    case 2: {
                        slotHB = 38;
                        break;
                    }
                    case 3: {
                        slotHB = 39;
                        break;
                    }
                    case 4: {
                        slotHB = 40;
                        break;
                    }
                    case 5: {
                        slotHB = 41;
                        break;
                    }
                    case 6: {
                        slotHB = 42;
                        break;
                    }
                    case 7: {
                        slotHB = 43;
                        break;
                    }
                    case 8: {
                        slotHB = 44;
                        break;
                    }
                }
                this.mc.playerController.windowClick(0, slotHB, 0, 2, this.mc.thePlayer);
                this.timer.reset();
            }
        }
    }
    public void dropItem(int slotID) {
        mc.playerController.windowClick(0, slotID, 1, 4, mc.thePlayer);
    }



    public double getSwordDamage(ItemStack itemStack) {
        double damage = 0;
        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = attributeModifier.get().getAmount();
        }
        damage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

        return damage;
    }
}
