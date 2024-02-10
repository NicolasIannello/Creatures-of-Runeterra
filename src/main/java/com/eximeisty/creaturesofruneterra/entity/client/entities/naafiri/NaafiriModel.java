package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class NaafiriModel extends AnimatedGeoModel<NaafiriEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(NaafiriEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(NaafiriEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }
    
    @Override
    public void setCustomAnimations(NaafiriEntity animatable, int instanceId, AnimationEvent animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData entityData = (EntityModelData) animationState.getExtraDataOfType(EntityModelData.class).get(0);
        if(animatable.getEntityData().get(NaafiriEntity.ATTACK)==0){
            if (head != null) {
                head.setRotationX(entityData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(entityData.netHeadYaw * ((float) Math.PI / 180F));
            }
            if (jaw != null) {
                jaw.setRotationX(entityData.headPitch * ((float) Math.PI / 180F));
                jaw.setRotationY(entityData.netHeadYaw * ((float) Math.PI / 180F));
            }
        }
	}

}