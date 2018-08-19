package com.sasha.xdolf.mixins.client;

import com.sasha.xdolf.module.ModuleManager;
import com.sasha.xdolf.module.modules.ModuleTracers;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Sasha on 09/08/2018 at 7:37 PM
 **/
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    /*
    @Inject(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 3), cancellable = true)
    public void orientCamera(float partialTicks, CallbackInfo info) {
        if (ModuleManager.getModuleByName("CameraClip").isEnabled()) {
            double d3 = (double)(this.thirdPersonDistancePrev + (4.0F - this.thirdPersonDistancePrev) * partialTicks);
            info.cancel();
        }
    }
*/
    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;matrixMode(I)V", ordinal = 4))
    public void renderWorldPass$0(int pass, float partialTicks, long finishTimeNano, CallbackInfo info){
        if (ModuleManager.moduleRegistry.get(1).isEnabled()) {
            GL11.glPushAttrib(1048575);
            GL11.glEnable(32823);
            GL11.glPolygonOffset(-1.0f, -1.0f);
            GL11.glLineWidth(1.0f);
            GL11.glPolygonMode(1032, 6913);
        }
    }
    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;shadeModel(I)V", ordinal = 1))
    public void renderWorldPass$1(int pass, float partialTicks, long finishTimeNano, CallbackInfo info){
        if (ModuleManager.moduleRegistry.get(1).isEnabled()) {
            GL11.glPopAttrib();
        }
    }
    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand(FI)V"))
    public void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo info){
        ModuleManager.renderModules();
        GL11.glColor4f(0f,0f,0f,0f);
    }
    @Inject(method = "applyBobbing", at = @At("HEAD"), cancellable = true)
    public void applyBobbing(float partialTicks, CallbackInfo info){
        if (ModuleTracers.i > 0){
            info.cancel();
            //todo make tracers bob against camera so that we dont need to do this
        }
    }
}
