package com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class DBShieldRenderer extends GeoProjectilesRenderer<DBShieldEntity> {

    public DBShieldRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DBShieldModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DBShieldEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/dbshield.png");
    }

    @Override
    public void renderEarly(DBShieldEntity animatable, PoseStack poseStack,float partialTick, MultiBufferSource bufferSource,
                            VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue,
                            float alpha) {
        poseStack.scale(2.5F, 2.5F, 2.5F);
        poseStack.translate(0D, -0.5D, 0D);
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}