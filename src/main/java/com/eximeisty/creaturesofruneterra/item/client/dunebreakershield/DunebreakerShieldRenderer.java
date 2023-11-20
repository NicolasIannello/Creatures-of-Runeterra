package com.eximeisty.creaturesofruneterra.item.client.dunebreakershield;

import com.eximeisty.creaturesofruneterra.item.custom.DunebreakerShield;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

public class DunebreakerShieldRenderer extends GeoItemRenderer<DunebreakerShield>{

    public DunebreakerShieldRenderer() {
        super(new DunebreakerShieldModel());
    }

//    @Override
//    public void render(DunebreakerShield animatable, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
//                       ItemStack stack) {
//        this.currentItemStack = stack;
//        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(animatable));
//        AnimationEvent animationEvent = new AnimationEvent(animatable, 0, 0, Minecraft.getInstance().getFrameTime(),
//                false, Collections.singletonList(stack));
//        this.dispatchedMat = poseStack.last().pose().copy();
//        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
//        this.modelProvider.setCustomAnimations(animatable, getInstanceId(animatable), animationEvent);
//        poseStack.pushPose();
//        poseStack.translate(0, 0.01f, 0);
//        poseStack.translate(0.5, 0.5, 0.5);
//        RenderSystem.setShaderTexture(0, getTextureLocation(animatable));
//        Color renderColor = getRenderColor(animatable, 0, poseStack, bufferSource, null, packedLight);
//        RenderType renderType = RenderType.itemEntityTranslucentCull(getTextureLocation(animatable));
//        render(model, animatable, 0, renderType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY,
//                renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f,
//                renderColor.getAlpha() / 255f);
//        poseStack.popPose();
//    }
}
