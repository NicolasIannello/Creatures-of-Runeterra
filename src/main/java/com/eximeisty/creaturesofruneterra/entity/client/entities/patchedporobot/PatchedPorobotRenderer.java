package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PatchedPorobotRenderer extends GeoEntityRenderer<PatchedPorobotEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/porobot.png");

    public PatchedPorobotRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PatchedPorobotModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PatchedPorobotEntity entity) {
        return rl;
    }

    @Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (bone.getName().equals("itemdispenser")){
			stack.push();
			stack.rotate(Vector3f.XP.rotationDegrees(80));
			stack.rotate(Vector3f.YP.rotationDegrees(170));
			stack.rotate(Vector3f.ZP.rotationDegrees(-45));
			stack.translate(-0.30D, -0.10D, 0.65D);
			stack.scale(0.50f, 0.50f, 0.50f);
			Minecraft.getInstance().getItemRenderer().renderItem(mainHand, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
			stack.pop();
			bufferIn = rtb.getBuffer(RenderType.getEntityTranslucent(whTexture));
		}
        if (bone.getName().equals("itemupgrade")){
			stack.push();
			stack.rotate(Vector3f.XP.rotationDegrees(80));
			stack.rotate(Vector3f.YP.rotationDegrees(170));
			stack.rotate(Vector3f.ZP.rotationDegrees(-45));
			stack.translate(-0.03D, -0.08D, 0.3D);
			stack.scale(1f, 1f, 1f);
			Minecraft.getInstance().getItemRenderer().renderItem(offHand, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
			stack.pop();
			bufferIn = rtb.getBuffer(RenderType.getEntityTranslucent(whTexture));
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}