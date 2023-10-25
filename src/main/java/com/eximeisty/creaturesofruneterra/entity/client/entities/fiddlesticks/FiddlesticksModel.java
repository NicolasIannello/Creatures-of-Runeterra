package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FiddlesticksModel extends GeoModel<FiddlesticksEntity> {

    @Override
    public ResourceLocation getAnimationResource(FiddlesticksEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fiddlesticks.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks.png");
    }

}