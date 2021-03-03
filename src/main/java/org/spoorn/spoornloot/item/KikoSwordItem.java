package org.spoorn.spoornloot.item;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.List;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;

@Log4j2
public class KikoSwordItem extends SwordItem {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "kiko_sword");

    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = ToolMaterials.DIAMOND;
    private static final int DEFAULT_ATK_DMG = 65;
    private static final float DEFAULT_ATK_SPD = 2.9f;
    private static final Settings DEFAULT_SETTINGS = new FabricItemSettings().group(SPOORN_ITEM_GROUP);

    public KikoSwordItem() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public KikoSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("item.spoornloot.kiko_sword.tooltip"));
    }
}
