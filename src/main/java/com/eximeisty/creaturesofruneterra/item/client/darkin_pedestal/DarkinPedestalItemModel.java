package com.eximeisty.creaturesofruneterra.item.client.darkin_pedestal;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DarkinPedestalItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DarkinPedestalItemModel extends GeoModel<DarkinPedestalItem> {
    @Override
    public ResourceLocation getAnimationResource(DarkinPedestalItem animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/darkin_pedestal.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DarkinPedestalItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/darkin_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DarkinPedestalItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");
    }

}