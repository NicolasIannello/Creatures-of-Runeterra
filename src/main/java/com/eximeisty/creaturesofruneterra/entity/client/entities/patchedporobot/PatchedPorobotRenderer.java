package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PatchedPorobotRenderer extends GeoEntityRenderer<PatchedPorobotEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/porobot.png");

    public PatchedPorobotRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PatchedPorobotModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PatchedPorobotEntity entity) {
        return rl;
    }

    @Override
	public void renderRecursively(PoseStack poseStack, PatchedPorobotEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (bone.getName().equals("itemdispenser")){
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(80));
			poseStack.mulPose(Axis.YP.rotationDegrees(170));
			poseStack.mulPose(Axis.ZP.rotationDegrees(-45));
			poseStack.translate(-0.30D, -0.10D, 0.65D);
			poseStack.scale(0.50f, 0.50f, 0.50f);
			BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.MAIN_HAND), animatable.level(), animatable, animatable.getId());
			Minecraft.getInstance().getItemRenderer().render(animatable.getItemInHand(InteractionHand.MAIN_HAND), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, isReRender, poseStack, bufferSource, packedLight, packedOverlay, bakedmodel);
			poseStack.popPose();
			buffer = bufferSource.getBuffer(RenderType.entityTranslucent(rl));
		}
        if (bone.getName().equals("itemupgrade")){
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(80));
			poseStack.mulPose(Axis.YP.rotationDegrees(170));
			poseStack.mulPose(Axis.ZP.rotationDegrees(-45));
			poseStack.translate(-0.03D, -0.08D, 0.3D);
			poseStack.scale(1f, 1f, 1f);
			BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.OFF_HAND), animatable.level(), animatable, animatable.getId());
			Minecraft.getInstance().getItemRenderer().render(animatable.getItemInHand(InteractionHand.OFF_HAND), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, isReRender, poseStack, bufferSource, packedLight, packedOverlay, bakedmodel);
			poseStack.popPose();
			buffer = bufferSource.getBuffer(RenderType.entityTranslucent(rl));
		}
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}