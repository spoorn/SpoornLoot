package org.spoorn.spoornloot.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnchantmentRegistry {

    public static final Enchantment DUAL_WIELD_ENCHANT =
        register(DualWieldEnchantment.DUAL_WIELD_ID, new DualWieldEnchantment());

    public static void init() {
        // Do nothing
    }

    private static <T extends Enchantment> T register(Identifier identifier, T enchantment) {
        return Registry.register(Registry.ENCHANTMENT, identifier, enchantment);
    }
}
