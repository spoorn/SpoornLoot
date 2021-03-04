package org.spoorn.spoornloot.item.swords;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import static net.minecraft.item.ToolMaterials.DIAMOND;

public class SpoornToolMaterial implements ToolMaterial {

    @Override
    public int getDurability() {
        return 999;
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
        return null;
    }
}
