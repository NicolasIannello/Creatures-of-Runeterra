package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CreaturesofRuneterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COR_TAB = CREATIVE_MODE_TABS.register("cor_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAI_HELMET.get()))
                    .title(Component.translatable("Creatures of Runeterra"))
                    .displayItems((pParameters, pOutput) -> {
                        //ITEMS
                        pOutput.accept(ModItems.HEXCORE.get());pOutput.accept(ModItems.GEMSTONE.get());pOutput.accept(ModItems.MISIL.get());pOutput.accept(ModItems.DUNEBREAKER_FANG.get());pOutput.accept(ModItems.DUNEBREAKER_HORN.get());pOutput.accept(ModItems.REKSAI_CLAW.get());
                        pOutput.accept(ModItems.REKSAI_PLAQUE.get());
                        //TOOLS
                        pOutput.accept(ModItems.SAI_SWORD.get());pOutput.accept(ModItems.SAI_PICKAXE.get());pOutput.accept(ModItems.SAI_AXE.get());pOutput.accept(ModItems.SAI_SHOVEL.get());pOutput.accept(ModItems.SAI_HOE.get());pOutput.accept(ModItems.SAI_BOW.get());
                        pOutput.accept(ModItems.TENDRIL_COMPASS.get());
                        //ARMORS
                        pOutput.accept(ModItems.SAI_HELMET.get());pOutput.accept(ModItems.SAI_CHESTPLATE.get());pOutput.accept(ModItems.SAI_LEGGINGS.get());pOutput.accept(ModItems.SAI_BOOTS.get());pOutput.accept(ModItems.SAI_ELYTRA.get());
                        pOutput.accept(ModItems.FIDDLE_HELMET.get());pOutput.accept(ModItems.FIDDLE_CHESTPLATE.get());pOutput.accept(ModItems.FIDDLE_BEARTRAP.get());pOutput.accept(ModItems.FIDDLE_BIRDCAGE.get());
                        pOutput.accept(ModItems.RHAAST_HELMET.get());pOutput.accept(ModItems.RHAAST_CHESTPLATE.get());pOutput.accept(ModItems.RHAAST_LEGGINGS.get());pOutput.accept(ModItems.RHAAST_BOOTS.get());
                        //WEAPONS
                        pOutput.accept(ModItems.FISHBONES.get());pOutput.accept(ModItems.ATLASG.get());pOutput.accept(ModItems.DUNEBREAKER_SHIELD.get());pOutput.accept(ModItems.FIDDLESCYTHE.get());pOutput.accept(ModItems.RHAAST.get());
                        //BLOCKS
                        pOutput.accept(ModBlocks.TENDRIL.get());pOutput.accept(ModBlocks.DRILL.get());pOutput.accept(ModBlocks.DARKIN_PEDESTAL.get());
                        //SPAWN EGGS
                        pOutput.accept(ModItems.XERSAI_HATCHLING_SPAWN_EGG.get());pOutput.accept(ModItems.XERSAI_DUNEBREAKER_SPAWN_EGG.get());pOutput.accept(ModItems.REKSAI_SPAWN_EGG.get());pOutput.accept(ModItems.PORO_SPAWN_EGG.get());pOutput.accept(ModItems.FABLEDPORO_SPAWN_EGG.get());
                        pOutput.accept(ModItems.PLUNDERPORO_SPAWN_EGG.get());pOutput.accept(ModItems.POROBOT_SPAWN_EGG.get());pOutput.accept(ModItems.EXALTEDPORO_SPAWN_EGG.get());
                        pOutput.accept(ModItems.FIDDLESTICKS_SPAWN_EGG.get());
                    }).build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}