package com.eximeisty.creaturesofruneterra.item.client.fishbones;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.Fishbones;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FishbonesModel extends AnimatedGeoModel<Fishbones>{

    @Override
    public ResourceLocation getAnimationResource(Fishbones animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/fishbones.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Fishbones object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/fishbones.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Fishbones object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/fishbones.png");
    }
    
}
