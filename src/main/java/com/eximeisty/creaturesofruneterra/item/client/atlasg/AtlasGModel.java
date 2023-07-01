package com.eximeisty.creaturesofruneterra.item.client.atlasg;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.AtlasG;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AtlasGModel extends GeoModel<AtlasG> {

    @Override
    public ResourceLocation getAnimationResource(AtlasG animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/items/atlasg.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AtlasG object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/item/atlasg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AtlasG object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/item/atlasg.png");
    }

}
