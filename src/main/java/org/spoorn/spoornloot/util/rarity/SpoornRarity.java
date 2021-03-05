package org.spoorn.spoornloot.util.rarity;

import lombok.Getter;

public class SpoornRarity {
    public static final SpoornRarity COMMON = new SpoornRarity(13401901, 20, 300);
    public static final SpoornRarity UNCOMMON = new SpoornRarity(5950509, 10, 450);
    public static final SpoornRarity RARE = new SpoornRarity(2977228, 8, 600);
    public static final SpoornRarity EPIC = new SpoornRarity(12278256, 5, 999);
    public static final SpoornRarity PINK = new SpoornRarity(16761035, 2, 1300);
    public static final SpoornRarity LEGENDARY = new SpoornRarity(3197337, 1, 1300);
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
