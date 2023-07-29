package com.eximeisty.creaturesofruneterra.entity.client.armor.fiddle;

import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class FiddleArmorRenderer extends GeoArmorRenderer<FiddleArmorItem>{

    public FiddleArmorRenderer() {
        super(new FiddleArmorModel());

        this.headBone="armorHead";
        this.bodyBone="armorBody";
        this.rightArmBone="armorRightArm";
        this.leftArmBone="armorLeftArm";
        this.rightLegBone="armorLeftLeg";
        this.leftLegBone="armorRightLeg";
        this.rightBootBone="armorLeftBoot";
        this.leftBootBone="armorRightBoot";
    }
    
}