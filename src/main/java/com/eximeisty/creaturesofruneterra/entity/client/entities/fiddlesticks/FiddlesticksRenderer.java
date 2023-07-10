package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class FiddlesticksRenderer extends GeoEntityRenderer<FiddlesticksEntity> {

    public FiddlesticksRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FiddlesticksModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getEntityTexture(FiddlesticksEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks.png");
    }

    @Override
	public void renderEarly(FiddlesticksEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }

    @Override
    protected float getDeathMaxRotation(FiddlesticksEntity entityLivingBaseIn) {
		return 0.0F;
	}
}