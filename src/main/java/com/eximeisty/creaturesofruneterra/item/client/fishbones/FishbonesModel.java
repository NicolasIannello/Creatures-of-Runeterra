package com.eximeisty.creaturesofruneterra.item.client.fishbones;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.Fishbones;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FishbonesModel extends AnimatedGeoModel<Fishbones>{

    @Override
    public ResourceLocation getAnimationFileLocation(Fishbones animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/fishbones.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Fishbones object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/fishbones.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Fishbones object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/fishbones.png");
    }
    
}