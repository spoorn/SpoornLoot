package org.spoorn.spoornloot.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;
import org.spoorn.spoornloot.util.SpoornUtil;

import java.util.List;

@Log4j2
@Mixin(LootTable.class)
public class LootTableMixin {

    // We want to apply attributes to Spoorn Loot on the server side when the loot is generated.
    // There doesn't seem to be any other easy way to add this NBT data to the items server-side unless we do it here.
    // I tried adding it to the Item itself, Tooltip rendering, etc.  But they either only generated once in
    // the player's inventory, or client-side without syncing to server.
    // Loot generation is actually the perfect place as this can be extensible to all attributes and all loot.
    @Inject(method="generateLoot(Lnet/minecraft/loot/context/LootContext;)Ljava/util/List;", at=@At("TAIL"))
    public void addcritChanceToSpoornItems(LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> itemStacks = cir.getReturnValue();
        for (ItemStack stack : itemStacks) {
            if (stack.getItem() instanceof BaseSpoornSwordItem) {
                CompoundTag compoundTag = stack.getOrCreateTag();
                if (!compoundTag.contains(SpoornUtil.CRIT_CHANCE)) {
                    int i = 0;
                    // This mean and sd makes it so  there's a ~0.1% chance of getting above 90% crit chance
                    float critChance = (float) SpoornUtil.getNextGaussian(20, 23.2, 0, 100) / 100;
                    log.info("Setting sword crit chance to {} for stack {}", critChance, stack);
                    compoundTag.putFloat(SpoornUtil.CRIT_CHANCE, critChance);
                }
            }
        }
    }
}
