package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;
import lombok.extern.log4j.Log4j2;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;
import org.spoorn.spoornloot.util.settings.SpoornItemSettings;

@Log4j2
public class EagleSword2Item extends BaseSpoornSwordItem {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "eagle_sword_2");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.RARE;
    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = new SpoornToolMaterial(DEFAULT_SPOORN_RARITY);
    private static final int DEFAULT_ATK_DMG = 6;
    private static final float DEFAULT_ATK_SPD = -2.4f;
    private static final Settings DEFAULT_SETTINGS = new SpoornItemSettings()
            .spoornRarity(DEFAULT_SPOORN_RARITY)
            .group(SPOORN_ITEM_GROUP)
            .rarity(Rarity.EPIC);

    public EagleSword2Item() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public EagleSword2Item(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, DEFAULT_SPOORN_RARITY, settings);
    }
}
