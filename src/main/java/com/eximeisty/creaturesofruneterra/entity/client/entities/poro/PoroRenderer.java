package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import javax.annotation.Nullable;

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

/*
 * "pivot": [0, 2.5, 0],
 * "origin": [-5, 4, -5],
 * "size": [8, 7, 8],
 *  "uv": [7, 27], "uv_size": [5, 5]
 */
public class PoroRenderer extends ExtendedGeoEntityRenderer<PoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/poro.png");

    public PoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PoroEntity entity) {
        return rl;
    }

	@Nullable
	@Override
	protected ItemStack getHeldItemForBone(String boneName, PoroEntity currentEntity) {
		if(boneName.equals("item_bone")) return mainHand;
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		if(boneName.equals("item_bone")) return TransformType.THIRD_PERSON_RIGHT_HAND;
		return TransformType.NONE;
	}

	@Override
	protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, PoroEntity currentEntity, IBone bone) {
		if (item == this.mainHand) {
			stack.rotate(Vector3f.XP.rotationDegrees(-75));
			stack.rotate(Vector3f.YP.rotationDegrees(0));
			stack.rotate(Vector3f.ZP.rotationDegrees(0));
			stack.translate(0D, 0.15D, 0D);
		}
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, PoroEntity currentEntity, IBone bone) { }

	@Override
	protected ItemStack getArmorForBone(String boneName, PoroEntity currentEntity) {
		if(boneName.equals("armor_head")) return helmet;
		return null;
	}

	@Nullable
	@Override
	protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, PoroEntity currentEntity) {
		switch (boneName) {
			case "item_bone": 	return EquipmentSlotType.MAINHAND;
			case "armor_head": 	return EquipmentSlotType.HEAD;
			default: 			return null;
		}
	}

	@Override
	protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorModel) {
		switch (name) {
			case "item_bone":	return armorModel.bipedRightArm;
			case "armor_head":	return armorModel.bipedHead;
			default:			return null;
		}
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected void preRenderBlock(MatrixStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Override
	protected void postRenderBlock(MatrixStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Nullable
	@Override
	protected ResourceLocation getTextureForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected boolean isArmorBone(GeoBone bone) { return bone.getName().startsWith("armor"); }
}