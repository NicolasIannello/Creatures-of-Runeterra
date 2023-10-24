package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleDummyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FiddleDummyRenderer extends GeoEntityRenderer<FiddleDummyEntity> {

	@SuppressWarnings("unchecked")
    public FiddleDummyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FiddleDummyModel());
        //this.shadowRadius = 0.3f;
        addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            public void render(PoseStack poseStack, FiddleDummyEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                RenderType emissiveRenderType = getRenderType(animatable);
                poseStack.pushPose();
                poseStack.scale(0.67F, 0.67F, 0.67F);
                poseStack.translate(-0.0005d, -0.0115d, 0.004);
                getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, emissiveRenderType,
                        bufferSource.getBuffer(emissiveRenderType), partialTick, 15728640, OverlayTexture.NO_OVERLAY,
                        1, 1, 1, 1);
                poseStack.popPose();
            }
        });    }

    @Override
    public ResourceLocation getTextureLocation(FiddleDummyEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticksdummy.png");
    }

    @Override
    public void preRender(PoseStack poseStack, FiddleDummyEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(1.5F, 1.5F, 1.5F);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(FiddleDummyEntity entityLivingBaseIn) {
		return 0.0F;
	}
}