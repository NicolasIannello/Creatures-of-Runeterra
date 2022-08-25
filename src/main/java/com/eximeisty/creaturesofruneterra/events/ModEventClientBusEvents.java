package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.client.drill.DrillRenderer;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;
import com.eximeisty.creaturesofruneterra.entity.client.armor.sai.SaiArmorRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final FMLClientSetupEvent event){
        GeoArmorRenderer.registerArmorRenderer(SaiArmorItem.class, () -> new SaiArmorRenderer());
    }

    @SubscribeEvent
    public static void registerBlockRenderers(final FMLClientSetupEvent event){
        ClientRegistry.bindTileEntityRenderer(ModTiles.DRILL.get(), DrillRenderer::new);
    }
}