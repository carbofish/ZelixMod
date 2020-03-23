package zelix.cc.client.modules.player;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.events.execute.EventTick;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Math.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.*;
import java.util.stream.Collectors;

public class InvCleaner
extends Module {
    private static final Random RANDOM = new Random();
    public static List<Integer> blacklistedItems = new ArrayList<Integer>();
    private boolean allowSwitch = true;
    public static final Minecraft MC = Minecraft.getMinecraft();
    private boolean hasNoItems;
    public final TimerUtil timer = new TimerUtil();
    public final Set<Integer> blacklistedItemIDs = new HashSet<>();

    public ItemStack[] bestArmorSet;
    public ItemStack bestSword;
    public ItemStack bestPickAxe;
    public ItemStack bestBow;
    private Option<Boolean> toggle = new Option<Boolean>("Toggle", "Toggle", false);

    public InvCleaner() {
        super("InvClean", ModuleType.Player);
        this.addSettings(toggle);
        Arrays.stream(new int[]{
                //Egg
                344,
                //Stick
                280,
                //String
                287,
                //Flint
                318,

                //Feather
                288,
                //Experience Bottle
                384,
                //Enchanting Table
                116,
                //Chest
                54,
                //Snowball
                332,
                //Anvil
                145
        }).forEach(this.blacklistedItemIDs::add);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.hasNoItems = false;
    }
    public double getDura(ItemStack e) {
        return e.getMaxDamage() - e.getItemDamage();

    }
    enum RankMode{
        Sword,
        Pickaxe,
        Bow;
    }

    public double getRankedPoint(ItemStack e){
      /*  int fireAccept = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,e);
        return getDura(e)*0.5+getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*0.4+fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept;
    */
        if(e.getItem() instanceof ItemSword){
            return rank(RankMode.Sword,e);
        }else
        if(e.getItem() instanceof ItemPickaxe){
            return rank(RankMode.Pickaxe,e);
        }else
        if(e.getItem() instanceof ItemBow){
            return  rank(RankMode.Bow,e);
        }else{
            return 0;
        }
    }

    double rank(RankMode e1,ItemStack e){
        if(e1.equals(RankMode.Sword)){
            int fireAccept = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,e);
            return getDura(e)*0.5
                    +getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*getSwordDamage(e)*0.4
                    +fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept*fireAccept;
        }else if(e1.equals(RankMode.Pickaxe)){
            double point = getMiningSpeed(e)*10;
            return  getDura(e)*0.5+point;
        }else if(e1.equals(RankMode.Bow)){
            double point = getBowPower(e)*10;
            return getDura(e)*0.1+point;
        }else{
            return 0;
        }
    }


    @Runnable
    public void onTick(EventTick event) {
        if (mc.thePlayer.ticksExisted % 2 == 0 && RANDOM.nextInt(2) == 0) {
            this.bestArmorSet = getBestArmorSet();
            this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getRankedPoint));
            this.bestPickAxe = getBestItem(ItemPickaxe.class, Comparator.comparingDouble(this::getRankedPoint));
            this.bestBow = getBestItem(ItemBow.class, Comparator.comparingDouble(this::getRankedPoint));

            Optional<Slot> blacklistedItem = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                    .stream()
                    .filter(Slot::getHasStack)
                    .filter(slot -> Arrays.stream(MC.thePlayer.inventory.armorInventory).noneMatch(slot.getStack()::equals))
                    .filter(slot -> !slot.getStack().equals(MC.thePlayer.getHeldItem()))
                    .filter(slot -> isItemBlackListed(slot.getStack()))
                    .findFirst();
            if (blacklistedItem.isPresent()) {

                this.dropItem(blacklistedItem.get().slotNumber);
            }else{
                if(toggle.getValue().booleanValue())setState(false);
            }
        }
    }
    public void dropItem(int slotID) {
        MC.playerController.windowClick(0, slotID, 1, 4, MC.thePlayer);
    }

    //Objects.requireNonNull is just for debugging. It can't be null
    //Things to throw out
    public boolean isItemBlackListed(ItemStack itemStack) {
        Item item = itemStack.getItem();

        return (blacklistedItemIDs.contains(Item.getIdFromItem(item)) ||
                item instanceof ItemBow && !this.bestBow.equals(itemStack) ||
                item instanceof ItemTool && !this.bestPickAxe.equals(itemStack) ||
                item instanceof ItemFishingRod || item instanceof ItemGlassBottle || item instanceof ItemBucket ||
                /*   !( item instanceof ItemFood) && !(item instanceof ItemAppleGold) */

                item instanceof ItemSword && !this.bestSword.equals(itemStack) ||
                item instanceof ItemArmor && !this.bestArmorSet[((ItemArmor) item).armorType].equals(itemStack) ||
                item instanceof ItemPotion && isPotionNegative(itemStack))&&
                !(item instanceof  ItemFood);
    }

    //Improved check to reduce copy pasty code
    public ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
        Optional<ItemStack> bestItem = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                .stream()
                .map(Slot::getStack)
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getItem().getClass().equals(itemType))
                .max(comparator);

        return bestItem.orElse(null);
    }
    //Armor check
    public ItemStack[] getBestArmorSet() {
        ItemStack[] bestArmorSet = new ItemStack[4];

        List<ItemStack> armor = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                .stream()
                .filter(Slot::getHasStack)
                .map(Slot::getStack)
                .filter(itemStack -> itemStack.getItem() instanceof ItemArmor)
                .collect(Collectors.toList());

        for (ItemStack itemStack : armor) {
            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

            ItemStack bestArmor = bestArmorSet[itemArmor.armorType];

            if (bestArmor == null || getArmorDamageReduction(itemStack) > getArmorDamageReduction(bestArmor)) {
                bestArmorSet[itemArmor.armorType] = itemStack;
            }
        }

        return bestArmorSet;
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

    public double getBowPower(ItemStack itemStack) {
        double power = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            power = attributeModifier.get().getAmount();
        }

        power += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

        return power;
    }

    public double getMiningSpeed(ItemStack itemStack) {
        double speed = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            speed = attributeModifier.get().getAmount();
        }

        speed += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

        return speed;
    }


    public double getArmorDamageReduction(ItemStack itemStack) {

        int damageReductionAmount = ((ItemArmor) itemStack.getItem()).damageReduceAmount;

        damageReductionAmount += EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack},
                DamageSource.causePlayerDamage(MC.thePlayer));

        return damageReductionAmount;
    }



    public boolean isPotionNegative(ItemStack itemStack) {
        ItemPotion potion = (ItemPotion) itemStack.getItem();

        List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

        return potionEffectList.stream()
                .map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
                .anyMatch(Potion::isBadEffect);
    }
}
