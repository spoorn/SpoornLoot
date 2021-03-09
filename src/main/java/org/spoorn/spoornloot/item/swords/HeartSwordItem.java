package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.SpoornUtil;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;
import org.spoorn.spoornloot.util.settings.SpoornItemSettings;

@Log4j2
public class HeartSwordItem extends BaseSpoornSwordItem {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "heart_sword");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.PINK;
    private static final ToolMaterial DEFAULT_TOOL_MATERIAL = new SpoornToolMaterial(DEFAULT_SPOORN_RARITY);
    private static final int DEFAULT_ATK_DMG = 8;
    private static final float DEFAULT_ATK_SPD = -2.2f;
    private static final Settings DEFAULT_SETTINGS = new SpoornItemSettings()
            .spoornRarity(DEFAULT_SPOORN_RARITY)
            .group(SPOORN_ITEM_GROUP)
            .rarity(Rarity.EPIC);

    public HeartSwordItem() {
        this(DEFAULT_TOOL_MATERIAL, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD, DEFAULT_SETTINGS);
    }

    public HeartSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, DEFAULT_SPOORN_RARITY, settings);
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
            boolean rightSituation = stack.getItem() instanceof HeartSwordItem && entity.isLiving();
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
