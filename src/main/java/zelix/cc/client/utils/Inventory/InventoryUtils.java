package zelix.cc.client.utils.Inventory;

import zelix.cc.client.utils.Util;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class InventoryUtils
extends Util {
    public static ItemStack getStackInSlot(int slot) {
        return InventoryUtils.mc.thePlayer.inventory.getStackInSlot(slot);
    }

    public static boolean hotbarHas(Item item, int slotID) {
        int index = 0;
        while (index <= 36) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item && InventoryUtils.getSlotID(stack.getItem()) == slotID) {
                return true;
            }
            ++index;
        }
        return false;
    }

    public static int getSlotID(Item item) {
        int index = 0;
        while (index <= 36) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack.getItem() == item) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    public static ItemStack getItemBySlotID(int slotID) {
        int index = 0;
        while (index <= 36) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(index);
            if (stack != null && InventoryUtils.getSlotID(stack.getItem()) == slotID) {
                return stack;
            }
            ++index;
        }
        return null;
    }

    public static int getBestSwordSlotID(ItemStack item, double damage) {
        int index = 0;
        while (index <= 36) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(index);
            if (stack != null && stack == item && InventoryUtils.getSwordDamage(stack) == InventoryUtils.getSwordDamage(item)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double damage = 0.0;
        Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = ((AttributeModifier)attributeModifier.get()).getAmount();
        }
        return damage += (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    public static int getItemType(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)itemStack.getItem();
            return armor.armorType;
        }
        return -1;
    }

    public static float getItemDamage(ItemStack itemStack) {
        Iterator iterator;
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            return attributeModifier.getAmount() > 1.0 ? 1.0f + (float)damage : 1.0f;
        }
        return 1.0f;
    }
}
