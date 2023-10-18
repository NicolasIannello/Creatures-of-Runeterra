package com.eximeisty.creaturesofruneterra.item.client.drill;

import com.eximeisty.creaturesofruneterra.item.custom.DrillItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DrillItemRenderer extends GeoItemRenderer<DrillItem> {

    public DrillItemRenderer() {
        super(new DrillItemModel());
    }

}