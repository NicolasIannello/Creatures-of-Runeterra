package com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

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
    public void renderRecursively(PoseStack poseStack, FabledPoroEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemforge")){
            poseStack.pushPose();
            poseStack.rotateAround(Axis.XP.rotationDegrees(0),0,0,0);
            poseStack.rotateAround(Axis.YP.rotationDegrees(0),0,0,0);
            poseStack.rotateAround(Axis.ZP.rotationDegrees(90),0,0,0);
            poseStack.translate(0.1D, 0D, -0.5D);
            poseStack.scale(1.0f, 1.0f, 1.0f);
            BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(animatable.getItemInHand(InteractionHand.MAIN_HAND), animatable.getLevel(), animatable, animatable.getId());
            Minecraft.getInstance().getItemRenderer().render(animatable.getItemInHand(InteractionHand.MAIN_HAND), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, isReRender, poseStack, bufferSource, packedLight, packedOverlay, bakedmodel);
            poseStack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityTranslucent(rl));
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
