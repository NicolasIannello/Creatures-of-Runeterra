package com.eximeisty.creaturesofruneterra;

import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.entity.client.entities.bullet.BulletRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dunebreaker.XerSaiDunebreakerRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.exaltedporo.ExaltedPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.fableporo.FabledPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.plunderporo.PlunderPoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.poro.PoroRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.reksai.RekSaiRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.EmptyRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.FiddleProyectileRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.dbshield.DBShieldRenderer;
import com.eximeisty.creaturesofruneterra.entity.client.entities.misil.MisilRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.HexcoreRenderer;
import com.eximeisty.creaturesofruneterra.entity.render.XerSaiHatchlingRenderer;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import com.eximeisty.creaturesofruneterra.util.ModItemModelProperties;
import com.eximeisty.creaturesofruneterra.util.ModSoundEvents;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(CreaturesofRuneterra.MOD_ID)
public class CreaturesofRuneterra
{
    public static final String MOD_ID = "creaturesofruneterra";

    public CreaturesofRuneterra()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModSoundEvents.register(eventBus);
        ModEntityTypes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModItemModelProperties.addCustomItemProperties();
        //BLOCKS
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TENDRIL.get(), RenderType.cutout());
        //PROYECTILES
        EntityRenderers.register(ModEntityTypes.HEXCORE.get(), HexcoreRenderer::new);
        EntityRenderers.register(ModEntityTypes.MISIL.get(), MisilRenderer::new);
        EntityRenderers.register(ModEntityTypes.DBSHIELD.get(), DBShieldRenderer::new);
        EntityRenderers.register(ModEntityTypes.FIDDLE_PROYECTILE.get(), FiddleProyectileRenderer::new);
        EntityRenderers.register(ModEntityTypes.BULLET.get(), BulletRenderer::new);
        //POROS
        EntityRenderers.register(ModEntityTypes.PORO.get(), PoroRenderer::new);
        EntityRenderers.register(ModEntityTypes.PLUNDERPORO.get(), PlunderPoroRenderer::new);
        EntityRenderers.register(ModEntityTypes.FABLEDPORO.get(), FabledPoroRenderer::new);
        EntityRenderers.register(ModEntityTypes.EXALTEDPORO.get(), ExaltedPoroRenderer::new);
        //VOID CREATURES
        EntityRenderers.register(ModEntityTypes.XERSAI_DUNEBREAKER.get(), XerSaiDunebreakerRenderer::new);
        EntityRenderers.register(ModEntityTypes.XERSAI_HATCHLING.get(), XerSaiHatchlingRenderer::new);
        //PART ENTITIES
        EntityRenderers.register(ModEntityTypes.WIVHIV.get(), EmptyRenderer::new);
        EntityRenderers.register(ModEntityTypes.WVIHV.get(), EmptyRenderer::new);
        EntityRenderers.register(ModEntityTypes.WVIIHVI.get(), EmptyRenderer::new);
        EntityRenderers.register(ModEntityTypes.WIIIHIII.get(), EmptyRenderer::new);
        //BOSSES
        EntityRenderers.register(ModEntityTypes.REKSAI.get(), RekSaiRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityTypes.PORO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        });
    }

}