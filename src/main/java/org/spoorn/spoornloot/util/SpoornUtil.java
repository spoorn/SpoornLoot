package org.spoorn.spoornloot.util;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;
import org.spoorn.spoornloot.item.swords.SwordRegistry;
import org.spoorn.spoornloot.mixin.ItemStackMixin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

@Log4j2
public final class SpoornUtil {

    public static final String MODID = "spoornloot";
    public static final String GENERAL = "general";
    public static final String CRIT_CHANCE = "critChance";
    public static final String CRIT_CHANCE_ID = "spoornloot.crit_chance";
    public static final String CRIT_WEAPON_MODIFIER = "Crit Chance";
    public static final String LIGHTNING_AFFINITY = "lightningAffinity";
    public static final String LIGHTNING_AFFINITY_ID = "item.spoornloot.lightning_affinity";
    public static final String SPOORN_NBT_TAG_NAME = "spoornConfig";
    /*public static final String LIGHTNING_CHANCE = "lightningChance";
    public static final String LIGHTNING_CHANCE_ID = "spoornloot.lightning_chance";
    public static final String LIGHTNING_WEAPON_MODIFIER = "Lightning Chance";*/
    public static final String COMBAT = "combat";
    public static final Identifier COMBAT_IDENTIFIER = new Identifier(MODID, GENERAL);
    public static final ItemGroup SPOORN_ITEM_GROUP = FabricItemGroupBuilder.create(COMBAT_IDENTIFIER)
        .icon(() -> new ItemStack(SwordRegistry.DEFAULT_SPOORN_SWORD))
        .build();

    public static final Random RANDOM = new Random();

    public static final EntityAttribute CRIT_CHANCE_ENTITY_ATTRIBUTE = register(
        CRIT_CHANCE_ID,
        new ClampedEntityAttribute(SpoornUtil.CRIT_WEAPON_MODIFIER, 0.0f, 0.0f, 1.0f).setTracked(true)
    );

    /*public static final EntityAttribute LIGHTNING_CHANCE_ENTITY_ATTRIBUTE = register(
            LIGHTNING_CHANCE_ID,
            new ClampedEntityAttribute(SpoornUtil.LIGHTNING_WEAPON_MODIFIER, 0.0f, 0.0f, 1.0f).setTracked(true)
    );*/

    public static final Map<EntityAttribute, AttributeInfo> ENTITY_ATTRIBUTES;

    static {
        Map<EntityAttribute, AttributeInfo> localMap = new HashMap<>();
        localMap.put(CRIT_CHANCE_ENTITY_ATTRIBUTE,
            new AttributeInfo(CRIT_WEAPON_MODIFIER, CRIT_CHANCE, (x) -> x * 100));
        /*localMap.put(LIGHTNING_CHANCE_ENTITY_ATTRIBUTE,
            new AttributeInfo(LIGHTNING_WEAPON_MODIFIER, LIGHTNING_CHANCE, (x) -> x * 100));*/
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

    public static void addSwordAttributes(List<ItemStack> itemStacks) {
        for (ItemStack stack : itemStacks) {
            addSwordAttributes(stack);
        }
    }

    public static CompoundTag getOrCreateSpoornCompoundTag(ItemStack stack) {
        return getOrCreateSpoornCompoundTag(stack, true);
    }

    public static CompoundTag getOrCreateSpoornCompoundTag(ItemStack stack, boolean createItemStackTagIfNotExists) {
        if (!createItemStackTagIfNotExists && !stack.hasTag()) {
            log.warn("ItemStack does not have a tag, but createItemStackTagIfNotExists=false");
            return null;
        }
        return getAndCreateSpoornCompoundTag(stack.getOrCreateTag());
    }

    public static CompoundTag getAndCreateSpoornCompoundTag(CompoundTag compoundTag) {
        if (compoundTag == null) {
            //log.warn("Can't get Spoorn NBT data from NULL compoundTag!");
            return null;
        }
        if (!compoundTag.contains(SPOORN_NBT_TAG_NAME, compoundTag.getType())) {
            compoundTag.put(SPOORN_NBT_TAG_NAME, new CompoundTag());
        }
        return compoundTag.getCompound(SPOORN_NBT_TAG_NAME);
    }

    public static void addSwordAttributes(ItemStack stack) {
        if (stack.getItem() instanceof BaseSpoornSwordItem) {
            CompoundTag compoundTag = getOrCreateSpoornCompoundTag(stack);
            if (!compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
                float randFloat = SpoornUtil.RANDOM.nextFloat();
                if (randFloat < (1.0 / ModConfig.get().serverConfig.critChanceChance)) {
                    // This mean and sd makes it so  there's a ~0.1% chance of getting above 90% crit chance
                    float critChance = (float) SpoornUtil.getNextGaussian(25, 20, 20, 100) / 100;
                    //log.info("Setting sword crit chance to {} for stack {}", critChance, stack);
                    compoundTag.putFloat(SpoornUtil.CRIT_CHANCE, critChance);
                } else {
                    compoundTag.putFloat(SpoornUtil.CRIT_CHANCE, 0);
                }
            }

            if (!compoundTag.contains(SpoornUtil.LIGHTNING_AFFINITY)) {
                float lightningChance = SpoornUtil.RANDOM.nextFloat();
                //log.info("Sword lightning chance is {} for stack {}", lightningChance, stack);
                boolean hasLightningAffinity = lightningChance < (1.0 / ModConfig.get().serverConfig.lightningAffinityChance);
                compoundTag.putBoolean(SpoornUtil.LIGHTNING_AFFINITY, hasLightningAffinity);
            }
        }
    }

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
