package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SilverwingModel extends GeoModel<SilverwingEntity> {
    private static final ResourceLocation[][] VARIANT = {
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_white.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_sandy.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_brown.png")
            },
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_green.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_blue.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_brown.png")
            },
            {
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_white.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_black.png"),
                new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/silverwing_blue.png")
            }
    };

    @Override
    public ResourceLocation getModelResource(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/silverwing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SilverwingEntity animatable) {
        return VARIANT[animatable.getBiome()][animatable.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/silverwing.animation.json");
    }

}