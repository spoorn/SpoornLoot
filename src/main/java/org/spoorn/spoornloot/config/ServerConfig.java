package org.spoorn.spoornloot.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ServerConfig {

    @Comment("Chance for swords to spawn in loot chests [1/value]")
    public int swordSpawnInLootChestsChance = 10;

    @Comment("Chance for swords to get a crit chance attribute [1/value]")
    public int critChanceChance = 10;

    @Comment("Chance for swords to have a lightning enchantment [1/value]")
    public int lightningAffinityChance = 100;

    @Comment("Chance for swords to have fire damage [1/value]")
    public int fireDamageChance = 80;

    @Comment("Maximum fire damage for swords [1/value]")
    public int maxSwordFireDamage = 30;
}
