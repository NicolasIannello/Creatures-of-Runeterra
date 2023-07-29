package com.eximeisty.creaturesofruneterra.entity.client.armor.fiddle;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FiddleArmorModel extends AnimatedGeoModel<FiddleArmorItem>{

    @Override
    public ResourceLocation getAnimationFileLocation(FiddleArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/fiddle_armor2.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FiddleArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/fiddle_armor2.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FiddleArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/fiddle_armor2.png");
    }
    
}