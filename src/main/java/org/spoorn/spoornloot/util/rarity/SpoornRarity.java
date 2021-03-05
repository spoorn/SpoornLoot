package org.spoorn.spoornloot.util.rarity;

import lombok.Getter;

public enum SpoornRarity {
    COMMON(13401901, 20),
    UNCOMMON(5950509, 10),
    RARE(2977228, 8),
    EPIC(12278256, 5),
    PINK(16761035, 2),
    LEGENDARY(3197337, 1),
    DOOM(13899581, 1);

    @Getter
    private Integer colorValue;
    @Getter
    private int weight;

    private SpoornRarity(Integer colorValue, int weight) {
        this.colorValue = colorValue;
        this.weight = weight;
    }
}
