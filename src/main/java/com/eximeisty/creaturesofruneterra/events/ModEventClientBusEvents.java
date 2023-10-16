package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.model.EmptyModel;
import com.eximeisty.creaturesofruneterra.entity.model.XerSaiHatchlingModel;
import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;
import com.eximeisty.creaturesofruneterra.entity.client.armor.fiddle.FiddleArmorRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.armor.sai.SaiArmorRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.armor.sai.SaiElytraRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final FMLClientSetupEvent event){
        GeoArmorRenderer.registerArmorRenderer(SaiArmorItem.class, () -> new SaiArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(SaiElytraItem.class, () -> new SaiElytraRenderer());
        GeoArmorRenderer.registerArmorRenderer(FiddleArmorItem.class, () -> new FiddleArmorRenderer());
    }

//    @SubscribeEvent
//    public static void registerBlockRenderers(final FMLClientSetupEvent event){
//        ClientRegistry.bindTileEntityRenderer(ModTiles.DRILL.get(), DrillRenderer::new);
//    }

//    @SubscribeEvent
//    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerEntityRenderer(ModEntityTypes.XERSAI_HATCHLING.get(), XerSaiHatchlingRenderer::new);
//    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(XerSaiHatchlingModel.LAYER_LOCATION, XerSaiHatchlingModel::createBodyLayer);
        event.registerLayerDefinition(EmptyModel.LAYER_LOCATION, EmptyModel::createBodyLayer);
    }
}