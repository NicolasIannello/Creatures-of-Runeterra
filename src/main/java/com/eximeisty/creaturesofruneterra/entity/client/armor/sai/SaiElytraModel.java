package com.eximeisty.creaturesofruneterra.entity.client.armor.sai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SaiElytraModel extends AnimatedGeoModel<SaiElytraItem>{

    @Override
    public ResourceLocation getAnimationFileLocation(SaiElytraItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_elytra.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(SaiElytraItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/sai_elytra.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SaiElytraItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/sai_armor.png");
    }
    
}
