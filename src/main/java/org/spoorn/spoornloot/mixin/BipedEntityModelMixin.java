package org.spoorn.spoornloot.mixin;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

    @Shadow public float leaningPitch;

    /*@ModifyVariable(method = "setAngles", name = "n", at = @At(value = "STORE"))
    public float replaceArm(float n) {
        return this.leaningPitch;
    }*/

    // This is for Third Person model
    /*@ModifyVariable(method = "method_29353", at = @At(value = "STORE"))
    public Arm replaceArm(Arm arm) {
        return Arm.LEFT;
    }*/
}
