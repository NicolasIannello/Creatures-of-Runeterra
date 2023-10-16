package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;
import com.eximeisty.creaturesofruneterra.entity.model.XerSaiHatchlingModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class XerSaiHatchlingRenderer extends MobRenderer<XerSaiHatchlingEntity, XerSaiHatchlingModel<XerSaiHatchlingEntity>>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_hatchling.png");

    public XerSaiHatchlingRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new XerSaiHatchlingModel<>(renderManagerIn.bakeLayer(XerSaiHatchlingModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(XerSaiHatchlingEntity entity) {
        return TEXTURE;
    }
}
