package com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PatchedPorobotEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PatchedPorobotModel extends GeoModel<PatchedPorobotEntity> {

    @Override
    public ResourceLocation getAnimationResource(PatchedPorobotEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/porobot.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(PatchedPorobotEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/porobot.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PatchedPorobotEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/porobot.png");
    }

}