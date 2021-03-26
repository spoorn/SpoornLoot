package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessorMixin {

    @Accessor("lastDamageTaken")
    public float getLastDamageTaken();
}
