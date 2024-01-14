package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriDaggerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NaafiriDaggerModel extends GeoModel<NaafiriDaggerEntity> {
    @Override
    public ResourceLocation getAnimationResource(NaafiriDaggerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri_dagger.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(NaafiriDaggerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri_dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NaafiriDaggerEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }
}
