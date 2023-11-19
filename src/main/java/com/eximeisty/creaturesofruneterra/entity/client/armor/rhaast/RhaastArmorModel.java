package com.eximeisty.creaturesofruneterra.entity.client.armor.rhaast;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.RhaastArmorItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RhaastArmorModel extends AnimatedGeoModel<RhaastArmorItem> {
    @Override
    public ResourceLocation getModelLocation(RhaastArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/rhaast_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RhaastArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/rhaast_armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RhaastArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_armor.animation.json");
    }

}