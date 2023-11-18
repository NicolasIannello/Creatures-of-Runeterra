package com.eximeisty.creaturesofruneterra.block.entity.client.darkinT;

import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class DarkinThingyRenderer extends GeoBlockRenderer<DarkinThingyTileEntity> {
    private float z = 0;

    public DarkinThingyRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new DarkinThingyModel());
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, DarkinThingyTileEntity animatable) {
                return switch (bone.getName()) {
                    case "item" -> animatable.itemHandler.getStackInSlot(0);
                    default -> null;
                };
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, DarkinThingyTileEntity animatable) {
                return switch (bone.getName()) {
                    case "item" -> ItemDisplayContext.GROUND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, DarkinThingyTileEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                z += 0.5;
                if(z>360) z=0;

                poseStack.rotateAround(Axis.XP.rotationDegrees(0),0,0,0);
                poseStack.rotateAround(Axis.YP.rotationDegrees(z),0,0,0);
                poseStack.rotateAround(Axis.ZP.rotationDegrees(0),0,0,0);

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

}