package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class PoroRenderer extends DynamicGeoEntityRenderer<PoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/poro.png");
    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;

    public PoroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PoroModel());

        addRenderLayer(new ItemArmorGeoLayer<PoroEntity>(this) {
            @Nullable
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, PoroEntity animatable) {
                if(bone.getName().equals("armor_head")) return this.helmetStack;
                return null;
            }

            @Nonnull
            @Override
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, PoroEntity animatable) {
                return switch (bone.getName()) {
                    case "item_bone" -> EquipmentSlot.MAINHAND;
                    case "armor_head" -> EquipmentSlot.HEAD;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @Nonnull
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, PoroEntity animatable, HumanoidModel<?> baseModel) {
                return switch (bone.getName()) {
                    case "item_bone" -> baseModel.rightArm;
                    case "armor_head" -> baseModel.head;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });

        addRenderLayer(new BlockAndItemGeoLayer<PoroEntity>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, PoroEntity animatable) {
                if(bone.getName().equals("item_bone")) return PoroRenderer.this.mainHandItem;
                return null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, PoroEntity animatable) {
                if(bone.getName().equals("item_bone")) return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                return ItemDisplayContext.NONE;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, PoroEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == PoroRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-75f));
                    poseStack.translate(0D, 0.15D, 0D);
                }
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(PoroEntity entity) {
        return rl;
    }

    @Override
    public void preRender(PoseStack poseStack, PoroEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }
}