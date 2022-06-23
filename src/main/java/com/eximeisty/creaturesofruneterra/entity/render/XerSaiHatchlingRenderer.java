package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerSaiHatchlingEntity;
import com.eximeisty.creaturesofruneterra.entity.model.XerSaiHatchlingModel;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


public class XerSaiHatchlingRenderer extends MobRenderer<XerSaiHatchlingEntity, XerSaiHatchlingModel<XerSaiHatchlingEntity>>{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xersai_hatchling.png");

    public XerSaiHatchlingRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new XerSaiHatchlingModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(XerSaiHatchlingEntity entity) {
        return TEXTURE;
    }
}
