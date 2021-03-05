package org.spoorn.spoornloot.util.rarity;

import lombok.Getter;

public enum SpoornRarity {
    COMMON(13401901),
    UNCOMMON(5950509),
    RARE(2977228),
    EPIC(12278256),
    LEGENDARY(3197337),
    DOOM(13899581),
    PINK(16761035);

    @Getter
    private Integer colorValue;

    private SpoornRarity(Integer colorValue) {
        this.colorValue = colorValue;
    }
}
