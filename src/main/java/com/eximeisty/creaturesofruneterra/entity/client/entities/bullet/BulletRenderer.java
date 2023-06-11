package com.eximeisty.creaturesofruneterra.entity.client.entities.bullet;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.BulletEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BulletRenderer extends GeoProjectilesRenderer<BulletEntity> {

    public BulletRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BulletModel());
    }

    @Override
    public ResourceLocation getEntityTexture(BulletEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/bullet.png");
    }

}