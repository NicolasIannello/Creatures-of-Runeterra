package com.eximeisty.creaturesofruneterra.item.client.dunebreakershield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DunebreakerShield;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DunebreakerShieldModel extends AnimatedGeoModel<DunebreakerShield>{

    @Override
    public ResourceLocation getAnimationFileLocation(DunebreakerShield animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/dunebreaker_shield.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DunebreakerShield object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/dunebreaker_shield.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DunebreakerShield object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/dunebreaker_shield.png");
    }
}