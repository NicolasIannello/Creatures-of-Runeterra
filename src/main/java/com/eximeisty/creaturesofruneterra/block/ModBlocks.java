package com.eximeisty.creaturesofruneterra.block;

import java.util.function.Supplier;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.custom.DrillBlock;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS= DeferredRegister.create(ForgeRegistries.BLOCKS, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<Block> DRILL= registerBlock("drill", () -> new DrillBlock(AbstractBlock.Properties.create(Material.ROCK)
    .harvestLevel(3).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5F).notSolid()));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn= BLOCKS.register(name, block);
        //registerBlockItem(name, toReturn);
        return toReturn;
    }

    /*private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.COR_GROUP)));
    }*/

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}