package com.eximeisty.creaturesofruneterra.block.entity.client.darkinT;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;


public class DarkinThingyRenderer extends GeoBlockRenderer<DarkinThingyTileEntity> {
    private float z = 0;
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");

    public DarkinThingyRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new DarkinThingyModel());
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (bone.getName().equals("item")) {
            z += 0.5;
            if(z>360) z=0;
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.YP.rotationDegrees(z));
            poseStack.translate(0D, 1.5D, 0D);
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.itemHandler.getStackInSlot(0), animatable.getLevel(), null, animatable.getTileData().getId());
            Minecraft.getInstance().getItemRenderer().render(animatable.itemHandler.getStackInSlot(0), ItemTransforms.TransformType.GROUND, true, poseStack, this.rtb, packedLight, packedOverlay, bakedmodel);
            poseStack.popPose();
            buffer = rtb.getBuffer(RenderType.entityTranslucent(rl));
        }

        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}