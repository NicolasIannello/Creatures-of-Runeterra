package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.custom.XerxarethEntity;
import com.eximeisty.creaturesofruneterra.entity.model.XerxarethModel;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


public class XerxarethRenderer extends MobRenderer<XerxarethEntity, XerxarethModel<XerxarethEntity>>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesofRuneterra.MOD_ID, "textures/entity/xerxareth.png");

    public XerxarethRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new XerxarethModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(XerxarethEntity entity) {
        return TEXTURE;
    }
}