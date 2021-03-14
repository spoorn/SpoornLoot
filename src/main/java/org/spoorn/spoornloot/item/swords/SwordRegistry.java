package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
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
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.HashSet;
import java.util.Set;

@Log4j2
public class SwordRegistry {

    public static final BaseSpoornSwordItem DEFAULT_SPOORN_SWORD = new SpoornSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_KIKO_SWORD = new KikoSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_PINK_SWORD = new PinkSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_CYAN_SWORD = new CyanSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_DAISY_SWORD = new DaisySwordItem();
    private static final BaseSpoornSwordItem DEFAULT_EAGLE_SWORD = new EagleSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_GREEN_SWORD = new GreenSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_HEART_SWORD = new HeartSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_LANNISTER_SWORD = new LannisterSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_GREEN_SWORD2 = new GreenSword2Item();
    private static final BaseSpoornSwordItem DEFAULT_HEART_PURPLE_SWORD = new HeartPurpleSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_POCKY_MATCHA_SWORD = new PockyMatchaSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_POCKY_STRAWBERRY_SWORD = new PockyStrawberrySwordItem();
    private static final BaseSpoornSwordItem DEFAULT_POCKY_SWORD = new PockySwordItem();
    private static final BaseSpoornSwordItem DEFAULT_RED_SWORD = new RedSwordItem();
    private static final BaseSpoornSwordItem DEFAULT_SWAMP_SWORD = new SwampSwordItem();

    private static final Identifier LOOT_CHEST_ID = new Identifier("minecraft", "chests");

    private static Set<BaseSpoornSwordItem> spoornSwords;

    public static void init() {
        spoornSwords = new HashSet<>();
        registerSwords();
        initSwordLootPools();
        registerLightningCallback();
        registerExplosiveCallback();
        registerSoundEventsCallback();
    }

    private static void registerSwords() {
        addSwordToRegistry(SpoornSwordItem.IDENTIFIER, DEFAULT_SPOORN_SWORD);
        addSwordToRegistry(KikoSwordItem.IDENTIFIER, DEFAULT_KIKO_SWORD);
        addSwordToRegistry(PinkSwordItem.IDENTIFIER, DEFAULT_PINK_SWORD);
        addSwordToRegistry(CyanSwordItem.IDENTIFIER, DEFAULT_CYAN_SWORD);
        addSwordToRegistry(DaisySwordItem.IDENTIFIER, DEFAULT_DAISY_SWORD);
        addSwordToRegistry(EagleSwordItem.IDENTIFIER, DEFAULT_EAGLE_SWORD);
        addSwordToRegistry(GreenSwordItem.IDENTIFIER, DEFAULT_GREEN_SWORD);
        addSwordToRegistry(HeartSwordItem.IDENTIFIER, DEFAULT_HEART_SWORD);
        addSwordToRegistry(LannisterSwordItem.IDENTIFIER, DEFAULT_LANNISTER_SWORD);
        addSwordToRegistry(GreenSword2Item.IDENTIFIER, DEFAULT_GREEN_SWORD2);
        addSwordToRegistry(HeartPurpleSwordItem.IDENTIFIER, DEFAULT_HEART_PURPLE_SWORD);
        addSwordToRegistry(PockyMatchaSwordItem.IDENTIFIER, DEFAULT_POCKY_MATCHA_SWORD);
        addSwordToRegistry(PockyStrawberrySwordItem.IDENTIFIER, DEFAULT_POCKY_STRAWBERRY_SWORD);
        addSwordToRegistry(PockySwordItem.IDENTIFIER, DEFAULT_POCKY_SWORD);
        addSwordToRegistry(RedSwordItem.IDENTIFIER, DEFAULT_RED_SWORD);
        addSwordToRegistry(SwampSwordItem.IDENTIFIER, DEFAULT_SWAMP_SWORD);
    }

    private static void addSwordToRegistry(Identifier identifier, BaseSpoornSwordItem item) {
        Registry.register(Registry.ITEM, identifier, item);
        spoornSwords.add(item);
    }

    // Fetch lightning data from NBT and apply lightning
    private static void registerLightningCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getMainHandStack().getItem();
            if (SpoornUtil.isSpoornSwordItem(item) && entity.isLiving()) {
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
            if (SpoornUtil.isSpoornSwordItem(item) && entity.isLiving()) {
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
                if (item instanceof SpoornSwordItem && entity.isLiving()) {
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
                if (item instanceof SpoornSwordItem) {
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
