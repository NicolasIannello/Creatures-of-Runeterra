package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NaafiriRenderer extends GeoEntityRenderer<NaafiriEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");

    public NaafiriRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NaafiriModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(PoseStack poseStack, NaafiriEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.9F, 0.9F, 0.9F);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}