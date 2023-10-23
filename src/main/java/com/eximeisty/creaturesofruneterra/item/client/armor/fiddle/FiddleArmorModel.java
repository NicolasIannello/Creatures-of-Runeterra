package com.eximeisty.creaturesofruneterra.item.client.armor.fiddle;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;

import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FiddleArmorModel extends GeoModel<FiddleArmorItem> {

    @Override
    public ResourceLocation getAnimationResource(FiddleArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/fiddle_armor2.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(FiddleArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/fiddle_armor2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FiddleArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/fiddle_armor_new2.png");
    }
}