package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

    public PoroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PoroModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PoroEntity entity) {
        return rl;
    }

	@Nullable
	@Override
	protected ItemStack getHeldItemForBone(String boneName, PoroEntity currentEntity) {
		if(boneName.equals("item_bone")) return mainHand;
		return null;
	}

	@Override
	protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		if(boneName.equals("item_bone")) return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
		return ItemTransforms.TransformType.NONE;
	}

	@Override
	protected void preRenderItem(PoseStack stack, ItemStack item, String boneName, PoroEntity currentEntity, IBone bone) {
		if (item == this.mainHand) {
			stack.mulPose(Vector3f.XP.rotationDegrees(-75));
			stack.mulPose(Vector3f.YP.rotationDegrees(0));
			stack.mulPose(Vector3f.ZP.rotationDegrees(0));
			stack.translate(0D, 0.15D, 0D);
		}
	}

	@Override
	protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, PoroEntity animatable, IBone bone) { }

	@Override
	protected ItemStack getArmorForBone(String boneName, PoroEntity currentEntity) {
		if(boneName.equals("armor_head")) return helmet;
		return null;
	}

	@Nullable
	@Override
	protected EquipmentSlot getEquipmentSlotForArmorBone(String boneName, PoroEntity currentEntity) {
		switch (boneName) {
			case "item_bone": 	return EquipmentSlot.MAINHAND;
			case "armor_head": 	return EquipmentSlot.HEAD;
			default: 			return null;
		}
	}

	@Override
	protected ModelPart getArmorPartForBone(String name, HumanoidModel<?> armorModel) {
		switch (name) {
			case "item_bone":	return armorModel.rightArm;
			case "armor_head":	return armorModel.head;
			default:			return null;
		}
	}

	@Nullable
	@Override
	protected BlockState getHeldBlockForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected void preRenderBlock(PoseStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Override
	protected void postRenderBlock(PoseStack stack, BlockState block, String boneName, PoroEntity currentEntity) { }

	@Nullable
	@Override
	protected ResourceLocation getTextureForBone(String boneName, PoroEntity currentEntity) { return null; }

	@Override
	protected boolean isArmorBone(GeoBone bone) { return bone.getName().startsWith("armor"); }
}