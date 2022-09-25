package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

public class PoroRenderer extends ExtendedGeoEntityRenderer<PoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/poro.png");

    public PoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PoroEntity entity) {
        return rl;
    }

	@Override
	protected ItemStack getHeldItemForBone(String boneName, PoroEntity currentEntity) {
		if(boneName=="item_bone") return mainHand;
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		if(boneName=="item_bone") return TransformType.THIRD_PERSON_RIGHT_HAND;
		return TransformType.NONE;
	}

	@Override
	protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, PoroEntity currentEntity, IBone bone) {
		if (item == this.mainHand) {
			//stack.push();
			stack.rotate(Vector3f.XP.rotationDegrees(-75));
			stack.rotate(Vector3f.YP.rotationDegrees(0));
			stack.rotate(Vector3f.ZP.rotationDegrees(0));
			stack.translate(0.2D, 0.4D, 0D);
			//stack.pop();
		}
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, PoroEntity currentEntity, IBone bone) { }

	@Override
	protected ItemStack getArmorForBone(String boneName, PoroEntity currentEntity) {
		if(boneName=="armor_head") return helmet;
		return null;
	}

	@Override
	protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, PoroEntity currentEntity) {
		switch (boneName) {
			case "item_bone":
				return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			case "armor_head":
				return EquipmentSlotType.HEAD;
			default:
				return null;
		}
	}

	@Override
	protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
		switch (name) {
			case "item_bone":
				return armorModel.bipedRightArm;
			case "armor_head":
				return armorModel.bipedHead;
			default:
				return null;
		}
	}

	@Override
	protected BlockState getHeldBlockForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Override
	protected ResourceLocation getTextureForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected boolean isArmorBone(GeoBone bone) { return bone.getName().startsWith("armor"); }

    // @Override
	// public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	// 	if (bone.getName().equals("item_bone")){
	// 		stack.push();
	// 		stack.rotate(Vector3f.XP.rotationDegrees(-75));
	// 		stack.rotate(Vector3f.YP.rotationDegrees(0));
	// 		stack.rotate(Vector3f.ZP.rotationDegrees(0));
	// 		stack.translate(0.2D, 0.4D, 0D);
	// 		stack.scale(1.0f, 1.0f, 1.0f);
	// 		Minecraft.getInstance().getItemRenderer().renderItem(mainHand, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
	// 		stack.pop();
	// 		bufferIn = rtb.getBuffer(RenderType.getEntityTranslucent(whTexture));
	// 	}
	// 	super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	// }
}