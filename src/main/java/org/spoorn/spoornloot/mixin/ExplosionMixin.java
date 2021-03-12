package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spoorn.spoornloot.util.SpoornUtil;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow public abstract DamageSource getDamageSource();

    @Redirect(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/entity/Entity;isImmuneToExplosion()Z"))
    public boolean playerImmuneToSpoornExplosion(Entity entity) {
        if (SpoornUtil.SPOORN_DMG_SRC.getName().equals(this.getDamageSource().getName())
            && entity instanceof PlayerEntity) {
            return true;
        }
        return entity.isImmuneToExplosion();
    }
}
