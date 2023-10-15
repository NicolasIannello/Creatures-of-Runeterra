package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class XerSaiDunebreakerRenderer extends GeoEntityRenderer<XerSaiDunebreakerEntity> {

    public XerSaiDunebreakerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new XerSaiDunebreakerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
    public void renderEarly(XerSaiDunebreakerEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }
}