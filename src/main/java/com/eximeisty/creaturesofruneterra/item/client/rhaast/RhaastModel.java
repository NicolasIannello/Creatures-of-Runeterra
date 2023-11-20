package com.eximeisty.creaturesofruneterra.item.client.rhaast;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.Rhaast;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RhaastModel extends AnimatedGeoModel<Rhaast> {

    @Override
    public ResourceLocation getAnimationFileLocation(Rhaast animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/rhaast.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(Rhaast object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/rhaast.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Rhaast object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/rhaast.png");
    }

}