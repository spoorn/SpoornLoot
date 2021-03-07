package org.spoorn.spoornloot.util;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.item.swords.SwordRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public final class SpoornUtil {

    public static final String MODID = "spoornloot";
    public static final String GENERAL = "general";
    public static final String CRIT_CHANCE = "critChance";
    public static final String CRIT_CHANCE_ID = "spoornloot.crit_chance";
    public static final String CRIT_WEAPON_MODIFIER = "Crit Chance";
    public static final String LIGHTNING_AFFINITY = "lightningAffinity";
    public static final String LIGHTNING_AFFINITY_ID = "item.spoornloot.lightning_affinity";
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
    public static final double getNextGaussian(int mean, double sd, int min, int max) {
        double nextGaussian = RANDOM.nextGaussian() * sd + mean;
        if (nextGaussian < min) {
            nextGaussian = min;
        } else if (nextGaussian > max) {
            nextGaussian = max;
        }
        return nextGaussian;
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
