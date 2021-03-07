package org.spoorn.spoornloot.item.swords;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spoorn.spoornloot.util.SpoornUtil;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

import java.util.List;

@Log4j2
public abstract class BaseSpoornSwordItem extends SwordItem {

    @Getter
    private SpoornRarity spoornRarity;
    @Getter
    private Settings baseSettings;

    public BaseSpoornSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed,
        SpoornRarity spoornRarity, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.spoornRarity = spoornRarity;
        this.baseSettings = settings;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        // Change tooltip name color
        if (!tooltip.isEmpty()) {
            //log.info("Changing tooltip for {}", this.getClass().getSimpleName());
            Style newStyle = tooltip.get(0).getStyle().withColor(TextColor.fromRgb(this.spoornRarity.getColorValue()));
            ((MutableText)(tooltip.get(0))).setStyle(newStyle);
        }

        // Add custom lore
        tooltip.add(new TranslatableText(this.getTranslationKey() + ".tooltip"));

        // Add lightning affinity
        if (stack.hasTag()) {
            CompoundTag compoundTag = stack.getTag();
            boolean hasLightningAffinity = compoundTag.contains(SpoornUtil.LIGHTNING_AFFINITY)
                ? compoundTag.getBoolean(SpoornUtil.LIGHTNING_AFFINITY) : false;
            if (hasLightningAffinity) {
                Style style = Style.EMPTY.withColor(TextColor.fromRgb(15990666));
                //tooltip.add(new LiteralText(" "));
                tooltip.add(new TranslatableText(SpoornUtil.LIGHTNING_AFFINITY_ID).setStyle(style));
            }
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}
