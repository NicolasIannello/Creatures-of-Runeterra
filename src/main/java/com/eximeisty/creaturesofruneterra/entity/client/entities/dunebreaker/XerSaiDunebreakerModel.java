package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import javax.annotation.Nullable;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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

    @Override @SuppressWarnings({"rawtypes","unchecked"})
	public void setLivingAnimations(XerSaiDunebreakerEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("Head");

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null && entity.getDataManager().get(XerSaiDunebreakerEntity.STATE)==0) {
			head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
			head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
		}
	}
}