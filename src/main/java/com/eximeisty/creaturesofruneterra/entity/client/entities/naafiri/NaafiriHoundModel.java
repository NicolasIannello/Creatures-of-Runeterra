package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriHoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NaafiriHoundModel extends GeoModel<NaafiriHoundEntity> {

    @Override
    public ResourceLocation getAnimationResource(NaafiriHoundEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(NaafiriHoundEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri_spawn.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NaafiriHoundEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri_spawn.png");
    }

    @Override
    public void setCustomAnimations(NaafiriHoundEntity animatable, long instanceId, AnimationState<NaafiriHoundEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(animatable.getEntityData().get(NaafiriHoundEntity.ATTACK)==0){
            if (head != null) {
                head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
                head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
            }
            if (jaw != null) {
                jaw.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
                jaw.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
            }
        }
    }
}