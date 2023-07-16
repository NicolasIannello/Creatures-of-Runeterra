package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class FiddlesticksModel extends AnimatedTickingGeoModel<FiddlesticksEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(FiddlesticksEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fiddlesticks.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks.png");
    }
}