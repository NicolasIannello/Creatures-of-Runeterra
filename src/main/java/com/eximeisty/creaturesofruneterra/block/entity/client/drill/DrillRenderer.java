package com.eximeisty.creaturesofruneterra.block.entity.client.drill;

import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DrillRenderer extends GeoBlockRenderer<DrillTileEntity> {

    public DrillRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new DrillModel()/*, rendererDispatcherIn*/);
    }

}