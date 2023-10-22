package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.CoRPartEntity;
import com.eximeisty.creaturesofruneterra.entity.model.EmptyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;


public class EmptyRenderer extends MobRenderer<CoRPartEntity, EmptyModel<CoRPartEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/empty.png");

    public EmptyRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new EmptyModel<>(renderManagerIn.bakeLayer(EmptyModel.LAYER_LOCATION)), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(CoRPartEntity entity) {
        return TEXTURE;
    }
}