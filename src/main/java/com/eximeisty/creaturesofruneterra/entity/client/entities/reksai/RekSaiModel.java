package com.eximeisty.creaturesofruneterra.entity.client.entities.reksai;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.RekSaiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RekSaiModel extends AnimatedGeoModel<RekSaiEntity> {

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
    public void setCustomAnimations(RekSaiEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("topHead");
        IBone jaw = this.getAnimationProcessor().getBone("jaw");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(entity.getEntityData().get(RekSaiEntity.STATE)!=1 && entity.getEntityData().get(RekSaiEntity.STATE)!=2 && entity.getEntityData().get(RekSaiEntity.STATE)!=6){
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