package com.eximeisty.creaturesofruneterra.block.entity.client.darkinT;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DarkinThingyModel extends GeoModel<DarkinThingyTileEntity> {
    @Override
    public ResourceLocation getAnimationResource(DarkinThingyTileEntity animatable) {
        return null;//new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/block/drill.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(DarkinThingyTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/block/darkin_pedestal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DarkinThingyTileEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/block/darkin_pedestal.png");
    }

}
