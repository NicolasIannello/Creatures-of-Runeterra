package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesofRuneterra.MOD_ID);

    //ITEMS
    public static final RegistryObject<Item> GEMSTONE = ITEMS.register("gemstone",
            () -> new Item(new Item.Properties().stacksTo(10)));
    public static final RegistryObject<Item> DUNEBREAKER_HORN = ITEMS.register("dunebreaker_horn",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DUNEBREAKER_FANG = ITEMS.register("dunebreaker_fang",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REKSAI_PLAQUE = ITEMS.register("reksai_plaque",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REKSAI_CLAW = ITEMS.register("reksai_claw",
            () -> new Item(new Item.Properties()));

    //SPAWN EGGS
    public static final RegistryObject<Item> XERSAI_DUNEBREAKER_SPAWN_EGG = ITEMS.register("xersai_dunebreaker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.XERSAI_DUNEBREAKER, 0x9F2BAF, 0x370E45,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}