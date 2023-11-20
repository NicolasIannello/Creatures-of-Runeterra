package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FiddlesticksRenderer extends GeoEntityRenderer<FiddlesticksEntity> {

	@SuppressWarnings("unchecked")
    public FiddlesticksRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FiddlesticksModel());
        //this.shadowRadius = 0.3f;
		this.addLayer(new FiddlesticksEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FiddlesticksEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks-normal.png");
    }

    @Override
	public void renderEarly(FiddlesticksEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }

    @Override
    protected float getDeathMaxRotation(FiddlesticksEntity entityLivingBaseIn) {
		return 0.0F;
	}
}