package org.spoorn.spoornloot.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;

@Log4j2
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow private ItemStack currentStack;

    // When hovering over an item in hand, update the text color as well
    @ModifyVariable(method="renderHeldItemTooltip", at=@At(value="STORE", ordinal = 0))
    public MutableText renderHeldItemToolTipTextOverride(MutableText mutableText) {
        Item item = this.currentStack.getItem();
        if (item instanceof BaseSpoornSwordItem) {
            BaseSpoornSwordItem baseSpoornSwordItem = (BaseSpoornSwordItem) item;
            Style newStyle = mutableText.getStyle().withColor(
                TextColor.fromRgb(baseSpoornSwordItem.getSpoornRarity().getColorValue()));
            mutableText.setStyle(newStyle);
        }
        return mutableText;
    }
}
