package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NaafiriModel extends GeoModel<NaafiriEntity> {

    @Override
    public ResourceLocation getAnimationResource(NaafiriEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/naafiri.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(NaafiriEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/naafiri.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NaafiriEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }
    
    @Override
    public void setCustomAnimations(NaafiriEntity animatable, long instanceId, AnimationState<NaafiriEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(animatable.getEntityData().get(NaafiriEntity.ATTACK)==0){
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