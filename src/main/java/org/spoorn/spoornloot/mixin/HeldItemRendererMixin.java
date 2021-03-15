package org.spoorn.spoornloot.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.spoornloot.util.SpoornUtil;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    private int counter = 0;

    @Shadow protected abstract void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta,
        float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light);

    @Shadow private float prevEquipProgressMainHand;

    @Shadow private float equipProgressMainHand;

    /**
     * Render dual wield swings in first person for Spoorn weapons.
     */
    @Inject(method = "renderFirstPersonItem", at = @At(value = "HEAD"), cancellable = true)
    public void renderOffArmFirstPersonSwing(AbstractClientPlayerEntity player, float tickDelta, float pitch,
        Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        swingProgress = player.getHandSwingProgress(tickDelta);
        Item mainHandItem = player.getMainHandStack().getItem();
        if (hand == Hand.OFF_HAND) {
            equipProgress = 1.0F - MathHelper.lerp(tickDelta, this.prevEquipProgressMainHand, this.equipProgressMainHand);
        }
        if ((swingProgress > 0 || equipProgress > 0) && hand == Hand.OFF_HAND
            && SpoornUtil.isDualWieldableCombo(mainHandItem, item.getItem())) {

            // counter is to prevent infinite recursion
            if (this.counter == 0) {
                this.counter++;
                // Replace off hand render with same swing as main hand
                this.renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item,
                        equipProgress, matrices, vertexConsumers, light);
                ci.cancel();
            } else {
                this.counter = 0;
            }
        }
    }
}
