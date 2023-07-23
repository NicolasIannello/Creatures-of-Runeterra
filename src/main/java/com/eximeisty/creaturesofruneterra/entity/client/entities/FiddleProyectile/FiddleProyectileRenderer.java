package com.eximeisty.creaturesofruneterra.entity.client.entities.FiddleProyectile;

import com.eximeisty.creaturesofruneterra.entity.custom.FiddleProyectileEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FiddleProyectileRenderer extends EntityRenderer<FiddleProyectileEntity> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/empty.png");

   public FiddleProyectileRenderer(EntityRendererManager manager) {
      super(manager);
   }

   public ResourceLocation getEntityTexture(FiddleProyectileEntity entity) {
      return TEXTURE;
   }
}