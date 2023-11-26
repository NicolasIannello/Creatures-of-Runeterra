package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SilverwingRenderer extends GeoEntityRenderer<SilverwingEntity> {

    public SilverwingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SilverwingModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
    public void renderRecursively(PoseStack poseStack, SilverwingEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float size = animatable.getEntityData().get(SilverwingEntity.SIZE);
        poseStack.scale(size, size, size);

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}