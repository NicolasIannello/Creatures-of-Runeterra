package com.eximeisty.creaturesofruneterra.item.client.drill;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DrillItem;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrillItemModel extends AnimatedGeoModel<DrillItem>{
    @Override
    public ResourceLocation getAnimationFileLocation(DrillItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DrillItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DrillItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/drill.png");
    }
}