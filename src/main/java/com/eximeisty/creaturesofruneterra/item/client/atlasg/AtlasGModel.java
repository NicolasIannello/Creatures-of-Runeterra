package com.eximeisty.creaturesofruneterra.item.client.atlasg;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.AtlasG;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AtlasGModel extends AnimatedGeoModel<AtlasG>{

    @Override
    public ResourceLocation getAnimationFileLocation(AtlasG animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/item/atlasg.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(AtlasG object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/atlasg.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AtlasG object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/atlasg.png");
    }
    
}
