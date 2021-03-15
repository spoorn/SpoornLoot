package org.spoorn.spoornloot.util;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.item.common.DualWieldable;
import org.spoorn.spoornloot.item.daggers.BaseDagger;
import org.spoorn.spoornloot.item.daggers.SpoornDagger;
import org.spoorn.spoornloot.item.daggers.SpoornDagger2;
import org.spoorn.spoornloot.item.swords.BaseLongSwordItem;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;
import org.spoorn.spoornloot.item.swords.SpoornSwordItem;
import org.spoorn.spoornloot.item.swords.SwordRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * Random Utility for code shared between multiple classes.
 */
@Log4j2
public final class SpoornUtil {

    public static final String MODID = "spoornloot";
    public static final String GENERAL = "general";
    public static final String CRIT_CHANCE = "critChance";
    public static final String CRIT_CHANCE_ID = "spoornloot.crit_chance";
    public static final String CRIT_WEAPON_MODIFIER = "Crit Chance";
    public static final String LIGHTNING_AFFINITY = "lightningAffinity";
    public static final String LIGHTNING_AFFINITY_ID = "item.spoornloot.lightning_affinity";
    public static final String FIRE_DAMAGE = "fireDamage";
    public static final String FIRE_DAMAGE_ID = "spoornloot.fire_damage";
    public static final String FIRE_DAMAGE_WEAPON_MODIFIER = "Fire Damage";
    public static final String COLD_DAMAGE = "coldDamage";
    public static final String COLD_DAMAGE_ID = "spoornloot.cold_damage";
    public static final String COLD_DAMAGE_WEAPON_MODIFIER = "Cold Damage";
    public static final String LIFESTEAL = "lifesteal";
    public static final String LIFESTEAL_ID = "spoornloot.lifesteal";
    public static final String LIFESTEAM_WEAPON_MODIFIER = "Lifesteal";
    public static final String EXPLOSIVE = "explosive";
    public static final String EXPLOSIVE_ID = "item.spoornloot.explosive";
    public static final DamageSource SPOORN_DMG_SRC = new SpoornDamageSource(EXPLOSIVE_ID);
    public static final String SPOORN_NBT_TAG_NAME = "spoornConfig";
    public static final String LAST_SPOORN_SOUND = "lastSpoornSound";
    public static final Identifier COMBAT_IDENTIFIER = new Identifier(MODID, GENERAL);
    public static final ItemGroup SPOORN_ITEM_GROUP = FabricItemGroupBuilder.create(COMBAT_IDENTIFIER)
        .icon(() -> new ItemStack(SwordRegistry.DEFAULT_SPOORN_SWORD))
        .build();
    public static final Style LIGHTNING_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(15990666));
    public static final Style EXPLOSIVE_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(11337728));

    public static final Random RANDOM = new Random();

    public static final EntityAttribute CRIT_CHANCE_ENTITY_ATTRIBUTE = register(
        CRIT_CHANCE_ID,
        new ClampedEntityAttribute(SpoornUtil.CRIT_WEAPON_MODIFIER, 0.0f, 0.0f, 1.0f).setTracked(true)
    );

    public static final EntityAttribute FIRE_DAMAGE_ENTITY_ATTRIBUTE = register(
        FIRE_DAMAGE_ID,
        new ClampedEntityAttribute(SpoornUtil.FIRE_DAMAGE_WEAPON_MODIFIER, 0.0f, 0.0f,
            ModConfig.get().serverConfig.maxSwordFireDamage).setTracked(true)
    );

    public static final EntityAttribute COLD_DAMAGE_ENTITY_ATTRIBUTE = register(
            COLD_DAMAGE_ID,
            new ClampedEntityAttribute(SpoornUtil.COLD_DAMAGE_WEAPON_MODIFIER, 0.0f, 0.0f,
                    ModConfig.get().serverConfig.maxSwordColdDamage).setTracked(true)
    );

    public static final EntityAttribute LIFESTEAL_ENTITY_ATTRIBUTE = register(
            LIFESTEAL_ID,
            new ClampedEntityAttribute(SpoornUtil.LIFESTEAM_WEAPON_MODIFIER, 0.0f, 0.0f,
                    ModConfig.get().serverConfig.maxLifesteal).setTracked(true)
    );

    public static final Map<EntityAttribute, AttributeInfo> ENTITY_ATTRIBUTES;

    static {
        Map<EntityAttribute, AttributeInfo> localMap = new HashMap<>();
        localMap.put(CRIT_CHANCE_ENTITY_ATTRIBUTE,
            new AttributeInfo(CRIT_WEAPON_MODIFIER, CRIT_CHANCE, (x) -> x * 100));
        localMap.put(FIRE_DAMAGE_ENTITY_ATTRIBUTE,
            new AttributeInfo(FIRE_DAMAGE_WEAPON_MODIFIER, FIRE_DAMAGE, Function.identity()));
        localMap.put(COLD_DAMAGE_ENTITY_ATTRIBUTE,
            new AttributeInfo(COLD_DAMAGE_WEAPON_MODIFIER, COLD_DAMAGE, Function.identity()));
        localMap.put(LIFESTEAL_ENTITY_ATTRIBUTE,
            new AttributeInfo(LIFESTEAM_WEAPON_MODIFIER, LIFESTEAL, Function.identity()));
        ENTITY_ATTRIBUTES = ImmutableMap.copyOf(localMap);
    }

    // Assumes parameters are correct
    public static double getNextGaussian(int mean, double sd, int min, int max) {
        double nextGaussian = RANDOM.nextGaussian() * sd + mean;
        if (nextGaussian < min) {
            nextGaussian = min;
        } else if (nextGaussian > max) {
            nextGaussian = max;
        }
        return nextGaussian;
    }

    public static boolean isSpoornSwordItem(Object object) {
        return object instanceof BaseSpoornSwordItem;
    }

    public static boolean isEnergySword(Object object) {
        return object instanceof SpoornSwordItem || object instanceof SpoornDagger || object instanceof SpoornDagger2;
    }

    /**
     * Returns true if mainHand and offHand are both dual wieldable, and both the same type of weapon. Else false.
     */
    public static boolean isDualWieldableCombo(Item mainHandItem, Item offHandItem) {
        boolean dualWieldable = isDualWieldable(mainHandItem) && isDualWieldable(offHandItem);
        if (!dualWieldable) {
            return false;
        } else {
            return isBothDagger(mainHandItem, offHandItem) || isBothLongSwordItem(mainHandItem, offHandItem);
        }
    }

    private static boolean isDualWieldable(Object object) { return object instanceof DualWieldable; }

    private static boolean isBothLongSwordItem(Item mainHandItem, Item offHandItem) {
        return mainHandItem instanceof BaseLongSwordItem && offHandItem instanceof BaseLongSwordItem;
    }

    private static boolean isBothDagger(Item mainHandItem, Item offHandItem) {
        return mainHandItem instanceof BaseDagger && offHandItem instanceof BaseDagger;
    }


    // Spoorn NBT helpers

    public static CompoundTag getButDontCreateSpoornCompoundTag(ItemStack stack) {
        return getOrCreateSpoornCompoundTag(stack, false);
    }

    public static CompoundTag getOrCreateSpoornCompoundTag(ItemStack stack, boolean createItemStackTagIfNotExists) {
        if (!createItemStackTagIfNotExists && !stack.hasTag()) {
            log.warn("ItemStack does not have a tag, but createItemStackTagIfNotExists=false");
            return null;
        }
        return getOrCreateSpoornCompoundTag(stack.getTag(), createItemStackTagIfNotExists);
    }

    public static CompoundTag getOrCreateSpoornCompoundTag(CompoundTag compoundTag, boolean createItemStackTagIfNotExists) {
        if (compoundTag == null) {
            //log.warn("Can't get Spoorn NBT data from NULL compoundTag!");
            return null;
        }
        if (!compoundTag.contains(SPOORN_NBT_TAG_NAME, compoundTag.getType())) {
            if (createItemStackTagIfNotExists) {
                compoundTag.put(SPOORN_NBT_TAG_NAME, new CompoundTag());
            } else {
                return null;
            }
        }
        return compoundTag.getCompound(SPOORN_NBT_TAG_NAME);
    }


    // Sword attributes

    public static void addSwordAttributes(List<ItemStack> itemStacks) {
        for (ItemStack stack : itemStacks) {
            addSwordAttributes(stack);
        }
    }

    public static void addSwordAttributes(ItemStack stack) {
        if (isSpoornSwordItem(stack.getItem())) {
            CompoundTag compoundTag = getOrCreateSpoornCompoundTag(stack, true);

            // Crit chance
            if (!compoundTag.contains(CRIT_CHANCE)) {
                float randFloat = RANDOM.nextFloat();
                if (randFloat < (1.0 / ModConfig.get().serverConfig.critChanceChance)) {
                    // This mean and sd makes it so  there's a ~0.1% chance of getting above 90% crit chance
                    float critChance = (float) getNextGaussian(25, 20, 20, 100) / 100;
                    //log.info("Setting sword crit chance to {} for stack {}", critChance, stack);
                    compoundTag.putFloat(CRIT_CHANCE, critChance);
                } else {
                    compoundTag.putFloat(CRIT_CHANCE, 0);
                }
            }

            // Lightning affinity
            if (!compoundTag.contains(LIGHTNING_AFFINITY)) {
                float lightningChance = RANDOM.nextFloat();
                //log.info("Sword lightning chance is {} for stack {}", lightningChance, stack);
                boolean hasLightningAffinity = lightningChance < (1.0 / ModConfig.get().serverConfig.lightningAffinityChance);
                compoundTag.putBoolean(LIGHTNING_AFFINITY, hasLightningAffinity);
            }

            // Fire damage
            if (!compoundTag.contains(FIRE_DAMAGE)) {
                float fireChance = RANDOM.nextFloat();
                if (fireChance < (1.0 / ModConfig.get().serverConfig.fireDamageChance)) {
                    // This mean and sd makes it so  there's a ~9% chance of getting above 5 fire damage
                    float fireDamage = (float) getNextGaussian(1, 3, 1, ModConfig.get().serverConfig.maxSwordFireDamage);
                    //log.info("Setting sword fire damage to {} for stack {}", fireDamage, stack);
                    compoundTag.putFloat(FIRE_DAMAGE, fireDamage);
                } else {
                    compoundTag.putFloat(FIRE_DAMAGE, 0);
                }
            }

            // Fire damage
            if (!compoundTag.contains(COLD_DAMAGE)) {
                float coldChance = RANDOM.nextFloat();
                if (coldChance < (1.0 / ModConfig.get().serverConfig.coldChance)) {
                    // This mean and sd makes it so  there's a ~9% chance of getting above 5 fire damage
                    float coldDamage = (float) getNextGaussian(1, 3, 1, ModConfig.get().serverConfig.maxSwordColdDamage);
                    //log.info("Setting sword cold damage to {} for stack {}", coldDamage, stack);
                    compoundTag.putFloat(COLD_DAMAGE, coldDamage);
                } else {
                    compoundTag.putFloat(COLD_DAMAGE, 0);
                }
            }

            // Lifesteal
            if (!compoundTag.contains(LIFESTEAL)) {
                float lifestealChance = RANDOM.nextFloat();
                if (lifestealChance < (1.0 / ModConfig.get().serverConfig.lifestealChance)) {
                    // This mean and sd makes it so  there's a ~10% chance of getting above 10 lifesteal
                    float lifesteal = (float) getNextGaussian(5, 3.9, 0, ModConfig.get().serverConfig.maxLifesteal);
                    //log.info("Setting sword fire damage to {} for stack {}", fireDamage, stack);
                    compoundTag.putFloat(LIFESTEAL, lifesteal);
                } else {
                    compoundTag.putFloat(LIFESTEAL, 0);
                }
            }

            // Explosive
            if (!compoundTag.contains(EXPLOSIVE)) {
                float explosiveChance = RANDOM.nextFloat();
                boolean explosive = explosiveChance < (1.0 / ModConfig.get().serverConfig.explosiveChance);
                compoundTag.putBoolean(EXPLOSIVE, explosive);
            }
        }
    }


    // private helper stuff

    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return (EntityAttribute) Registry.register(Registry.ATTRIBUTE, id, attribute);
    }

    public static class AttributeInfo {
        @Getter
        private String name;
        @Getter
        private String tagName;
        @Getter
        private Function<Float, Float> modifierFunction;

        AttributeInfo(String name, String tagName, Function<Float, Float> modifierFunction) {
            this.name = name;
            this.tagName = tagName;
            this.modifierFunction = modifierFunction;
        }
    }
}
