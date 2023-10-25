package com.eximeisty.creaturesofruneterra.block.entity.client.drill;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DrillModel extends GeoModel<DrillTileEntity> {
    @Override
    public ResourceLocation getAnimationResource(DrillTileEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DrillTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrillTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/drill.png");
    }

}
