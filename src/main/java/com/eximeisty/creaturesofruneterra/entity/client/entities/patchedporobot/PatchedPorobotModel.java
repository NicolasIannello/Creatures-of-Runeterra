package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PatchedPorobotModel extends AnimatedGeoModel<PatchedPorobotEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(PatchedPorobotEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/porobot.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(PatchedPorobotEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/porobot.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(PatchedPorobotEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/porobot.png");
    }
    
}