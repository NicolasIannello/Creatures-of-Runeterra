package com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.ExaltedPoroEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class ExaltedPoroRenderer extends GeoEntityRenderer<ExaltedPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/exaltedporo.png");

    public ExaltedPoroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ExaltedPoroModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ExaltedPoroEntity entity) {
        return rl;
    }
}