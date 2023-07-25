package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

@SuppressWarnings("rawtypes")
public class FiddlesticksEmissiveLayer extends GeoLayerRenderer {
    private static final ResourceLocation LAYER = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks-emissive.png");
    private static final ResourceLocation MODEL = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");

    @SuppressWarnings("unchecked")
    public FiddlesticksEmissiveLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.getEntityAlpha(LAYER, 0.5F);
        matrixStackIn.push();
        matrixStackIn.scale(0.67F, 0.67F, 0.67F);
        matrixStackIn.translate(-0.0005d, -0.0115d, 0.004);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn, bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.pop();
    }
}