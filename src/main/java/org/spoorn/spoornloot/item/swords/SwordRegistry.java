package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.config.ModConfig;

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

    private static final Identifier LOOT_CHEST_ID = new Identifier("minecraft", "chests");

    private static Set<BaseSpoornSwordItem> spoornSwords;

    public static void init() {
        spoornSwords = new HashSet<>();
        registerSwords();
        initSwordLootPools();
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
    }

    private static void addSwordToRegistry(Identifier identifier, BaseSpoornSwordItem item) {
        Registry.register(Registry.ITEM, identifier, item);
        spoornSwords.add(item);
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
            if (id.getPath().contains("entities/sheep/white")
                    || (LOOT_CHEST_ID.getNamespace().equals(namespace) && path != null && path.startsWith(LOOT_CHEST_ID.getPath()))) {
                //log.info("### register loot pool for {}", id);
                supplier.withPool(lootPool);
            }
        });
    }
}
