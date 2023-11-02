package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class FiddleProyectileRenderer extends EntityRenderer<FiddleProyectileEntity> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/particle/soul_0.png");
   private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
   private static final ResourceLocation TEXTURE3 = new ResourceLocation("minecraft", "textures/particle/sculk_soul_0.png");
   private static final RenderType RENDER_TYPE3 = RenderType.entityCutoutNoCull(TEXTURE3);
   private static final ResourceLocation TEXTURE5 = new ResourceLocation("minecraft", "textures/particle/sculk_charge_pop_0.png");
   private static final RenderType RENDER_TYPE5 = RenderType.entityCutoutNoCull(TEXTURE5);

   public FiddleProyectileRenderer(EntityRendererProvider.Context manager) {
      super(manager);
   }

   protected int getBlockLightLevel(FiddleProyectileEntity pEntity, BlockPos pPos) {
      return 15;
   }

   public void render(FiddleProyectileEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
      pMatrixStack.pushPose();
      pMatrixStack.scale(1F, 1F, 1F);
      pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
      pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
      PoseStack.Pose posestack$pose = pMatrixStack.last();
      Matrix4f matrix4f = posestack$pose.pose();
      Matrix3f matrix3f = posestack$pose.normal();
      Entity owner = pEntity.getOwner();
      VertexConsumer vertexconsumer;
      if(owner instanceof FiddlesticksEntity) {
         vertexconsumer = pBuffer.getBuffer(RENDER_TYPE3);
      }else if (owner instanceof Player){
         vertexconsumer = pBuffer.getBuffer(RENDER_TYPE5);
      }else{
         vertexconsumer = pBuffer.getBuffer(RENDER_TYPE);
      }
      vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 0, 0, 1);
      vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 0, 1, 1);
      vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 1.0F, 1, 1, 0);
      vertex(vertexconsumer, matrix4f, matrix3f, pPackedLight, 0.0F, 1, 0, 0);
      pMatrixStack.popPose();
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
   }

   private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
      p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
   }

   public ResourceLocation getTextureLocation(FiddleProyectileEntity entity) {
      return TEXTURE;
   }
}