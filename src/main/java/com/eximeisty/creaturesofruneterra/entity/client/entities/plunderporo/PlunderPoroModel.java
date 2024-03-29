package com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PlunderPoroEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PlunderPoroModel extends GeoModel<PlunderPoroEntity> {

    @Override
    public ResourceLocation getAnimationResource(PlunderPoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/plunderporo.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(PlunderPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/plunderporo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlunderPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/plunderporo.png");
    }
}