package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SilverwingRenderer extends GeoEntityRenderer<SilverwingEntity> {

    public SilverwingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SilverwingModel());
        addRenderLayer(new SilverwingSaddleLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing.png");
    }

    @Override
    public void preRender(PoseStack poseStack, SilverwingEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float size = animatable.getEntityData().get(SilverwingEntity.SIZE);
        scaleModelForRender(size*0.25F, size*0.25F, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        //poseStack.scale(size*0.25F, size*0.25F, size*0.25F);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}