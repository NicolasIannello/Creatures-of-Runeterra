package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RekSaiModel extends GeoModel<RekSaiEntity> {

    @Override
    public ResourceLocation getAnimationResource(RekSaiEntity animatable) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "animations/entities/reksai.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "geo/entities/reksai.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RekSaiEntity object) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/reksai.png");
    }
    
    @Override
    public void setCustomAnimations(RekSaiEntity animatable, long instanceId, AnimationState<RekSaiEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("topHead");
        CoreGeoBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(animatable.getEntityData().get(RekSaiEntity.STATE)!=1 && animatable.getEntityData().get(RekSaiEntity.STATE)!=2 && animatable.getEntityData().get(RekSaiEntity.STATE)!=6){
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