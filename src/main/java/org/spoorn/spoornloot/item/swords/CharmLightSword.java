package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import net.minecraft.util.Identifier;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

public class CharmLightSword extends BaseCharmSword {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "charm_light_sword");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.EPIC;
    private static final int DEFAULT_ATK_DMG = 8;
    private static final float DEFAULT_ATK_SPD = -2.4f;

    public CharmLightSword() {
        this(DEFAULT_SPOORN_RARITY, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD);
    }

    public CharmLightSword(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }
}
