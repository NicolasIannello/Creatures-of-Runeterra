package com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DBShieldModel extends AnimatedGeoModel<DBShieldEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(DBShieldEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/dbshield.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DBShieldEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/dbshield.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DBShieldEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/dbshield.png");
    }
}