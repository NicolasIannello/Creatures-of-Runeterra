package com.eximeisty.creaturesofruneterra.item.client.armor;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SaiArmorModel extends GeoModel<SaiArmorItem> {
    @Override
    public ResourceLocation getModelResource(SaiArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/sai_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SaiArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/sai_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SaiArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_armor.animation.json");
    }
}