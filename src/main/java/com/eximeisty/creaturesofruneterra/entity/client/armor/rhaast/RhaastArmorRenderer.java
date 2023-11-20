package com.eximeisty.creaturesofruneterra.entity.client.armor.rhaast;

import com.eximeisty.creaturesofruneterra.item.custom.RhaastArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RhaastArmorRenderer extends GeoArmorRenderer<RhaastArmorItem> {
    public RhaastArmorRenderer() {
        super(new RhaastArmorModel());

        this.headBone="armorHead";
        this.bodyBone="armorBody";
        this.rightArmBone="armorRightArm";
        this.leftArmBone="armorLeftArm";
        this.rightLegBone="armorRightLeg";
        this.leftLegBone="armorLeftLeg";
        this.rightBootBone="armorRightBoot";
        this.leftBootBone="armorLeftBoot";
    }
}
