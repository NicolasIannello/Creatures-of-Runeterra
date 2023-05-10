package com.eximeisty.creaturesofruneterra.entity.client.entities.bullet;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.BulletEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BulletModel extends AnimatedGeoModel<BulletEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(BulletEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/bullet.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(BulletEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/bullet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/bullet.png");
    }
}