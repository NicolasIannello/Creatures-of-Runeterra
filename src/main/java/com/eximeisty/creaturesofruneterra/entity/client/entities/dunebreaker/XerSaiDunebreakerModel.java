package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class XerSaiDunebreakerModel extends AnimatedGeoModel<XerSaiDunebreakerEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/xersai_dunebreaker.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(XerSaiDunebreakerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/xersai_dunebreaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(XerSaiDunebreakerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }
    
}