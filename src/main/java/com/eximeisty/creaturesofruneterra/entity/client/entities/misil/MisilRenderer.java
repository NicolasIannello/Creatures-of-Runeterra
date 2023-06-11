package com.eximeisty.creaturesofruneterra.entity.client.entities.misil;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MisilRenderer extends GeoProjectilesRenderer<MisilEntity> {

    public MisilRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MisilModel());
    }

    @Override
    public ResourceLocation getEntityTexture(MisilEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/misil.png");
    }

}