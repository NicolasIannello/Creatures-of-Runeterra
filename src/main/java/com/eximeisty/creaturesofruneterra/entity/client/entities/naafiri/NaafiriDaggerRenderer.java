package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriDaggerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class NaafiriDaggerRenderer  extends GeoProjectilesRenderer<NaafiriDaggerEntity> {

    public NaafiriDaggerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NaafiriDaggerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriDaggerEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }

}
