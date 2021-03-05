package org.spoorn.spoornloot.item.swords;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Log4j2
public class SwordRegistry {

    public static final Item DEFAULT_SPOORN_SWORD = new SpoornSwordItem();
    public static final Item DEFAULT_KIKO_SWORD = new KikoSwordItem();
    public static final Item DEFAULT_PINK_SWORD = new PinkSwordItem();
    public static final Item DEFAULT_CYAN_SWORD = new CyanSwordItem();
    public static final Item DEFAULT_DAISY_SWORD = new DaisySwordItem();
    public static final Item DEFAULT_EAGLE_SWORD = new EagleSwordItem();
    public static final Item DEFAULT_GREEN_SWORD = new GreenSwordItem();
    public static final Item DEFAULT_HEART_SWORD = new HeartSwordItem();
    public static final Item DEFAULT_LANNISTER_SWORD = new LannisterSwordItem();

    private static final Identifier LOOT_CHEST_ID = new Identifier("minecraft", "chests");

    @Getter
    private LootPool swordLootPool;

    public SwordRegistry() {
        init();
    }

    public void addToLootChests() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            String namespace = id.getNamespace();
            String path = id.getPath();
            if (id.getPath().contains("entities/sheep/white")
                    || (LOOT_CHEST_ID.getNamespace().equals(namespace) && path != null && path.startsWith(LOOT_CHEST_ID.getPath()))) {
                log.error("### register loot pool for {}", id);
                supplier.withPool(this.getSwordLootPool());
            }
        });
    }

    private void init() {
        registerSwords();
        initSwordLootPool();
    }

    private void registerSwords() {
        Registry.register(Registry.ITEM, SpoornSwordItem.IDENTIFIER, DEFAULT_SPOORN_SWORD);
        Registry.register(Registry.ITEM, KikoSwordItem.IDENTIFIER, DEFAULT_KIKO_SWORD);
        Registry.register(Registry.ITEM, PinkSwordItem.IDENTIFIER, DEFAULT_PINK_SWORD);
        Registry.register(Registry.ITEM, CyanSwordItem.IDENTIFIER, DEFAULT_CYAN_SWORD);
        Registry.register(Registry.ITEM, DaisySwordItem.IDENTIFIER, DEFAULT_DAISY_SWORD);
        Registry.register(Registry.ITEM, EagleSwordItem.IDENTIFIER, DEFAULT_EAGLE_SWORD);
        Registry.register(Registry.ITEM, GreenSwordItem.IDENTIFIER, DEFAULT_GREEN_SWORD);
        Registry.register(Registry.ITEM, HeartSwordItem.IDENTIFIER, DEFAULT_HEART_SWORD);
        Registry.register(Registry.ITEM, LannisterSwordItem.IDENTIFIER, DEFAULT_LANNISTER_SWORD);
    }

    private void initSwordLootPool() {
        swordLootPool = FabricLootPoolBuilder.builder()
                .rolls(ConstantLootTableRange.create(1))
                .withCondition(RandomChanceLootCondition.builder(0.5f).build())
                .withEntry(ItemEntry.builder(DEFAULT_SPOORN_SWORD).build())
                .withEntry(ItemEntry.builder(DEFAULT_KIKO_SWORD).build())
                .withEntry(ItemEntry.builder(DEFAULT_PINK_SWORD).build())
                .build();
    }
}
