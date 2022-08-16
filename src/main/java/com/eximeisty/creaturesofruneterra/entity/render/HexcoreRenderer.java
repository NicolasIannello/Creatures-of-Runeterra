package com.eximeisty.creaturesofruneterra.entity.render;

import com.eximeisty.creaturesofruneterra.entity.custom.HexcoreEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class HexcoreRenderer implements IRenderFactory<HexcoreEntity> {
    @Override
    public EntityRenderer<? super HexcoreEntity> createRenderFor(EntityRendererManager manager) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        return new SpriteRenderer<>(manager, itemRenderer);
    }
}