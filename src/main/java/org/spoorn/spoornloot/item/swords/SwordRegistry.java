package org.spoorn.spoornloot.item.swords;

import com.google.common.collect.Multimap;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
        registerCritCallback();
        registerLightningCallback();
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

    // Fetch crit data from NBT and apply crit damage
    private static void registerCritCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getMainHandStack().getItem();
            if (item instanceof BaseSpoornSwordItem && entity instanceof LivingEntity) {
                BaseSpoornSwordItem spoornSwordItem = (BaseSpoornSwordItem) item;
                CompoundTag compoundTag = player.getMainHandStack().getTag();
                if (compoundTag == null || !compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
                    log.error("Could not find CritChance data on Spoorn Sword.  This should not happen!");
                    return ActionResult.PASS;
                }
                float critChance = compoundTag.getFloat(SpoornUtil.CRIT_CHANCE);
                float randFloat = new Random().nextFloat();
                //log.error("### critchance={}", critChance);
                if (randFloat < critChance) {
                    AtomicReference<Float> damage = new AtomicReference<>(0.0f);
                    Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers =
                            spoornSwordItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
                    if (attributeModifiers.containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                        attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                                .forEach((entityAttributeModifier -> {
                                    damage.updateAndGet(v -> new Float((float)(v + entityAttributeModifier.getValue())));
                                    //log.error("### updating with={}", entityAttributeModifier.getValue());
                                }));
                    }
                    // Damage shown on tooltip is player + item + enchantments damage.  Got this from ItemStack.java
                    float playerDamage = (float)player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    float enchantmentDamage = EnchantmentHelper.getAttackDamage(player.getMainHandStack(), EntityGroup.DEFAULT);
                    entity.damage(DamageSource.player(player), (damage.get() + playerDamage + enchantmentDamage) * 1.5f);
                }
            }
            return ActionResult.PASS;
        });
    }

    // Fetch lightning data from NBT and apply lightning
    private static void registerLightningCallback() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getMainHandStack().getItem();
            if (item instanceof BaseSpoornSwordItem && entity instanceof LivingEntity) {
                CompoundTag compoundTag = player.getMainHandStack().getTag();
                if (compoundTag == null || !compoundTag.contains(SpoornUtil.LIGHTNING_AFFINITY)) {
                    log.warn("Could not find LightningAffinity data on Spoorn Sword.");
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
