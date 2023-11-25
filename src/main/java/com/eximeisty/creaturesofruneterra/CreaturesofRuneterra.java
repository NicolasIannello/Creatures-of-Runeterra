package com.eximeisty.creaturesofruneterra;

import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.block.ModTiles;
import com.eximeisty.creaturesofruneterra.container.ModContainers;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.entity.client.entities.bullet.BulletRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield.DBShieldRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker.XerSaiDunebreakerRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo.ExaltedPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo.FabledPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks.FiddleDummyRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.fiddlesticks.FiddlesticksRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.misil.MisilRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.patchedporobot.PatchedPorobotRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo.PlunderPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.poro.PoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.reksai.RekSaiRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.silverwing.SilverwingRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.EmptyRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.FiddleProyectileRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.HexcoreRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.XerSaiHatchlingRenderer;
import com.eximeisty.creaturesofruneterra.item.ModCreativeModeTabs;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.screen.PorobotScreen;
import com.eximeisty.creaturesofruneterra.util.ModItemModelProperties;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CreaturesofRuneterra.MOD_ID)
public class CreaturesofRuneterra
{
    public static final String MOD_ID = "creaturesofruneterra";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreaturesofRuneterra()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTiles.register(modEventBus);
        ModContainers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event)
//    {
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModItemModelProperties.addCustomItemProperties();
            //ENTITIES
            EntityRenderers.register(ModEntities.XERSAI_HATCHLING.get(), XerSaiHatchlingRenderer::new);
            EntityRenderers.register(ModEntities.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerRenderer::new);
            EntityRenderers.register(ModEntities.PORO.get(), PoroRenderer::new);
            EntityRenderers.register(ModEntities.FABLEDPORO.get(), FabledPoroRenderer::new);
            EntityRenderers.register(ModEntities.PLUNDERPORO.get(), PlunderPoroRenderer::new);
            EntityRenderers.register(ModEntities.EXALTEDPORO.get(), ExaltedPoroRenderer::new);
            EntityRenderers.register(ModEntities.PATCHEDPOROBOT.get(), PatchedPorobotRenderer::new);
            EntityRenderers.register(ModEntities.SILVERWING.get(), SilverwingRenderer::new);
            //BOSSES
            EntityRenderers.register(ModEntities.FIDDLESTICKS.get(), FiddlesticksRenderer::new);
            EntityRenderers.register(ModEntities.FIDDLEDUMMY.get(), FiddleDummyRenderer::new);
            EntityRenderers.register(ModEntities.REKSAI.get(), RekSaiRenderer::new);
            //ITEMS
            EntityRenderers.register(ModEntities.BULLET.get(), BulletRenderer::new);
            EntityRenderers.register(ModEntities.MISIL.get(), MisilRenderer::new);
            EntityRenderers.register(ModEntities.DBSHIELD.get(), DBShieldRenderer::new);
            EntityRenderers.register(ModEntities.HEXCORE.get(), HexcoreRenderer::new);
            EntityRenderers.register(ModEntities.FIDDLE_PROYECTILE.get(), FiddleProyectileRenderer::new);
            //PART ENTITIES
            EntityRenderers.register(ModEntities.WIVHIV.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WVIHV.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WVIIHVI.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WIIIHIII.get(), EmptyRenderer::new);
            //SCREENS
            MenuScreens.register(ModContainers.POROBOT_CONTAINER.get(), PorobotScreen::new);
        }
    }
}
