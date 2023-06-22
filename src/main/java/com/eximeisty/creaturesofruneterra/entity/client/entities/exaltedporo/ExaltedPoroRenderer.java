package com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.ExaltedPoroEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ExaltedPoroRenderer extends GeoEntityRenderer<ExaltedPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/exaltedporo.png");

    public ExaltedPoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ExaltedPoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(ExaltedPoroEntity entity) {
        return rl;
    }
}