package com.eximeisty.creaturesofruneterra.entity.model;

import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class XerSaiHatchlingModel <T extends XerSaiHatchlingEntity> extends EntityModel<T>{
	private final ModelRenderer root;
	private final ModelRenderer body;
	private final ModelRenderer spike6_r1;
	private final ModelRenderer head;
	private final ModelRenderer jawhorn2_r1;
	private final ModelRenderer horn3_r1;
	private final ModelRenderer horn2_r1;
	private final ModelRenderer left_arm;
	private final ModelRenderer right_arm;
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;
	private final ModelRenderer tail;

	public XerSaiHatchlingModel() {
		textureWidth = 64;
		textureHeight = 64;

		root = new ModelRenderer(this);
		root.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		root.addChild(body);
		body.setTextureOffset(24, 24).addBox(-4.0F, -9.0F, -12.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
		body.setTextureOffset(0, 17).addBox(-4.0F, -9.0F, 4.0F, 8.0F, 7.0F, 8.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-5.0F, -11.0F, -4.0F, 10.0F, 9.0F, 8.0F, 0.0F, false);

		spike6_r1 = new ModelRenderer(this);
		spike6_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(spike6_r1);
		setRotationAngle(spike6_r1, -0.3054F, 0.0F, 0.0F);
		spike6_r1.setTextureOffset(22, 51).addBox(-1.0F, -10.0F, -9.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		spike6_r1.setTextureOffset(26, 51).addBox(-1.0F, -8.0F, -13.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		spike6_r1.setTextureOffset(18, 51).addBox(-1.0F, -12.0F, -6.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		spike6_r1.setTextureOffset(48, 50).addBox(-1.0F, -13.0F, 6.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		spike6_r1.setTextureOffset(0, 51).addBox(-1.0F, -13.0F, 2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		spike6_r1.setTextureOffset(4, 51).addBox(-1.0F, -13.0F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -5.0F, -12.0F);
		root.addChild(head);
		head.setTextureOffset(28, 0).addBox(-2.0F, -3.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(38, 46).addBox(-3.0F, -3.0F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(44, 5).addBox(2.0F, -3.0F, -4.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(24, 17).addBox(-3.0F, 2.0F, -4.0F, 6.0F, 1.0F, 4.0F, 0.0F, false);
		head.setTextureOffset(28, 22).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 1.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(6, 0).addBox(-2.0F, 1.0F, -4.0F, 0.0F, 1.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addBox(2.0F, 1.0F, -4.0F, 0.0F, 1.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(48, 26).addBox(-3.0F, 0.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		head.setTextureOffset(48, 21).addBox(2.0F, 0.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
		head.setTextureOffset(44, 5).addBox(-0.5F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		jawhorn2_r1 = new ModelRenderer(this);
		jawhorn2_r1.setRotationPoint(0.0F, -1.0F, 0.0F);
		head.addChild(jawhorn2_r1);
		setRotationAngle(jawhorn2_r1, -0.48F, 0.0F, 0.0F);
		jawhorn2_r1.setTextureOffset(43, 39).addBox(-0.5F, 5.8F, -1.4F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		horn3_r1 = new ModelRenderer(this);
		horn3_r1.setRotationPoint(0.0F, -1.0F, 0.0F);
		head.addChild(horn3_r1);
		setRotationAngle(horn3_r1, 0.2618F, 0.0F, 0.0F);
		horn3_r1.setTextureOffset(48, 39).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		horn3_r1.setTextureOffset(18, 32).addBox(-1.0F, -5.1F, -4.0F, 2.0F, 5.0F, 1.0F, 0.0F, false);

		horn2_r1 = new ModelRenderer(this);
		horn2_r1.setRotationPoint(0.0F, -1.0F, 0.0F);
		head.addChild(horn2_r1);
		setRotationAngle(horn2_r1, -0.4363F, 0.0F, 0.0F);
		horn2_r1.setTextureOffset(24, 17).addBox(-0.5F, -4.35F, -6.34F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(4.0F, -5.0F, -8.0F);
		root.addChild(left_arm);
		left_arm.setTextureOffset(18, 39).addBox(0.0F, -3.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
		left_arm.setTextureOffset(48, 11).addBox(2.0F, -2.5F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		left_arm.setTextureOffset(10, 45).addBox(4.0F, -2.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		left_arm.setTextureOffset(44, 0).addBox(3.5F, 3.5F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		left_arm.setTextureOffset(52, 42).addBox(3.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		left_arm.setTextureOffset(42, 52).addBox(5.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		left_arm.setTextureOffset(36, 52).addBox(4.5F, 4.5F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		right_arm = new ModelRenderer(this);
		right_arm.setRotationPoint(4.0F, -5.0F, -8.0F);
		root.addChild(right_arm);
		right_arm.setTextureOffset(0, 39).addBox(-10.0F, -3.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
		right_arm.setTextureOffset(18, 47).addBox(-13.0F, -2.5F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		right_arm.setTextureOffset(30, 44).addBox(-14.0F, -2.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		right_arm.setTextureOffset(39, 41).addBox(-14.5F, 3.5F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		right_arm.setTextureOffset(30, 52).addBox(-14.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		right_arm.setTextureOffset(50, 5).addBox(-12.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		right_arm.setTextureOffset(52, 18).addBox(-13.5F, 4.5F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(-4.0F, -5.0F, 8.0F);
		root.addChild(right_leg);
		right_leg.setTextureOffset(36, 8).addBox(-2.0F, -3.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
		right_leg.setTextureOffset(0, 47).addBox(-5.0F, -2.5F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		right_leg.setTextureOffset(0, 17).addBox(-6.0F, -2.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		right_leg.setTextureOffset(40, 16).addBox(-6.5F, 3.5F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		right_leg.setTextureOffset(49, 15).addBox(-6.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		right_leg.setTextureOffset(42, 21).addBox(-4.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		right_leg.setTextureOffset(40, 0).addBox(-5.5F, 4.5F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(4.0F, -5.0F, 8.0F);
		root.addChild(left_leg);
		left_leg.setTextureOffset(10, 35).addBox(0.0F, -3.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
		left_leg.setTextureOffset(44, 46).addBox(2.0F, -2.5F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		left_leg.setTextureOffset(0, 0).addBox(4.0F, -2.5F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		left_leg.setTextureOffset(30, 39).addBox(3.5F, 3.5F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		left_leg.setTextureOffset(26, 39).addBox(3.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		left_leg.setTextureOffset(11, 32).addBox(5.5F, 4.5F, -3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		left_leg.setTextureOffset(24, 22).addBox(4.5F, 4.5F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setRotationPoint(0.0F, -6.0F, 12.0F);
		root.addChild(tail);
		tail.setTextureOffset(0, 32).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
		tail.setTextureOffset(39, 39).addBox(-2.0F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(0, 39).addBox(-2.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(36, 10).addBox(-2.0F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(36, 8).addBox(1.0F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(35, 22).addBox(-0.5F, -0.5F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(28, 2).addBox(-0.5F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(28, 0).addBox(-0.5F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(12, 43).addBox(1.0F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		tail.setTextureOffset(0, 41).addBox(1.0F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}