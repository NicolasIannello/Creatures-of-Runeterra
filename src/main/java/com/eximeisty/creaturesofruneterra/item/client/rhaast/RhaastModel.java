package com.eximeisty.creaturesofruneterra.item.client.rhaast;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.Rhaast;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RhaastModel extends GeoModel<Rhaast> {

    @Override
    public ResourceLocation getAnimationResource(Rhaast animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/rhaast.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(Rhaast object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/rhaast.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Rhaast object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/rhaast.png");
    }

}