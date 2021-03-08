package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import static org.spoorn.spoornloot.util.SpoornUtil.SPOORN_ITEM_GROUP;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
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
        registerSounds();
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

    private void registerSounds() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient()) {
                Item item = player.getMainHandStack().getItem();
                if (item instanceof HeartSwordItem && entity.isLiving()) {
                    world.playSound(
                        null,
                        player.getBlockPos(),
                        SpoornSoundsUtil.SM_WAND_SOUND,
                        SoundCategory.PLAYERS,
                        0.4f,
                        1f
                    );
                }
            }
            return ActionResult.PASS;
        });
    }
}
