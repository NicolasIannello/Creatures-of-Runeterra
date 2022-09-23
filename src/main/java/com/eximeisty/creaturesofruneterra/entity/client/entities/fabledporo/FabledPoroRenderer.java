package com.eximeisty.creaturesofruneterra.entity.client.entities.fabledporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.FabledPoroEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class FabledPoroRenderer extends GeoEntityRenderer<FabledPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/fabledporo.png");

    public FabledPoroRenderer(EntityRendererManager renderManager) {
        super(renderManager, new FabledPoroModel());
    }

    @Override
    public ResourceLocation getEntityTexture(FabledPoroEntity entity) {
        return rl;
    }
}