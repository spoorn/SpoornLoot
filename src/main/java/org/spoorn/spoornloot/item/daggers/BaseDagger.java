package org.spoorn.spoornloot.item.daggers;

import net.minecraft.item.ToolMaterial;
import org.spoorn.spoornloot.item.common.DualWieldable;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

/**
 * Base Dagger class.  All Daggers are Dual Wieldable.
 */
public abstract class BaseDagger extends BaseSpoornSwordItem implements DualWieldable {

    public BaseDagger(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, SpoornRarity spoornRarity,
        Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, spoornRarity, settings);
    }

    // Convenience constructor
    public BaseDagger(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }
}
