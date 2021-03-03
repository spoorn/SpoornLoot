package org.spoorn.spoornloot;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.item.KikoSwordItem;
import org.spoorn.spoornloot.item.SpoornSwordItem;
import org.spoorn.spoornloot.util.SpoornUtil;

@Log4j2
public class SpoornLoot implements ModInitializer {

    public static final Item SPOORN_SWORD_ITEM = new SpoornSwordItem(ToolMaterials.DIAMOND, 20, 2.4f,
        new FabricItemSettings().group(SpoornUtil.SPOORN_ITEM_GROUP));
    public static final Item KIKO_SWORD_ITEM = new KikoSwordItem();

    private static final Identifier SHEEP_ID = new Identifier("minecraft", "blocks/coal_ore");

    @Override
    public void onInitialize() {
        log.info("Hello mod from SpoornLoot!");
        Registry.register(Registry.ITEM, SpoornSwordItem.IDENTIFIER, SPOORN_SWORD_ITEM);
        Registry.register(Registry.ITEM, KikoSwordItem.IDENTIFIER, KIKO_SWORD_ITEM);

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (id.getPath().contains("entities/sheep/white")) {
                log.error("### register loot pool for {}", id);
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                    .rolls(ConstantLootTableRange.create(1))
                    .withCondition(RandomChanceLootCondition.builder(0.5f).build())
                    .withEntry(ItemEntry.builder(SPOORN_SWORD_ITEM).weight(1).build())
                    .withEntry(ItemEntry.builder(KIKO_SWORD_ITEM).weight(1).build());

                supplier.withPool(poolBuilder.build());
            }
        });
    }
}
