package com.eximeisty.creaturesofruneterra.entity.model;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class XerSaiHatchlingModel <T extends XerSaiHatchlingEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_hatchling.png"), "main");
	private final ModelPart root;

	public XerSaiHatchlingModel(ModelPart root) {
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 2.0F, -12.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.0F, 2.0F, 4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.0F, 2.0F, -4.0F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spike6_r1 = body.addOrReplaceChild("spike6_r1", CubeListBuilder.create().texOffs(43, 41).addBox(0.0F, 6.0F, -9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(43, 41).addBox(0.0F, 8.0F, -6.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(43, 41).addBox(0.0F, 9.0F, 6.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(43, 41).addBox(0.0F, 9.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(43, 41).addBox(0.0F, 9.0F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 0.0F, 0.0F, 0.0053F, 0.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 9).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 5).addBox(2.0F, 1.0F, -4.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 46).addBox(-3.0F, 1.0F, -4.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(5, 9).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 22).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(2.0F, -2.0F, -4.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(6, 0).addBox(-2.0F, -2.0F, -4.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(-1, -1).addBox(2.0F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(-1, -1).addBox(-3.0F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(1, 1).addBox(-0.5F, -4.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.25F, -12.0F));

		PartDefinition jawhorn2_r1 = head.addOrReplaceChild("jawhorn2_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -3.8F, -1.4F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition horn3_r1 = head.addOrReplaceChild("horn3_r1", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(25, 28).addBox(-1.0F, 0.1F, -4.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition horn2_r1 = head.addOrReplaceChild("horn2_r1", CubeListBuilder.create().texOffs(43, 41).addBox(-0.5F, 1.35F, -1.09F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(7, 5).addBox(-2.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(9, 7).addBox(-5.0F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(9, 7).addBox(-6.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(8, 8).addBox(-6.5F, -5.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(-4.5F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(-6.5F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-5.5F, -5.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.5F, -6.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(1, 4).addBox(8.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(2, 5).addBox(10.0F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(5, 6).addBox(12.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(20, 21).addBox(11.0F, -5.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(13.0F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(11.0F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(12.0F, -5.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.5F, -6.0F));

		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(9, 7).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(36, 12).addBox(1.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 13).addBox(1.0F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 13).addBox(1.0F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 12).addBox(-2.0F, -0.5F, 3.0F, 1.0F, 1.0F, 1.1F, new CubeDeformation(0.0F))
				.texOffs(37, 13).addBox(-0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(37, 13).addBox(-0.5F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(36, 12).addBox(-0.5F, -2.0F, 3.0F, 1.0F, 1.0F, 1.1F, new CubeDeformation(0.0F))
				.texOffs(36, 12).addBox(-2.0F, -2.0F, 3.0F, 1.0F, 1.0F, 1.1F, new CubeDeformation(0.0F))
				.texOffs(37, 13).addBox(-2.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, 5.5F, 12.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 27).addBox(8.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(3, 5).addBox(10.0F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(4, 7).addBox(12.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(-1, -1).addBox(11.0F, -5.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(13.0F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(11.0F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(12.0F, -5.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.5F, 7.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(7, 5).addBox(-2.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(9, 7).addBox(-5.0F, 0.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(9, 7).addBox(-6.0F, -3.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(8, 6).addBox(-6.5F, -5.5F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(-4.5F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(-6.5F, -5.5F, -3.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(30, 52).addBox(-5.5F, -5.5F, 1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 5.5F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}