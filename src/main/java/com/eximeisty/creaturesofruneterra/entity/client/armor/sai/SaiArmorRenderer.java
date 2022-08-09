package com.eximeisty.creaturesofruneterra.entity.client.armor.sai;

import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;

import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SaiArmorRenderer extends GeoArmorRenderer<SaiArmorItem>{

    public SaiArmorRenderer() {
        super(new SaiArmorModel());

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
