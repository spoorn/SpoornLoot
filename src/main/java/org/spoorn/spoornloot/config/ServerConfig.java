package org.spoorn.spoornloot.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ServerConfig {

    @Comment("Chance for swords to spawn in loot chests [1/value] [default = 10]")
    public int swordSpawnInLootChestsChance = 10;

    @Comment("Chance for swords to get a crit chance attribute [1/value] [default = 10]")
    public int critChanceChance = 10;

    @Comment("Chance for swords to have a lightning enchantment [1/value] [default = 100]")
    public int lightningAffinityChance = 100;

    @Comment("Chance for swords to have fire damage [1/value] [default = 80]")
    public int fireDamageChance = 80;

    @Comment("Maximum fire damage for swords [1/value] [default = 30]")
    public int maxSwordFireDamage = 30;

    @Comment("Chance for swords to have lifesteal [1/value] [default = 50]")
    public int lifestealChance = 50;

    @Comment("Maximum lifesteam for swords [1/value] [default = 30]")
    public int maxLifesteal = 30;
}
