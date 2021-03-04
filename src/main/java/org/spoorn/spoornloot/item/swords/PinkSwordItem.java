package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;

@Log4j2
public class PinkSwordItem extends BaseSpoornSwordItem {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "pink_sword");

    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = ToolMaterials.DIAMOND;
    private static final int DEFAULT_ATK_DMG = 30;
    private static final float DEFAULT_ATK_SPD = 3.9f;
    private static final Settings DEFAULT_SETTINGS = new FabricItemSettings()
        .group(SPOORN_ITEM_GROUP)
        .rarity(Rarity.EPIC);

    public PinkSwordItem() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public PinkSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
}
