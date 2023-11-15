package com.eximeisty.creaturesofruneterra.block;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.custom.DarkinThingyBlock;
import com.eximeisty.creaturesofruneterra.block.custom.DrillBlock;
import com.eximeisty.creaturesofruneterra.block.custom.EmptyBlock;
import com.eximeisty.creaturesofruneterra.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<Block> DRILL= registerBlock("drill", () -> new DrillBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
            .destroyTime(5F).noOcclusion()), false);

    public static final RegistryObject<Block> TENDRIL= registerBlock("tendril", () -> new EmptyBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
            .destroyTime(0.2F).noOcclusion().noCollission()), true);

    public static final RegistryObject<Block> DARKIN_PEDESTAL= registerBlock("darkin_pedestal", () -> new DarkinThingyBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).
            destroyTime(3F).noOcclusion()), false);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean flag){
        RegistryObject<T> toReturn= BLOCKS.register(name, block);
        if(flag) registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
