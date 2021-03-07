package org.spoorn.spoornloot.util;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.item.swords.SwordRegistry;

import java.util.Random;

public final class SpoornUtil {

    public static final String MODID = "spoornloot";
    public static final String GENERAL = "general";
    public static final String CRIT_CHANCE = "critChance";
    public static final String CRIT_CHANCE_ID = "spoornloot.crit_chance";
    public static final String WEAPON_MODIFIER = "Crit Chance";
    public static final String COMBAT = "combat";
    public static final Identifier COMBAT_IDENTIFIER = new Identifier(MODID, GENERAL);
    public static final ItemGroup SPOORN_ITEM_GROUP = FabricItemGroupBuilder.create(COMBAT_IDENTIFIER)
        .icon(() -> new ItemStack(SwordRegistry.DEFAULT_SPOORN_SWORD))
        .build();

    private static final Random gaussianRandom = new Random();

    public static final EntityAttribute CRIT_CHANCE_ENTITY_ATTRIBUTE = register(
        CRIT_CHANCE_ID,
        new ClampedEntityAttribute(SpoornUtil.WEAPON_MODIFIER, 0.0f, 0.0f, 1.0f).setTracked(true)
    );

    // Assumes parameters are correct
    public static final double getNextGaussian(int mean, double sd, int min, int max) {
        double nextGaussian = gaussianRandom.nextGaussian() * sd + mean;
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
}
