package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NaafiriRenderer extends GeoEntityRenderer<NaafiriEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");

    public NaafiriRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NaafiriModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getEntityTexture(NaafiriEntity entity) {
        return TEXTURE;
    }

    @Override
    public void renderEarly(NaafiriEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        stackIn.scale(0.9F, 0.9F, 0.9F);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }
}