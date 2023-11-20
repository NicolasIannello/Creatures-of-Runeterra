package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (bone.getName().equals("itemdispenser")){
			stack.pushPose();
			stack.mulPose(Vector3f.XP.rotationDegrees(80));
			stack.mulPose(Vector3f.YP.rotationDegrees(170));
			stack.mulPose(Vector3f.ZP.rotationDegrees(-45));
			stack.translate(-0.30D, -0.10D, 0.65D);
			stack.scale(0.50f, 0.50f, 0.50f);
			BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.MAIN_HAND), animatable.getLevel(), animatable, animatable.getId());
			Minecraft.getInstance().getItemRenderer().render(mainHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, true, stack, this.rtb, packedLightIn, packedOverlayIn, bakedmodel);
			stack.popPose();
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		}
        if (bone.getName().equals("itemupgrade")){
			stack.pushPose();
			stack.mulPose(Vector3f.XP.rotationDegrees(80));
			stack.mulPose(Vector3f.YP.rotationDegrees(170));
			stack.mulPose(Vector3f.ZP.rotationDegrees(-45));
			stack.translate(-0.03D, -0.08D, 0.3D);
			stack.scale(1f, 1f, 1f);
			BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.OFF_HAND), animatable.getLevel(), animatable, animatable.getId());
			Minecraft.getInstance().getItemRenderer().render(offHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, true, stack, this.rtb, packedLightIn, packedOverlayIn, bakedmodel);
			stack.popPose();
			bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}