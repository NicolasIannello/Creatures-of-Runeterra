package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleDummyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FiddleDummyModel extends GeoModel<FiddleDummyEntity> {

    @Override
    public ResourceLocation getAnimationResource(FiddleDummyEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fiddlesticks.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(FiddleDummyEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FiddleDummyEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks.png");
    }
}