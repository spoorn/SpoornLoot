/*
package org.spoorn.spoornloot.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel> {

    @Shadow protected M model;

    @Shadow protected abstract float getAnimationCounter(T entity, float tickDelta);

    @Shadow protected abstract boolean isVisible(T entity);

    @Shadow @Nullable protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    private static final Logger log = LogManager.getLogger("LivingEntityRendererMixin");

    */
/*@ModifyVariable(method = "render", at = @At(value = "STORE"))
    public VertexConsumer render2(VertexConsumer vertexConsumer) {
        //log.error("vertexConsumer = {}", vertexConsumer);
        vertexConsumer.color(0, 0, 0, 1);
        return vertexConsumer;
    }*//*


    */
/*@Inject(method = "render", at=@At(value = "HEAD"))
    public void myRender(T livingEntity, float f, float g, MatrixStack matrixStack,
         VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        //log.error("test");
        RenderSystem.pushMatrix();
        RenderSystem.setupOverlayColor(() -> 5, 16);
        RenderSystem.popMatrix();
    }*//*

*/
/*
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void testRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        log.error("test");
    }*//*


    @Redirect(method = "getOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/OverlayTexture;getV(Z)I"))
    private static int overrideDamageOverlayColor(boolean hurt) {
        return hurt ? 3 : 10;
    }

    @Inject(method = "getOverlay", at = @At(value = "TAIL"), cancellable = true)
    private static void overrideOverlayColor(LivingEntity entity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
        log.error("old val: {}", cir.getReturnValue());
        int newVal = 262144;

        //log.error("new val: {}", newVal);
        cir.setReturnValue(newVal);
    }
}
*/
