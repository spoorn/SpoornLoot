package org.spoorn.spoornloot.mixin;

import static org.spoorn.spoornloot.util.SpoornUtil.AttributeInfo;
import static org.spoorn.spoornloot.util.SpoornUtil.ENTITY_ATTRIBUTES;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private static final Logger log = LogManager.getLogger("ItemStackMixin");

    @Shadow @Nullable public abstract CompoundTag getTag();

    /**
     * Adds my attribute modifiers when ItemStack.getAttributeModifiers() is called.  This is to make sure the
     * Tooltip is rendered correctly with the new attributes.
     */
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
            if (compoundTag != null) {
                Multimap<EntityAttribute, EntityAttributeModifier> multimap = cir.getReturnValue();
                Multimap<EntityAttribute, EntityAttributeModifier> mutableMultimap = LinkedListMultimap.create(multimap);
                for (Map.Entry<EntityAttribute, AttributeInfo> entry : ENTITY_ATTRIBUTES.entrySet()) {
                    AttributeInfo attributeInfo = entry.getValue();
                    if (!multimap.containsKey(entry.getKey()) && compoundTag.contains(attributeInfo.getTagName())) {
                        float modifiedAttribute =
                            attributeInfo.getModifierFunction().apply(compoundTag.getFloat(attributeInfo.getTagName()));
                        //log.info("Adding attribute [{}]={}", attributeInfo.getTagName(), modifiedAttribute);
                        mutableMultimap.put(
                            entry.getKey(),
                            new EntityAttributeModifier(UUID.randomUUID(), attributeInfo.getName(),
                                    modifiedAttribute, EntityAttributeModifier.Operation.ADDITION));
                    }
                }
                cir.setReturnValue(ImmutableMultimap.copyOf(mutableMultimap));
            }
        }
    }
}
