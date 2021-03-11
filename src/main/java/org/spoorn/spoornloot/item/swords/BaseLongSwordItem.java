package org.spoorn.spoornloot.item.swords;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ToolMaterial;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

public class BaseLongSwordItem extends BaseSpoornSwordItem {

    private static final float ATTACK_RANGE = 1.0f;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public BaseLongSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, SpoornRarity spoornRarity,
        Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, spoornRarity, settings);

        // Add reach
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(EquipmentSlot.MAINHAND));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier("Reach modifier", ATTACK_RANGE, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }
}
