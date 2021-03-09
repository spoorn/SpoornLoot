package org.spoorn.spoornloot.item.swords;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.SpoornUtil;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

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
        if (!world.isClient()) {
            if (stack.getCooldown() == 0) {
                world.playSound(
                        null,
                        user.getBlockPos(),
                        SpoornSoundsUtil.SM_THEME_SOUND,
                        SoundCategory.PLAYERS,
                        0.6f,
                        1f
                );
                stack.setCooldown(600);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    private void registerHitMechanics() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack stack = player.getMainHandStack();
            boolean rightSituation = stack.getItem() instanceof BaseHeartSwordItem && entity.isLiving();
            if (rightSituation && !world.isClient()) {
                if (stack.getCooldown() == 0) {
                    world.playSound(
                            null,
                            player.getBlockPos(),
                            SpoornSoundsUtil.SM_WAND_SOUND,
                            SoundCategory.PLAYERS,
                            0.3f,
                            1f
                    );
                    stack.setCooldown(80);
                }
            } else if (rightSituation && world.isClient()) {
                for (int i = 0; i < 3; ++i) {
                    double d = SpoornUtil.RANDOM.nextGaussian() * 0.02D;
                    double e = SpoornUtil.RANDOM.nextGaussian() * 0.02D;
                    double f = SpoornUtil.RANDOM.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.HEART, entity.getParticleX(1.0D),
                            entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), d, e, f);
                }
            }
            return ActionResult.PASS;
        });
    }
}