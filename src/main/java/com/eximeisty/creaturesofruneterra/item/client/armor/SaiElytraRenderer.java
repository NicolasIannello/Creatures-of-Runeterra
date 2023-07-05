package com.eximeisty.creaturesofruneterra.item.client.armor;

import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SaiElytraRenderer extends GeoArmorRenderer<SaiElytraItem> {
    public SaiElytraRenderer() {
        super(new SaiElytraModel());
    }
}