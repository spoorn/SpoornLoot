package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import lombok.extern.log4j.Log4j2;
import net.minecraft.util.Identifier;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

@Log4j2
public class CharmSword extends BaseCharmSword {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "charm_sword");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.DOOM;
    private static final int DEFAULT_ATK_DMG = 15;
    private static final float DEFAULT_ATK_SPD = -2.4f;

    public CharmSword() {
        this(DEFAULT_SPOORN_RARITY, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD);
    }

    public CharmSword(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }
}
