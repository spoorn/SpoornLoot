package org.spoorn.spoornloot.mixin;

import static org.spoorn.spoornloot.util.SpoornUtil.CRIT_CHANCE_ENTITY_ATTRIBUTE;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.UUID;

@Log4j2
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow @Nullable public abstract CompoundTag getTag();

    @Inject(method="getAttributeModifiers", at=@At(value="TAIL"), cancellable = true)
    public void addMoreAttributeModifiers(EquipmentSlot equipmentSlot,
        CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        /*
            Implementation of ItemStack.getAttributeModifiers() is weird.  It returns the ItemStack's multimap
            of EntityAttribute/Modifiers if available, otherwise it'll return the Item's multimap.  This means
            if we get the multimap here then try to call ItemStack.addAttributeModifier(), it'll create a NEW
            multimap for the ItemStack, so the next call to ItemStack.getAttributeModifiers() will fetch this new
            multimap instead of the Item's.  This causes Attack Damage and Attack Speed to now show up in the
            tooltip, unless we also call ItemStack.addAttributeModifier() with the result from
            ItemStack.getAttributeModifiers().

            So we have to make the multimap immutable first, add our attributes and inject that into the return.
         */
        if (EquipmentSlot.MAINHAND == equipmentSlot) {
            CompoundTag compoundTag = this.getTag();
            if (compoundTag != null && compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
                Multimap<EntityAttribute, EntityAttributeModifier> multimap = cir.getReturnValue();
                if (!multimap.containsKey(CRIT_CHANCE_ENTITY_ATTRIBUTE)) {
                    Multimap<EntityAttribute, EntityAttributeModifier> mutableMultimap = HashMultimap.create(multimap);
                    mutableMultimap.put(
                            CRIT_CHANCE_ENTITY_ATTRIBUTE,
                            new EntityAttributeModifier(UUID.randomUUID(), "Crit chance",
                                    compoundTag.getFloat(SpoornUtil.CRIT_CHANCE) * 100, EntityAttributeModifier.Operation.ADDITION));
                    cir.setReturnValue(ImmutableMultimap.copyOf(mutableMultimap));
                }
            }
        }
    }
}
