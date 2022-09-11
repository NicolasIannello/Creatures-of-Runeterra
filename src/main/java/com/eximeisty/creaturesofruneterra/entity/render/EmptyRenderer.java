package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;
import com.eximeisty.creaturesofruneterra.entity.model.EmptyModel;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


public class EmptyRenderer extends MobRenderer<CoRPartEntity, EmptyModel<CoRPartEntity>>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/empty.png");

    public EmptyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EmptyModel<>(), 0F);
    }

    @Override
    public ResourceLocation getEntityTexture(CoRPartEntity entity) {
        return TEXTURE;
    }
}