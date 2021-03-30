package org.spoorn.spoornloot.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ServerConfig {

    @Comment("Chance for swords to spawn in loot chests [1/value, 0 = no spawn] [default = 10]")
    public int swordSpawnInLootChestsChance = 10;

    @Comment("Chance for swords to get a crit chance attribute [1/value, 0 = no spawn] [default = 10]")
    public int critChanceChance = 10;

    @Comment("Chance for swords to have a lightning enchantment [1/value, 0 = no spawn] [default = 100]")
    public int lightningAffinityChance = 100;

    @Comment("Chance for swords to have fire damage [1/value, 0 = no spawn] [default = 80]")
    public int fireDamageChance = 80;

    @Comment("Maximum fire damage for swords [default = 30]")
    public int maxSwordFireDamage = 30;

    @Comment("Chance for sword to have cold damage [1/value, 0 = no spawn] [default = 80]")
    public int coldChance = 80;

    @Comment("Maximum cold damage for swords [default = 30]")
    public int maxSwordColdDamage = 30;

    @Comment("Chance for swords to have lifesteal [1/value, 0 = no spawn] [default = 50]")
    public int lifestealChance = 50;

    @Comment("Maximum lifesteam for swords [default = 30]")
    public int maxLifesteal = 30;

    @Comment("Chance for sword to be Explosive [1/value, 0 = no spawn] [default = 200]")
    public int explosiveChance = 200;

    @Comment("This is how much we scale the damage of the offhand weapon when dual wielding [1/value] [default = 2 (50%)]")
    public int dualWieldDamageScale = 2;

    @Comment("Block radius range of Heart Sword Charm effect on entities [default = 20]")
    public int heartSwordCharmRadius = 20;

    @Comment("Charm effect duration in seconds [default = 10]")
    public int charmEffectDuration = 10;

    @Comment("Charm effect cooldown in seconds [default = 30]")
    public int charmEffectCooldown = 30;

    @Comment("True if Heart Sword Charm should affect other players, else false (EXPERIMENTAL) [default = false]")
    public boolean shouldCharmAffectPlayers = false;
}
