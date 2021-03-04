package org.spoorn.spoornloot.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornloot.item.swords.BaseSpoornSwordItem;

import java.lang.reflect.Field;
import java.util.List;

@Log4j2
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

   /* @Shadow public abstract Item getItem();

    @Inject(method="getTooltip", at=@At("TAIL"), cancellable = true)
    public void getTooltipTail(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        try {
            if (this.getItem() instanceof BaseSpoornSwordItem) {
                log.error("### YES");
                List<Text> textList = cir.getReturnValue();
                Text text = textList.get(0);
                TextColor textColor = text.getStyle().getColor();
                Field rgbField = textColor.getClass().getDeclaredField("rgb");
                rgbField.setAccessible(true);
                rgbField.setInt(textColor, 16761035);
                rgbField.setAccessible(false);
                cir.setReturnValue(textList);
            } else {
                log.error("### NO");
                List<Text> textList = cir.getReturnValue();
                Text text = textList.get(0);
                TextColor textColor = text.getStyle().getColor();
                Field rgbField = textColor.getClass().getDeclaredField("rgb");
                rgbField.setAccessible(true);
                rgbField.setInt(textColor, 0);
                rgbField.setAccessible(false);
                cir.setReturnValue(textList);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Could not set rgb field for TextColor to be accessible");
        }
    }

    @Inject(method="toHoverableText", at=@At("TAIL"), cancellable = true)
    public void toHoverableTextTail(CallbackInfoReturnable<Text> cir) {
        log.error("### HERE 2");
        Text text = cir.getReturnValue();
        Style style = text.getStyle().withColor(TextColor.fromRgb(16761035));
        cir.setReturnValue(text);
        //style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(this));
    }*/
}
