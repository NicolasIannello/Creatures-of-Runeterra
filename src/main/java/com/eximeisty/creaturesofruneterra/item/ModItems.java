package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.entity.ModEntities;
import com.eximeisty.creaturesofruneterra.item.custom.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesofRuneterra.MOD_ID);

    //ITEMS
    public static final RegistryObject<Item> HEXCORE = ITEMS.register("hexcore",
            () -> new HexcoreItem(new Item.Properties().stacksTo(10)));

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

    //TOOLS
    public static final RegistryObject<Item> SAI_SWORD = ITEMS.register("sai_sword",
            () -> new SwordItem(ModItemTier.SAI, 8, -2.4F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SAI_SHOVEL = ITEMS.register("sai_shovel",
            () -> new ShovelItem(ModItemTier.SAI, 2F, -3.0F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SAI_PICKAXE = ITEMS.register("sai_pickaxe",
            () -> new PickaxeItem(ModItemTier.SAI, 2, -2.8F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SAI_AXE = ITEMS.register("sai_axe",
            () -> new AxeItem(ModItemTier.SAI, 11.0F, -3.0F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SAI_HOE = ITEMS.register("sai_hoe",
            () -> new HoeItem(ModItemTier.SAI, -4, 0.0F, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> SAI_BOW = ITEMS.register("sai_bow",
            () -> new SaiBowItem(new Item.Properties().stacksTo(1).durability(384).fireResistant()));

    public static final RegistryObject<Item> TENDRIL_COMPASS = ITEMS.register("tendril_compass",
            () -> new TendrilCompassItem(new Item.Properties()));

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

    public static final RegistryObject<Item> FIDDLE_HELMET = ITEMS.register("fiddle_helmet",
            () -> new FiddleArmorItem(ArmorMaterials.CHAIN, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> FIDDLE_CHESTPLATE = ITEMS.register("fiddle_chestplate",
            () -> new FiddleArmorItem(ArmorMaterials.CHAIN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> FIDDLE_BEARTRAP = ITEMS.register("fiddle_beartrap",
            () -> new FiddleArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> FIDDLE_BIRDCAGE = ITEMS.register("fiddle_birdcage",
            () -> new FiddleArmorItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> RHAAST_HELMET = ITEMS.register("rhaast_helmet",
            () -> new RhaastArmorItem(ModArmorMaterials.DARKIN, ArmorItem.Type.HELMET, new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> RHAAST_CHESTPLATE = ITEMS.register("rhaast_chestplate",
            () -> new RhaastArmorItem(ModArmorMaterials.DARKIN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> RHAAST_LEGGINGS = ITEMS.register("rhaast_leggings",
            () -> new RhaastArmorItem(ModArmorMaterials.DARKIN, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> RHAAST_BOOTS = ITEMS.register("rhaast_boots",
            () -> new RhaastArmorItem(ModArmorMaterials.DARKIN, ArmorItem.Type.BOOTS, new Item.Properties().durability(1000)));

    //WEAPONS
    public static final RegistryObject<Item> FISHBONES = ITEMS.register("fishbones",
            () -> new Fishbones(new Item.Properties().durability(150)));

    public static final RegistryObject<Item> ATLASG = ITEMS.register("atlasg",
            () -> new AtlasG(ModItemTier.ATLAS, 4, -1.5F, new Item.Properties().durability(400)));

    public static final RegistryObject<Item> DUNEBREAKER_SHIELD = ITEMS.register("dunebreaker_shield",
            () -> new DunebreakerShield(new Item.Properties().durability(600)));

    public static final RegistryObject<Item> FIDDLESCYTHE = ITEMS.register("fiddle_scythe",
            () -> new FiddleScythe(ModItemTier.FIDDLE, 6, -2F, new Item.Properties().durability(100)));

    public static final RegistryObject<Item> RHAAST = ITEMS.register("rhaast",
            () -> new Rhaast(ModItemTier.DARKIN, 2, -2F, new Item.Properties().durability(100)));

    //SPAWN EGGS
    public static final RegistryObject<Item> XERSAI_HATCHLING_SPAWN_EGG = ITEMS.register("xersai_hatchling_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.XERSAI_HATCHLING, 0x9F2BAF, 0x67276F, new Item.Properties()));

    public static final RegistryObject<Item> XERSAI_DUNEBREAKER_SPAWN_EGG = ITEMS.register("xersai_dunebreaker_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.XERSAI_DUNEBREAKER, 0x9F2BAF, 0x370E45, new Item.Properties()));

//        public static final RegistryObject<Item> XERXARETH_SPAWN_EGG = ITEMS.register("xerxareth_spawn_egg",
//        () -> new ForgeSpawnEggItem(ModEntities.XERXARETH, 0x9F2BAF, 0xF3D978, new Item.Properties().);

    public static final RegistryObject<Item> REKSAI_SPAWN_EGG = ITEMS.register("reksai_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.REKSAI, 0x9F2BAF, 0x005f8f, new Item.Properties()));

    public static final RegistryObject<Item> PORO_SPAWN_EGG = ITEMS.register("poro_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PORO, 0xffffff, 0xdde5e4, new Item.Properties()));

    public static final RegistryObject<Item> FABLEDPORO_SPAWN_EGG = ITEMS.register("fabledporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FABLEDPORO, 0xffffff, 0x2e2b2b, new Item.Properties()));

    public static final RegistryObject<Item> PLUNDERPORO_SPAWN_EGG = ITEMS.register("plunderporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PLUNDERPORO, 0xffffff, 0x0c6822, new Item.Properties()));

    public static final RegistryObject<Item> POROBOT_SPAWN_EGG = ITEMS.register("porobot_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PATCHEDPOROBOT, 0xc25719, 0x612c0e, new Item.Properties()));

    public static final RegistryObject<Item> EXALTEDPORO_SPAWN_EGG = ITEMS.register("exaltedporo_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.EXALTEDPORO, 0xffffff, 0xeee839, new Item.Properties()));

    public static final RegistryObject<Item> FIDDLESTICKS_SPAWN_EGG = ITEMS.register("fiddlesticks_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FIDDLESTICKS, 0x0f0705, 0x3b1210, new Item.Properties()));

//    BLOCKS
    public static final RegistryObject<BlockItem> DRILL = ITEMS.register("drill",
            () -> new DrillItem(ModBlocks.DRILL.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}