package com.eximeisty.creaturesofruneterra.entity.client.entities.empty;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EmptyModel<T extends CoRPartEntity> extends GeoModel<CoRPartEntity> {

	@Override
	public ResourceLocation getAnimationResource(CoRPartEntity animatable) {
		return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/empty.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CoRPartEntity object) {
		return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/empty.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CoRPartEntity object) {
		return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/empty.png");
	}
}