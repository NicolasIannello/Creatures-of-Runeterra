package com.eximeisty.creaturesofruneterra.item.client.dunebreakershield;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DunebreakerShield;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DunebreakerShieldModel extends GeoModel<DunebreakerShield> {

    @Override
    public ResourceLocation getAnimationResource(DunebreakerShield animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/items/dunebreaker_shield.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DunebreakerShield object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/dunebreaker_shield.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DunebreakerShield object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/dunebreaker_shield.png");
    }
}