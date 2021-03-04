package org.spoorn.spoornloot.mixin;

import lombok.extern.log4j.Log4j2;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Mixin(TextColorMixin.class)
public abstract class TextColorMixin {

    /*@Inject(method="values", at=@At(value = "TAIL"), cancellable = true)
    private static void valuesTail(CallbackInfoReturnable<Formatting[]> cir) {
        Formatting[] originalFormatting = cir.getReturnValue();
        List<Formatting> formattingList = Arrays.asList(originalFormatting);
        try {
            Constructor<Formatting> formattingConstructor = Formatting.class.getConstructor(Formatting.class);
            Formatting pink = formattingConstructor.newInstance("PINK", 'g', 16, 16761035);
            formattingList.add(pink);
        } catch (NoSuchMethodException ex) {
            log.error("Couldn't get Formatting constructor", ex);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            log.error("Could not construct instance of Formatting", ex);
        }

        cir.setReturnValue((Formatting[]) formattingList.toArray());
    }*/

    /*@Accessor("rgb")
    public abstract void setRGB(int rgb);

    @Inject(method="<init>(I)V", at=@At(value = "TAIL"))
    public void constructorTail(int rgb, CallbackInfo ci) {
        setRGB(16761035);
    }

    @Inject(method="<init>(ILjava/lang/String;)V", at=@At(value = "TAIL"))
    public void constructorTail(int rgb, String name, CallbackInfo ci) {
        setRGB(16761035);
    }*/
}
