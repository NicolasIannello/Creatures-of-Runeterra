package com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.DBShieldEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DBShieldModel extends GeoModel<DBShieldEntity> {

    @Override
    public ResourceLocation getAnimationResource(DBShieldEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/dbshield.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DBShieldEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/dbshield.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DBShieldEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/dbshield.png");
    }
}