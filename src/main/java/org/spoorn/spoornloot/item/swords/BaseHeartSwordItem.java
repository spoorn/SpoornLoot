package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
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

@Log4j2
abstract class BaseHeartSwordItem extends BaseSpoornSwordItem {

    public BaseHeartSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, SpoornRarity spoornRarity,
        Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, spoornRarity, settings);
        registerHitMechanics();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient() && stack.getItem() instanceof HeartPurpleSwordItem) {
            if (!user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                user.getItemCooldownManager().set(stack.getItem(), 600);
                world.playSound(
                        null,
                        user.getBlockPos(),
                        SpoornSoundsUtil.SM_THEME_SOUND,
                        SoundCategory.PLAYERS,
                        0.6f,
                        1f
                );

                int r = ModConfig.get().serverConfig.heartSwordCharmRadius;
                Box box = new Box(user.getX()-r, user.getY()-r, user.getZ()-r,
                        user.getX()+r, user.getY()+r, user.getZ()+r);
                world.getOtherEntities(user, box, entity ->
                        SpoornUtil.isLivingEntity(entity)
                        && (ModConfig.get().serverConfig.shouldCharmAffectPlayers || !(entity instanceof PlayerEntity))
                ).forEach(entity -> {
                    // Register Spoorn TrackedData at runtime if it doesn't exist yet
                    if (!SpoornEntityDataUtil.containsTrackedData(entity.getClass())) {
                        SpoornEntityDataUtil.registerSpoornEntityTrackedData(entity);
                    }

                    // Start tracking the new Spoorn TrackedData if it isn't already
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
                    entity.getDataTracker().set(SpoornEntityDataUtil.getTrackedData(
                            entity.getClass()), spoornTrackedData);
                    SpoornUtil.spawnHeartParticles(world, entity);
                });

                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    private void registerHitMechanics() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getMainHandStack();
            boolean rightSituation = (stack.getItem() instanceof BaseHeartSwordItem) && SpoornUtil.isLivingEntity(entity);
            if (rightSituation && !world.isClient() && stack.getItem() instanceof HeartPurpleSwordItem) {
                CompoundTag compoundTag = SpoornUtil.getButDontCreateSpoornCompoundTag(stack);
                long currTime = world.getTime();
                if (!compoundTag.contains(SpoornUtil.LAST_SPOORN_SOUND)
                    || compoundTag.getFloat(SpoornUtil.LAST_SPOORN_SOUND) + 80 < currTime) {
                    world.playSound(
                            null,
                            player.getBlockPos(),
                            SpoornSoundsUtil.SM_WAND_SOUND,
                            SoundCategory.PLAYERS,
                            0.3f,
                            1f
                    );
                    compoundTag.putFloat(SpoornUtil.LAST_SPOORN_SOUND, currTime);
                }

                SpoornUtil.spawnHeartParticles(world, entity);
            }
            return ActionResult.PASS;
        });
    }
}
