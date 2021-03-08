package org.spoorn.spoornloot.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.config.ModConfig;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.List;

@Mixin(LootTable.class)
public class LootTableMixin {

    private static final Logger log = LogManager.getLogger("LootTableMixin");

    /**
     * Add attributes to SpoornLoot ItemStack NBT when generated.
     *
     * We want to apply attributes to Spoorn Loot on the server side when the loot is generated.
     * There doesn't seem to be any other easy way to add this NBT data to the items server-side unless we do it here.
     * I tried adding it to the Item itself, Tooltip rendering, etc.  But they either only generated once in
     * the player's inventory, or client-side without syncing to server.
     * Loot generation is actually the perfect place as this can be extensible to all attributes and all loot.
     */
    @Inject(method="generateLoot(Lnet/minecraft/loot/context/LootContext;)Ljava/util/List;", at=@At("TAIL"))
    public void addAttributesToSpoornItems(LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> itemStacks = cir.getReturnValue();
        SpoornUtil.addSwordAttributes(itemStacks);
    }
}
