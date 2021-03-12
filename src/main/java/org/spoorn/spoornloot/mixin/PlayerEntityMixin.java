package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.util.SpoornUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    // If this is from a sword explosion, player doesn't take any damage
    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void invulnerableToSelfExplosive(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (SpoornUtil.SPOORN_DMG_SRC.getName().equals(damageSource.getName())) {
            cir.setReturnValue(true);
        }
    }
}
