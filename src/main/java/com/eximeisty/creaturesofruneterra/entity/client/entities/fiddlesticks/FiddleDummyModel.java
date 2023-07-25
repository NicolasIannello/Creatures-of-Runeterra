package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddleDummyEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class FiddleDummyModel extends AnimatedTickingGeoModel<FiddleDummyEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(FiddleDummyEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fiddlesticks.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FiddleDummyEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FiddleDummyEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks-normal.png");
    }
}