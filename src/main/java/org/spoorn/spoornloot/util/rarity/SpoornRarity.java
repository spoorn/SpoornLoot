package org.spoorn.spoornloot.util.rarity;

import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

import java.lang.reflect.Constructor;

public enum SpoornRarity {
    COMMON(Rarity.COMMON.formatting.getColorValue()),
    UNCOMMON(Rarity.UNCOMMON.formatting.getColorValue()),
    RARE(Rarity.RARE.formatting.getColorValue()),
    EPIC(Rarity.EPIC.formatting.getColorValue()),
    PINK(16761035);

    public Integer colorValue;

    private SpoornRarity(Integer colorValue) {
        this.colorValue = colorValue;
    }
}
