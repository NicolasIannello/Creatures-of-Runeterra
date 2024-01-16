package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriHoundEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NaafiriHoundRenderer extends GeoEntityRenderer<NaafiriHoundEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri_spawn.png");

    public NaafiriHoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NaafiriHoundModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriHoundEntity entity) {
        return TEXTURE;
    }

    @Override
    public void renderEarly(NaafiriHoundEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        poseStack.scale(0.8F, 0.8F, 0.8F);
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
    }
}