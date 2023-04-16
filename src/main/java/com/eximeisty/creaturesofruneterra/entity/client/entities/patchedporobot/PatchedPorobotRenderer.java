package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PatchedPorobotRenderer extends GeoEntityRenderer<PatchedPorobotEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/porobot.png");

    public PatchedPorobotRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PatchedPorobotModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PatchedPorobotEntity entity) {
        return rl;
    }
}