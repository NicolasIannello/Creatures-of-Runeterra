package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriHoundEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class NaafiriHoundModel extends AnimatedGeoModel<NaafiriHoundEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(NaafiriHoundEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(NaafiriHoundEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri_spawn.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriHoundEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri_spawn.png");
    }

    @Override
    public void setLivingAnimations(NaafiriHoundEntity animatable, Integer instanceId, @Nullable AnimationEvent animationState) {
        super.setLivingAnimations(animatable, instanceId, animationState);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData entityData = (EntityModelData) animationState.getExtraDataOfType(EntityModelData.class).get(0);
        if(animatable.getDataManager().get(NaafiriHoundEntity.ATTACK)==0){
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