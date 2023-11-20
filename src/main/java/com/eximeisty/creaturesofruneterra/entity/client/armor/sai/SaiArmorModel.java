package com.eximeisty.creaturesofruneterra.entity.client.armor.sai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SaiArmorModel extends AnimatedGeoModel<SaiArmorItem>{

    @Override
    public ResourceLocation getAnimationResource(SaiArmorItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_armor.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SaiArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/sai_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SaiArmorItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/sai_armor.png");
    }
    
}
