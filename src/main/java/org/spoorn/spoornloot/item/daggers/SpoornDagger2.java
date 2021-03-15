package org.spoorn.spoornloot.item.daggers;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.spoorn.spoornloot.item.swords.SpoornToolMaterial;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;
import org.spoorn.spoornloot.util.settings.SpoornItemSettings;

public class SpoornDagger2 extends BaseDagger {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "spoorn_dagger_2");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.DOOM;
    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = new SpoornToolMaterial(DEFAULT_SPOORN_RARITY);
    private static final int DEFAULT_ATK_DMG = 7;
    private static final float DEFAULT_ATK_SPD = -1.6f;
    private static final Settings DEFAULT_SETTINGS = new SpoornItemSettings()
            .spoornRarity(DEFAULT_SPOORN_RARITY)
            .group(SPOORN_ITEM_GROUP)
            .rarity(Rarity.EPIC);

    public SpoornDagger2() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public SpoornDagger2(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, DEFAULT_SPOORN_RARITY, settings);
    }
}
