package com.eximeisty.creaturesofruneterra.item.client.drill;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DrillItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrillItemModel extends AnimatedGeoModel<DrillItem>{
    @Override
    public ResourceLocation getAnimationResource(DrillItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DrillItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrillItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/drill.png");
    }
}