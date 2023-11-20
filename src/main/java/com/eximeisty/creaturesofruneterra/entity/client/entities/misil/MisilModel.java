package com.eximeisty.creaturesofruneterra.entity.client.entities.misil;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MisilModel extends AnimatedGeoModel<MisilEntity> {

    @Override
    public ResourceLocation getAnimationResource(MisilEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/misil.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(MisilEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/misil.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MisilEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/misil.png");
    }
}