package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RekSaiModel extends AnimatedTickingGeoModel<RekSaiEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(RekSaiEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/reksai.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/reksai.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/reksai.png");
    }
    
    @Override @SuppressWarnings({"rawtypes","unchecked"})
	public void setLivingAnimations(RekSaiEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("topHead");
        IBone jaw = this.getAnimationProcessor().getBone("jaw");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if(entity.getDataManager().get(entity.STATE)!=1 && entity.getDataManager().get(entity.STATE)!=2 && entity.getDataManager().get(entity.STATE)!=6){
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
}