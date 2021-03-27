package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.explosion.Explosion;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.item.daggers.*;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.HashSet;
import java.util.Set;

@Log4j2
public class SwordRegistry {

    // All swords in this Set will be added to the Spoorn Loot Pool
    private static Set<BaseSpoornSwordItem> spoornSwords = new HashSet<>();

    // All swords
    public static final BaseSpoornSwordItem DEFAULT_SPOORN_SWORD = registerSword(SpoornSwordItem.IDENTIFIER, new SpoornSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_KIKO_SWORD = registerSword(KikoSwordItem.IDENTIFIER, new KikoSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_PINK_SWORD = registerSword(PinkSwordItem.IDENTIFIER, new PinkSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_CYAN_SWORD = registerSword(CyanSwordItem.IDENTIFIER, new CyanSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_DAISY_SWORD = registerSword(DaisySwordItem.IDENTIFIER, new DaisySwordItem());
    private static final BaseSpoornSwordItem DEFAULT_EAGLE_SWORD = registerSword(EagleSwordItem.IDENTIFIER, new EagleSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_GREEN_SWORD = registerSword(GreenSwordItem.IDENTIFIER, new GreenSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_HEART_SWORD = registerSword(HeartSwordItem.IDENTIFIER, new HeartSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_LANNISTER_SWORD = registerSword(LannisterSwordItem.IDENTIFIER, new LannisterSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_GREEN_SWORD2 = registerSword(GreenSword2Item.IDENTIFIER, new GreenSword2Item());
    private static final BaseSpoornSwordItem DEFAULT_HEART_PURPLE_SWORD = registerSword(HeartPurpleSwordItem.IDENTIFIER, new HeartPurpleSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_POCKY_MATCHA_SWORD = registerSword(PockyMatchaSwordItem.IDENTIFIER, new PockyMatchaSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_POCKY_STRAWBERRY_SWORD = registerSword(PockyStrawberrySwordItem.IDENTIFIER, new PockyStrawberrySwordItem());
    private static final BaseSpoornSwordItem DEFAULT_POCKY_SWORD = registerSword(PockySwordItem.IDENTIFIER, new PockySwordItem());
    private static final BaseSpoornSwordItem DEFAULT_RED_SWORD = registerSword(RedSwordItem.IDENTIFIER, new RedSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_SWAMP_SWORD = registerSword(SwampSwordItem.IDENTIFIER, new SwampSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_EAGLE2 = registerSword(EagleSword2Item.IDENTIFIER, new EagleSword2Item());
    private static final BaseSpoornSwordItem DEFAULT_POCKY_CARAMEL = registerSword(PockyCaramelSwordItem.IDENTIFIER, new PockyCaramelSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_POCKY_CHERRY = registerSword(PockyCherrySwordItem.IDENTIFIER, new PockyCherrySwordItem());
    private static final BaseSpoornSwordItem DEFAULT_GREENTEA = registerSword(PockyGreenTeaSwordItem.IDENTIFIER, new PockyGreenTeaSwordItem());
    private static final BaseSpoornSwordItem DEFAULT_CHARM = registerSword(CharmSword.IDENTIFIER, new CharmSword());
    private static final BaseSpoornSwordItem DEFAULT_CHARM_LIGHT = registerSword(CharmLightSword.IDENTIFIER, new CharmLightSword());
    private static final BaseSpoornSwordItem DEFAULT_CHARM_GRADIENT = registerSword(CharmGradientSword.IDENTIFIER, new CharmGradientSword());
    private static final BaseDagger DEFAULT_CYAN_DAGGER = registerSword(CyanDagger.IDENTIFIER, new CyanDagger());
    private static final BaseDagger DEFAULT_DAISY_DAGGER = registerSword(DaisyDagger.IDENTIFIER, new DaisyDagger());
    private static final BaseDagger DEFAULT_GREEN_DAGGER = registerSword(GreenDagger.IDENTIFIER, new GreenDagger());
    private static final BaseDagger DEFAULT_GREEN_DAGGER_2 = registerSword(GreenDagger2.IDENTIFIER, new GreenDagger2());
    private static final BaseDagger DEFAULT_KIKO_DAGGER = registerSword(KikoDagger.IDENTIFIER, new KikoDagger());
    private static final BaseDagger DEFAULT_RED_DAGGER = registerSword(RedDagger.IDENTIFIER, new RedDagger());
    private static final BaseDagger DEFAULT_SPOORN_DAGGER = registerSword(SpoornDagger.IDENTIFIER, new SpoornDagger());
    private static final BaseDagger DEFAULT_SPOORN_DAGGER_2 = registerSword(SpoornDagger2.IDENTIFIER, new SpoornDagger2());

    private static final Identifier LOOT_CHEST_ID = new Identifier("minecraft", "chests");

    public static void init() {
        initSwordLootPools();
        registerLightningCallback();
        registerExplosiveCallback();
        registerSoundEventsCallback();
    }

    private static <T extends BaseSpoornSwordItem> T registerSword(Identifier identifier, T item) {
        Registry.register(Registry.ITEM, identifier, item);
        spoornSwords.add(item);
        return item;
    }

    // Fetch lightning data from NBT and apply lightning
    private static void registerLightningCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getMainHandStack().getItem();
            if (SpoornUtil.isSpoornSwordItem(item) && SpoornUtil.isLivingEntity(entity)) {
                CompoundTag compoundTag = SpoornUtil.getButDontCreateSpoornCompoundTag(player.getMainHandStack());
                if (compoundTag == null || !compoundTag.contains(SpoornUtil.LIGHTNING_AFFINITY)) {
                    log.error("Could not find LightningAffinity data on Spoorn Sword.");
                    return ActionResult.PASS;
                }
                if (compoundTag.getBoolean(SpoornUtil.LIGHTNING_AFFINITY)) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningEntity.teleport(entity.getX(), entity.getY(), entity.getZ());
                    world.spawnEntity(lightningEntity);
                    //log.info("Lightning strike at [{}, {}, {}] from SpoornLoot",
                    //    entity.getX(), entity.getY(), entity.getZ());
                }
            }

            return ActionResult.PASS;
        });
    }

    // Fetch explosive data from NBT and apply explosion
    private static void registerExplosiveCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getMainHandStack().getItem();
            if (SpoornUtil.isSpoornSwordItem(item) && SpoornUtil.isLivingEntity(entity)) {
                CompoundTag compoundTag = SpoornUtil.getButDontCreateSpoornCompoundTag(player.getMainHandStack());
                if (compoundTag == null || !compoundTag.contains(SpoornUtil.EXPLOSIVE)) {
                    log.error("Could not find Explosive data on Spoorn Sword.");
                    return ActionResult.PASS;
                }
                if (compoundTag.getBoolean(SpoornUtil.EXPLOSIVE)) {
                    world.createExplosion(entity, SpoornUtil.SPOORN_DMG_SRC, null,
                        entity.getX(), entity.getY(), entity.getZ(), 2.0f, false, Explosion.DestructionType.NONE);
                }
            }

            return ActionResult.PASS;
        });
    }

    private static void registerSoundEventsCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient()) {
                Item item = player.getMainHandStack().getItem();
                if (SpoornUtil.isEnergySword(item) && SpoornUtil.isLivingEntity(entity)) {
                    world.playSound(
                        null,
                        player.getBlockPos(),
                        SpoornSoundsUtil.SPOORN_SWORD_HIT_CHAR_SOUND,
                        SoundCategory.PLAYERS,
                        0.4f,
                        1f
                    );
                }
            }
            return ActionResult.PASS;
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!world.isClient()) {
                Item item = player.getMainHandStack().getItem();
                if (SpoornUtil.isEnergySword(item)) {
                    world.playSound(
                            null,
                            player.getBlockPos(),
                            SpoornSoundsUtil.SPOORN_SWORD_HIT_ENV_SOUND,
                            SoundCategory.PLAYERS,
                            0.4f,
                            1f
                    );
                }
            }
            return ActionResult.PASS;
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getMainHandStack();
            boolean rightSituation = (stack.getItem() instanceof HeartPurpleSwordItem) && SpoornUtil.isLivingEntity(entity);
            if (rightSituation && !world.isClient()) {
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

    private static void initSwordLootPools() {
        float chance = 1.0f / ModConfig.get().serverConfig.swordSpawnInLootChestsChance;
        log.info("Spoorn sword spawn chance is {}", chance);
        FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootTableRange.create(1))
                .withCondition(RandomChanceLootCondition.builder(chance).build());
        for (BaseSpoornSwordItem item : spoornSwords) {
            builder.withEntry(ItemEntry.builder(item).weight(item.getSpoornRarity().getWeight()).build());
        }

        addToLootChests(builder.build());
    }

    private static void addToLootChests(LootPool lootPool) {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            String namespace = id.getNamespace();
            String path = id.getPath();
            if (LOOT_CHEST_ID.getNamespace().equals(namespace) && path != null && path.startsWith(LOOT_CHEST_ID.getPath())) {
                //log.info("### register loot pool for {}", id);
                supplier.withPool(lootPool);
            }
        });
    }
}
