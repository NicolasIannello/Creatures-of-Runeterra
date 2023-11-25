package com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.SilverwingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SilverwingModel extends GeoModel<SilverwingEntity> {

    @Override
    public ResourceLocation getModelResource(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/xersai_dunebreaker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_dunebreaker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SilverwingEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/xersai_dunebreaker.animation.json");
    }

//    @Override
//    public void setCustomAnimations(SilverwingEntity animatable, long instanceId, AnimationState<SilverwingEntity> animationState) {
//        super.setCustomAnimations(animatable, instanceId, animationState);
//        CoreGeoBone head = getAnimationProcessor().getBone("Head");
//
//        if (head != null && animatable.getEntityData().get(SilverwingEntity.STATE)==0) {
//            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
//            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
//            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
//        }
//    }
}