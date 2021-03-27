package org.spoorn.spoornloot.item.daggers;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import net.minecraft.util.Identifier;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

public class SunsetDagger extends BaseDagger {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "sunset_dagger");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.DOOM;
    private static final int DEFAULT_ATK_DMG = 7;
    private static final float DEFAULT_ATK_SPD = -1.6f;

    public SunsetDagger() {
        this(DEFAULT_SPOORN_RARITY, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD);
    }

    public SunsetDagger(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }
}
