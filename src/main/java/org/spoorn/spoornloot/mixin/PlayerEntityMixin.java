package org.spoorn.spoornloot.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static final Logger log = LogManager.getLogger("SpoornPlayerEntityMixin");

    // If this is from a sword explosion, player doesn't take any damage
    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void invulnerableToSelfExplosive(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (SpoornUtil.SPOORN_DMG_SRC.getName().equals(damageSource.getName())) {
            cir.setReturnValue(true);
        }
    }

    // Fetch data from NBT and apply damage modifiers
    @ModifyVariable(method = "attack", ordinal = 0, at = @At(value = "STORE", ordinal = 0))
    public float modifyDamageFromSpoornLoot(float f, Entity target) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        float modifiedDamage = f;
        if (player instanceof ServerPlayerEntity) {
            World world = player.getEntityWorld();
            if (!world.isClient()) {
                boolean isLivingTarget = target instanceof LivingEntity;
                if (isLivingTarget) {
                    Item item = player.getMainHandStack().getItem();
                    // Main hand
                    if (SpoornUtil.isSpoornSwordItem(item)) {
                        CompoundTag compoundTag = SpoornUtil.getButDontCreateSpoornCompoundTag(player.getMainHandStack());
                        if (compoundTag != null) {
                            //log.info("Crit damage: {}", damage);
                            modifiedDamage += getFireDamage(target, compoundTag);
                            modifiedDamage += getColdDamage(target, compoundTag);
                            modifiedDamage += getCritDamage(modifiedDamage, compoundTag);
                        } else {
                            log.error("Got NULL compoundTag when trying to register Spoorn Attributes for item [{}]",
                                    player.getMainHandStack());
                        }
                    }

                    // Dual Wield damage
                    Item offHandItem = player.getOffHandStack().getItem();
                    if (SpoornUtil.isDualWieldableCombo(player.getMainHandStack(), player.getOffHandStack())) {
                        // At minimum, add base damage of offhand weapon
                        float offHandDamage = getBaseDamage(player, offHandItem);

                        // Additionally calculate Spoorn attributes for Spoorn items
                        if (SpoornUtil.isSpoornSwordItem(offHandItem)) {
                            CompoundTag offHandTag = SpoornUtil.getButDontCreateSpoornCompoundTag(player.getOffHandStack());
                            if (offHandTag != null) {
                                offHandDamage += getFireDamage(target, offHandTag);
                                offHandDamage += getColdDamage(target, offHandTag);
                                offHandDamage += getCritDamage(offHandDamage, offHandTag);
                            } else {
                                log.error("Got NULL offHandTag when trying to register Spoorn Attributes for item [{}]",
                                        player.getOffHandStack());
                            }
                        }
                        modifiedDamage += (offHandDamage * 1.0/ModConfig.get().serverConfig.dualWieldDamageScale);
                    }
                }
            }
        }
        //log.info("final modified damage: " + modifiedDamage);
        return modifiedDamage;
    }

    // Gets base damage
    private static float getBaseDamage(PlayerEntity player, Item item) {
        AtomicReference<Float> damage = new AtomicReference<>(0.0f);
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers =
                        item.getAttributeModifiers(EquipmentSlot.MAINHAND);
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
        return damage.get() + playerDamage + enchantmentDamage;
    }

    // Get bonus damage that comes from crit
    private static float getCritDamage(float baseDamage, CompoundTag compoundTag) {
        if (compoundTag == null || !compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
            log.error("Could not find CritChance data on Spoorn Sword.  This should not happen!");
            return 0;
        }
        float critChance = compoundTag.getFloat(SpoornUtil.CRIT_CHANCE);
        float randFloat = SpoornUtil.RANDOM.nextFloat();
        //log.error("### critchance={}", critChance);
        if (randFloat < critChance) {
            return baseDamage * 0.5f;
        }

        return 0;
    }

    // Get bonus damage from fire damage and set target on fire
    private static float getFireDamage(Entity entity, CompoundTag compoundTag) {
        if (compoundTag == null || !compoundTag.contains(SpoornUtil.FIRE_DAMAGE)) {
            log.error("Could not find FireDamage data on Spoorn Sword.  This should not happen!");
            return 0;
        }

        float fireDamage = compoundTag.getFloat(SpoornUtil.FIRE_DAMAGE);
        //log.info("Fire damage: {}", fireDamage);
        if (fireDamage > 0 && entity.isLiving()) {
            entity.setOnFireFor(5);
        }
        return fireDamage;
    }

    // Get bonus damage from cold damage and slow target
    private static float getColdDamage(Entity entity, CompoundTag compoundTag) {
        if (compoundTag == null || !compoundTag.contains(SpoornUtil.COLD_DAMAGE)) {
            log.error("Could not find ColdDamage data on Spoorn Sword.  This should not happen!");
            return 0;
        }

        float coldDamage = compoundTag.getFloat(SpoornUtil.COLD_DAMAGE);
        //log.info("Cold damage: {}", coldDamage);
        if (coldDamage > 0 && entity.isLiving()) {
            ((LivingEntity)entity).applyStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
        }
        return coldDamage;
    }
}
