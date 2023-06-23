package com.eximeisty.creaturesofruneterra;

import com.eximeisty.creaturesofruneterra.item.ModCreativeModeTabs;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreaturesofRuneterra.MOD_ID)
public class CreaturesofRuneterra{
    public static final String MOD_ID = "creaturesofruneterra";

    public CreaturesofRuneterra(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == ModCreativeModeTabs.COR_TAB) {
            event.accept(ModItems.GEMSTONE);event.accept(ModItems.DUNEBREAKER_FANG);event.accept(ModItems.DUNEBREAKER_HORN);event.accept(ModItems.REKSAI_CLAW);
            event.accept(ModItems.REKSAI_PLAQUE);
        }
    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
