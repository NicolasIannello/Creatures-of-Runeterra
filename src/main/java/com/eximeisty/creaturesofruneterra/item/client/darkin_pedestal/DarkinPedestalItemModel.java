package com.eximeisty.creaturesofruneterra.item.client.darkin_pedestal;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.item.custom.DarkinPedestalItem;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DarkinPedestalItemModel extends AnimatedGeoModel<DarkinPedestalItem> {
    @Override
    public ResourceLocation getAnimationFileLocation(DarkinPedestalItem animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/darkin_pedestal.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DarkinPedestalItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/darkin_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DarkinPedestalItem object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");
    }

}