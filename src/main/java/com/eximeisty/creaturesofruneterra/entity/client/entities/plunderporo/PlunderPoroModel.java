package com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PlunderPoroEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlunderPoroModel extends AnimatedGeoModel<PlunderPoroEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(PlunderPoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/plunderporo.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(PlunderPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/plunderporo.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PlunderPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/plunderporo.png");
    }
    
}