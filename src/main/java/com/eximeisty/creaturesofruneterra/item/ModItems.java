package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
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
    public static final RegistryObject<Item> MISIL = ITEMS.register("misil",
            () -> new MisilItem(new Item.Properties()));
    public static final RegistryObject<Item> DUNEBREAKER_HORN = ITEMS.register("dunebreaker_horn",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DUNEBREAKER_FANG = ITEMS.register("dunebreaker_fang",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REKSAI_PLAQUE = ITEMS.register("reksai_plaque",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REKSAI_CLAW = ITEMS.register("reksai_claw",
            () -> new Item(new Item.Properties()));

    //ARMORS
    public static final RegistryObject<Item> SAI_HELMET = ITEMS.register("sai_helmet",
            () -> new SaiArmorItem(ModArmorMaterials.SAI, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SAI_CHESTPLATE = ITEMS.register("sai_chestplate",
            () -> new SaiArmorItem(ModArmorMaterials.SAI, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SAI_LEGGINGS = ITEMS.register("sai_leggings",
            () -> new SaiArmorItem(ModArmorMaterials.SAI, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SAI_BOOTS = ITEMS.register("sai_boots",
            () -> new SaiArmorItem(ModArmorMaterials.SAI, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> SAI_ELYTRA = ITEMS.register("sai_elytra",
            () -> new SaiElytraItem(ModArmorMaterials.SAI, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    //WEAPONS
    public static final RegistryObject<Item> FISHBONES = ITEMS.register("fishbones",
            () -> new Fishbones(new Item.Properties().durability(150)));
    public static final RegistryObject<Item> ATLASG = ITEMS.register("atlasg",
            () -> new AtlasG(Tiers.DIAMOND, 4, -2F, new Item.Properties().durability(400)));
    public static final RegistryObject<Item> DUNEBREAKER_SHIELD = ITEMS.register("dunebreaker_shield",
            () -> new DunebreakerShield(new Item.Properties().durability(600)));

    //SPAWN EGGS
    public static final RegistryObject<Item> XERSAI_DUNEBREAKER_SPAWN_EGG = ITEMS.register("xersai_dunebreaker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.XERSAI_DUNEBREAKER, 0x9F2BAF, 0x370E45, new Item.Properties()));
    public static final RegistryObject<Item> PORO_SPAWN_EGG = ITEMS.register("poro_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PORO, 0xffffff, 0xdde5e4, new Item.Properties()));
    public static final RegistryObject<Item> FABLEDPORO_SPAWN_EGG = ITEMS.register("fabledporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FABLEDPORO, 0xffffff, 0x2e2b2b, new Item.Properties()));
    public static final RegistryObject<Item> PLUNDERPORO_SPAWN_EGG = ITEMS.register("plunderporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PLUNDERPORO, 0xffffff, 0x0c6822, new Item.Properties()));
    public static final RegistryObject<Item> EXALTEDPORO_SPAWN_EGG = ITEMS.register("exaltedporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.EXALTEDPORO, 0xffffff, 0xeee839, new Item.Properties()));
    public static final RegistryObject<Item> REKSAI_SPAWN_EGG = ITEMS.register("reksai_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.REKSAI, 0x9F2BAF, 0x005f8f, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}