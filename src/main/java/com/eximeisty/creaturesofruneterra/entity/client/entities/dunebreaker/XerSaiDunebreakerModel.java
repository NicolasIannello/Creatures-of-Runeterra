package com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiDunebreakerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class XerSaiDunebreakerModel extends AnimatedGeoModel<XerSaiDunebreakerEntity> {

    @Override
    public ResourceLocation getModelResource(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/xersai_dunebreaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(XerSaiDunebreakerEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/xersai_dunebreaker.animation.json");
    }

    @Override
    public void setCustomAnimations(XerSaiDunebreakerEntity entity, int uniqueID, AnimationEvent animationEvent) {
        super.setCustomAnimations(entity, uniqueID, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("Head");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null && entity.getEntityData().get(XerSaiDunebreakerEntity.STATE)==0) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}