package com.eximeisty.creaturesofruneterra.item.client.fiddleScythe;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.FiddleScythe;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FiddleScytheModel extends AnimatedGeoModel<FiddleScythe>{

    @Override
    public ResourceLocation getAnimationFileLocation(FiddleScythe animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/fiddle_scythe.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FiddleScythe object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/fiddle_scythe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FiddleScythe object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/fiddle_scythe.png");
    }
    
}