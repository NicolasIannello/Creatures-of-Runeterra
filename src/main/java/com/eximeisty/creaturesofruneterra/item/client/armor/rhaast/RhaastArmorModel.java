package com.eximeisty.creaturesofruneterra.item.client.armor.rhaast;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.RhaastArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RhaastArmorModel extends GeoModel<RhaastArmorItem> {
    @Override
    public ResourceLocation getModelResource(RhaastArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/rhaast_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RhaastArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/rhaast_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RhaastArmorItem animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_armor.animation.json");
    }
}