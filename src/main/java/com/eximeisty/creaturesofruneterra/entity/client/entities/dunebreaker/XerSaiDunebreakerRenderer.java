package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;

public class XerSaiDunebreakerRenderer extends GeoEntityRenderer<XerSaiDunebreakerEntity> {

    public XerSaiDunebreakerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new XerSaiDunebreakerModel());
    }

    @Override
    public ResourceLocation getEntityTexture(XerSaiDunebreakerEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
	public void renderEarly(XerSaiDunebreakerEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(1.5F, 1.5F, 1.5F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);    
    }
}