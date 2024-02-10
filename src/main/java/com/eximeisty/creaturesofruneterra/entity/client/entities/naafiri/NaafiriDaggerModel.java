package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriDaggerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NaafiriDaggerModel extends AnimatedGeoModel<NaafiriDaggerEntity> {
    @Override
    public ResourceLocation getAnimationFileLocation(NaafiriDaggerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri_dagger.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(NaafiriDaggerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri_dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriDaggerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }

}
