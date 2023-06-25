package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class XerSaiDunebreakerRenderer extends GeoEntityRenderer<XerSaiDunebreakerEntity> {

    public XerSaiDunebreakerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new XerSaiDunebreakerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
    public void preRender(PoseStack poseStack, XerSaiDunebreakerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(1.5F, 1.5F, 1.5F);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}