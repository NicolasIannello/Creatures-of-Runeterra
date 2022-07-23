package com.eximeisty.creaturesofruneterra.entity.client.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RekSaiModel extends AnimatedGeoModel<RekSaiEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(RekSaiEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/reksai.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/reksai.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/reksai.png");
    }
    
}