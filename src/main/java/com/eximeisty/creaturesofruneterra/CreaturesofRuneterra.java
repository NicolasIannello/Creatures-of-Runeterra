package com.eximeisty.creaturesofruneterra;

import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.entity.client.entities.bullet.BulletRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker.XerSaiDunebreakerRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.empty.EmptyRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo.ExaltedPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo.FabledPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.misil.MisilRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo.PlunderPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.poro.PoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.reksai.RekSaiRenderer;
import com.eximeisty.creaturesofruneterra.item.ModCreativeModeTabs;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.util.ModSounds;
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
            event.accept(ModItems.GEMSTONE);event.accept(ModItems.DUNEBREAKER_FANG);event.accept(ModItems.DUNEBREAKER_HORN);event.accept(ModItems.REKSAI_CLAW);
            event.accept(ModItems.REKSAI_PLAQUE);event.accept(ModItems.MISIL);event.accept(ModItems.FISHBONES);event.accept(ModItems.ATLASG);
            //BLOCKS
            event.accept(ModBlocks.TENDRIL);
            //SPAWN EGGS
            event.accept(ModItems.XERSAI_DUNEBREAKER_SPAWN_EGG);event.accept(ModItems.REKSAI_SPAWN_EGG);event.accept(ModItems.PORO_SPAWN_EGG);event.accept(ModItems.FABLEDPORO_SPAWN_EGG);
            event.accept(ModItems.PLUNDERPORO_SPAWN_EGG);event.accept(ModItems.EXALTEDPORO_SPAWN_EGG);
        }
    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerRenderer::new);
            EntityRenderers.register(ModEntities.PORO.get(), PoroRenderer::new);
            EntityRenderers.register(ModEntities.FABLEDPORO.get(), FabledPoroRenderer::new);
            EntityRenderers.register(ModEntities.PLUNDERPORO.get(), PlunderPoroRenderer::new);
            EntityRenderers.register(ModEntities.EXALTEDPORO.get(), ExaltedPoroRenderer::new);
            EntityRenderers.register(ModEntities.BULLET.get(), BulletRenderer::new);
            EntityRenderers.register(ModEntities.REKSAI.get(), RekSaiRenderer::new);
            EntityRenderers.register(ModEntities.BULLET.get(), BulletRenderer::new);
            EntityRenderers.register(ModEntities.MISIL.get(), MisilRenderer::new);
            //PART ENTITIES
            EntityRenderers.register(ModEntities.WIVHIV.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WVIHV.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WVIIHVI.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.WIIIHIII.get(), EmptyRenderer::new);
        }
    }
}
