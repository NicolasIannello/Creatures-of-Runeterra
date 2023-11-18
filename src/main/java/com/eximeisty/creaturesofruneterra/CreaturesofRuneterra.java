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
import com.eximeisty.creaturesofruneterra.entity.render.EmptyRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.FiddleProyectileRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.HexcoreRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.XerSaiHatchlingRenderer;
import com.eximeisty.creaturesofruneterra.item.ModCreativeModeTabs;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.screen.PorobotScreen;
import com.eximeisty.creaturesofruneterra.util.ModItemModelProperties;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
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
        ModBlocks.register(modEventBus);
        ModTiles.register(modEventBus);
        ModContainers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == ModCreativeModeTabs.COR_TAB) {
            //ITEMS
            event.accept(ModItems.HEXCORE);event.accept(ModItems.GEMSTONE);event.accept(ModItems.MISIL);event.accept(ModItems.DUNEBREAKER_FANG);event.accept(ModItems.DUNEBREAKER_HORN);event.accept(ModItems.REKSAI_CLAW);
            event.accept(ModItems.REKSAI_PLAQUE);
            //TOOLS
            event.accept(ModItems.SAI_SWORD);event.accept(ModItems.SAI_PICKAXE);event.accept(ModItems.SAI_AXE);event.accept(ModItems.SAI_SHOVEL);event.accept(ModItems.SAI_HOE);event.accept(ModItems.SAI_BOW);
            event.accept(ModItems.TENDRIL_COMPASS);
            //ARMORS
            event.accept(ModItems.SAI_HELMET);event.accept(ModItems.SAI_CHESTPLATE);event.accept(ModItems.SAI_LEGGINGS);event.accept(ModItems.SAI_BOOTS);event.accept(ModItems.SAI_ELYTRA);
            event.accept(ModItems.FIDDLE_HELMET);event.accept(ModItems.FIDDLE_CHESTPLATE);event.accept(ModItems.FIDDLE_BEARTRAP);event.accept(ModItems.FIDDLE_BIRDCAGE);
            event.accept(ModItems.RHAAST_HELMET.get());event.accept(ModItems.RHAAST_CHESTPLATE.get());event.accept(ModItems.RHAAST_LEGGINGS.get());event.accept(ModItems.RHAAST_BOOTS.get());
            //WEAPONS
            event.accept(ModItems.FISHBONES);event.accept(ModItems.ATLASG);event.accept(ModItems.DUNEBREAKER_SHIELD);event.accept(ModItems.FIDDLESCYTHE);event.accept(ModItems.RHAAST.get());
            //BLOCKS
            event.accept(ModBlocks.TENDRIL);event.accept(ModBlocks.DRILL);event.accept(ModBlocks.DARKIN_PEDESTAL.get());
            //SPAWN EGGS
            event.accept(ModItems.XERSAI_HATCHLING_SPAWN_EGG);event.accept(ModItems.XERSAI_DUNEBREAKER_SPAWN_EGG);event.accept(ModItems.REKSAI_SPAWN_EGG);event.accept(ModItems.PORO_SPAWN_EGG);event.accept(ModItems.FABLEDPORO_SPAWN_EGG);
            event.accept(ModItems.PLUNDERPORO_SPAWN_EGG);event.accept(ModItems.POROBOT_SPAWN_EGG);event.accept(ModItems.EXALTEDPORO_SPAWN_EGG);
            event.accept(ModItems.FIDDLESTICKS_SPAWN_EGG);
        }
    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModItemModelProperties.addCustomItemProperties();
            //ENTITIES
            EntityRenderers.register(ModEntities.XERSAI_HATCHLING.get(), XerSaiHatchlingRenderer::new);
            EntityRenderers.register(ModEntities.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerRenderer::new);
            EntityRenderers.register(ModEntities.PORO.get(), PoroRenderer::new);
            EntityRenderers.register(ModEntities.FABLEDPORO.get(), FabledPoroRenderer::new);
            EntityRenderers.register(ModEntities.PLUNDERPORO.get(), PlunderPoroRenderer::new);
            EntityRenderers.register(ModEntities.EXALTEDPORO.get(), ExaltedPoroRenderer::new);
            EntityRenderers.register(ModEntities.PATCHEDPOROBOT.get(), PatchedPorobotRenderer::new);
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
