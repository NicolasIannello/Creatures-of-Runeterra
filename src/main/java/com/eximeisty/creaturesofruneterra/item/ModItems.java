package com.eximeisty.creaturesofruneterra.item;

import com.eximeisty.creaturesofruneterra.CreaturesofRuneterra;
import com.eximeisty.creaturesofruneterra.block.ModBlocks;
import com.eximeisty.creaturesofruneterra.entity.ModEntityTypes;
import com.eximeisty.creaturesofruneterra.item.custom.AtlasG;
import com.eximeisty.creaturesofruneterra.item.custom.DrillItem;
import com.eximeisty.creaturesofruneterra.item.custom.DunebreakerShield;
import com.eximeisty.creaturesofruneterra.item.custom.Fishbones;
import com.eximeisty.creaturesofruneterra.item.custom.HexcoreItem;
import com.eximeisty.creaturesofruneterra.item.custom.MisilItem;
import com.eximeisty.creaturesofruneterra.item.custom.ModSpawnEggItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiArmorItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiBowItem;
import com.eximeisty.creaturesofruneterra.item.custom.SaiElytraItem;
import com.eximeisty.creaturesofruneterra.item.custom.TendrilCompassItem;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesofRuneterra.MOD_ID);

        //ITEMS
        public static final RegistryObject<Item> HEXCORE = ITEMS.register("hexcore", 
        () -> new HexcoreItem(new Item.Properties().maxStackSize(10).group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> GEMSTONE = ITEMS.register("gemstone", 
        () -> new Item(new Item.Properties().maxStackSize(10).group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> MISIL = ITEMS.register("misil", 
        () -> new MisilItem(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> DUNEBREAKER_HORN = ITEMS.register("dunebreaker_horn",
        () -> new Item(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> DUNEBREAKER_FANG = ITEMS.register("dunebreaker_fang",
        () -> new Item(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> REKSAI_PLAQUE = ITEMS.register("reksai_plaque",
        () -> new Item(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> REKSAI_CLAW = ITEMS.register("reksai_claw",
        () -> new Item(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        //TOOLS
        public static final RegistryObject<Item> SAI_SWORD = ITEMS.register("sai_sword",
        () -> new SwordItem(ModItemTier.SAI, 8, -2.4F, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_SHOVEL = ITEMS.register("sai_shovel",
        () -> new ShovelItem(ModItemTier.SAI, 2F, -3.0F, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_PICKAXE = ITEMS.register("sai_pickaxe",
        () -> new PickaxeItem(ModItemTier.SAI, 2, -2.8F, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_AXE = ITEMS.register("sai_axe",
        () -> new AxeItem(ModItemTier.SAI, 11.0F, -3.0F, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_HOE = ITEMS.register("sai_hoe",
        () -> new HoeItem(ModItemTier.SAI, -4, 0.0F, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_BOW = ITEMS.register("sai_bow", 
        () -> new SaiBowItem(new Item.Properties().maxStackSize(1).maxDamage(384).group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> TENDRIL_COMPASS = ITEMS.register("tendril_compass",
        () -> new TendrilCompassItem(new Item.Properties().group(ModItemGroup.COR_GROUP)));

        //ARMORS
        public static final RegistryObject<Item> SAI_HELMET = ITEMS.register("sai_helmet",
        () -> new SaiArmorItem(ModArmorMaterial.SAI, EquipmentSlotType.HEAD, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_CHESTPLATE = ITEMS.register("sai_chestplate",
        () -> new SaiArmorItem(ModArmorMaterial.SAI, EquipmentSlotType.CHEST, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_LEGGINGS = ITEMS.register("sai_leggings",
        () -> new SaiArmorItem(ModArmorMaterial.SAI, EquipmentSlotType.LEGS, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_BOOTS = ITEMS.register("sai_boots",
        () -> new SaiArmorItem(ModArmorMaterial.SAI, EquipmentSlotType.FEET, new Item.Properties().group(ModItemGroup.COR_GROUP).isImmuneToFire()));

        public static final RegistryObject<Item> SAI_ELYTRA = ITEMS.register("sai_elytra",
        () -> new SaiElytraItem(ModArmorMaterial.SAI, EquipmentSlotType.CHEST, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        //WEAPONS
        public static final RegistryObject<Item> FISHBONES = ITEMS.register("fishbones",
        () -> new Fishbones(new Item.Properties().maxDamage(150).group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> ATLASG = ITEMS.register("atlasg",
        () -> new AtlasG(ModItemTier.ATLAS, 4, -2F, new Item.Properties().maxDamage(400).group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<Item> DUNEBREAKER_SHIELD = ITEMS.register("dunebreaker_shield",
        () -> new DunebreakerShield(new Item.Properties().maxDamage(600).group(ModItemGroup.COR_GROUP)));
        
        //SPAWN EGGS
        public static final RegistryObject<ModSpawnEggItem> XERSAI_HATCHLING_SPAWN_EGG = ITEMS.register("xersai_hatchling_spawn_egg",
        () -> new ModSpawnEggItem(ModEntityTypes.XERSAI_HATCHLING, 0x9F2BAF, 0x67276F, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<ModSpawnEggItem> XERSAI_DUNEBREAKER_SPAWN_EGG = ITEMS.register("xersai_dunebreaker_spawn_egg",
        () -> new ModSpawnEggItem(ModEntityTypes.XERSAI_DUNEBREAKER, 0x9F2BAF, 0x370E45, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<ModSpawnEggItem> XERXARETH_SPAWN_EGG = ITEMS.register("xerxareth_spawn_egg",
        () -> new ModSpawnEggItem(ModEntityTypes.XERXARETH, 0x9F2BAF, 0xF3D978, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<ModSpawnEggItem> REKSAI_SPAWN_EGG = ITEMS.register("reksai_spawn_egg",
        () -> new ModSpawnEggItem(ModEntityTypes.REKSAI, 0x9F2BAF, 0x005f8f, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        public static final RegistryObject<ModSpawnEggItem> PORO_SPAWN_EGG = ITEMS.register("poro_spawn_egg",
        () -> new ModSpawnEggItem(ModEntityTypes.PORO, 0xffffff, 0xdde5e4, new Item.Properties().group(ModItemGroup.COR_GROUP)));

        //BLOCKS
        public static final RegistryObject<Item> DRILL = ITEMS.register("drill",
        () -> new DrillItem(ModBlocks.DRILL.get(), new Item.Properties().group(ModItemGroup.COR_GROUP)));
        
        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}