package com.eximeisty.creaturesofruneterra.block.entity.client.darkinT;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DarkinThingyModel extends AnimatedGeoModel<DarkinThingyTileEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(DarkinThingyTileEntity animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DarkinThingyTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/darkin_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DarkinThingyTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");
    }
}