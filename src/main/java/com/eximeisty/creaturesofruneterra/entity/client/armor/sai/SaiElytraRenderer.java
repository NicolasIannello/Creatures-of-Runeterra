package com.eximeisty.creaturesofruneterra.entity.client.armor.sai;

import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SaiElytraRenderer extends GeoArmorRenderer<SaiElytraItem>{

    public SaiElytraRenderer() {
        super(new SaiElytraModel());

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
