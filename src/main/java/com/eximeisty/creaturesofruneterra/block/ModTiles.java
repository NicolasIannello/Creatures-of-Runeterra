package com.eximeisty.creaturesofruneterra.block;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.entity.DarkinThingyTileEntity;
import com.eximeisty.creaturesofruneterra.block.entity.DrillTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTiles {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<TileEntityType<DrillTileEntity>> DRILL = TILES.register("drill", 
    ()-> TileEntityType.Builder.create(DrillTileEntity::new, ModBlocks.DRILL.get()).build(null));

    public static final RegistryObject<TileEntityType<DarkinThingyTileEntity>> DARKIN_PEDESTAL = TILES.register("darkin_pedestal",
            ()-> TileEntityType.Builder.create(DarkinThingyTileEntity::new, ModBlocks.DARKIN_PEDESTAL.get()).build(null));

    public static void register(IEventBus eventBus){
        TILES.register(eventBus);
    }
}