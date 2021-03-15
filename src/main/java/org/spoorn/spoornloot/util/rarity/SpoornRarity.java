package org.spoorn.spoornloot.util.rarity;

import lombok.Getter;

/**
 * Custom Spoorn Rarities to control different properties related to rarity.  Minecraft's native Rarity is too
 * limited and not extensible.
 */
public class SpoornRarity {
    public static final SpoornRarity COMMON = new SpoornRarity(13401901, 30, 300);
    public static final SpoornRarity UNCOMMON = new SpoornRarity(5950509, 20, 450);
    public static final SpoornRarity RARE = new SpoornRarity(2977228, 12, 600);
    public static final SpoornRarity EPIC = new SpoornRarity(12278256, 8, 999);
    public static final SpoornRarity PINK = new SpoornRarity(16761035, 3, 1300);
    public static final SpoornRarity LEGENDARY = new SpoornRarity(3197337, 2, 1300);
    public static final SpoornRarity DOOM = new SpoornRarity(13899581, 1, 1600);

    @Getter
    private Integer colorValue;
    @Getter
    private int weight;
    @Getter
    private int defaultDurability;

    private SpoornRarity(Integer colorValue, int weight, int defaultDurability) {
        this.colorValue = colorValue;
        this.weight = weight;
        this.defaultDurability = defaultDurability;
    }
}
