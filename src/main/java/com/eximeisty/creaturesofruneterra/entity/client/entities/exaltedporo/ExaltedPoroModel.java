package com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.ExaltedPoroEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ExaltedPoroModel extends AnimatedGeoModel<ExaltedPoroEntity> {

    @Override
    public ResourceLocation getAnimationResource(ExaltedPoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/exaltedporo.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(ExaltedPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/exaltedporo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ExaltedPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/exaltedporo.png");
    }

}