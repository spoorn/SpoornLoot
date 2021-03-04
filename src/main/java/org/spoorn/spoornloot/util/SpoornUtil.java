package org.spoorn.spoornloot.util;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spoorn.spoornloot.item.swords.SwordRegistry;

public final class SpoornUtil {

    public static final String MODID = "spoornloot";
    public static final String GENERAL = "general";
    public static final String COMBAT = "combat";
    public static final Identifier COMBAT_IDENTIFIER = new Identifier(MODID, GENERAL);
    public static final ItemGroup SPOORN_ITEM_GROUP = FabricItemGroupBuilder.create(COMBAT_IDENTIFIER)
        .icon(() -> new ItemStack(SwordRegistry.DEFAULT_SPOORN_SWORD))
        .build();
}
