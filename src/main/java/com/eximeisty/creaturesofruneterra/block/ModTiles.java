package com.eximeisty.creaturesofruneterra.block;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<BlockEntityType<DrillTileEntity>> DRILL = TILES.register("drill",
    ()-> BlockEntityType.Builder.of(DrillTileEntity::new, ModBlocks.DRILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<DarkinThingyTileEntity>> DARKIN_PEDESTAL = TILES.register("darkin_pedestal",
            ()-> BlockEntityType.Builder.of(DarkinThingyTileEntity::new, ModBlocks.DARKIN_PEDESTAL.get()).build(null));

    public static void register(IEventBus eventBus){
        TILES.register(eventBus);
    }
}