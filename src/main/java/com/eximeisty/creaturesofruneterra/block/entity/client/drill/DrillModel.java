package com.eximeisty.creaturesofruneterra.block.entity.client.drill;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DrillModel extends AnimatedGeoModel<DrillTileEntity>{
    @Override
    public ResourceLocation getAnimationFileLocation(DrillTileEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(DrillTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DrillTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/drill.png");
    }
}
