package com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FiddlesticksEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class FiddlesticksModel extends AnimatedTickingGeoModel<FiddlesticksEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(FiddlesticksEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/fiddlesticks.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/fiddlesticks.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(FiddlesticksEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fiddlesticks.png");
    }
    
    @Override @SuppressWarnings({"rawtypes","unchecked"})
	public void setLivingAnimations(FiddlesticksEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("top");
        IBone jaw = this.getAnimationProcessor().getBone("jaw");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
        if (jaw != null) {
            jaw.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            jaw.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
	}
}