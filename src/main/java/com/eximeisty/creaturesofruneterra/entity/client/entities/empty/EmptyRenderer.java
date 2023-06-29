package com.eximeisty.creaturesofruneterra.entity.client.entities.empty;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmptyRenderer extends GeoEntityRenderer<CoRPartEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/empty.png");

    public EmptyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmptyModel());
    }

    @Override
    public ResourceLocation getTextureLocation(CoRPartEntity entity) {
        return TEXTURE;
    }
}