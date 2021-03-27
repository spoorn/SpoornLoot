package org.spoorn.spoornloot.item.swords;

import static org.spoorn.spoornloot.util.SpoornUtil.MODID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spoorn.spoornloot.sounds.SpoornSoundsUtil;
import org.spoorn.spoornloot.util.rarity.SpoornRarity;

public class HeartPurpleSwordItem extends BaseCharmSword {

    public static final Identifier IDENTIFIER = new Identifier(MODID, "heart_purple_sword");

    private static final SpoornRarity DEFAULT_SPOORN_RARITY = SpoornRarity.PINK;
    private static final int DEFAULT_ATK_DMG = 8;
    private static final float DEFAULT_ATK_SPD = -2.2f;

    public HeartPurpleSwordItem() {
        this(DEFAULT_SPOORN_RARITY, DEFAULT_ATK_DMG, DEFAULT_ATK_SPD);
    }

    public HeartPurpleSwordItem(SpoornRarity spoornRarity, int attackDamage, float attackSpeed) {
        super(spoornRarity, attackDamage, attackSpeed);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient()) {
            if (!user.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                user.getItemCooldownManager().set(stack.getItem(), 600);

                // Only play song for the Heart Purple Sword
                world.playSound(
                        null,
                        user.getBlockPos(),
                        SpoornSoundsUtil.SM_THEME_SOUND,
                        SoundCategory.PLAYERS,
                        0.6f,
                        1f
                );

                return TypedActionResult.success(stack);
            }
        }

        return TypedActionResult.pass(stack);
    }
}
