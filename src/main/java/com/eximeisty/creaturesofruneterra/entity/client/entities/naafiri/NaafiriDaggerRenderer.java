package com.eximeisty.creaturesofruneterra.entity.client.entities.naafiri;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.NaafiriDaggerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class NaafiriDaggerRenderer  extends GeoProjectilesRenderer<NaafiriDaggerEntity> {

    public NaafiriDaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NaafiriDaggerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(NaafiriDaggerEntity entity) {
        return new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/naafiri.png");
    }

}
