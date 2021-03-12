package org.spoorn.spoornloot.item.swords;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

        // Add various attributes to tooltip
        if (stack.hasTag()) {
            CompoundTag compoundTag = SpoornUtil.getOrCreateSpoornCompoundTag(stack, false);
            if (compoundTag != null) {
                // Lightning affinity tag on the tooltip
                boolean hasLightningAffinity = compoundTag.contains(SpoornUtil.LIGHTNING_AFFINITY)
                        ? compoundTag.getBoolean(SpoornUtil.LIGHTNING_AFFINITY) : false;
                if (hasLightningAffinity) {
                    tooltip.add(new TranslatableText(SpoornUtil.LIGHTNING_AFFINITY_ID).setStyle(SpoornUtil.LIGHTNING_STYLE));
                }

                // Explosive tag on the tooltip
                boolean isExplosive = compoundTag.contains(SpoornUtil.EXPLOSIVE)
                        ? compoundTag.getBoolean(SpoornUtil.EXPLOSIVE) : false;
                if (isExplosive) {
                    tooltip.add(new TranslatableText(SpoornUtil.EXPLOSIVE_ID).setStyle(SpoornUtil.EXPLOSIVE_STYLE));
                }
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // Item attributes are calculated when loot is generated for loot chests.  This is a fallback in case the loot
        // came from somewhere else such as a mob drop.
        SpoornUtil.addSwordAttributes(stack);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getItem() instanceof BaseSpoornSwordItem) {
            float lifesteal = getLifesteal(SpoornUtil.getOrCreateSpoornCompoundTag(stack, false)) / 100;
            //log.info("Lifesteal: {}", lifesteal);
            try {
                float lastDamageTaken = target.getClass().getField(SpoornUtil.LAST_DAMAGE_TAKEN_FIELD).getFloat(target);
                //log.info("Last damage taken: {}", lastDamageTaken);
                //log.info("Heal amount: {}", lifesteal * lastDamageTaken);
                attacker.heal(lifesteal * lastDamageTaken);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return super.postHit(stack, target, attacker);
    }

    // Get lifesteal from NBT
    private static float getLifesteal(CompoundTag compoundTag) {
        if (compoundTag == null || !compoundTag.contains(SpoornUtil.LIFESTEAL)) {
            log.error("Could not find Lifesteal data on Spoorn Sword.  This should not happen!");
            return 0;
        }

        return compoundTag.getFloat(SpoornUtil.LIFESTEAL);
    }
}
