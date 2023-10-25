package com.eximeisty.creaturesofruneterra.item.client.fiddleScythe;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.FiddleScythe;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FiddleScytheModel extends GeoModel<FiddleScythe> {

    @Override
    public ResourceLocation getAnimationResource(FiddleScythe animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/fiddle_scythe.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(FiddleScythe object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/fiddle_scythe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FiddleScythe object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/fiddle_scythe.png");
    }

}