package com.eximeisty.creaturesofruneterra.entity.client.entities.bullet;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.BulletEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BulletRenderer extends GeoProjectilesRenderer<BulletEntity> {

    public BulletRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BulletModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/bullet.png");
    }

}