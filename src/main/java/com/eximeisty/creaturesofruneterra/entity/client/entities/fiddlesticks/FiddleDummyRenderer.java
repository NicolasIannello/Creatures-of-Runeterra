package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleDummyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FiddleDummyRenderer extends GeoEntityRenderer<FiddleDummyEntity> {

	@SuppressWarnings("unchecked")
    public FiddleDummyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FiddleDummyModel());
        //this.shadowRadius = 0.3f;
        this.addLayer(new LanternEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FiddleDummyEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks-normal.png");
    }

    @Override
	public void renderEarly(FiddleDummyEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }

    @Override
    protected float getDeathMaxRotation(FiddleDummyEntity entityLivingBaseIn) {
		return 0.0F;
	}
}