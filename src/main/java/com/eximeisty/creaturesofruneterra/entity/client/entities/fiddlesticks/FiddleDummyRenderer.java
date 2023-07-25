package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleDummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class FiddleDummyRenderer extends GeoEntityRenderer<FiddleDummyEntity> {

	@SuppressWarnings("unchecked")
    public FiddleDummyRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FiddleDummyModel());
        //this.shadowRadius = 0.3f;
        this.addLayer(new LanternEmissiveLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture(FiddleDummyEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks-normal.png");
    }

    @Override
	public void renderEarly(FiddleDummyEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }

    @Override
    protected float getDeathMaxRotation(FiddleDummyEntity entityLivingBaseIn) {
		return 0.0F;
	}
}