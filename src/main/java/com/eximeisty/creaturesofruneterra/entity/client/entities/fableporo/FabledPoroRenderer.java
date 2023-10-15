package com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FabledPoroRenderer extends GeoEntityRenderer<FabledPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fabledporo.png");

    public FabledPoroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FabledPoroModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FabledPoroEntity entity) {
        return rl;
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemforge")){
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.XP.rotationDegrees(0));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(0));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
            poseStack.translate(0.1D, 0D, -0.5D);
            poseStack.scale(1.0f, 1.0f, 1.0f);
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.MAIN_HAND), animatable.getLevel(), animatable, animatable.getId());
            Minecraft.getInstance().getItemRenderer().render(mainHand, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, true, poseStack, this.rtb, packedLight, packedOverlay, bakedmodel);
            poseStack.popPose();
            buffer = rtb.getBuffer(RenderType.entityTranslucent(rl));
        }
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
