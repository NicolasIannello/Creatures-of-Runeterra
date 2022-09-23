package com.eximeisty.creaturesofruneterra.entity.client.entities.fabledporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FabledPoroModel extends AnimatedGeoModel<FabledPoroEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(FabledPoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fabledporo.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FabledPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fabledporo.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FabledPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fabledporo.png");
    }
    
}