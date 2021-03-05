package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;
import org.spoorn.spoornloot.util.settings.SpoornItemSettings;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;

@Log4j2
public class HeartSwordItem extends BaseSpoornSwordItem {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "heart_sword");

    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = new SpoornToolMaterial();
    private static final int DEFAULT_ATK_DMG = 65;
    private static final float DEFAULT_ATK_SPD = 2.9f;
    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.PINK;
    private static final Settings DEFAULT_SETTINGS = new SpoornItemSettings()
            .spoornRarity(DEFAULT_SPOORN_RARITY)
            .group(SPOORN_ITEM_GROUP)
            .rarity(Rarity.EPIC);

    public HeartSwordItem() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public HeartSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, DEFAULT_SPOORN_RARITY, settings);
    }
}
