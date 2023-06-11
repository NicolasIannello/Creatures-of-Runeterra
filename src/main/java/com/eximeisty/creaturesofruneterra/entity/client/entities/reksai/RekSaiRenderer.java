package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class RekSaiRenderer extends GeoEntityRenderer<RekSaiEntity> {

    public RekSaiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new RekSaiModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getEntityTexture(RekSaiEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/reksai.png");
    }

    @Override
	public RenderType getRenderType(RekSaiEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}

    @Override
	public void renderEarly(RekSaiEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(4.5F, 4.5F, 4.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }

    @Override
    protected float getDeathMaxRotation(RekSaiEntity entityLivingBaseIn) {
		return 0.0F;
	}

    @Override
    public boolean shouldRender(RekSaiEntity livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        if (!livingEntityIn.isInRangeToRender3d(camX, camY, camZ)) {
            return false;
        } else if (livingEntityIn.ignoreFrustumCheck) {
            return true;
        } else {
            return true;
        }
    }
}