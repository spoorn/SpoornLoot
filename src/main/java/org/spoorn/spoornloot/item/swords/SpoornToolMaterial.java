package org.spoorn.spoornloot.item.swords;

import static net.minecraft.item.ToolMaterials.DIAMOND;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

public class SpoornToolMaterial implements ToolMaterial {

    private SpoornRarity spoornRarity;

    public SpoornToolMaterial() {
        spoornRarity = SpoornRarity.COMMON;
    }

    public SpoornToolMaterial(SpoornRarity spoornRarity) {
        this.spoornRarity = spoornRarity;
    }

    @Override
    public int getDurability() {
        return this.spoornRarity.getDefaultDurability();
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return DIAMOND.getMiningLevel();
    }

    @Override
    public int getEnchantability() {
        return DIAMOND.getEnchantability();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.ENDER_PEARL);
    }
}
