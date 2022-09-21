package com.eximeisty.creaturesofruneterra;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield.DBShieldRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker.XerSaiDunebreakerRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.misil.MisilRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.reksai.RekSaiRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.EmptyRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.HexcoreRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.XerSaiHatchlingRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.XerxarethRenderer;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.util.ModItemModelProperties;
import com.eximeisty.creaturesofruneterra.world.structure.ModStructures;
import software.bernie.geckolib3.GeckoLib;

@Mod(CreaturesofRuneterra.MOD_ID)
public class CreaturesofRuneterra {
    public static final String MOD_ID = "creaturesofruneterra";

    public CreaturesofRuneterra() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModTiles.register(eventBus);
        ModStructures.register(eventBus);
        ModEntityTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::doClientStuff);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModStructures.setupStructures();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModItemModelProperties.makeBow(ModItems.SAI_BOW.get());
            RenderTypeLookup.setRenderLayer(ModBlocks.TENDRIL.get(), RenderType.getCutout());
        });
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.XERSAI_HATCHLING.get(), XerSaiHatchlingRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.XERXARETH.get(), XerxarethRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.REKSAI.get(), RekSaiRenderer::new);
        //ITEMS
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MISIL.get(), MisilRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.DBSHIELD.get(), DBShieldRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HEXCORE.get(), new HexcoreRenderer());
        //PART ENTITIES
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WIVHIV.get(), EmptyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WVIHV.get(), EmptyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WVIIHVI.get(), EmptyRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WIIIHIII.get(), EmptyRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        // InterModComms.sendTo("examplemod", "helloworld", () -> {
        //     LOGGER.info("Hello world from the MDK");
        //     return "Hello world";
        // });
    }

    private void processIMC(final InterModProcessEvent event) { }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) { }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {

        }
    }
}
