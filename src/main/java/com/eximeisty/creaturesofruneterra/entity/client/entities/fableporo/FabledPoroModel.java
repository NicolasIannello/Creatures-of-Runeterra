package com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FabledPoroModel extends GeoModel<FabledPoroEntity> {

    @Override
    public ResourceLocation getAnimationResource(FabledPoroEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fabledporo.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(FabledPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fabledporo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FabledPoroEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fabledporo.png");
    }

}