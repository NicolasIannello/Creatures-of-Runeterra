package com.eximeisty.creaturesofruneterra.entity.client.entities.misil;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.MisilEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MisilRenderer extends GeoEntityRenderer<MisilEntity> {

    public MisilRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MisilModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MisilEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/misil.png");
    }

}