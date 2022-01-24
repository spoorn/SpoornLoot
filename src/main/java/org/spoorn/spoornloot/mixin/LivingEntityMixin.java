package org.spoorn.spoornloot.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.entity.SpoornEntityDataUtil;
import org.spoorn.spoornloot.entity.SpoornTrackedData;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.Optional;

/**
 * Execute charm on affected entities.  Server side only.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private static final Logger log = LogManager.getLogger("SpoornLivingEntityMixin");

    /**
     * Redirect entity velocity towards Charm owner if entity is charmed.
     */
    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"))
    public void redirectCharmedVelocity(LivingEntity livingEntity, double x, double y, double z) {
        boolean shouldCharmAffectPlayers = ModConfig.get().serverConfig.shouldCharmAffectPlayers;
        World world = livingEntity.getEntityWorld();
        if (!world.isClient() && (shouldCharmAffectPlayers || !(livingEntity instanceof PlayerEntity))) {
            Optional<SpoornTrackedData> optional = Optional.empty();
            if (SpoornEntityDataUtil.containsTrackedData(livingEntity.getClass())
                && SpoornEntityDataUtil.entityDataTrackerHasSpoornData(livingEntity)) {
                optional = livingEntity.getDataTracker().get(SpoornEntityDataUtil.getTrackedData(livingEntity.getClass()));
            }

            if (optional.isPresent()) {
                SpoornTrackedData spoornTrackedData = optional.get();
                //System.out.println(spoornTrackedData);
                if ((world.getTime() - spoornTrackedData.getLastCharmTickTime())
                        <= (ModConfig.get().serverConfig.charmEffectDuration * 20)) {
                    PlayerEntity player = world.getPlayerByUuid(spoornTrackedData.getCharmOwnerUUID());
                    if (player == null) {
                        log.error("Entity [{}] is charmed by playerUUID=[{}], but player doesn't exist!",
                            livingEntity, spoornTrackedData.getCharmOwnerUUID());
                        // Default
                        livingEntity.setVelocity(x, y, z);
                    } else {
                        Vec3d playerPos = player.getPos();
                        Vec3d entityPos = livingEntity.getPos();
                        Vec3d charmedVelocity;
                        if (playerPos.distanceTo(entityPos) <= 3) {
                            charmedVelocity = new Vec3d(0, 0, 0);
                        } else {
                            charmedVelocity = playerPos.subtract(entityPos).normalize().multiply(0.05, 0, 0.05);
                            if (livingEntity.horizontalCollision) {
                                // Jump if colliding
                                charmedVelocity = charmedVelocity.add(0, 0.2D, 0);
                            } else {
                                // Keep the original Y velocity
                                charmedVelocity = charmedVelocity.add(0, y, 0);
                            }
                        }
                        //System.out.println("original vel: (" + x + "," + y + "," + z);
                        //System.out.println("new vel: (" + charmedVelocity.getX() + "," + charmedVelocity.getY() + "," + charmedVelocity.getZ());
                        //System.out.println("collision? h=" + livingEntity.horizontalCollision + ",v=" + livingEntity.verticalCollision);

                        // Update entity's velocity, head and body yaw rotations
                        livingEntity.setVelocity(charmedVelocity);
                        setRotation(livingEntity, entityPos, playerPos);

                        // Only spawn heart particles every 20 ticks or else there will be too many
                        if (world.getTime() % 20 == 0) {
                            SpoornUtil.spawnHeartParticles(world, livingEntity);
                        }
                    }
                }
            } else {
                // Default
                livingEntity.setVelocity(x, y, z);
            }
        }
    }

    // Equivalent logic to LivingEntity.lookAt with some customization
    private void setRotation(LivingEntity entity, Vec3d vec3d, Vec3d target) {
        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = (double)MathHelper.sqrt(d * d + f * f);
        float newPitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * (180.0/Math.PI))));
        float newYaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * (180.0/Math.PI)) - 90.0F);
        // TODO: Entities spin for some reason, tried different logic but still can't fix spinning
        //float yawChange = targetYaw - entity.yaw;
        //float pitchChange = targetPitch - entity.pitch;
        //yawChange = yawChange > 30 ? 30 : yawChange < -30 ? -30 : yawChange;
        //pitchChange = pitchChange > 30 ? 30 : pitchChange < -30 ? -30 : pitchChange;
        //float newYaw = targetYaw;
        //float newPitch = targetPitch;

        // Don't rotate if we're already about in the right direction
        if (Math.abs(Math.abs(newYaw) - Math.abs(entity.yaw)) > 5) {
            //System.out.println("oldYaw: " + entity.yaw);
            //System.out.println("newYaw: " + newYaw);
            entity.pitch = newPitch;
            entity.yaw = newYaw;
            entity.setHeadYaw(newYaw);

            // A little misleading, prev values are for the next iteration, not actually the previous iteration
            entity.prevPitch = entity.pitch;
            entity.prevYaw = entity.yaw;
            entity.prevHeadYaw = entity.headYaw;
            entity.bodyYaw = entity.headYaw;
            entity.prevBodyYaw = entity.bodyYaw;
        }
    }
}
