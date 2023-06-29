package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RekSaiRenderer extends GeoEntityRenderer<RekSaiEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/reksai.png");

    public RekSaiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RekSaiModel());
        //this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(RekSaiEntity entity) {
        return TEXTURE;
    }

    @Override
    public void preRender(PoseStack poseStack, RekSaiEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(4.5F, 4.5F, 4.5F);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(RekSaiEntity entityLivingBaseIn) {
		return 0.0F;
	}

    @Override
    public boolean shouldRender(RekSaiEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (!livingEntityIn.shouldRender(camX, camY, camZ)) {
            return false;
        } else if (livingEntityIn.noCulling) {
            return true;
        } else {
            return true;
        }
    }
}