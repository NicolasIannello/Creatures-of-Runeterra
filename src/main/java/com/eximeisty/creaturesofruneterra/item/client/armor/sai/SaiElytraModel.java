package com.eximeisty.creaturesofruneterra.item.client.armor.sai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SaiElytraModel extends GeoModel<SaiElytraItem> {
    @Override
    public ResourceLocation getModelResource(SaiElytraItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/armor/sai_elytra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SaiElytraItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/model/armor/sai_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SaiElytraItem animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/armor/sai_elytra.animation.json");
    }
}