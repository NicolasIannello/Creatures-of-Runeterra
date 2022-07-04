package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.custom.ModSpawnEggItem;

import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesofRuneterra.MOD_ID);

    public static final RegistryObject<ModSpawnEggItem> XERSAI_HATCHLING_SPAWN_EGG = ITEMS.register("xersai_hatchling_spawn_egg",
    () -> new ModSpawnEggItem(ModEntityTypes.XERSAI_HATCHLING, 0x464F56, 0x1D6336,
            new Item.Properties().group(ItemGroup.MISC)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
