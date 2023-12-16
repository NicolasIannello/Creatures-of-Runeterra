package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SilverwingRenderer extends GeoEntityRenderer<SilverwingEntity> {
    private static final ResourceLocation[][] VARIANT = {
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_white.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_sandy.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_brown.png")
            },
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_green.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_blue.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_brown.png")
            },
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_white.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_black.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_blue.png")
            }
    };

    public SilverwingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SilverwingModel());
        this.addLayer(new SilverwingSaddleLayer(this));
        this.addLayer(new SilverwingFeatherLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SilverwingEntity animatable) {
        return VARIANT[animatable.getBiome()][animatable.getVariant()];
    }

    @Override
    public void renderEarly(SilverwingEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        float size = animatable.getEntityData().get(SilverwingEntity.SIZE);
        //scaleModelForRender(size*0.25F, size*0.25F, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        poseStack.scale(size*0.25F, size*0.25F, size*0.25F);
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
    }
}