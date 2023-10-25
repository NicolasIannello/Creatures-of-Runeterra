package com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.PlunderPoroEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PlunderPoroRenderer extends GeoEntityRenderer<PlunderPoroEntity> {
    private ResourceLocation rl=new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/plunderporo.png");

    public PlunderPoroRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlunderPoroModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PlunderPoroEntity entity) {
        return rl;
    }
}