package com.eximeisty.creaturesofruneterra.entity.model;

import com.eximeisty.creaturesofruneterra.entity.custom.XerxarethEntity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports

public class XerxarethModel <T extends XerxarethEntity> extends EntityModel<T> {

	private final ModelRenderer root;
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer point_r1;
	private final ModelRenderer headright_r1;
	private final ModelRenderer headleft_r1;
	private final ModelRenderer jaw;
	private final ModelRenderer jawpoint_r1;
	private final ModelRenderer jawright_r1;
	private final ModelRenderer jawleft_r1;
	private final ModelRenderer armleft;
	private final ModelRenderer finger1;
	private final ModelRenderer claw_r1;
	private final ModelRenderer claw2_r1;
	private final ModelRenderer finger2;
	private final ModelRenderer claw_r2;
	private final ModelRenderer claw2_r2;
	private final ModelRenderer finger3;
	private final ModelRenderer claw_r3;
	private final ModelRenderer claw2_r3;
	private final ModelRenderer armright;
	private final ModelRenderer finger4;
	private final ModelRenderer claw_r4;
	private final ModelRenderer claw2_r4;
	private final ModelRenderer finger5;
	private final ModelRenderer claw_r5;
	private final ModelRenderer claw2_r5;
	private final ModelRenderer finger6;
	private final ModelRenderer claw_r6;
	private final ModelRenderer claw2_r6;
	private final ModelRenderer leftleg;
	private final ModelRenderer legleft3_r1;
	private final ModelRenderer legleft2_r1;
	private final ModelRenderer legleft_r1;
	private final ModelRenderer leftleg2;
	private final ModelRenderer legleft3_r2;
	private final ModelRenderer legleft2_r2;
	private final ModelRenderer legleft_r2;
	private final ModelRenderer rightleg;
	private final ModelRenderer legleft3_r3;
	private final ModelRenderer legleft2_r3;
	private final ModelRenderer legleft_r3;
	private final ModelRenderer rightleg2;
	private final ModelRenderer legleft3_r4;
	private final ModelRenderer legleft2_r4;
	private final ModelRenderer legleft_r4;

	public XerxarethModel() {
		textureWidth = 512;
		textureHeight = 512;

		root = new ModelRenderer(this);
		root.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 0.0F, 0.0F);
		root.addChild(body);
		body.setTextureOffset(282, 331).addBox(-20.0F, -66.0F, -84.0F, 40.0F, 30.0F, 20.0F, 0.0F, false);
		body.setTextureOffset(0, 97).addBox(-32.0F, -68.0F, -64.0F, 64.0F, 50.0F, 44.0F, 0.0F, false);
		body.setTextureOffset(176, 151).addBox(-30.0F, -66.0F, -20.0F, 60.0F, 46.0F, 40.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-28.0F, -64.0F, 20.0F, 56.0F, 42.0F, 55.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -43.0F, 0.0F);
		root.addChild(head);
		head.setTextureOffset(104, 237).addBox(-9.0478F, -24.0F, -141.0F, 17.0F, 16.0F, 62.0F, 0.0F, false);

		point_r1 = new ModelRenderer(this);
		point_r1.setRotationPoint(0.0F, -10.0F, -83.4358F);
		head.addChild(point_r1);
		setRotationAngle(point_r1, 0.0F, -0.7854F, 0.0F);
		point_r1.setTextureOffset(0, 191).addBox(-47.0F, -14.0F, -46.2642F, 12.0F, 16.0F, 12.0F, 0.0F, false);

		headright_r1 = new ModelRenderer(this);
		headright_r1.setRotationPoint(0.0F, -10.0F, -83.4358F);
		head.addChild(headright_r1);
		setRotationAngle(headright_r1, 0.0F, 1.3526F, 0.0F);
		headright_r1.setTextureOffset(324, 109).addBox(-6.0478F, -14.0F, -21.2642F, 60.0F, 16.0F, 14.0F, 0.0F, false);

		headleft_r1 = new ModelRenderer(this);
		headleft_r1.setRotationPoint(0.0F, -10.0F, -83.4358F);
		head.addChild(headleft_r1);
		setRotationAngle(headleft_r1, 0.0F, -1.3265F, 0.0F);
		headleft_r1.setTextureOffset(134, 331).addBox(-54.0F, -14.0F, -21.5642F, 60.0F, 16.0F, 14.0F, 0.0F, false);

		jaw = new ModelRenderer(this);
		jaw.setRotationPoint(0.0F, 19.0F, 0.0F);
		head.addChild(jaw);
		jaw.setTextureOffset(202, 255).addBox(-9.0478F, -23.0F, -141.0F, 17.0F, 16.0F, 60.0F, 0.0F, false);

		jawpoint_r1 = new ModelRenderer(this);
		jawpoint_r1.setRotationPoint(0.0F, -9.0F, -83.4358F);
		jaw.addChild(jawpoint_r1);
		setRotationAngle(jawpoint_r1, 0.0F, -0.7854F, 0.0F);
		jawpoint_r1.setTextureOffset(0, 0).addBox(-47.0F, -14.0F, -46.2642F, 12.0F, 16.0F, 12.0F, 0.0F, false);

		jawright_r1 = new ModelRenderer(this);
		jawright_r1.setRotationPoint(0.0F, -9.0F, -83.4358F);
		jaw.addChild(jawright_r1);
		setRotationAngle(jawright_r1, 0.0F, 1.3526F, 0.0F);
		jawright_r1.setTextureOffset(281, 56).addBox(-6.0478F, -14.0F, -21.2642F, 60.0F, 16.0F, 14.0F, 0.0F, false);

		jawleft_r1 = new ModelRenderer(this);
		jawleft_r1.setRotationPoint(0.0F, -9.0F, -83.4358F);
		jaw.addChild(jawleft_r1);
		setRotationAngle(jawleft_r1, 0.0F, -1.3265F, 0.0F);
		jawleft_r1.setTextureOffset(0, 315).addBox(-54.0F, -14.0F, -21.5642F, 60.0F, 16.0F, 14.0F, 0.0F, false);

		armleft = new ModelRenderer(this);
		armleft.setRotationPoint(0.0F, -43.0F, 0.0F);
		root.addChild(armleft);
		armleft.setTextureOffset(376, 139).addBox(32.0F, -20.0F, -57.0F, 15.0F, 30.0F, 30.0F, 0.0F, false);
		armleft.setTextureOffset(296, 237).addBox(36.0F, -19.0F, -56.0F, 51.0F, 28.0F, 28.0F, 0.0F, false);
		armleft.setTextureOffset(0, 191).addBox(61.0F, -18.0F, -96.0F, 26.0F, 26.0F, 57.0F, 0.0F, false);
		armleft.setTextureOffset(114, 361).addBox(63.0F, -15.0F, -117.0F, 22.0F, 20.0F, 35.0F, 0.0F, false);

		finger1 = new ModelRenderer(this);
		finger1.setRotationPoint(74.0F, -5.0F, -95.5F);
		armleft.addChild(finger1);
		finger1.setTextureOffset(203, 30).addBox(-16.0F, -8.0F, -14.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r1 = new ModelRenderer(this);
		claw_r1.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger1.addChild(claw_r1);
		setRotationAngle(claw_r1, 0.0F, 0.0F, 0.1309F);
		claw_r1.setTextureOffset(0, 219).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r1 = new ModelRenderer(this);
		claw2_r1.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger1.addChild(claw2_r1);
		setRotationAngle(claw2_r1, 0.0F, 0.0F, -1.2217F);
		claw2_r1.setTextureOffset(0, 127).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		finger2 = new ModelRenderer(this);
		finger2.setRotationPoint(58.0F, -5.0F, -105.5F);
		armleft.addChild(finger2);
		setRotationAngle(finger2, 0.0F, -1.5708F, 0.0F);
		finger2.setTextureOffset(135, 191).addBox(-16.0F, -8.0F, -14.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r2 = new ModelRenderer(this);
		claw_r2.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger2.addChild(claw_r2);
		setRotationAngle(claw_r2, 0.0F, 0.0F, 0.1309F);
		claw_r2.setTextureOffset(137, 217).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r2 = new ModelRenderer(this);
		claw2_r2.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger2.addChild(claw2_r2);
		setRotationAngle(claw2_r2, 0.0F, 0.0F, -1.2217F);
		claw2_r2.setTextureOffset(0, 117).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		finger3 = new ModelRenderer(this);
		finger3.setRotationPoint(58.0F, -5.0F, -105.5F);
		armleft.addChild(finger3);
		setRotationAngle(finger3, 0.0F, -1.5708F, 0.0F);
		finger3.setTextureOffset(109, 191).addBox(-16.0F, -8.0F, -25.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r3 = new ModelRenderer(this);
		claw_r3.setRotationPoint(-13.5F, -4.0F, -21.5F);
		finger3.addChild(claw_r3);
		setRotationAngle(claw_r3, 0.0F, 0.0F, 0.1309F);
		claw_r3.setTextureOffset(109, 217).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r3 = new ModelRenderer(this);
		claw2_r3.setRotationPoint(-13.5F, -4.0F, -21.5F);
		finger3.addChild(claw2_r3);
		setRotationAngle(claw2_r3, 0.0F, 0.0F, -1.2217F);
		claw2_r3.setTextureOffset(0, 107).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		armright = new ModelRenderer(this);
		armright.setRotationPoint(-170.0F, -43.0F, 0.0F);
		root.addChild(armright);
		armright.setTextureOffset(372, 351).addBox(123.0F, -20.0F, -57.0F, 15.0F, 30.0F, 30.0F, 0.0F, false);
		armright.setTextureOffset(281, 0).addBox(81.0F, -19.0F, -56.0F, 51.0F, 28.0F, 28.0F, 0.0F, false);
		armright.setTextureOffset(172, 40).addBox(81.0F, -18.0F, -94.0F, 26.0F, 26.0F, 57.0F, 0.0F, false);
		armright.setTextureOffset(0, 345).addBox(83.0F, -15.0F, -117.0F, 22.0F, 20.0F, 35.0F, 0.0F, false);

		finger4 = new ModelRenderer(this);
		finger4.setRotationPoint(94.0F, -5.0F, -115.5F);
		armright.addChild(finger4);
		setRotationAngle(finger4, 0.0F, 3.1416F, 0.0F);
		finger4.setTextureOffset(185, 38).addBox(-16.0F, -8.0F, -14.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r4 = new ModelRenderer(this);
		claw_r4.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger4.addChild(claw_r4);
		setRotationAngle(claw_r4, 0.0F, 0.0F, 0.1309F);
		claw_r4.setTextureOffset(137, 207).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r4 = new ModelRenderer(this);
		claw2_r4.setRotationPoint(-13.5F, -4.0F, -10.5F);
		finger4.addChild(claw2_r4);
		setRotationAngle(claw2_r4, 0.0F, 0.0F, -1.2217F);
		claw2_r4.setTextureOffset(0, 97).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		finger5 = new ModelRenderer(this);
		finger5.setRotationPoint(58.0F, -5.0F, -105.5F);
		armright.addChild(finger5);
		setRotationAngle(finger5, 0.0F, -1.5708F, 0.0F);
		finger5.setTextureOffset(172, 123).addBox(-16.0F, -8.0F, -34.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r5 = new ModelRenderer(this);
		claw_r5.setRotationPoint(-13.5F, -4.0F, -30.5F);
		finger5.addChild(claw_r5);
		setRotationAngle(claw_r5, 0.0F, 0.0F, 0.1309F);
		claw_r5.setTextureOffset(109, 207).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r5 = new ModelRenderer(this);
		claw2_r5.setRotationPoint(-13.5F, -4.0F, -30.5F);
		finger5.addChild(claw2_r5);
		setRotationAngle(claw2_r5, 0.0F, 0.0F, -1.2217F);
		claw2_r5.setTextureOffset(0, 38).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		finger6 = new ModelRenderer(this);
		finger6.setRotationPoint(58.0F, -5.0F, -105.5F);
		armright.addChild(finger6);
		setRotationAngle(finger6, 0.0F, -1.5708F, 0.0F);
		finger6.setTextureOffset(167, 30).addBox(-16.0F, -8.0F, -45.5F, 5.0F, 8.0F, 8.0F, 0.0F, false);

		claw_r6 = new ModelRenderer(this);
		claw_r6.setRotationPoint(-13.5F, -4.0F, -41.5F);
		finger6.addChild(claw_r6);
		setRotationAngle(claw_r6, 0.0F, 0.0F, 0.1309F);
		claw_r6.setTextureOffset(198, 123).addBox(-10.5F, -2.5F, -2.5F, 9.0F, 5.0F, 5.0F, 0.0F, false);

		claw2_r6 = new ModelRenderer(this);
		claw2_r6.setRotationPoint(-13.5F, -4.0F, -41.5F);
		finger6.addChild(claw2_r6);
		setRotationAngle(claw2_r6, 0.0F, 0.0F, -1.2217F);
		claw2_r6.setTextureOffset(0, 28).addBox(-12.5F, -14.5F, -2.5F, 16.0F, 5.0F, 5.0F, 0.0F, false);

		leftleg = new ModelRenderer(this);
		leftleg.setRotationPoint(-89.5F, -52.0F, -126.0F);
		root.addChild(leftleg);
		

		legleft3_r1 = new ModelRenderer(this);
		legleft3_r1.setRotationPoint(165.8993F, 25.5981F, 135.5F);
		leftleg.addChild(legleft3_r1);
		setRotationAngle(legleft3_r1, 0.0F, 0.0F, 1.2217F);
		legleft3_r1.setTextureOffset(413, 411).addBox(-1.5F, -2.5F, -5.5F, 25.0F, 13.0F, 13.0F, 0.0F, false);

		legleft2_r1 = new ModelRenderer(this);
		legleft2_r1.setRotationPoint(143.6147F, 35.0F, 136.5F);
		leftleg.addChild(legleft2_r1);
		setRotationAngle(legleft2_r1, 0.0F, 0.0F, -0.5236F);
		legleft2_r1.setTextureOffset(0, 400).addBox(-6.5F, -10.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		legleft_r1 = new ModelRenderer(this);
		legleft_r1.setRotationPoint(118.5F, 20.5F, 136.5F);
		leftleg.addChild(legleft_r1);
		setRotationAngle(legleft_r1, 0.0F, 0.0F, 0.5236F);
		legleft_r1.setTextureOffset(313, 411).addBox(-5.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		leftleg2 = new ModelRenderer(this);
		leftleg2.setRotationPoint(-89.5F, -52.0F, -126.0F);
		root.addChild(leftleg2);
		

		legleft3_r2 = new ModelRenderer(this);
		legleft3_r2.setRotationPoint(165.8993F, 25.5981F, 176.5F);
		leftleg2.addChild(legleft3_r2);
		setRotationAngle(legleft3_r2, 0.0F, 0.0F, 1.2217F);
		legleft3_r2.setTextureOffset(411, 0).addBox(-1.5F, -2.5F, -5.5F, 25.0F, 13.0F, 13.0F, 0.0F, false);

		legleft2_r2 = new ModelRenderer(this);
		legleft2_r2.setRotationPoint(143.6147F, 35.0F, 177.5F);
		leftleg2.addChild(legleft2_r2);
		setRotationAngle(legleft2_r2, 0.0F, 0.0F, -0.5236F);
		legleft2_r2.setTextureOffset(376, 199).addBox(-6.5F, -10.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		legleft_r2 = new ModelRenderer(this);
		legleft_r2.setRotationPoint(118.5F, 20.5F, 177.5F);
		leftleg2.addChild(legleft_r2);
		setRotationAngle(legleft_r2, 0.0F, 0.0F, 0.5236F);
		legleft_r2.setTextureOffset(228, 396).addBox(-5.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		rightleg = new ModelRenderer(this);
		rightleg.setRotationPoint(-238.5F, -52.0F, -126.0F);
		root.addChild(rightleg);
		

		legleft3_r3 = new ModelRenderer(this);
		legleft3_r3.setRotationPoint(165.8993F, 25.5981F, 176.5F);
		rightleg.addChild(legleft3_r3);
		setRotationAngle(legleft3_r3, 0.0F, 0.0F, -1.2217F);
		legleft3_r3.setTextureOffset(382, 323).addBox(-20.5F, -4.5F, -5.5F, 25.0F, 13.0F, 13.0F, 0.0F, false);

		legleft2_r3 = new ModelRenderer(this);
		legleft2_r3.setRotationPoint(213.9269F, 18.5407F, 177.5F);
		rightleg.addChild(legleft2_r3);
		setRotationAngle(legleft2_r3, 0.0F, 0.0F, -0.5236F);
		legleft2_r3.setTextureOffset(356, 293).addBox(-32.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		legleft_r3 = new ModelRenderer(this);
		legleft_r3.setRotationPoint(190.0885F, 32.9641F, 177.5F);
		rightleg.addChild(legleft_r3);
		setRotationAngle(legleft_r3, 0.0F, 0.0F, 0.5236F);
		legleft_r3.setTextureOffset(193, 366).addBox(-30.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		rightleg2 = new ModelRenderer(this);
		rightleg2.setRotationPoint(-238.5F, -52.0F, -168.0F);
		root.addChild(rightleg2);
		

		legleft3_r4 = new ModelRenderer(this);
		legleft3_r4.setRotationPoint(165.8993F, 25.5981F, 176.5F);
		rightleg2.addChild(legleft3_r4);
		setRotationAngle(legleft3_r4, 0.0F, 0.0F, -1.2217F);
		legleft3_r4.setTextureOffset(216, 123).addBox(-20.5F, -4.5F, -5.5F, 25.0F, 13.0F, 13.0F, 0.0F, false);

		legleft2_r4 = new ModelRenderer(this);
		legleft2_r4.setRotationPoint(213.9269F, 18.5407F, 177.5F);
		rightleg2.addChild(legleft2_r4);
		setRotationAngle(legleft2_r4, 0.0F, 0.0F, -0.5236F);
		legleft2_r4.setTextureOffset(167, 0).addBox(-32.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

		legleft_r4 = new ModelRenderer(this);
		legleft_r4.setRotationPoint(190.0885F, 32.9641F, 177.5F);
		rightleg2.addChild(legleft_r4);
		setRotationAngle(legleft_r4, 0.0F, 0.0F, 0.5236F);
		legleft_r4.setTextureOffset(0, 274).addBox(-30.5F, -7.5F, -7.5F, 35.0F, 15.0F, 15.0F, 0.0F, false);

	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		//matrixStack.scale(0.5F, 0.5F, 0.5F);

		root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleX = headPitch * ((float)Math.PI / 180F);
        this.head.rotateAngleY = netHeadYaw * ((float)Math.PI / 180F);
	}
}