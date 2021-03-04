package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Log4j2
public abstract class BaseSpoornSwordItem extends SwordItem {

    public BaseSpoornSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!tooltip.isEmpty()) {
            //log.info("Changing tooltip for {}", this.getClass().getSimpleName());
            Style newStyle = tooltip.get(0).getStyle().withColor(TextColor.fromRgb(16761035));
            ((MutableText)(tooltip.get(0))).setStyle(newStyle);
        }
        tooltip.add(new TranslatableText(this.getTranslationKey() + ".tooltip"));
    }
}
