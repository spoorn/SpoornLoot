package org.spoorn.spoornloot.item.swords;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
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
import java.util.Random;

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
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        CompoundTag compoundTag = stack.getOrCreateTag();
        if (!compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
            float critChance = new Random().nextFloat();
            log.info("Setting sword crit chance to {} for stack {}", critChance, stack);
            compoundTag.putFloat(SpoornUtil.CRIT_CHANCE, critChance);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
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

        super.appendTooltip(stack, world, tooltip, context);
    }
}
