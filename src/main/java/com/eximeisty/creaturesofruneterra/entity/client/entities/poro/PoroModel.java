package com.eximeisty.creaturesofruneterra.entity.client.entities.poro;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PoroEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PoroModel extends AnimatedGeoModel<PoroEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(PoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/poro.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(PoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/poro.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/poro.png");
    }
    
}