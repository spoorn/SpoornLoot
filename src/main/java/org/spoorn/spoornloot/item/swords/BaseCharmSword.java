package org.spoorn.spoornloot.item.swords;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.entity.SpoornEntityDataUtil;
import org.spoorn.spoornloot.entity.SpoornTrackedData;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.SpoornUtil;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

import java.util.Optional;

public class BaseCharmSword extends BaseSpoornSwordItem {

    public BaseCharmSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, SpoornRarity spoornRarity, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, spoornRarity, settings);
    }

    // Convenience constructor
    public BaseCharmSword(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if (!user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                user.getItemCooldownManager().set(stack.getItem(), ModConfig.get().serverConfig.charmEffectCooldown * 20);

                int r = ModConfig.get().serverConfig.heartSwordCharmRadius;
                Box box = new Box(user.getX()-r, user.getY()-r, user.getZ()-r,
                        user.getX()+r, user.getY()+r, user.getZ()+r);
                world.getOtherEntities(user, box, entity ->
                        SpoornUtil.isLivingEntity(entity)
                                && (ModConfig.get().serverConfig.shouldCharmAffectPlayers || !(entity instanceof PlayerEntity))
                ).forEach(entity -> {
                    // Start tracking the new Spoorn TrackedData if it isn't already
                    TrackedData<Optional<SpoornTrackedData>> trackedData =
                            SpoornEntityDataUtil.startTrackingSpoornTrackedDataIfNotTracking(entity);

                    // Get TrackedData from entity if available, else create a new one
                    // **Note** The SpoornTrackedData should ALWAYS be available since we just started tracking above
                    Optional<SpoornTrackedData> spoornTrackedData =
                            SpoornEntityDataUtil.getSpoornTrackedDataOrCreate(entity);

                    // Code beyond this point assumes TrackedData is already available!!

                    // Set Charm owner UUID to entity data and current tick time
                    spoornTrackedData.get().setCharmOwnerUUID(user.getUuid());
                    spoornTrackedData.get().setLastCharmTickTime(world.getTime());

                    // Update entity data tracker with the new or changed SpoornTrackedData with new charm owner UUID
                    SpoornEntityDataUtil.setSpoornTrackedDataOnEntity(trackedData, spoornTrackedData, entity);
                    SpoornUtil.spawnHeartParticles(world, entity);
                });

                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }
}
