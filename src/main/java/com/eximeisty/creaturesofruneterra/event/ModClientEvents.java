package com.eximeisty.creaturesofruneterra.event;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.client.drill.DrillRenderer;
import com.eximeisty.creaturesofruneterra.entity.model.EmptyModel;
import com.eximeisty.creaturesofruneterra.entity.model.XerSaiHatchlingModel;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

//    @SubscribeEvent
//    public static void registerArmorRenderers(final FMLClientSetupEvent event){
//        GeoArmorRenderer.registerArmorRenderer(SaiArmorItem.class, () -> new SaiArmorRenderer());
//        GeoArmorRenderer.registerArmorRenderer(SaiElytraItem.class, () -> new SaiElytraRenderer());
//        GeoArmorRenderer.registerArmorRenderer(FiddleArmorItem.class, () -> new FiddleArmorRenderer());
//    }

    @SubscribeEvent
    public static void registerBlockRenderers(final EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModTiles.DRILL.get(), DrillRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(XerSaiHatchlingModel.LAYER_LOCATION, XerSaiHatchlingModel::createBodyLayer);
        event.registerLayerDefinition(EmptyModel.LAYER_LOCATION, EmptyModel::createBodyLayer);
    }
}