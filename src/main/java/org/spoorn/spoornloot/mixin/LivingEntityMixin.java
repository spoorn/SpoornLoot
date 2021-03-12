package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityMixin {

    @Accessor("lastDamageTaken")
    public float getLastDamageTaken();
}
