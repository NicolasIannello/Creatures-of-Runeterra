package com.eximeisty.creaturesofruneterra.events;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.block.entity.client.darkinT.DarkinThingyRenderer;
import com.eximeisty.creaturesofruneterra.block.entity.client.drill.DrillRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.armor.rhaast.RhaastArmorRenderer;
import com.eximeisty.creaturesofruneterra.item.custom.FiddleArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.RhaastArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;
import com.eximeisty.creaturesofruneterra.entity.client.armor.fiddle.FiddleArmorRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.armor.sai.SaiArmorRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.armor.sai.SaiElytraRenderer;

import com.eximeisty.creaturesofruneterra.util.KeyBindings;
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
        GeoArmorRenderer.registerArmorRenderer(SaiElytraItem.class, () -> new SaiElytraRenderer());
        GeoArmorRenderer.registerArmorRenderer(FiddleArmorItem.class, () -> new FiddleArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(RhaastArmorItem.class, () -> new RhaastArmorRenderer());
    }

    @SubscribeEvent
    public static void registerBlockRenderers(final FMLClientSetupEvent event){
        ClientRegistry.bindTileEntityRenderer(ModTiles.DRILL.get(), DrillRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTiles.DARKIN_PEDESTAL.get(), DarkinThingyRenderer::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(KeyBindings.FLY_UP);
        ClientRegistry.registerKeyBinding(KeyBindings.FLY_DOWN);
        ClientRegistry.registerKeyBinding(KeyBindings.ITEM_HABILITY);
        ClientRegistry.registerKeyBinding(KeyBindings.ITEM_HABILITY2);
        ClientRegistry.registerKeyBinding(KeyBindings.ARMOR_HABILITY);
    }
}