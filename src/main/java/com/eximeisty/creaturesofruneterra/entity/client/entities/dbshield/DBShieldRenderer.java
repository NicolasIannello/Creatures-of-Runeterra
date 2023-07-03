package com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DBShieldRenderer extends GeoEntityRenderer<DBShieldEntity> {

    public DBShieldRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DBShieldModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DBShieldEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/dbshield.png");
    }

    @Override
    public void preRender(PoseStack poseStack, DBShieldEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(2.5F, 2.5F, 2.5F);
        poseStack.translate(0D, -0.5D, 0D);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}