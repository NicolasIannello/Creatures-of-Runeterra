package com.eximeisty.creaturesofruneterra.entity.client.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import net.minecraft.client.renderer.RenderType;
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
        stack.scale(4.5F, 4.5F, 4.5F);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}