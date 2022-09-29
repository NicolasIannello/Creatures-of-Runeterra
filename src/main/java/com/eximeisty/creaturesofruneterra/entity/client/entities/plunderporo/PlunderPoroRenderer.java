package com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PlunderPoroEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PlunderPoroRenderer extends GeoEntityRenderer<PlunderPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/plunderporo.png");

    public PlunderPoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PlunderPoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PlunderPoroEntity entity) {
        return rl;
    }
}